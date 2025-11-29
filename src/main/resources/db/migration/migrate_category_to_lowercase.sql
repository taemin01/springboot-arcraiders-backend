-- CATEGORY (대문자 테이블)에서 category (소문자 테이블)로 데이터 마이그레이션
-- 실행 방법: psql -h localhost -p 55432 -U taemin -d arc_raiders -f migrate_category_to_lowercase.sql

BEGIN;

-- 1. 소문자 테이블이 존재하는지 확인
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'category') THEN
        RAISE NOTICE 'category 테이블이 존재하지 않습니다. JPA가 자동 생성하도록 애플리케이션을 먼저 실행하세요.';
    END IF;
END $$;

-- 2. 기존 소문자 테이블의 데이터 삭제 (있다면)
TRUNCATE TABLE category RESTART IDENTITY CASCADE;

-- 3. 대문자 테이블에서 소문자 테이블로 데이터 복사
-- 컬럼명도 대문자인 경우
INSERT INTO category (category_name, sub_category_name, code_type)
SELECT
    "CATEGORY_NAME",
    "SUB_CATEGORY_NAME",
    "CODE_TYPE"
FROM "CATEGORY"
WHERE "CATEGORY_NAME" IS NOT NULL;

-- 만약 컬럼명이 이미 소문자라면 아래 쿼리를 대신 사용하세요:
-- INSERT INTO category (category_name, sub_category_name, code_type)
-- SELECT category_name, sub_category_name, code_type
-- FROM "CATEGORY"
-- WHERE category_name IS NOT NULL;

-- 4. 시퀀스 업데이트 (ID 충돌 방지)
SELECT setval('category_id_seq', (SELECT COALESCE(MAX(id), 1) FROM category));

-- 5. 복사된 데이터 확인
SELECT COUNT(*) as "복사된 레코드 수" FROM category;

COMMIT;

-- 확인 쿼리
SELECT * FROM category LIMIT 10;
