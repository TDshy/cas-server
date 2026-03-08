--
-- CAS Server 7.x Database Initialization Script
-- For MariaDB / MySQL
-- Target: jdbc:mariadb://shuheyu.cn:3306/ruoyi
--

-- Use database
USE ruoyi;

-- Create user table for authentication
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '电话',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    expired TINYINT DEFAULT 0 COMMENT '是否过期：0-否，1-是',
    disabled TINYINT DEFAULT 0 COMMENT '是否禁用：0-否，1-是',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='CAS用户表';

-- Insert default test user (password: casuser)
-- BCrypt hash for 'casuser'
INSERT INTO sys_user (username, password, email, status, expired, disabled) VALUES
('casuser', '$2a$10$IqTJTjn39IU5.3sLleXQtO4K7W0W1bZ.IEydN.Hq2eMaYPJ7gB0gK', 'casuser@example.com', 1, 0, 0)
ON DUPLICATE KEY UPDATE password = '$2a$10$IqTJTjn39IU5.3sLleXQtO4K7W0W1bZ.IEydN.Hq2eMaYPJ7gB0gK';

-- Insert another test user (password: 123456)
-- BCrypt hash for '123456'
INSERT INTO sys_user (username, password, email, status, expired, disabled) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', 'admin@example.com', 1, 0, 0)
ON DUPLICATE KEY UPDATE password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO';

-- Verify
SELECT 'Database initialized successfully!' AS result;
SELECT * FROM sys_user;
