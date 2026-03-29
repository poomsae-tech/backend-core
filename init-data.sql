-- Seed data для таблиц regions, federations, organizations, age_groups, belt_levels, disciplines, weight_categories

-- Регионы
INSERT INTO regions (id, name, deleted, created_at, created_by, updated_at, updated_by)
VALUES 
    (1, 'Москва', false, NOW(), 1, NULL, NULL),
    (2, 'Санкт-Петербург', false, NOW(), 1, NULL, NULL),
    (3, 'Республика Татарстан', false, NOW(), 1, NULL, NULL),
    (4, 'Краснодарский край', false, NOW(), 1, NULL, NULL),
    (5, 'Свердловская область', false, NOW(), 1, NULL, NULL);

-- Федерация общероссийская
INSERT INTO federations (id, name, region_id, federation_type, deleted, created_at, created_by, updated_at, updated_by)
VALUES 
    (1, 'Российская федерация тхэквондо', 1, 'ALL_RUSSIAN', false, NOW(), 1, NULL, NULL);

-- Региональные федерации
INSERT INTO federations (id, name, region_id, federation_type, deleted, created_at, created_by, updated_at, updated_by)
VALUES 
    (2, 'Федерация тхэквондо Москвы', 1, 'REGIONAL', false, NOW(), 1, NULL, NULL),
    (3, 'Федерация тхэквондо Санкт-Петербурга', 2, 'REGIONAL', false, NOW(), 1, NULL, NULL),
    (4, 'Федерация тхэквондо Республики Татарстан', 3, 'REGIONAL', false, NOW(), 1, NULL, NULL),
    (5, 'Федерация тхэквондо Краснодарского края', 4, 'REGIONAL', false, NOW(), 1, NULL, NULL);

-- Организации (клубы)
INSERT INTO organizations (id, name, inn, address, status, federation_id, region_id, deleted, created_at, created_by, updated_at, updated_by)
VALUES 
    (1, 'Клуб тхэквондо "Чемпион"', '7701234567', 'г. Москва, ул. Ленина, д. 10', 'ACCREDITED', 2, 1, false, NOW(), 1, NULL, NULL),
    (2, 'Спортивный клуб "Тхэквондо-Про"', '7702345678', 'г. Москва, проспект Мира, д. 25', 'ACCREDITED', 2, 1, false, NOW(), 1, NULL, NULL),
    (3, 'Клуб боевых искусств "Победа"', '7801234567', 'г. Санкт-Петербург, Невский проспект, д. 50', 'ACCREDITED', 3, 2, false, NOW(), 1, NULL, NULL),
    (4, 'Детский клуб тхэквондо "Лидер"', '1601234567', 'г. Казань, ул. Баумана, д. 15', 'PENDING', 4, 3, false, NOW(), 1, NULL, NULL),
    (5, 'Федерация тхэквондо "Юг"', '2301234567', 'г. Краснодар, ул. Красная, д. 100', 'ACCREDITED', 5, 4, false, NOW(), 1, NULL, NULL),
    (6, 'Клуб тхэквондо "Урал"', '6601234567', 'г. Екатеринбург, ул. Ленина, д. 30', 'PENDING', 1, 5, false, NOW(), 1, NULL, NULL);

-- Возрастные группы
INSERT INTO age_groups (id, name, min_age, max_age, deleted, created_at, created_by, updated_at, updated_by)
VALUES
    (1, 'Дети', 6, 11, false, NOW(), 1, NULL, NULL),
    (2, 'Кадеты', 12, 14, false, NOW(), 1, NULL, NULL),
    (3, 'Юниоры', 15, 17, false, NOW(), 1, NULL, NULL),
    (4, 'Взрослые', 18, 35, false, NOW(), 1, NULL, NULL),
    (5, 'Мастера', 36, 60, false, NOW(), 1, NULL, NULL);

-- Пояса
INSERT INTO belt_levels (id, name, deleted, created_at, created_by, updated_at, updated_by)
VALUES
    (1, '10 гып', false, NOW(), 1, NULL, NULL),
    (2, '9 гып', false, NOW(), 1, NULL, NULL),
    (3, '8 гып', false, NOW(), 1, NULL, NULL),
    (4, '7 гып', false, NOW(), 1, NULL, NULL),
    (5, '6 гып', false, NOW(), 1, NULL, NULL),
    (6, '5 гып', false, NOW(), 1, NULL, NULL),
    (7, '4 гып', false, NOW(), 1, NULL, NULL),
    (8, '3 гып', false, NOW(), 1, NULL, NULL),
    (9, '2 гып', false, NOW(), 1, NULL, NULL),
    (10, '1 гып', false, NOW(), 1, NULL, NULL),
    (11, '1 дан', false, NOW(), 1, NULL, NULL),
    (12, '2 дан', false, NOW(), 1, NULL, NULL),
    (13, '3 дан', false, NOW(), 1, NULL, NULL);

-- Дисциплины
INSERT INTO disciplines (id, name, deleted, created_at, created_by, updated_at, updated_by)
VALUES
    (1, 'Спарринг', false, NOW(), 1, NULL, NULL),
    (2, 'Туль', false, NOW(), 1, NULL, NULL);

-- Весовые категории
INSERT INTO weight_categories (id, name, min_weight, max_weight, gender, deleted, created_at, created_by, updated_at, updated_by)
VALUES
    (1, 'До 30 кг', 0, 30, 'MALE', false, NOW(), 1, NULL, NULL),
    (2, '30–35 кг', 30, 35, 'MALE', false, NOW(), 1, NULL, NULL),
    (3, '35–40 кг', 35, 40, 'MALE', false, NOW(), 1, NULL, NULL),
    (4, '40–45 кг', 40, 45, 'MALE', false, NOW(), 1, NULL, NULL),
    (5, '45–50 кг', 45, 50, 'MALE', false, NOW(), 1, NULL, NULL),
    (6, '50–55 кг', 50, 55, 'MALE', false, NOW(), 1, NULL, NULL),
    (7, 'До 30 кг (жен)', 0, 30, 'FEMALE', false, NOW(), 1, NULL, NULL),
    (8, '30–35 кг (жен)', 30, 35, 'FEMALE', false, NOW(), 1, NULL, NULL),
    (9, '35–40 кг (жен)', 35, 40, 'FEMALE', false, NOW(), 1, NULL, NULL),
    (10, '40–45 кг (жен)', 40, 45, 'FEMALE', false, NOW(), 1, NULL, NULL),
    (11, '45–50 кг (жен)', 45, 50, 'FEMALE', false, NOW(), 1, NULL, NULL),
    (12, '50–55 кг (жен)', 50, 55, 'FEMALE', false, NOW(), 1, NULL, NULL);