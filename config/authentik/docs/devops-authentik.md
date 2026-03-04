# Authentik SSO — Руководство DevOps

## Содержание

1. [Архитектура](#архитектура)
2. [Первый запуск](#первый-запуск)
3. [Переменные окружения](#переменные-окружения)
4. [Настройка почтового сервера](#настройка-почтового-сервера)
5. [Blueprints](#blueprints)
6. [Роли и группы](#роли-и-группы)
7. [Кастомные шаблоны UI](#кастомные-шаблоны-ui)
8. [Смена client_secret (OIDC)](#смена-client_secret-oidc)
9. [Резервное копирование](#резервное-копирование)
10. [Checklist перед production](#checklist-перед-production)

---

## Архитектура

```
docker compose
├── postgresql   — база данных Authentik
├── server       — HTTP-сервер + Admin UI (порты 9000 / 9443)
└── worker       — фоновые задачи: email, blueprints, outposts
```

| Монтирование | Хост | Контейнер |
|---|---|---|
| Blueprints | `./blueprints/` | `/blueprints/custom/` (worker) |
| Кастомные шаблоны | `./custom-templates/` | `/templates/` (server + worker) |
| Данные | `./data/` | `/data/` |

---

## Первый запуск

### 1. Подготовить `.env`

Скопировать `.env.example` в `.env` и сгенерировать секреты:

```bash
cp .env.example .env
echo "PG_PASS=$(openssl rand -base64 36 | tr -d '\n')" >> .env
echo "AUTHENTIK_SECRET_KEY=$(openssl rand -base64 60 | tr -d '\n')" >> .env
```

> **Важно:** `AUTHENTIK_SECRET_KEY` нельзя менять после первого запуска — он используется для шифрования данных в БД.

### 2. Запустить сервисы

```bash
docker compose pull
docker compose up -d
```

Дождаться готовности (30–60 секунд):

```bash
docker compose logs -f server
# Ждём: "Starting HTTP server"
```

### 3. Получить пароль администратора

```bash
docker compose logs worker | grep "Initial admin"
```

Или создать вручную:

```bash
docker compose exec worker ak create_admin_group --password <пароль>
```

### 4. Открыть UI

- **Admin UI:** http://localhost:9000/if/admin/
- **User UI:** http://localhost:9000/if/user/

Войти с `akadmin` и паролем из шага 3.

---

## Переменные окружения

Все переменные задаются в `.env`.

| Переменная | Обязательна | Описание |
|---|---|---|
| `PG_PASS` | Да | Пароль PostgreSQL (максимум 99 символов) |
| `PG_USER` | Нет (default: `authentik`) | Пользователь PostgreSQL |
| `PG_DB` | Нет (default: `authentik`) | Имя базы данных |
| `AUTHENTIK_SECRET_KEY` | Да | Ключ шифрования. Минимум 50 символов. **Не менять после первого запуска.** |
| `AUTHENTIK_ERROR_REPORTING__ENABLED` | Нет | Анонимные отчёты об ошибках (`true` / `false`) |
| `COMPOSE_PORT_HTTP` | Нет (default: `9000`) | Внешний HTTP-порт |
| `COMPOSE_PORT_HTTPS` | Нет (default: `9443`) | Внешний HTTPS-порт |
| `AUTHENTIK_EMAIL__*` | Нет | Настройки SMTP (см. ниже) |

---

## Настройка почтового сервера

Почта нужна для отправки ссылки сброса пароля и уведомлений администратора.

Добавить в `.env`:

```dotenv
AUTHENTIK_EMAIL__HOST=smtp.example.com
AUTHENTIK_EMAIL__PORT=587
AUTHENTIK_EMAIL__USERNAME=noreply@example.com
AUTHENTIK_EMAIL__PASSWORD=your_smtp_password
AUTHENTIK_EMAIL__USE_TLS=true
AUTHENTIK_EMAIL__USE_SSL=false
AUTHENTIK_EMAIL__FROM=noreply@example.com
AUTHENTIK_EMAIL__TIMEOUT=10
```

### Примеры для популярных провайдеров

#### Gmail (App Password)

> Требует 2FA и App Password в Google Account.

```dotenv
AUTHENTIK_EMAIL__HOST=smtp.gmail.com
AUTHENTIK_EMAIL__PORT=587
AUTHENTIK_EMAIL__USERNAME=your@gmail.com
AUTHENTIK_EMAIL__PASSWORD=xxxx_xxxx_xxxx_xxxx
AUTHENTIK_EMAIL__USE_TLS=true
AUTHENTIK_EMAIL__FROM=your@gmail.com
```

#### Яндекс Почта

```dotenv
AUTHENTIK_EMAIL__HOST=smtp.yandex.ru
AUTHENTIK_EMAIL__PORT=587
AUTHENTIK_EMAIL__USERNAME=noreply@yourdomain.ru
AUTHENTIK_EMAIL__PASSWORD=your_password
AUTHENTIK_EMAIL__USE_TLS=true
AUTHENTIK_EMAIL__FROM=noreply@yourdomain.ru
```

#### Mail.ru

```dotenv
AUTHENTIK_EMAIL__HOST=smtp.mail.ru
AUTHENTIK_EMAIL__PORT=465
AUTHENTIK_EMAIL__USERNAME=noreply@mail.ru
AUTHENTIK_EMAIL__PASSWORD=your_password
AUTHENTIK_EMAIL__USE_TLS=false
AUTHENTIK_EMAIL__USE_SSL=true
AUTHENTIK_EMAIL__FROM=noreply@mail.ru
```

#### Office 365

```dotenv
AUTHENTIK_EMAIL__HOST=smtp.office365.com
AUTHENTIK_EMAIL__PORT=587
AUTHENTIK_EMAIL__USERNAME=noreply@company.com
AUTHENTIK_EMAIL__PASSWORD=your_password
AUTHENTIK_EMAIL__USE_TLS=true
AUTHENTIK_EMAIL__FROM=noreply@company.com
```

### Проверка

После изменения `.env`:

```bash
docker compose up -d
```

Admin UI → **System → Email → Test** — отправить тестовое письмо.

### Локальный SMTP для разработки (MailHog)

```bash
docker run -d -p 1025:1025 -p 8025:8025 mailhog/mailhog
```

```dotenv
AUTHENTIK_EMAIL__HOST=host.docker.internal
AUTHENTIK_EMAIL__PORT=1025
AUTHENTIK_EMAIL__USE_TLS=false
AUTHENTIK_EMAIL__FROM=noreply@taekwondo.local
```

Веб-интерфейс MailHog: http://localhost:8025

---

## Blueprints

Blueprints — декларативные YAML-файлы в `./blueprints/`, автоматически применяемые при старте worker. Blueprints идемпотентны: повторное применение обновляет объекты, не создаёт дубликаты.

### Структура

```
blueprints/
├── 01-password-policy.yaml   — политика сложности паролей
├── 02-groups-roles.yaml      — группы и иерархия ролей
├── 03-scope-mappings.yaml    — кастомные JWT claims (roles, org_id, judge_type)
├── 04-oidc-providers.yaml    — OAuth2/OIDC провайдеры и приложения
└── 05-flows.yaml             — flows: вход, смена и сброс пароля
```

> **Порядок важен:** `01 → 02 → 03 → 04 → 05`.  
> Blueprint 04 зависит от 03 (scope mappings ищутся через `!Find` по имени в БД).

### Применение вручную

Через Admin UI: **System → Blueprints → Run**.

Через CLI:

```bash
docker compose exec worker ak apply_blueprint /blueprints/custom/01-password-policy.yaml
```

### После редактирования файла

Worker перехватывает изменение по хешу файла и перезапускает применение автоматически. Если нужно принудительно — перезапустить worker:

```bash
docker compose restart worker
```

---

## Роли и группы

Группы создаются blueprint `02-groups-roles.yaml`.

### Доступные группы

| Группа | `role` в токене | `judge_type` | Суперпользователь Authentik |
|---|---|---|---|
| `superadmin` | `superadmin` | — | да |
| `federation_representative` | `federation_representative` | — | нет |
| `org_admin` | `org_admin` | — | нет |
| `coach` | `coach` | — | нет |
| `athlete` | `athlete` | — | нет |
| `judge_main` | `judge` | `main` | нет |
| `judge_corner` | `judge` | `corner` | нет |
| `judge_secretary` | `judge` | `secretary` | нет |

### Назначение роли пользователю

Admin UI → **Directory → Users** → выбрать пользователя → вкладка **Groups → Add to group**.

### Атрибут `org_id`

Привязка пользователя к организации — добавить вручную:

1. Открыть пользователя → **Attributes**
2. Ключ: `org_id`, значение: UUID организации из бэкенда

Атрибут автоматически попадёт в JWT claim `org_id` через scope mapping `taekwondo-scope-org`.

### Настройка домена (production)

Admin UI → **System → Brands** → Edit → поле **Domain** → указать реальный домен (например, `auth.taekwondo.ru`).

Домен влияет на URL в письмах сброса пароля.

---

## Кастомные шаблоны UI

Шаблоны монтируются из `./custom-templates/` в `/templates/` внутри контейнера. Файлы из `/templates/` имеют приоритет над встроенными шаблонами Authentik.

Активный кастомный шаблон: `custom-templates/if/flow.html` — переопределяет страницу всех flows (вход, смена пароля, сброс пароля).

### Отключить кастомный шаблон

```bash
mv custom-templates/if/flow.html custom-templates/if/flow.html.disabled
docker compose restart server
```

### Подробнее

См. [docs/designer-auth-forms.md](designer-auth-forms.md).

---

## Смена client_secret (OIDC)

```bash
openssl rand -hex 32
```

1. Обновить в Admin UI → **Applications → Providers → taekwondo-web-provider → Client Secret**
2. Обновить в конфигурации Spring Backend (переменная `AUTHENTIK_CLIENT_SECRET`)

---

## Резервное копирование

```bash
# Создать резервную копию (сохраняется в ./data/backups/)
docker compose exec worker ak backup

# Восстановить
docker compose exec worker ak restore /backups/your-backup.tar.gz

# Дамп PostgreSQL напрямую
docker compose exec postgresql pg_dump -U authentik authentik > backup_$(date +%Y%m%d).sql
```

---

## Checklist перед production

- [ ] Сгенерированы уникальные `PG_PASS` и `AUTHENTIK_SECRET_KEY`
- [ ] `AUTHENTIK_SECRET_KEY` сохранён в защищённом хранилище (Vault, AWS Secrets Manager и т.д.)
- [ ] Настроен SMTP и проверена отправка тестового письма
- [ ] Домен в Brands обновлён на реальный
- [ ] `client_secret` в `04-oidc-providers.yaml` заменён (`REPLACE_WITH_STRONG_SECRET` → реальный секрет)
- [ ] Redirect URIs в провайдерах обновлены на production URL
- [ ] Настроен HTTPS (порт 9443 или reverse proxy с TLS termination)
- [ ] Blueprints применены в правильном порядке (`01 → 02 → 03 → 04 → 05`)
- [ ] `AUTHENTIK_ERROR_REPORTING__ENABLED` выставлен по политике компании
- [ ] Настроен мониторинг (healthcheck уже есть в `compose.yml`)
- [ ] Настроено автоматическое резервное копирование
- [ ] Кастомный шаблон `custom-templates/if/flow.html` обновлён финальным дизайном
