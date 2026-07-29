---
name: flyway
description: 生成 Flyway 数据库迁移 SQL 文件，文件命名和格式遵循项目规范。
---

# Flyway 迁移文件生成专家

## 任务目标
根据用户描述的数据库变更需求，自动生成符合项目规范的 Flyway 迁移 SQL 文件。

## 文件规范

### 存放路径
```
yshop-server/src/main/resources/db/migration/
```

### 命名规则
```
V{yyyyMMddHHmm}__{英文描述}.sql
```

- 前缀 `V` + 14 位时间戳（年 4 位 + 月 2 位 + 日 2 位 + 时 2 位 + 分 2 位）
- 双下划线 `__` 分隔
- 英文描述，用下划线连接单词
- 示例：`V202606181200__alter_contract_add_status.sql`

### 时间戳生成
- 使用**当前系统时间**生成时间戳
- 确保与已有迁移文件不重复（检查目录中最后一个文件的时间戳）

## 执行步骤

### 1. 收集信息
向用户确认：

1. **变更类型**：新建表 / 修改表 / 插入字典数据 / 修改数据 / 删除数据
2. **变更内容描述**：如"合同表添加 status 字段"、"创建官费名称表"
3. **SQL 细节**（根据需要询问）：
   - 新建表：表名、字段列表、索引、外键
   - 修改表：表名、新增/修改/删除的字段
   - 字典数据：字典类型、键值对列表

### 2. 生成文件名
- 读取 `yshop-server/src/main/resources/db/migration/` 目录，获取最新迁移文件的时间戳
- 如果当前时间与最新文件时间戳相同或更早，加 1 分钟确保不重复
- 根据变更内容生成英文描述

### 3. 生成 SQL 内容

#### 新建表模板
```sql
CREATE TABLE `{table_name}` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `{field1}` {type} NOT NULL DEFAULT '{default}' COMMENT '{comment}',
  `{field2}` {type} DEFAULT NULL COMMENT '{comment}',
  -- ...更多字段...
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_{field}` (`{field}`) -- 按需加索引
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='{表说明}';
```

#### 修改表模板
```sql
ALTER TABLE `{table_name}`
ADD COLUMN `{field}` {type} NOT NULL DEFAULT '{default}' COMMENT '{comment}' AFTER `{after_field}`;
```

#### 插入字典类型模板
```sql
INSERT INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES ('{字典名称}', '{字典类型}', 0, '{备注}', 'admin', NOW(), 'admin', NOW(), b'0');
```

#### 插入字典数据模板
```sql
INSERT INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES ({排序}, '{标签}', '{值}', '{字典类型}', 0, '{备注}', 'admin', NOW(), 'admin', NOW(), b'0');
```

#### 修改数据模板
```sql
UPDATE `{table_name}`
SET `{field}` = '{new_value}'
WHERE `{condition}`;
```

#### 删除数据模板
```sql
DELETE FROM `{table_name}`
WHERE `{condition}`;
```

### 4. 创建文件
- 使用生成的文件名，在 `yshop-server/src/main/resources/db/migration/` 目录创建 SQL 文件
- 告知用户文件路径和变更摘要

## 注意事项
- SQL 关键字用大写
- 表名、字段名用反引号包裹
- 每条 SQL 语句以 `;` 结尾
- 已有表的新增字段使用 `AFTER` 子句控制字段顺序
- 建表时 `ENGINE=InnoDB DEFAULT CHARSET=utf8mb4`
- 所有表必须包含 `id`/`creator`/`create_time`/`updater`/`update_time`/`deleted` 这 6 个基础字段
- 插入语句使用 `NOW()` 填充时间字段
- 写入数据使用 `b'0'` 表示未删除
