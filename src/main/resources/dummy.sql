-- 1️⃣ 더 다양한 사용자 데이터 삽입
INSERT INTO users (email, password, nickname, image_url, coin, role, created_at, updated_at, status, boarder_url)
VALUES
    ('user1@gmail.com', 'password123', '도전왕', 'https://example.com/user1.jpg', 1000, 'USER', NOW(), NOW(), 'ACTIVE', 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/1.svg'),
    ('user2@gmail.com', 'password456', '절약왕', 'https://example.com/user2.jpg', 800, 'USER', NOW(), NOW(), 'ACTIVE', 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/1.svg'),
    ('user3@gmail.com', 'password789', '저축왕', 'https://example.com/user3.jpg', 500, 'USER', NOW(), NOW(), 'ACTIVE', 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/1.svg'),
    ('user4@gmail.com', 'password101', '배달매니아', 'https://example.com/user4.jpg', 1200, 'USER', NOW(), NOW(), 'ACTIVE', 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/1.svg'),
    ('user5@gmail.com', 'password102', '커피홀릭', 'https://example.com/user5.jpg', 300, 'USER', NOW(), NOW(), 'ACTIVE', 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/1.svg'),
    ('user6@gmail.com', 'password103', '술최강', 'https://example.com/user6.jpg', 2000, 'USER', NOW(), NOW(), 'ACTIVE', 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/1.svg'),
    ('user7@gmail.com', 'password104', '절약고수', 'https://example.com/user7.jpg', 1500, 'USER', NOW(), NOW(), 'ACTIVE', 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/1.svg'),
    ('user8@gmail.com', 'password105', '알뜰살뜰', 'https://example.com/user8.jpg', 900, 'USER', NOW(), NOW(), 'ACTIVE', 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/1.svg'),
    ('user9@gmail.com', 'password106', '무지출챔피언', 'https://example.com/user9.jpg', 1800, 'USER', NOW(), NOW(), 'ACTIVE', 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/1.svg'),
    ('user10@gmail.com', 'password107', '절약의달인', 'https://example.com/user10.jpg', 2200, 'USER', NOW(), NOW(), 'ACTIVE', 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/1.svg'),
    ('user11@gmail.com', 'password108', '저축의신', 'https://example.com/user11.jpg', 1700, 'USER', NOW(), NOW(), 'ACTIVE', 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/1.svg'),
    ('user12@gmail.com', 'password109', '알뜰왕', 'https://example.com/user12.jpg', 1100, 'USER', NOW(), NOW(), 'ACTIVE', 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/1.svg'),
    ('admin@moamoa.com', 'adminpass', '관리자', 'https://example.com/admin.jpg', 9999, 'ADMIN', NOW(), NOW(), 'ACTIVE', 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/1.svg');

-- 2️⃣ 다양한 유저 그룹 생성
INSERT INTO user_groups (title, created_at, updated_at, status)
VALUES
    ('절약특공대1', NOW(), NOW(), 'ACTIVE'),
    ('절약특공대2', NOW(), NOW(), 'ACTIVE'),
    ('커피러버모임', NOW(), NOW(), 'ACTIVE'),
    ('직장인야식클럽', NOW(), NOW(), 'ACTIVE'),
    ('알뜰살뜰모임', NOW(), NOW(), 'ACTIVE'),
    ('20대절약러', NOW(), NOW(), 'ACTIVE'),
    ('30대절약러', NOW(), NOW(), 'ACTIVE'),
    ('무지출챌린저', NOW(), NOW(), 'ACTIVE'),
    ('식비절약모임', NOW(), NOW(), 'ACTIVE'),
    ('절약습관만들기', NOW(), NOW(), 'ACTIVE'),
    ('알뜰생활러', NOW(), NOW(), 'ACTIVE'),
    ('똑똑한소비자', NOW(), NOW(), 'ACTIVE');

-- 3️⃣ 더 많은 유저-그룹 연결
INSERT INTO user_user_group_junction (user_id, user_group_id, created_at, updated_at, status)
VALUES
    (1, 1, NOW(), NOW(), 'ACTIVE'),
    (1, 2, NOW(), NOW(), 'ACTIVE'),
    (1, 5, NOW(), NOW(), 'ACTIVE'),
    (1, 8, NOW(), NOW(), 'ACTIVE'),
    (1, 9, NOW(), NOW(), 'ACTIVE'),
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
    ('30일 식비 절약', '한 달 동안 식비 40만원 이내로 도전! (배달비 포함)', 20, 30, false, 400000, 300,
     'DELIVERY_FOOD', DATEADD('DAY', 2, CURRENT_DATE), DATEADD('DAY', 32, CURRENT_DATE),
     DATEADD('DAY', 1, CURRENT_DATE), NOW(), NOW(), 'RECRUITING', 1),

    ('2주 커피값 줄이기', '2주간 카페인 음료 5만원 이내로! (편의점 음료 포함)', 15, 14, false, 50000, 200,
     'COFFEE', DATEADD('DAY', 3, CURRENT_DATE), DATEADD('DAY', 17, CURRENT_DATE),
     DATEADD('DAY', 2, CURRENT_DATE), NOW(), NOW(), 'RECRUITING', 3),

    ('21일 음료비 절약', '3주 동안 모든 음료 구매 10만원 이내로! (카페/편의점/배달)', 25, 21, true, 100000, 150,
     'COFFEE', DATEADD('DAY', 4, CURRENT_DATE), DATEADD('DAY', 25, CURRENT_DATE),
     DATEADD('DAY', 3, CURRENT_DATE), NOW(), NOW(), 'RECRUITING', NULL),

    -- 진행 중인 챌린지들
    ('7일 카페인 디톡스', '일주일간 커피 zero 도전', 30, 7, true, 100, 150,
     'COFFEE', DATEADD('DAY', -2, CURRENT_DATE), DATEADD('DAY', 5, CURRENT_DATE),
     DATEADD('DAY', -3, CURRENT_DATE), NOW(), NOW(), 'ONGOING', NULL),

    ('한 달 야식비 절약', '야식은 주 2회까지만! (배달/편의점 포함)', 18, 30, false, 150000, 200,
     'DELIVERY_FOOD', DATEADD('DAY', -3, CURRENT_DATE), DATEADD('DAY', 27, CURRENT_DATE),
     DATEADD('DAY', -4, CURRENT_DATE), NOW(), NOW(), 'ONGOING', 4),

    ('2주 외식비 줄이기', '2주간 점심 도시락 챌린지 (주2회 외식 가능)', 22, 14, true, 150000, 100,
     'DELIVERY_FOOD', DATEADD('DAY', -4, CURRENT_DATE), DATEADD('DAY', 10, CURRENT_DATE),
     DATEADD('DAY', -5, CURRENT_DATE), NOW(), NOW(), 'ONGOING', NULL),

    -- 완료된 챌린지들
    ('10일 배달비 아끼기', '배달앱 쿠폰/포인트 필수 사용 & 가까운 곳은 픽업하기', 12, 10, false, 10000, 100,
     'DELIVERY_FOOD', DATEADD('DAY', -15, CURRENT_DATE), DATEADD('DAY', -5, CURRENT_DATE),
     DATEADD('DAY', -16, CURRENT_DATE), NOW(), NOW(), 'COMPLETED', 2),

    ('한 달 술자리 관리', '회식/술자리 월 25만원 이내로! (2차 포함)', 28, 30, true, 250000, 200,
     'DRINKING', DATEADD('DAY', -35, CURRENT_DATE), DATEADD('DAY', -5, CURRENT_DATE),
     DATEADD('DAY', -36, CURRENT_DATE), NOW(), NOW(), 'COMPLETED', NULL),

    ('15일 홈술 챌린지', '2주간 술자리는 홈술로! 배달 음식 제외', 16, 15, true, 100000, 200,
     'DRINKING', DATEADD('DAY', -20, CURRENT_DATE), DATEADD('DAY', -5, CURRENT_DATE),
     DATEADD('DAY', -21, CURRENT_DATE), NOW(), NOW(), 'COMPLETED', NULL),

    -- 비공개 챌린지들
    ('주말 술자리 관리', '주말 술자리 1차만! (2차 없애기)', 8, 14, false, 100000, 300,
     'DRINKING', DATEADD('DAY', 3, CURRENT_DATE), DATEADD('DAY', 17, CURRENT_DATE),
     DATEADD('DAY', 2, CURRENT_DATE), NOW(), NOW(), 'RECRUITING', 6),

    ('21일 절약 습관', '매일 지출 계획 세우고 기록하기', 10, 21, false, 500000, 200,
     'IMPULSE_BUY', DATEADD('DAY', -2, CURRENT_DATE), DATEADD('DAY', 19, CURRENT_DATE),
     DATEADD('DAY', -3, CURRENT_DATE), NOW(), NOW(), 'ONGOING', 5),

    ('2주 점심값 줄이기', '구내식당 + 도시락 번갈아가며 먹기', 12, 14, false, 100000, 250,
     'DELIVERY_FOOD', DATEADD('DAY', -1, CURRENT_DATE), DATEADD('DAY', 13, CURRENT_DATE),
     DATEADD('DAY', -2, CURRENT_DATE), NOW(), NOW(), 'ONGOING', NULL),

    -- 추가 챌린지들
    ('15일 계획 소비', '모든 지출 전날 기록하고 계획하기', 25, 15, true, 200000, 100,
     'IMPULSE_BUY', DATEADD('DAY', 4, CURRENT_DATE), DATEADD('DAY', 19, CURRENT_DATE),
     DATEADD('DAY', 3, CURRENT_DATE), NOW(), NOW(), 'RECRUITING', NULL),

    ('7일 도시락 챌린지', '일주일간 점심 도시락 싸기', 20, 7, true, 50000, 200,
     'DELIVERY_FOOD', DATEADD('DAY', -3, CURRENT_DATE), DATEADD('DAY', 4, CURRENT_DATE),
     DATEADD('DAY', -4, CURRENT_DATE), NOW(), NOW(), 'ONGOING', NULL),

    ('주말 브런치 절약', '2주간 주말 브런치는 홈카페로!', 15, 14, true, 100000, 150,
     'COFFEE', DATEADD('DAY', -20, CURRENT_DATE), DATEADD('DAY', -6, CURRENT_DATE),
     DATEADD('DAY', -21, CURRENT_DATE), NOW(), NOW(), 'COMPLETED', NULL),

    ('10일 아침 루틴', '평일 2주간 아침밥 먹고 커피 집에서 내려 마시기', 30, 10, true, 50000, 100,
     'DELIVERY_FOOD', DATEADD('DAY', 5, CURRENT_DATE), DATEADD('DAY', 15, CURRENT_DATE),
     DATEADD('DAY', 4, CURRENT_DATE), NOW(), NOW(), 'RECRUITING', NULL),

    ('한 달 술자리 관리', '월 회식 4회로 제한 & 2차는 근처 가게로!', 25, 30, true, 300000, 300,
     'DRINKING', DATEADD('DAY', -2, CURRENT_DATE), DATEADD('DAY', 28, CURRENT_DATE),
     DATEADD('DAY', -3, CURRENT_DATE), NOW(), NOW(), 'ONGOING', NULL),

    ('일주일 편의점 절제', '편의점 식사 주 2회 제한 (야식 제외)', 20, 7, true, 30000, 400,
     'DELIVERY_FOOD', DATEADD('DAY', -12, CURRENT_DATE), DATEADD('DAY', -5, CURRENT_DATE),
     DATEADD('DAY', -13, CURRENT_DATE), NOW(), NOW(), 'COMPLETED', NULL),

    ('15일 배달앱 절약', '배달 주문 시 최소주문금액 맞추고 쿠폰 필수 사용', 15, 15, false, 150000, 150,
     'DELIVERY_FOOD', DATEADD('DAY', 5, CURRENT_DATE), DATEADD('DAY', 20, CURRENT_DATE),
     DATEADD('DAY', 4, CURRENT_DATE), NOW(), NOW(), 'RECRUITING', 9);

-- 5️⃣ 다양한 진행상황의 챌린지 프로그레스
INSERT INTO challenge_progress (challenge_id, user_id, used_amount, is_goal_achieved, reward_claimed,
                                created_at, updated_at, status)
VALUES
    -- 모집 중인 챌린지 참가자
    (1, 2, 0, false, false, NOW(), NOW(), 'ACTIVE'),
    (1, 3, 0, false, false, NOW(), NOW(), 'ACTIVE'),

    -- 진행 중인 챌린지 참가자 (다양한 진행상황)
    (3, 2, 20000, false, false, NOW(), NOW(), 'ACTIVE'),
    (3, 3, 15000, false, false, NOW(), NOW(), 'ACTIVE'),
    (4, 5, 40000, false, false, NOW(), NOW(), 'ACTIVE'),

    -- 완료된 챌린지 참가자 (성공/실패 혼합)
    (5, 2, 90000, true, true, NOW(), NOW(), 'ACTIVE'),
    (5, 3, 120000, false, false, NOW(), NOW(), 'ACTIVE'),
    (6, 5, 180000, true, false, NOW(), NOW(), 'ACTIVE'),
    (6, 6, 250000, false, false, NOW(), NOW(), 'ACTIVE'),

    -- 비공개 챌린지 참가자
    (7, 2, 0, false, false, NOW(), NOW(), 'ACTIVE'),
    (8, 3, 20000, false, false, NOW(), NOW(), 'ACTIVE'),
    (8, 4, 15000, false, false, NOW(), NOW(), 'ACTIVE'),
    (12, 2, 0, false, false, NOW(), NOW(), 'ACTIVE'),
    (16, 2, 0, false, false, NOW(), NOW(), 'ACTIVE'),

    -- User1의 모집중인 챌린지
    (1, 1, 0, false, false, NOW(), NOW(), 'ACTIVE'),
    (3, 1, 0, false, false, NOW(), NOW(), 'ACTIVE'),
    (19, 1, 0, false, false, NOW(), NOW(), 'ACTIVE'),

    -- User1 완료된 챌린지
    (7, 1, 6900, true, true, NOW(), NOW(), 'ACTIVE'),
    (8, 1, 139200, true, false, NOW(), NOW(), 'ACTIVE'),
    (9, 1, 107200, false, false, NOW(), NOW(), 'ACTIVE'),
    (15, 1, 124200, false, true, NOW(), NOW(), 'ACTIVE'),
    (18, 1, 6900, true, false, NOW(), NOW(), 'ACTIVE'),

    -- User1의 진행중인 챌린지
    (11, 1, 26800, false, false, NOW(), NOW(), 'ACTIVE'),
    (12, 1, 29900, false, false, NOW(), NOW(), 'ACTIVE'),
    (4, 1, 0, false, false, NOW(), NOW(), 'ACTIVE'),
    (17, 1, 41000, false, false, NOW(), NOW(), 'ACTIVE');

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
    (2, 2, '배달음식 덜 시켜보려고요!', NOW(), NOW(), 'ACTIVE'),
    (1, 2, '좋아요! 저도 참여할게요.', NOW(), NOW(), 'ACTIVE'),
    (1, 3, '저도 동참합니다!', NOW(), NOW(), 'ACTIVE'),
    (2, 2, '배달음식 덜 시켜보려고요!', NOW(), NOW(), 'ACTIVE'),
    (2, 1, '좋은 생각이에요!', NOW(), NOW(), 'ACTIVE'),
    (3, 4, '커피 줄이기 어렵네요ㅠㅠ', NOW(), NOW(), 'ACTIVE'),
    (3, 5, '화이팅해봐요!', NOW(), NOW(), 'ACTIVE'),
    (4, 6, '야식비가 너무 많이 나가요', NOW(), NOW(), 'ACTIVE'),
    (4, 4, '저도요ㅠㅠ 같이 줄여봐요!', NOW(), NOW(), 'ACTIVE'),
    (5, 5, '절약 꿀팁 공유해요~', NOW(), NOW(), 'ACTIVE');

INSERT INTO items (NAME, PRICE, IMAGE_URL, STATUS, CREATED_AT, UPDATED_AT, item_id) VALUES
    ('멋진 테두리', 10, 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/1.svg', 'ACTIVE', '2024-02-08 10:30:00', '2024-02-08 10:30:00', 1);

INSERT INTO items (NAME, PRICE, IMAGE_URL, STATUS, CREATED_AT, UPDATED_AT, item_id) VALUES
    ('짱 멋진 테두리', 300, 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/2.svg', 'ACTIVE', '2024-02-07 10:30:00', '2024-02-07 10:30:00', 2);

INSERT INTO items (NAME, PRICE, IMAGE_URL, STATUS, CREATED_AT, UPDATED_AT, item_id) VALUES
    ('짱 멋진 테두리2', 300, 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/3.svg', 'ACTIVE', '2024-02-07 10:30:00', '2024-02-07 10:30:00', 3);

INSERT INTO items (NAME, PRICE, IMAGE_URL, STATUS, CREATED_AT, UPDATED_AT, item_id) VALUES
    ('짱 멋진 테두리3', 300, 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/4.svg', 'ACTIVE', '2024-02-07 10:30:00', '2024-02-07 10:30:00', 4);

INSERT INTO items (NAME, PRICE, IMAGE_URL, STATUS, CREATED_AT, UPDATED_AT, item_id) VALUES
    ('짱 멋진 테두리3', 300, 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/5.svg', 'ACTIVE', '2024-02-07 10:30:00', '2024-02-07 10:30:00', 5);

INSERT INTO items (NAME, PRICE, IMAGE_URL, STATUS, CREATED_AT, UPDATED_AT, item_id) VALUES
    ('짱 멋진 테두리3', 300, 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/6.svg', 'ACTIVE', '2024-02-07 10:30:00', '2024-02-07 10:30:00', 6);

INSERT INTO items (NAME, PRICE, IMAGE_URL, STATUS, CREATED_AT, UPDATED_AT, item_id) VALUES
    ('짱 멋진 테두리3', 300, 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/7.svg', 'ACTIVE', '2024-02-07 10:30:00', '2024-02-07 10:30:00', 7);

INSERT INTO items (NAME, PRICE, IMAGE_URL, STATUS, CREATED_AT, UPDATED_AT, item_id) VALUES
    ('짱 멋진 테두리3', 300, 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/8.svg', 'ACTIVE', '2024-02-07 10:30:00', '2024-02-07 10:30:00', 8);

INSERT INTO items (NAME, PRICE, IMAGE_URL, STATUS, CREATED_AT, UPDATED_AT, item_id) VALUES
    ('짱 멋진 테두리3', 300, 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/9.svg', 'ACTIVE', '2024-02-07 10:30:00', '2024-02-07 10:30:00', 9);

INSERT INTO items (NAME, PRICE, IMAGE_URL, STATUS, CREATED_AT, UPDATED_AT, item_id) VALUES
    ('짱 멋진 테두리4', 30, 'https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/boarder/10.svg', 'ACTIVE', '2024-02-07 10:30:00', '2024-02-07 10:30:00', 10);

INSERT INTO purchase_records (purchase_record_id, USER_ID, ITEM_ID, TRANSACTION, STATUS, CREATED_AT, UPDATED_AT) VALUES
    (1, 1, 1, 200, 'ACTIVE', '2024-02-07 14:30:00', '2024-02-07 14:30:00'),
    (2, 1, 2, 300, 'ACTIVE', '2024-02-08 14:30:00', '2024-02-08 14:30:00');

INSERT INTO challenge_records (END_DATE, START_DATE, CHALLENGE_RECORD_ID, CREATED_AT, TRANSACTION, UPDATED_AT, USER_ID, TITLE, STATUS) VALUES
    ('2024-06-17', '2023-11-18', 1, '2023-10-15 12:30:45', 100, '2023-03-13 14:22:10', 1, '밥값아끼기', 'ACTIVE');

-- 8️⃣ 더 많은 채팅 데이터
INSERT INTO attendances (attendance_id, user_id, created_at, updated_at, status) VALUES
(1, 1, '2024-01-01 08:30:00', '2024-01-01 08:30:00', 'ACTIVE'),
(2, 1, '2024-01-15 09:45:00', '2024-01-15 09:45:00', 'ACTIVE'),
(3, 1, '2024-02-01 10:15:30', '2024-02-02 11:45:00', 'ACTIVE'),

(4, 2, '2024-01-10 08:00:00', '2024-01-10 08:30:00', 'ACTIVE'),
(5, 2, '2024-01-20 09:00:00', '2024-01-20 09:15:00', 'ACTIVE'),
(6, 2, '2024-02-05 10:30:00', '2024-02-06 12:00:00', 'ACTIVE'),

(7, 3, '2024-01-05 07:45:00', '2024-01-05 08:00:00', 'ACTIVE'),
(8, 3, '2024-01-25 10:00:00', '2024-01-25 10:30:00', 'ACTIVE'),
(9, 3, '2024-02-07 11:45:00', '2024-02-08 13:00:00', 'ACTIVE');

------------------------------------------------------------------------------------------------

INSERT INTO consumption_challenges (
    END_DATE, PRIZE, START_DATE, TARGET_AMOUNT,
    CONSUMPTION_CHALLENGE_ID, CREATED_AT, UPDATED_AT, USER_ID, STATUS
)
VALUES
    ('2025-02-02', 200, '2025-01-27', 180000, 1, '2025-01-27 09:00:00', '2025-01-27 10:00:00', 1, 'ACTIVE'),
    ('2025-02-09', 300, '2025-02-03', 250000, 2, '2025-02-03 09:00:00', '2025-02-03 10:00:00', 1, 'ACTIVE'),
    ('2025-02-17', 400, '2025-02-11', 220000, 3, '2025-02-11 09:00:00', '2025-02-11 10:00:00', 1, 'ACTIVE'),
    ('2025-02-25', 400, '2025-02-19', 270000, 4, '2025-02-19 09:00:00', '2025-02-19 10:00:00', 1, 'ACTIVE');



INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (1, 1, 19600, 'LIVING', 1, 'TAXI', '2025-01-26 00:00:00', '2025-01-26 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (2, 1, 17000, 'CELEBRATION', 1, 'HOBBY', '2025-01-27 00:00:00', '2025-01-27 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (3, 1, 14700, 'ETC', 1, 'TAXI', '2025-01-28 00:00:00', '2025-01-28 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (4, 1, 32000, 'ETC', 1, 'DRINKING', '2025-01-29 00:00:00', '2025-01-29 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (5, 1, 40500, 'CELEBRATION', 1, 'IMPULSE_BUY', '2025-01-30 00:00:00', '2025-01-30 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (6, 1, 45800, 'ACTIVITY', 1, 'DELIVERY_FOOD', '2025-01-31 00:00:00', '2025-01-31 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (7, 1, 8800, 'CELEBRATION', 1, 'COFFEE', '2025-02-01 00:00:00', '2025-02-01 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (8, 1, 43700, 'BEAUTY', 1, 'DRINKING', '2025-02-02 00:00:00', '2025-02-02 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (9, 1, 30700, 'ACTIVITY', 2, 'IMPULSE_BUY', '2025-02-03 00:00:00', '2025-02-03 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (10, 1, 38100, 'ETC', 2, 'COFFEE', '2025-02-04 00:00:00', '2025-02-04 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (11, 1, 31300, 'FIXED', 2, 'IMPULSE_BUY', '2025-02-05 00:00:00', '2025-02-05 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (12, 1, 37100, 'LIVING', 2, 'TAXI', '2025-02-06 00:00:00', '2025-02-06 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (13, 1, 8100, 'ACTIVITY', 2, 'IMPULSE_BUY', '2025-02-07 00:00:00', '2025-02-07 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (14, 1, 41500, 'LIVING', 2, 'TAXI', '2025-02-08 00:00:00', '2025-02-08 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (15, 1, 38000, 'ETC', 2, 'DRINKING', '2025-02-09 00:00:00', '2025-02-09 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (16, 1, 9800, 'FIXED', 3, 'IMPULSE_BUY', '2025-02-10 00:00:00', '2025-02-10 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (17, 1, 28400, 'ACTIVITY', 3, 'COFFEE', '2025-02-11 00:00:00', '2025-02-11 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (18, 1, 48900, 'FIXED', 3, 'COFFEE', '2025-02-12 00:00:00', '2025-02-12 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (19, 1, 5800, 'LIVING', 3, 'HOBBY', '2025-02-13 00:00:00', '2025-02-13 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (20, 1, 6900, 'FIXED', 3, 'DELIVERY_FOOD', '2025-02-14 00:00:00', '2025-02-14 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (21, 1, 25500, 'FIXED', 3, 'DRINKING', '2025-02-15 00:00:00', '2025-02-15 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (22, 1, 24500, 'ETC', 3, 'COFFEE', '2025-02-16 00:00:00', '2025-02-16 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (23, 1, 16200, 'ACTIVITY', 4, 'DRINKING', '2025-02-17 00:00:00', '2025-02-17 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (24, 1, 43200, 'FIXED', 4, 'IMPULSE_BUY', '2025-02-18 00:00:00', '2025-02-18 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (25, 1, 41000, 'LIVING', 4, 'DRINKING', '2025-02-19 00:00:00', '2025-02-19 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (26, 1, 26800, 'FIXED', 4, 'IMPULSE_BUY', '2025-02-20 00:00:00', '2025-02-20 00:00:00', 'ACTIVE');
INSERT INTO consumptions
(consumption_id, user_id, amount, consumption_category, consumption_challenge_id, challenge_category, created_at, updated_at, status)
VALUES (27, 1, 29900, 'FIXED', 4, 'DELIVERY_FOOD', '2025-02-21 00:00:00', '2025-02-21 00:00:00', 'ACTIVE');
