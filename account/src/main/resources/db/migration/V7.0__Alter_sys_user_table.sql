
-- 使 ACCOUNT 字段唯一（MySQL 允许多个 NULL 值，不影响）
ALTER TABLE sys_user
    ADD UNIQUE INDEX `uniq_account` (`ACCOUNT`);

-- 使 POSITION_ID 字段允许为空
ALTER TABLE sys_user
    MODIFY COLUMN `POSITION_ID` bigint NULL DEFAULT NULL COMMENT '职位id';