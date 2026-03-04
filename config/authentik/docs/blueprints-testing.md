# Blueprints — функциональность и тестирование

Admin UI: **http://localhost:9000/if/admin/**

Blueprints применяются автоматически при старте worker в порядке `01 → 02 → 03 → 04 → 05`.

---

## 01 — Password Policy

**Файл:** `blueprints/01-password-policy.yaml`

### Что создаётся

Политика сложности паролей `taekwondo-password-policy`, применяемая в flows смены и сброса пароля.

| Правило | Значение |
|---|---|
| Минимальная длина | 10 символов |
| Заглавные буквы | минимум 1 |
| Строчные буквы | минимум 1 |
| Цифры | минимум 1 |
| Спецсимволы (`!@#$%^&*...`) | минимум 1 |
| Запрет username/email в пароле | да |
| Проверка HaveIBeenPwned | да (0 утечек разрешено) |

### Проверка

Admin UI → **Policy → Policies** → должна появиться `taekwondo-password-policy`.

**Test Policy:**
Admin UI → политика → **Test Policy** → выбрать пользователя → проверить значения:

| Пароль | Ожидаемый результат |
|---|---|
| `123456` | Отказ — слишком короткий |
| `password` | Отказ — нет цифр и спецсимволов |
| `Password1` | Отказ — нет спецсимвола, меньше 10 символов |
| `Password1!` | Успех |

---

## 02 — Groups and Roles

**Файл:** `blueprints/02-groups-roles.yaml`

### Что создаётся

8 групп с атрибутами `role`, `permissions` и (для судей) `judge_type`. Атрибуты попадают в JWT через scope mappings из blueprint 03.

| Группа | `role` в токене | `judge_type` | Суперпользователь |
|---|---|---|---|
| `superadmin` | `superadmin` | — | да |
| `federation_representative` | `federation_representative` | — | нет |
| `org_admin` | `org_admin` | — | нет |
| `coach` | `coach` | — | нет |
| `athlete` | `athlete` | — | нет |
| `judge_main` | `judge` | `main` | нет |
| `judge_corner` | `judge` | `corner` | нет |
| `judge_secretary` | `judge` | `secretary` | нет |

### Проверка

Admin UI → **Directory → Groups** → должно быть 8 групп.

**Тестирование ролей в токене:**
1. Создать тестового пользователя: **Directory → Users → Create**
2. Добавить в группу `coach`: вкладка **Groups → Add to group**
3. Получить токен (см. раздел 04) и декодировать — claim `roles` должен содержать `["coach"]`

Для пользователя в `judge_corner`:
```json
{
  "roles": ["judge"],
  "judge_type": "corner"
}
```

---

## 03 — OIDC Scope Mappings

**Файл:** `blueprints/03-scope-mappings.yaml`

### Что создаётся

| Scope | Claim | Источник |
|---|---|---|
| `roles` | `roles` | `group.attributes.role` (массив из всех групп пользователя) |
| `org` | `org_id` | `user.attributes.org_id` |
| `org` | `judge_type` | `group.attributes.judge_type` (только у judge-групп) |

### Проверка

Admin UI → **Customization → Property Mappings** → должны быть:
- `taekwondo-scope-roles`
- `taekwondo-scope-org`

**Test Mapping:**
Admin UI → `taekwondo-scope-roles` → **Test** → выбрать пользователя-тренера → ожидаемый результат:
```json
{ "roles": ["coach"] }
```

---

## 04 — OIDC Providers & Applications

**Файл:** `blueprints/04-oidc-providers.yaml`

### Что создаётся

| | Web | Mobile |
|---|---|---|
| **Client type** | Confidential | Public |
| **Flow** | Authorization Code | Authorization Code + PKCE |
| **client_secret** | да (задать перед тестом) | нет |
| **Access token** | 5 минут | 5 минут |
| **Refresh token** | 30 дней | 30 дней |
| **Redirect URIs** | `localhost:3000`, `localhost:8080` | `taekwondo://...`, `localhost:8081` |

### Перед тестированием

Заменить `REPLACE_WITH_STRONG_SECRET` в файле:

```bash
openssl rand -hex 32
```

Вставить результат в `04-oidc-providers.yaml` → сохранить → blueprint применится автоматически.

### Проверка

Admin UI → **Applications → Providers** → `taekwondo-web-provider` и `taekwondo-mobile-provider`  
Admin UI → **Applications → Applications** → `Taekwondo Web` и `Taekwondo Mobile`

OIDC Discovery:
```
http://localhost:9000/application/o/taekwondo-web/.well-known/openid-configuration
```

### Получение токена (Authorization Code flow)

**Шаг 1.** Открыть в браузере:
```
http://localhost:9000/application/o/authorize/?client_id=taekwondo-web&response_type=code&redirect_uri=http://localhost:8080/login/oauth2/code/authentik&scope=openid+profile+email+roles+org
```

Войти под тестовым пользователем → взять `code=` из URL редиректа.

**Шаг 2.** Обменять code на токен:
```bash
curl -X POST http://localhost:9000/application/o/token/ \
  -d "grant_type=authorization_code" \
  -d "code=КОД_ИЗ_URL" \
  -d "client_id=taekwondo-web" \
  -d "client_secret=ВАШ_СЕКРЕТ" \
  -d "redirect_uri=http://localhost:8080/login/oauth2/code/authentik"
```

**Шаг 3.** Декодировать `access_token`:
```bash
# jwt-cli (macOS: brew install mike-engel/jwt-cli/jwt-cli)
jwt decode ВАШ_ACCESS_TOKEN

# Или онлайн: https://jwt.io (только просмотр payload, без ввода секрета)
```

Ожидаемый payload для тренера:
```json
{
  "sub": "хэш-uuid",
  "preferred_username": "ivanov",
  "email": "ivanov@club.ru",
  "roles": ["coach"],
  "org_id": "uuid-организации",
  "iss": "http://localhost:9000/application/o/taekwondo-web/"
}
```

### Дополнительные проверки

```bash
# Token introspection
curl -X POST http://localhost:9000/application/o/introspect/ \
  -u "taekwondo-web:ВАШ_СЕКРЕТ" \
  -d "token=ВАШ_ACCESS_TOKEN"
# Ответ: { "active": true, ... }

# JWKS (публичные ключи для Spring)
curl http://localhost:9000/application/o/taekwondo-web/jwks/ | python3 -m json.tool
```

---

## 05 — Authentication Flows

**Файл:** `blueprints/05-flows.yaml`

### Что создаётся

| Flow | URL | Назначение |
|---|---|---|
| **Taekwondo Login** | `/if/flow/taekwondo-login/` | Вход по email/username + пароль |
| **Taekwondo Password Change** | `/if/flow/taekwondo-password-change/` | Смена пароля (требует авторизации) |
| **Taekwondo Password Reset** | `/if/flow/taekwondo-password-reset/` | Восстановление пароля по email |

### Проверка

Admin UI → **Flows → Flows** → три flow с префиксом `Taekwondo`.

### Тест 1 — Вход

1. Открыть http://localhost:9000/if/flow/taekwondo-login/
2. Ввести email/username и пароль тестового пользователя
3. Ожидаемый результат: успешный вход

**Защита от перебора:** 5 неверных попыток → flow прерывается.  
**pretend_user_exists:** несуществующий email → то же сообщение об ошибке, что и для неверного пароля.

### Тест 2 — Смена пароля

> Требует: пользователь авторизован в сессии Authentik.

1. Войти через login flow
2. Открыть http://localhost:9000/if/flow/taekwondo-password-change/
3. Ввести новый пароль

| Новый пароль | Ожидаемый результат |
|---|---|
| `short` | Отказ — слишком короткий |
| `NoSpecial123` | Отказ — нет спецсимвола |
| `Correct!Pass1` | Успех |

### Тест 3 — Восстановление пароля

> Требует: настроенный SMTP или MailHog (см. `docs/devops-authentik.md`).

1. Открыть http://localhost:9000/if/flow/taekwondo-password-reset/
2. Ввести email существующего пользователя
3. Получить письмо (ссылка действует 30 минут)
4. Перейти по ссылке → ввести новый пароль → автоматический вход

---

## Быстрый чеклист после применения всех blueprints

```
[ ] Policy → Policies: есть taekwondo-password-policy
[ ] Directory → Groups: есть 8 групп
[ ] Customization → Property Mappings: есть taekwondo-scope-roles, taekwondo-scope-org
[ ] Applications → Providers: есть taekwondo-web-provider, taekwondo-mobile-provider
[ ] Applications → Applications: есть Taekwondo Web, Taekwondo Mobile
[ ] Flows → Flows: есть 3 flow с префиксом Taekwondo
[ ] http://localhost:9000/application/o/taekwondo-web/.well-known/openid-configuration — JSON
[ ] http://localhost:9000/if/flow/taekwondo-login/ — кастомный UI, форма входа
[ ] Login flow: успешный вход тестового пользователя
[ ] Password change flow: смена пароля с проверкой политики
[ ] Токен содержит claims: roles, org_id (если задан)
```
