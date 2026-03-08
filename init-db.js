const mysql = require('mysql2/promise');

const config = {
  host: 'shuheyu.cn',
  port: 3306,
  user: 'ssm',
  password: 'Ssm@12345678',
  database: 'ruoyi'
};

async function init() {
  const connection = await mysql.createConnection(config);
  
  try {
    console.log('Connected to database successfully!');
    
    // Create table
    await connection.execute(`
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
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='CAS用户表'
    `);
    console.log('Table sys_user created successfully!');
    
    // Insert test users
    const users = [
      {
        username: 'casuser',
        password: '$2a$10$IqTJTjn39IU5.3sLleXQtO4K7W0W1bZ.IEydN.Hq2eMaYPJ7gB0gK',
        email: 'casuser@example.com'
      },
      {
        username: 'admin',
        password: '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO',
        email: 'admin@example.com'
      }
    ];
    
    for (const user of users) {
      await connection.execute(
        `INSERT INTO sys_user (username, password, email, status, expired, disabled) 
         VALUES (?, ?, ?, 1, 0, 0)
         ON DUPLICATE KEY UPDATE password = VALUES(password)`,
        [user.username, user.password, user.email]
      );
      console.log(`User '${user.username}' inserted/updated successfully!`);
    }
    
    // Verify
    const [rows] = await connection.execute('SELECT id, username, email, status FROM sys_user');
    console.log('\nUsers in database:');
    console.table(rows);
    
    console.log('\n✅ Database initialized successfully!');
    
  } catch (error) {
    console.error('❌ Error:', error.message);
    process.exit(1);
  } finally {
    await connection.end();
  }
}

init();
