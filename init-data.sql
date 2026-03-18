-- Seed data для таблиц regions, federations, organizations

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
