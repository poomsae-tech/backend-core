# Кастомные формы Authentik — Руководство дизайнера

## Содержание

1. [Как работает рендеринг форм](#как-работает-рендеринг-форм)
2. [Структура шаблонного файла](#структура-шаблонного-файла)
3. [CSS-переменные и брендинг](#css-переменные-и-брендинг)
4. [Как включить и отключить кастомный шаблон](#как-включить-и-отключить-кастомный-шаблон)
5. [CSS-кастомизация через Brand settings (без файлов)](#css-кастомизация-через-brand-settings)
6. [Частые ошибки](#частые-ошибки)

---

## Как работает рендеринг форм

Authentik (начиная с версии ~2022.x) использует **JavaScript SPA** для рендеринга форм flow. Страница `/if/flow/<slug>/` — это Single-Page Application на веб-компонентах (Lit), а не Django-шаблон.

```
Браузер → /if/flow/taekwondo-login/
    ↓
Django рендерит HTML-оболочку: if/flow.html
    ↓
HTML загружает JS-бандл: dist/flow/FlowInterface-*.js
    ↓
<ak-flow-executor> рисует форму на клиенте (JS)
```

**Что можно переопределить через Django-шаблон:**
- HTML-оболочку страницы (фон, структура, мета-теги)
- CSS-стили, загружаемые до JS
- Дополнительные скрипты

**Что нельзя изменить через Django-шаблон:**
- Поля ввода, кнопки и сообщения об ошибках (рендерятся JS)

Для кастомизации самих полей и кнопок используйте CSS-переменные PatternFly и кастомный CSS.

---

## Структура шаблонного файла

Активный шаблон проекта: **`custom-templates/if/flow.html`**

Этот файл переопределяет встроенный `/authentik/flows/templates/if/flow.html` для **всех** flow (вход, смена пароля, сброс пароля).

### Правила файла

1. Расширяет `base/skeleton.html` (не `if/flow.html` — это вызвало бы бесконечную рекурсию)
2. Обязательно сохраняет `{% block head_before %}` с загрузкой JS и CSS-бандла
3. Обязательно сохраняет `<ak-flow-executor>` в блоке `body`

Минимальный шаблон с кастомным CSS:

```html
{% extends "base/skeleton.html" %}
{% load i18n %}
{% load static %}
{% load authentik_core %}

{% block head_before %}
{{ block.super }}
{% include "base/header_js.html" %}
<script data-id="flow-config">
    "use strict";
    window.authentik.flow = { "layout": "{{ flow.layout }}" };
</script>
<link rel="stylesheet" type="text/css"
      href="{% versioned_script 'dist/styles/static-%v.css' %}" />
{% endblock %}

{% block head %}
<script src="{% versioned_script 'dist/flow/FlowInterface-%v.js' %}"
        type="module"></script>
<style>
  /* Ваш кастомный CSS */
  .pf-c-login {
    background-color: #1A1A2E;
  }
</style>
{% endblock %}

{% block body %}
<ak-flow-executor
    slug="{{ flow.slug }}"
    class="pf-c-login"
    data-layout="{{ flow.layout|default:'stacked' }}"
    loading
>
    {% include "base/placeholder.html" %}
    <ak-brand-links name="flow-links" slot="footer"></ak-brand-links>
</ak-flow-executor>
{% endblock %}
```

---

## CSS-переменные и брендинг

Текущий шаблон проекта (`custom-templates/if/flow.html`) использует CSS-переменные для брендинга. Чтобы адаптировать под свой дизайн — изменить значения в блоке `:root`:

```css
:root {
  --brand-primary:    #C0392B;   /* основной акцентный цвет (кнопки, ссылки) */
  --brand-secondary:  #1A1A2E;   /* фоновый цвет страницы                     */
  --brand-surface:    #16213E;   /* цвет карточки/панели формы                */
  --brand-text:       #EAEAEA;   /* основной цвет текста                      */
  --brand-muted:      #8892A4;   /* цвет подписей/placeholder                 */
  --brand-border:     #2D3561;   /* цвет рамки полей ввода                    */
  --brand-radius:     8px;       /* скругление углов                          */
  --brand-font:       'Inter', system-ui, sans-serif;
}
```

### Добавить логотип

В блоке `body`, перед `<ak-flow-executor>`:

```html
{% block body %}
<div style="text-align:center; padding: 2rem 0 1rem;">
  <img src="/path/to/logo.svg" alt="Логотип" style="height:56px;">
</div>
<ak-flow-executor ...>
```

Или разместить логотип через фоновое изображение:

```css
.pf-c-login::before {
  content: '';
  display: block;
  height: 80px;
  background: url('/static/logo.svg') center / contain no-repeat;
  margin-bottom: 1.5rem;
}
```

### Полезные CSS-селекторы PatternFly

| Элемент | Селектор |
|---|---|
| Страница/фон | `.pf-c-login` |
| Карточка формы | `.pf-c-login__main` |
| Заголовок карточки | `.pf-c-login__main-header` |
| Поля ввода | `.pf-c-form-control` |
| Кнопка отправки | `.pf-m-primary` |
| Сообщения об ошибках | `.pf-c-alert.pf-m-danger` |
| Ссылки в footer | `.pf-c-login__main-footer-links-item-link` |

---

## Как включить и отключить кастомный шаблон

### Текущее состояние

Шаблон **активен** — файл `custom-templates/if/flow.html` существует.

### Отключить (вернуть стандартный UI)

```bash
mv custom-templates/if/flow.html custom-templates/if/flow.html.disabled
docker compose restart server
```

### Включить обратно

```bash
mv custom-templates/if/flow.html.disabled custom-templates/if/flow.html
docker compose restart server
```

> Перезапуск `server` обязателен — Django кеширует шаблоны при старте.

---

## CSS-кастомизация через Brand settings

Альтернативный способ — без файлов шаблонов. Подходит для небольших правок цветов и шрифтов.

1. Admin UI → **System → Brands** → Edit
2. Поле **Custom CSS** — вставить CSS

```css
:root {
  --ak-accent: #C0392B;
}
.pf-c-login {
  background-color: #1A1A2E;
}
.pf-c-login__main {
  background: #16213E;
  border: 1px solid #2D3561;
}
```

CSS из Brand settings вставляется в `<style data-id="brand-css">` внутри `<head>` — работает в любом режиме, даже без файла шаблона.

---

## Частые ошибки

### Шаблон не применяется

Убедиться, что:
1. Файл находится по пути `custom-templates/if/flow.html` (именно `if/` подпапка)
2. `server` перезапущен после добавления/изменения файла

### Страница сломана (белый экран / пустая страница)

Проверить, что в `{% block head_before %}` присутствует `{{ block.super }}` и вызов `{% include "base/header_js.html" %}`.

Проверить, что `<ak-flow-executor>` не удалён из блока `body`.

### Стили не применяются

Добавить к CSS-правилам `!important` — PatternFly и Authentik используют специфичные селекторы, которые могут переопределить ваши стили.

### Изменения не отображаются

Очистить кеш браузера: `Ctrl+Shift+R` / `Cmd+Shift+R`.

### Шаблон распространяется на все flows

Это ожидаемое поведение: `if/flow.html` — единый шаблон для всех flows. Чтобы различать flows в CSS, используйте атрибут `slug`:

```css
ak-flow-executor[slug="taekwondo-login"] {
  /* стили только для login flow */
}
```
