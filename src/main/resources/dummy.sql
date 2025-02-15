-- 1️⃣ 더 다양한 사용자 데이터 삽입
INSERT INTO users (email, password, nickname, image_url, coin, role, created_at, updated_at, status)
VALUES
    ('user1@gmail.com', 'password123', '도전왕', 'https://example.com/user1.jpg', 1000, 'USER', NOW(), NOW(), 'ACTIVE'),
    ('user2@gmail.com', 'password456', '절약왕', 'https://example.com/user2.jpg', 800, 'USER', NOW(), NOW(), 'ACTIVE'),
    ('user3@gmail.com', 'password789', '저축왕', 'https://example.com/user3.jpg', 500, 'USER', NOW(), NOW(), 'ACTIVE'),
    ('user4@gmail.com', 'password101', '배달매니아', 'https://example.com/user4.jpg', 1200, 'USER', NOW(), NOW(), 'ACTIVE'),
    ('user5@gmail.com', 'password102', '커피홀릭', 'https://example.com/user5.jpg', 300, 'USER', NOW(), NOW(), 'ACTIVE'),
    ('user6@gmail.com', 'password103', '술최강', 'https://example.com/user6.jpg', 2000, 'USER', NOW(), NOW(), 'ACTIVE'),
    ('admin@moamoa.com', 'adminpass', '관리자', 'https://example.com/admin.jpg', 9999, 'ADMIN', NOW(), NOW(), 'ACTIVE');

-- 2️⃣ 다양한 유저 그룹 생성
INSERT INTO user_groups (title, created_at, updated_at, status)
VALUES
    ('절약특공대1', NOW(), NOW(), 'ACTIVE'),
    ('절약특공대2', NOW(), NOW(), 'ACTIVE'),
    ('커피러버모임', NOW(), NOW(), 'ACTIVE'),
    ('직장인야식클럽', NOW(), NOW(), 'ACTIVE'),
    ('알뜰살뜰모임', NOW(), NOW(), 'ACTIVE'),
    ('20대절약러', NOW(), NOW(), 'ACTIVE');

-- 3️⃣ 더 많은 유저-그룹 연결
INSERT INTO user_user_group_junction (user_id, user_group_id, created_at, updated_at, status)
VALUES
    (1, 1, NOW(), NOW(), 'ACTIVE'),
    (1, 2, NOW(), NOW(), 'ACTIVE'),
    (1, 3, NOW(), NOW(), 'ACTIVE'),
    (1, 4, NOW(), NOW(), 'ACTIVE'),
    (1, 5, NOW(), NOW(), 'ACTIVE'),
    (1, 6, NOW(), NOW(), 'ACTIVE'),
    (2, 1, NOW(), NOW(), 'ACTIVE'),
    (2, 2, NOW(), NOW(), 'ACTIVE'),
    (3, 1, NOW(), NOW(), 'ACTIVE'),
    (3, 3, NOW(), NOW(), 'ACTIVE'),
    (4, 4, NOW(), NOW(), 'ACTIVE'),
    (5, 3, NOW(), NOW(), 'ACTIVE'),
    (6, 6, NOW(), NOW(), 'ACTIVE');

-- 4️⃣ 다양한 상태와 카테고리의 챌린지 데이터 삽입
INSERT INTO challenges (title, content, head_count, duration, public_challenge, goal_amount, battle_coin,
                        challenge_category, start_date, end_date, recruitment_deadline,
                        created_at, updated_at, challenge_status, user_group_id)
VALUES
    -- 모집 중인 챌린지들
    ('30일 식비 절약', '한 달 동안 식비 30만원 이내로 쓰기', 5, 30, false, 300000, 100,
     'DELIVERY_FOOD', DATEADD('DAY', 2, CURRENT_DATE), DATEADD('DAY', 32, CURRENT_DATE),
     DATEADD('DAY', 1, CURRENT_DATE), NOW(), NOW(), 'RECRUITING', 1),

    ('커피값 줄이기', '스타벅스 대신 믹스커피', 4, 30, false, 50000, 200,
     'COFFEE', DATEADD('DAY', 3, CURRENT_DATE), DATEADD('DAY', 33, CURRENT_DATE),
     DATEADD('DAY', 2, CURRENT_DATE), NOW(), NOW(), 'RECRUITING', 3),

    -- 진행 중인 챌린지들
    ('카페인 줄이기', '한 달 동안 카페인 음료 5만원 이내로 쓰기', 3, 30, true, 50000, 150,
     'COFFEE', DATEADD('DAY', -5, CURRENT_DATE), DATEADD('DAY', 25, CURRENT_DATE),
     DATEADD('DAY', -6, CURRENT_DATE), NOW(), NOW(), 'ONGOING', NULL),

    ('야식비 절약', '야식 한 달 10만원 이내로!', 5, 30, false, 100000, 300,
     'DELIVERY_FOOD', DATEADD('DAY', -3, CURRENT_DATE), DATEADD('DAY', 27, CURRENT_DATE),
     DATEADD('DAY', -4, CURRENT_DATE), NOW(), NOW(), 'ONGOING', 4),

    -- 완료된 챌린지들
    ('배달비 아끼기', '배달비 절약하기', 4, 30, false, 100000, 200,
     'DELIVERY_FOOD', DATEADD('DAY', -40, CURRENT_DATE), DATEADD('DAY', -10, CURRENT_DATE),
     DATEADD('DAY', -41, CURRENT_DATE), NOW(), NOW(), 'COMPLETED', 2),

    ('술자리 비용 줄이기', '한 달 술값 20만원 이내로!', 6, 30, true, 200000, 400,
     'DRINKING', DATEADD('DAY', -35, CURRENT_DATE), DATEADD('DAY', -5, CURRENT_DATE),
     DATEADD('DAY', -36, CURRENT_DATE), NOW(), NOW(), 'COMPLETED', NULL),

    -- 비공개 챌린지들
    ('친구들과 술값 줄이기', '한 달 동안 술값 10만원 이내로 쓰기', 3, 30, false, 100000, 100,
     'DRINKING', DATEADD('DAY', 3, CURRENT_DATE), DATEADD('DAY', 33, CURRENT_DATE),
     DATEADD('DAY', 2, CURRENT_DATE), NOW(), NOW(), 'RECRUITING', 6),

    ('비밀 절약 모임', '무지출 챌린지', 4, 30, false, 50000, 500,
     'IMPULSE_BUY', DATEADD('DAY', -2, CURRENT_DATE), DATEADD('DAY', 28, CURRENT_DATE),
     DATEADD('DAY', -3, CURRENT_DATE), NOW(), NOW(), 'ONGOING', 5),

    -- 모집 중인 챌린지
    ('모집중인 챌린지1', '모집중인 챌린지1', 5, 30, true, 300000, 100,
     'IMPULSE_BUY', DATEADD('DAY', 2, CURRENT_DATE), DATEADD('DAY', 32, CURRENT_DATE),
     DATEADD('DAY', 1, CURRENT_DATE), NOW(), NOW(), 'RECRUITING', NULL),

    -- 모집 중인 챌린지
    ('배달음식 줄이기', '모집중인 챌린지2', 5, 30, true, 300000, 300,
     'DELIVERY_FOOD', DATEADD('DAY', 3, CURRENT_DATE), DATEADD('DAY', 32, CURRENT_DATE),
     DATEADD('DAY', 2, CURRENT_DATE), NOW(), NOW(), 'RECRUITING', NULL),

    -- 모집 중인 챌린지
    ('모집중인 챌린지3', '배달음식 줄이기', 5, 30, true, 300000, 400,
     'DRINKING', DATEADD('DAY', 4, CURRENT_DATE), DATEADD('DAY', 32, CURRENT_DATE),
     DATEADD('DAY', 3, CURRENT_DATE), NOW(), NOW(), 'RECRUITING', NULL),

    -- 모집 중인 챌린지
    ('모집 중인 친구 공개 챌린지', '배달음식 줄이기', 5, 30, false, 300000, 500,
     'DELIVERY_FOOD', DATEADD('DAY', 5, CURRENT_DATE), DATEADD('DAY', 32, CURRENT_DATE),
     DATEADD('DAY', 4, CURRENT_DATE), NOW(), NOW(), 'RECRUITING', NULL);

-- 5️⃣ 다양한 진행상황의 챌린지 프로그레스
INSERT INTO challenge_progress (challenge_id, user_id, used_amount, is_goal_achieved, reward_claimed,
                                created_at, updated_at, status)
VALUES
    -- 모집 중인 챌린지 참가자
    (1, 1, 0, false, false, NOW(), NOW(), 'ACTIVE'),
    (1, 2, 0, false, false, NOW(), NOW(), 'ACTIVE'),
    (1, 3, 0, false, false, NOW(), NOW(), 'ACTIVE'),
    (2, 4, 0, false, false, NOW(), NOW(), 'ACTIVE'),
    (11, 1, 0, false, false, NOW(), NOW(), 'ACTIVE'),

    -- 진행 중인 챌린지 참가자 (다양한 진행상황)
    (3, 1, 30000, false, false, NOW(), NOW(), 'ACTIVE'),
    (3, 2, 20000, false, false, NOW(), NOW(), 'ACTIVE'),
    (3, 3, 15000, false, false, NOW(), NOW(), 'ACTIVE'),
    (4, 4, 50000, false, false, NOW(), NOW(), 'ACTIVE'),
    (4, 5, 40000, false, false, NOW(), NOW(), 'ACTIVE'),
    (4, 1, 45000, false, false, NOW(), NOW(), 'ACTIVE'),

    -- 완료된 챌린지 참가자 (성공/실패 혼합)
    (5, 1, 80000, true, false, NOW(), NOW(), 'ACTIVE'),
    (5, 2, 90000, true, true, NOW(), NOW(), 'ACTIVE'),
    (5, 3, 120000, false, false, NOW(), NOW(), 'ACTIVE'),
    (6, 1, 150000, true, false, NOW(), NOW(), 'ACTIVE'),
    (6, 5, 180000, true, false, NOW(), NOW(), 'ACTIVE'),
    (6, 6, 250000, false, false, NOW(), NOW(), 'ACTIVE'),

    -- 비공개 챌린지 참가자
    (7, 1, 0, false, false, NOW(), NOW(), 'ACTIVE'),
    (7, 2, 0, false, false, NOW(), NOW(), 'ACTIVE'),
    (8, 3, 20000, false, false, NOW(), NOW(), 'ACTIVE'),
    (8, 4, 15000, false, false, NOW(), NOW(), 'ACTIVE'),
    (12, 2, 0, false, false, NOW(), NOW(), 'ACTIVE');

-- 6️⃣ 더 복잡한 친구 관계망
INSERT INTO friendships (to_user_id, from_user_id, created_at, updated_at, status)
VALUES
    (1, 2, NOW(), NOW(), 'ACTIVE'),
    (2, 1, NOW(), NOW(), 'ACTIVE'),
    (1, 3, NOW(), NOW(), 'ACTIVE'),
    (3, 1, NOW(), NOW(), 'ACTIVE'),
    (2, 3, NOW(), NOW(), 'ACTIVE'),
    (3, 2, NOW(), NOW(), 'ACTIVE'),
    (4, 1, NOW(), NOW(), 'ACTIVE'),
    (1, 4, NOW(), NOW(), 'ACTIVE'),
    (5, 2, NOW(), NOW(), 'ACTIVE'),
    (2, 5, NOW(), NOW(), 'ACTIVE'),
    (6, 3, NOW(), NOW(), 'ACTIVE'),
    (3, 6, NOW(), NOW(), 'ACTIVE');

-- 7️⃣ 더 많은 채팅 데이터
INSERT INTO chats (user_group_id, user_id, content, created_at, updated_at, status)
VALUES
    (1, 1, '안녕하세요! 챌린지 시작해볼까요?', NOW(), NOW(), 'ACTIVE'),
    (1, 2, '좋아요! 저도 참여할게요.', NOW(), NOW(), 'ACTIVE'),
    (1, 3, '저도 동참합니다!', NOW(), NOW(), 'ACTIVE'),
    (2, 2, '배달음식 덜 시켜보려고요!', NOW(), NOW(), 'ACTIVE'),
    (2, 1, '좋은 생각이에요!', NOW(), NOW(), 'ACTIVE'),
    (3, 4, '커피 줄이기 어렵네요ㅠㅠ', NOW(), NOW(), 'ACTIVE'),
    (3, 5, '화이팅해봐요!', NOW(), NOW(), 'ACTIVE'),
    (4, 6, '야식비가 너무 많이 나가요', NOW(), NOW(), 'ACTIVE'),
    (4, 4, '저도요ㅠㅠ 같이 줄여봐요!', NOW(), NOW(), 'ACTIVE'),
    (5, 5, '절약 꿀팁 공유해요~', NOW(), NOW(), 'ACTIVE');

-- 8️⃣ 아이템 데이터 확장
INSERT INTO items (NAME, PRICE, IMAGE_URL, STATUS, CREATED_AT, UPDATED_AT)
VALUES
    ('기본 테두리', 100, 'https://example.com/border1.jpg', 'ACTIVE', NOW(), NOW()),
    ('실버 테두리', 200, 'https://example.com/border2.jpg', 'ACTIVE', NOW(), NOW()),
    ('골드 테두리', 300, 'https://example.com/border3.jpg', 'ACTIVE', NOW(), NOW()),
    ('다이아몬드 테두리', 500, 'https://example.com/border4.jpg', 'ACTIVE', NOW(), NOW()),
    ('레인보우 테두리', 1000, 'https://example.com/border5.jpg', 'ACTIVE', NOW(), NOW()),
    ('홀로그램 테두리', 800, 'https://example.com/border6.jpg', 'ACTIVE', NOW(), NOW());

-- 9️⃣ 구매 기록 확장
INSERT INTO purchase_records (USER_ID, ITEM_ID, TRANSACTION, STATUS, CREATED_AT, UPDATED_AT)
VALUES
    (1, 1, 100, 'ACTIVE', NOW(), NOW()),
    (1, 2, 200, 'ACTIVE', NOW(), NOW()),
    (1, 3, 300, 'ACTIVE', NOW(), NOW()),
    (1, 4, 500, 'ACTIVE', NOW(), NOW()),
    (1, 5, 1000, 'ACTIVE', NOW(), NOW()),
    (2, 2, 200, 'ACTIVE', NOW(), NOW()),
    (3, 3, 300, 'ACTIVE', NOW(), NOW()),
    (4, 1, 100, 'ACTIVE', NOW(), NOW()),
    (5, 2, 200, 'ACTIVE', NOW(), NOW()),
    (6, 6, 800, 'ACTIVE', NOW(), NOW());

-- 🔟 챌린지 기록 확장
INSERT INTO challenge_records (END_DATE, START_DATE, CREATED_AT, TRANSACTION, UPDATED_AT, USER_ID, TITLE, STATUS)
VALUES
    ('2024-01-31', '2024-01-01', NOW(), 100, NOW(), 1, '1월 식비 절약', 'ACTIVE'),
    ('2024-01-31', '2024-01-01', NOW(), 150, NOW(), 2, '1월 커피값', 'ACTIVE');