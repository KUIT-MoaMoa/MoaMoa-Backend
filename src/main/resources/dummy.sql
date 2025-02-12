-- 1️⃣ 사용자 데이터 삽입
INSERT INTO users (email, password, nickname, image_url, coin, role, created_at, updated_at, status)
VALUES
    ('user1@gmail.com', 'password123', '도전왕', 'https://example.com/user1.jpg', 1000, 'USER', NOW(), NOW(), 'ACTIVE'),
    ('user2@gmail.com', 'password456', '절약왕', 'https://example.com/user2.jpg', 800, 'USER', NOW(), NOW(), 'ACTIVE'),
    ('user3@gmail.com', 'password789', '저축왕', 'https://example.com/user3.jpg', 500, 'USER', NOW(), NOW(), 'ACTIVE');


-- 2️⃣ 유저 그룹 생성
INSERT INTO user_groups (title, created_at, updated_at, status)
VALUES
    ('절약특공대1', NOW(), NOW(), 'ACTIVE'),
    ('절약특공대2', NOW(), NOW(), 'ACTIVE');


-- 3️⃣ 유저-그룹 연결
INSERT INTO user_user_group_junction (user_id, user_group_id, created_at, updated_at, status)
VALUES
    (1, 1, NOW(), NOW(), 'ACTIVE'),
    (2, 2, NOW(), NOW(), 'ACTIVE');

-- 4️⃣ 챌린지 데이터 삽입
INSERT INTO challenges (title, content, head_count, duration, public_challenge, goal_amount, battle_coin,
                        challenge_category, start_date, end_date, recruitment_deadline,
                        created_at, updated_at, challenge_status, user_group_id)
VALUES
    -- 모집 중인 챌린지
    ('30일 식비 절약', '한 달 동안 식비 30만원 이내로 쓰기', 5, 30, true, 300000, 100,
     'DELIVERY_FOOD', DATEADD('DAY', 2, CURRENT_DATE), DATEADD('DAY', 32, CURRENT_DATE),
     DATEADD('DAY', 1, CURRENT_DATE), NOW(), NOW(), 'RECRUITING', 1),

    -- 진행 중인 챌린지
    ('카페인 줄이기', '한 달 동안 카페인 음료 5만원 이내로 쓰기', 3, 30, true, 50000, 150,
     'COFFEE', DATEADD('DAY', -5, CURRENT_DATE), DATEADD('DAY', 25, CURRENT_DATE),
     DATEADD('DAY', -6, CURRENT_DATE), NOW(), NOW(), 'ONGOING', NULL),

    -- 완료된 챌린지
    ('배달비 아끼기', '배달비 절약하기', 4, 30, true, 100000, 200,
     'DELIVERY_FOOD', DATEADD('DAY', -40, CURRENT_DATE), DATEADD('DAY', -10, CURRENT_DATE),
     DATEADD('DAY', -41, CURRENT_DATE), NOW(), NOW(), 'COMPLETED', 2),

    -- 비공개 챌린지
    ('친구들과 술값 줄이기', '한 달 동안 술값 10만원 이내로 쓰기', 3, 30, false, 100000, 100,
     'DRINKING', DATEADD('DAY', 3, CURRENT_DATE), DATEADD('DAY', 33, CURRENT_DATE),
     DATEADD('DAY', 2, CURRENT_DATE), NOW(), NOW(), 'RECRUITING', NULL);

-- 5️⃣ 챌린지 진행상황 데이터 삽입
INSERT INTO challenge_progress (challenge_id, user_id, used_amount, is_goal_achieved, reward_claimed,
                                created_at, updated_at, status)
VALUES
    -- 모집 중인 챌린지 참가자
    (1, 1, 0, false, false, NOW(), NOW(), 'ACTIVE'),
    (1, 2, 0, false, false, NOW(), NOW(), 'ACTIVE'),

    -- 진행 중인 챌린지 참가자
    (2, 1, 30000, false, false, NOW(), NOW(), 'ACTIVE'),
    (2, 2, 20000, false, false, NOW(), NOW(), 'ACTIVE'),
    (2, 3, 15000, false, false, NOW(), NOW(), 'ACTIVE'),

    -- 완료된 챌린지 참가자
    (3, 1, 80000, true, false, NOW(), NOW(), 'ACTIVE'),
    (3, 2, 90000, true, true, NOW(), NOW(), 'ACTIVE'),

    -- 비공개 챌린지 참가자
    (4, 1, 0, false, false, NOW(), NOW(), 'ACTIVE'),
    (4, 2, 0, false, false, NOW(), NOW(), 'ACTIVE');

-- 6️⃣ 친구 관계 데이터 삽입
INSERT INTO friendships (to_user_id, from_user_id, created_at, updated_at, status)
VALUES
    (1, 2, NOW(), NOW(), 'ACTIVE'),
    (2, 1, NOW(), NOW(), 'ACTIVE'),
    (1, 3, NOW(), NOW(), 'ACTIVE'),
    (3, 1, NOW(), NOW(), 'ACTIVE');

-- 7️⃣ 채팅 데이터 삽입
INSERT INTO chats (user_group_id, user_id, content, created_at, updated_at, status)
VALUES
    (1, 1, '안녕하세요! 챌린지 시작해볼까요?', NOW(), NOW(), 'ACTIVE'),
    (2, 2, '배달음식 덜 시켜보려고요!', NOW(), NOW(), 'ACTIVE');

INSERT INTO items (NAME, PRICE, IMAGE_URL, STATUS, CREATED_AT, UPDATED_AT, item_id) VALUES
    ('멋진 테두리', 200, 'https://example.com/images.jpg', 'ACTIVE', '2024-02-08 10:30:00', '2024-02-08 10:30:00', 1);

INSERT INTO items (NAME, PRICE, IMAGE_URL, STATUS, CREATED_AT, UPDATED_AT, item_id) VALUES
    ('짱 멋진 테두리', 300, 'https://example.com/images2.jpg', 'ACTIVE', '2024-02-07 10:30:00', '2024-02-07 10:30:00', 2);

INSERT INTO items (NAME, PRICE, IMAGE_URL, STATUS, CREATED_AT, UPDATED_AT, item_id) VALUES
    ('짱 멋진 테두리2', 300, 'https://example.com/images2.jpg', 'ACTIVE', '2024-02-07 10:30:00', '2024-02-07 10:30:00', 3);

INSERT INTO items (NAME, PRICE, IMAGE_URL, STATUS, CREATED_AT, UPDATED_AT, item_id) VALUES
    ('짱 멋진 테두리3', 300, 'https://example.com/images2.jpg', 'ACTIVE', '2024-02-07 10:30:00', '2024-02-07 10:30:00', 4);

INSERT INTO items (NAME, PRICE, IMAGE_URL, STATUS, CREATED_AT, UPDATED_AT, item_id) VALUES
    ('짱 멋진 테두리4', 300, 'https://example.com/images2.jpg', 'ACTIVE', '2024-02-07 10:30:00', '2024-02-07 10:30:00', 5);

INSERT INTO purchase_records (purchase_record_id, USER_ID, NAME, TRANSACTION, STATUS, CREATED_AT, UPDATED_AT) VALUES
    (1, 1, '멋멋진 테두리', 200, 'ACTIVE', '2024-02-07 14:30:00', '2024-02-07 14:30:00'),
    (2, 1, '멋멋멋진 테두리', 300, 'ACTIVE', '2024-02-08 14:30:00', '2024-02-08 14:30:00');

INSERT INTO challenge_records (END_DATE, START_DATE, CHALLENGE_RECORD_ID, CREATED_AT, TRANSACTION, UPDATED_AT, USER_ID, TITLE, STATUS) VALUES
    ('2024-06-17', '2023-11-18', 1, '2023-10-15 12:30:45', 100, '2023-03-13 14:22:10', 1, '밥값아끼기', 'ACTIVE');