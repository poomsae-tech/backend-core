# Интеграция Kotlin Spring с Authentik

## Содержание

1. [Как это работает](#как-это-работает)
2. [Зависимости](#зависимости)
3. [Конфигурация приложения](#конфигурация-приложения)
4. [Security Config](#security-config)
5. [Извлечение ролей и атрибутов из токена](#извлечение-ролей-и-атрибутов-из-токена)
6. [Авторизация по ролям](#авторизация-по-ролям)
7. [Пример контроллера](#пример-контроллера)
8. [Тестирование локально](#тестирование-локально)

---

## Как это работает

```
Клиент (браузер / мобильное приложение)
    │
    │  1. Redirect → Authentik (login flow)
    │  2. Authentik выдаёт JWT Access Token
    │
    ▼
Kotlin Spring Resource Server
    │
    │  3. Принимает запрос с заголовком: Authorization: Bearer <jwt>
    │  4. Загружает JWKS с Authentik (публичные ключи)
    │  5. Проверяет подпись и срок действия токена
    │  6. Извлекает claims: roles, org_id, judge_type
    │  7. Устанавливает SecurityContext
    │
    ▼
Контроллер: @PreAuthorize("hasRole('COACH')")
```

JWT содержит:
- `sub` — стабильный UUID пользователя
- `roles` — массив ролей из групп Authentik
- `org_id` — UUID организации пользователя
- `judge_type` — подтип судьи (`main` / `corner` / `secretary`) если применимо

---

## Зависимости

### Gradle (build.gradle.kts)

```kotlin
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-oauth2-jose")
    // Kotlin coroutines (если используются)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
}
```

### Maven (pom.xml)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-oauth2-resource-server</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-oauth2-jose</artifactId>
</dependency>
```

---

## Конфигурация приложения

### application.yml

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          # Authentik публикует JWKS по этому URL.
          # Для локальной разработки: http://localhost:9000
          jwk-set-uri: ${AUTHENTIK_JWK_URI:http://localhost:9000/application/o/taekwondo-web/jwks/}
          # Проверка issuer — должен совпадать с AUTHENTIK_HOST/application/o/slug/
          issuer-uri: ${AUTHENTIK_ISSUER_URI:http://localhost:9000/application/o/taekwondo-web/}

authentik:
  client-id: ${AUTHENTIK_CLIENT_ID:taekwondo-web}
  client-secret: ${AUTHENTIK_CLIENT_SECRET:REPLACE_WITH_STRONG_SECRET}
  base-url: ${AUTHENTIK_BASE_URL:http://localhost:9000}
```

### application-local.yml (для разработки)

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: http://localhost:9000/application/o/taekwondo-web/jwks/
          issuer-uri: http://localhost:9000/application/o/taekwondo-web/
```

---

## Security Config

```kotlin
// src/main/kotlin/com/taekwondo/config/SecurityConfig.kt

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    // Публичные эндпоинты
                    .requestMatchers("/actuator/health", "/api/public/**").permitAll()
                    // Всё остальное — только с валидным токеном
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                }
            }
        return http.build()
    }

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter { jwt ->
            // Читаем claim "roles" из токена Authentik
            val roles = jwt.getClaimAsStringList("roles") ?: emptyList()
            // Преобразуем в Spring Security GrantedAuthority с префиксом ROLE_
            roles.map { role ->
                SimpleGrantedAuthority("ROLE_${role.uppercase()}")
            }
        }
        // Claim для отображения имени пользователя
        converter.setPrincipalClaimName("preferred_username")
        return converter
    }
}
```

---

## Извлечение ролей и атрибутов из токена

Удобный data class для доступа к claims текущего пользователя:

```kotlin
// src/main/kotlin/com/taekwondo/security/AuthenticatedUser.kt

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt

data class AuthenticatedUser(
    val jwt: Jwt
) {
    val userId: String get() = jwt.subject
    val username: String get() = jwt.getClaimAsString("preferred_username") ?: ""
    val email: String get() = jwt.getClaimAsString("email") ?: ""
    val orgId: String? get() = jwt.getClaimAsString("org_id")
    val roles: List<String> get() = jwt.getClaimAsStringList("roles") ?: emptyList()
    val judgeType: String? get() = jwt.getClaimAsString("judge_type")

    fun hasRole(role: String): Boolean = roles.contains(role)
    fun isJudge(): Boolean = hasRole("judge")
    fun isSuperAdmin(): Boolean = hasRole("superadmin")
}
```

Фабричный метод для использования в контроллерах:

```kotlin
// src/main/kotlin/com/taekwondo/security/UserExtensions.kt

import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

fun JwtAuthenticationToken.toAuthenticatedUser(): AuthenticatedUser =
    AuthenticatedUser(this.token as Jwt)
```

---

## Авторизация по ролям

С аннотацией `@PreAuthorize` на уровне метода:

```kotlin
import org.springframework.security.access.prepost.PreAuthorize

@RestController
@RequestMapping("/api/athletes")
class AthleteController {

    // Только тренер или администратор организации
    @PostMapping
    @PreAuthorize("hasAnyRole('COACH', 'ORG_ADMIN')")
    fun createAthlete(@RequestBody dto: AthleteDto): ResponseEntity<Athlete> {
        TODO()
    }

    // Только суперадмин
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    fun deleteAthlete(@PathVariable id: String): ResponseEntity<Void> {
        TODO()
    }

    // Спортсмен видит только свой профиль
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('COACH', 'ORG_ADMIN', 'SUPERADMIN') or " +
                  "(hasRole('ATHLETE') and #id == authentication.name)")
    fun getAthlete(@PathVariable id: String): ResponseEntity<Athlete> {
        TODO()
    }
}
```

### Таблица ролей → Spring Security

| Роль в Authentik | Spring Security role | Пример @PreAuthorize |
|---|---|---|
| `superadmin` | `ROLE_SUPERADMIN` | `hasRole('SUPERADMIN')` |
| `federation_representative` | `ROLE_FEDERATION_REPRESENTATIVE` | `hasRole('FEDERATION_REPRESENTATIVE')` |
| `org_admin` | `ROLE_ORG_ADMIN` | `hasRole('ORG_ADMIN')` |
| `coach` | `ROLE_COACH` | `hasRole('COACH')` |
| `athlete` | `ROLE_ATHLETE` | `hasRole('ATHLETE')` |
| `judge` | `ROLE_JUDGE` | `hasRole('JUDGE')` |

---

## Пример контроллера

```kotlin
// src/main/kotlin/com/taekwondo/controller/TournamentController.kt

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tournaments")
class TournamentController {

    /**
     * Список турниров — доступен всем аутентифицированным пользователям.
     */
    @GetMapping
    fun listTournaments(): List<Tournament> {
        TODO()
    }

    /**
     * Создание турнира — только представитель федерации или суперадмин.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('FEDERATION_REPRESENTATIVE', 'SUPERADMIN')")
    fun createTournament(@RequestBody dto: TournamentDto): Tournament {
        TODO()
    }

    /**
     * Управление счётом в поединке — только судья.
     * judge_type проверяется дополнительно в сервисном слое по токену.
     */
    @PostMapping("/{tournamentId}/bouts/{boutId}/score")
    @PreAuthorize("hasRole('JUDGE')")
    fun submitScore(
        @PathVariable tournamentId: String,
        @PathVariable boutId: String,
        @RequestBody score: ScoreDto,
        @AuthenticationPrincipal jwt: Jwt   // доступ к полному JWT
    ): ResponseEntity<Void> {
        val judgeType = jwt.getClaimAsString("judge_type")
        if (judgeType != "corner") {
            return ResponseEntity.status(403).build()
        }
        TODO()
    }

    /**
     * Взвешивание — только секретарь (judge_type = secretary).
     */
    @PostMapping("/{tournamentId}/weigh-in")
    @PreAuthorize("hasRole('JUDGE')")
    fun recordWeighIn(
        @PathVariable tournamentId: String,
        @RequestBody data: WeighInDto,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<Void> {
        val judgeType = jwt.getClaimAsString("judge_type")
        if (judgeType != "secretary") {
            return ResponseEntity.status(403).build()
        }
        TODO()
    }
}
```

---

## Тестирование локально

### 1. Запустить Authentik

```bash
docker compose up -d
```

### 2. Создать тестового пользователя

Admin UI (http://localhost:9000/if/admin/) → **Directory → Users → Create**

Добавить пользователя в группу (например, `coach`) → вкладка **Groups**.

### 3. Получить токен через curl (Resource Owner Password — только для тестов)

> Для настоящего тестирования используйте браузерный OAuth2 flow или Postman.

```bash
# Получить токен через Authorization Code flow (через браузер)
# Открыть URL в браузере:
# http://localhost:9000/application/o/authorize/?
#   client_id=taekwondo-web&
#   response_type=code&
#   redirect_uri=http://localhost:8080/login/oauth2/code/authentik&
#   scope=openid+profile+email+roles+org

# После редиректа взять code= из URL и обменять на токен:
curl -X POST http://localhost:9000/application/o/token/ \
  -d "grant_type=authorization_code" \
  -d "code=CODE_FROM_URL" \
  -d "client_id=taekwondo-web" \
  -d "client_secret=ВАШ_СЕКРЕТ" \
  -d "redirect_uri=http://localhost:8080/login/oauth2/code/authentik"
```

### 4. Декодировать и проверить JWT

```bash
# Установить jwt-cli (опционально)
brew install mike-engel/jwt-cli/jwt-cli

# Декодировать (без проверки подписи — только для просмотра claims)
jwt decode <ваш_токен>
```

Ожидаемые claims в токене:

```json
{
  "sub": "550e8400-e29b-41d4-a716-446655440000",
  "preferred_username": "ivanov_coach",
  "email": "ivanov@club.ru",
  "roles": ["coach"],
  "org_id": "org-uuid-here",
  "iss": "http://localhost:9000/application/o/taekwondo-web/",
  "exp": 1234567890
}
```

### 5. Сделать запрос к Spring API с токеном

```bash
TOKEN="eyJ..."   # вставить access_token из шага 3

curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/athletes
```

### 6. Проверить JWKS endpoint Authentik

```bash
curl http://localhost:9000/application/o/taekwondo-web/jwks/ | jq .
```

Spring Resource Server автоматически кеширует публичные ключи с этого URL и обновляет их при ротации.

### 7. Token Introspection (проверка валидности токена на стороне сервера)

```bash
curl -X POST http://localhost:9000/application/o/introspect/ \
  -u "taekwondo-web:ВАШ_СЕКРЕТ" \
  -d "token=$TOKEN"
```

Ответ:

```json
{
  "active": true,
  "username": "ivanov_coach",
  "sub": "...",
  "roles": ["coach"],
  ...
}
```
