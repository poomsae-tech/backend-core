# Taekwondo Platform — Authentik SSO

## Стек

- **Authentik 2026.2** — Identity Provider (OAuth2/OIDC, JWT, flows)
- **PostgreSQL 16** — база данных Authentik

## Быстрый старт

```bash
# 1. Создать .env из примера и сгенерировать секреты
cp .env.example .env
echo "PG_PASS=$(openssl rand -base64 36 | tr -d '\n')" >> .env
echo "AUTHENTIK_SECRET_KEY=$(openssl rand -base64 60 | tr -d '\n')" >> .env

# 2. Запустить
docker compose up -d

# 3. Получить пароль администратора
docker compose logs worker | grep "Initial admin"
```

- **Admin UI:** http://localhost:9000/if/admin/
- **Login:** http://localhost:9000/if/flow/taekwondo-login/

## Структура проекта

```
.
├── compose.yml                     — Docker Compose (server, worker, postgresql)
├── .env.example                    — пример переменных окружения
├── blueprints/
│   ├── 01-password-policy.yaml    — политика сложности паролей
│   ├── 02-groups-roles.yaml       — 8 групп: иерархия ролей
│   ├── 03-scope-mappings.yaml     — кастомные JWT claims (roles, org_id, judge_type)
│   ├── 04-oidc-providers.yaml     — OAuth2/OIDC провайдеры (web + mobile PKCE)
│   └── 05-flows.yaml              — flows: вход, смена и сброс пароля
├── custom-templates/
│   └── if/flow.html               — кастомный UI (тёмная тема, брендинг тхэквондо)
└── docs/
    ├── devops-authentik.md        — DevOps: первый запуск, email, production checklist
    ├── blueprints-testing.md      — что создаёт каждый blueprint и как тестировать
    ├── designer-auth-forms.md     — кастомизация UI форм (CSS, шаблоны)
    └── spring-integration.md     — интеграция Kotlin Spring (JWT, роли, примеры)
```

## Роли пользователей

| Роль | JWT `roles` | `judge_type` | Описание |
|---|---|---|---|
| SuperAdmin | `superadmin` | — | Глобальное управление платформой |
| Представитель федерации | `federation_representative` | — | Аккредитация клубов, назначение судей |
| Администратор организации | `org_admin` | — | Управление клубом, тренерами, атлетами |
| Тренер | `coach` | — | Создание профилей, подача заявок |
| Спортсмен | `athlete` | — | Личный кабинет (только чтение) |
| Главный судья | `judge` | `main` | Управление процессом турнира |
| Угловой судья | `judge` | `corner` | Выставление счёта через мобильный пульт |
| Секретарь | `judge` | `secretary` | Взвешивание и секретариат |

## Документация

| Файл | Аудитория |
|---|---|
| [docs/devops-authentik.md](docs/devops-authentik.md) | DevOps: запуск, настройка, production |
| [docs/blueprints-testing.md](docs/blueprints-testing.md) | Разработчики: тестирование blueprints |
| [docs/designer-auth-forms.md](docs/designer-auth-forms.md) | Дизайнеры: кастомизация UI форм |
| [docs/spring-integration.md](docs/spring-integration.md) | Backend: интеграция Kotlin Spring |
