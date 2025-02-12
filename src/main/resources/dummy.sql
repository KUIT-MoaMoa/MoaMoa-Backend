-- 1️⃣ 챌린지 데이터 삽입
INSERT INTO challenges (title, content, head_count, duration, battle_coin, challenge_category, created_at, updated_at, status)
VALUES
    ('하루 10,000보 걷기', '매일 10,000보 걷기를 목표로 합니다.', 10, 30, 100, 'HOBBY', NOW(), NOW(), 'ACTIVE'),
    ('배달음식 줄이기', '한 달 동안 배달음식을 줄여보세요.', 5, 30, 50, 'DELIVERY_FOOD', NOW(), NOW(), 'ACTIVE');

-- 2️⃣ 유저 데이터 삽입
INSERT INTO users (login_id, password, nickname, image_url, coin, created_at, updated_at, status)
VALUES
    ('user1', 'password123', '도전왕', 'https://example.com/user1.jpg', 500, NOW(), NOW(), 'ACTIVE'),
    ('user2', 'password456', '절약왕', 'https://example.com/user2.jpg', 300, NOW(), NOW(), 'ACTIVE');

-- 3️⃣ 유저 그룹 생성 (challenge_id 1, 2가 존재해야 함)
INSERT INTO user_groups (challenge_id, title, created_at, updated_at, status)
VALUES
    (1, '절약특공대1',NOW(), NOW(), 'ACTIVE'),
    (2, '절약특공대2',NOW(), NOW(), 'ACTIVE');

-- 4️⃣ 유저-그룹 연결
INSERT INTO user_user_group_junction (user_id, user_group_id, created_at, updated_at, status)
VALUES
    (1, 1, NOW(), NOW(), 'ACTIVE'),
    (2, 2, NOW(), NOW(), 'ACTIVE');

-- 5️⃣ 채팅 데이터 삽입
INSERT INTO chats (user_group_id, content, user_id, created_at, updated_at, status)
VALUES
    (1, '안녕하세요! 챌린지 시작해볼까요?', 1, NOW(), NOW(), 'ACTIVE'),
    (2, '배달음식 덜 시켜보려고요!', 2, NOW(), NOW(), 'ACTIVE');

INSERT INTO items (NAME, PRICE, IMAGE_URL, STATUS, CREATED_AT, UPDATED_AT, item_id) VALUES
    ('멋진 테두리', 200, 'https://example.com/images.jpg', 'ACTIVE', '2024-02-08 10:30:00', '2024-02-08 10:30:00', 1);

INSERT INTO items (NAME, PRICE, IMAGE_URL, STATUS, CREATED_AT, UPDATED_AT, item_id) VALUES
    ('짱 멋진 테두리', 300, 'https://example.com/images2.jpg', 'ACTIVE', '2024-02-07 10:30:00', '2024-02-07 10:30:00', 2);

    INSERT INTO purchase_records (purchase_record_id, USER_ID, NAME, TRANSACTION, STATUS, CREATED_AT, UPDATED_AT) VALUES
    (1, 1, '멋멋진 테두리', 200, 'ACTIVE', '2024-02-07 14:30:00', '2024-02-07 14:30:00'),
    (2, 1, '멋멋멋진 테두리', 300, 'ACTIVE', '2024-02-08 14:30:00', '2024-02-08 14:30:00');

INSERT INTO challenge_records (END_DATE, START_DATE, CHALLENGE_RECORD_ID, CREATED_AT, TRANSACTION, UPDATED_AT, USER_ID, TITLE, STATUS) VALUES
    ('2024-06-17', '2023-11-18', 1, '2023-10-15 12:30:45', 100, '2023-03-13 14:22:10', 1, '밥값아끼기', 'ACTIVE');