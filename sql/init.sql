-- 一体化平台用户管理系统 - 数据库初始化脚本
-- 创建数据库（如docker-compose未自动创建）
-- CREATE DATABASE IF NOT EXISTS user_center DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE user_center;

-- 应用表 (t_app)
CREATE TABLE IF NOT EXISTS `t_app` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `app_name` VARCHAR(100) NOT NULL COMMENT '应用名称',
    `app_code` VARCHAR(50) NOT NULL COMMENT '应用代码(唯一)',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_app_code` (`app_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='应用表';

-- 组织表 (t_org)
CREATE TABLE IF NOT EXISTS `t_org` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `org_name` VARCHAR(100) NOT NULL COMMENT '组织名称',
    `org_code` VARCHAR(50) NOT NULL COMMENT '组织编码(唯一)',
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父级ID: 0-根节点',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_org_code` (`org_code`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组织表';

-- 平台用户表 (t_platform_user)
CREATE TABLE IF NOT EXISTS `t_platform_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code` VARCHAR(100) NOT NULL COMMENT '用户编码(全局唯一)',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `password` VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `org_id` BIGINT DEFAULT NULL COMMENT '所属组织ID',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='平台用户表';

-- 应用用户表 (t_app_user)
CREATE TABLE IF NOT EXISTS `t_app_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `app_code` VARCHAR(50) NOT NULL COMMENT '应用代码',
    `user_code` VARCHAR(100) NOT NULL COMMENT '应用内用户编码',
    `username` VARCHAR(50) DEFAULT NULL COMMENT '用户名',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `platform_user_id` BIGINT DEFAULT NULL COMMENT '映射的平台用户ID',
    `sync_time` DATETIME DEFAULT NULL COMMENT '最近同步时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_app_user` (`app_code`, `user_code`),
    KEY `idx_platform_user_id` (`platform_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='应用用户表';

-- 插入初始数据
-- 插入默认组织
INSERT INTO `t_org` (`org_name`, `org_code`, `parent_id`, `sort_order`, `status`) VALUES
('平台总部', 'HQ', 0, 1, 1),
('研发中心', 'RD', 1, 1, 1),
('产品部门', 'PM', 1, 2, 1),
('测试部门', 'QA', 1, 3, 1);

-- 插入默认应用
INSERT INTO `t_app` (`app_name`, `app_code`, `description`, `status`) VALUES
('订单管理系统', 'oms', '订单管理应用', 1),
('库存管理系统', 'wms', '库存管理应用', 1),
('客户关系管理', 'crm', '客户关系管理应用', 1);

-- 插入管理员用户 (密码: admin123，使用BCrypt加密)
INSERT INTO `t_platform_user` (`code`, `username`, `nickname`, `password`, `email`, `phone`, `org_id`, `status`) VALUES
('PLAT_ADMIN001', 'admin', '系统管理员', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'admin@platform.com', '13800138000', 1, 1);
