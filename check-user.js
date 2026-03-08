const mysql = require('mysql2/promise');

async function check() {
  const connection = await mysql.createConnection({
    host: 'shuheyu.cn',
    port: 3306,
    user: 'ssm',
    password: 'Ssm@12345678',
    database: 'ruoyi'
  });
  
  try {
    // 查看当前用户
    const [current] = await connection.execute('SELECT CURRENT_USER() as user');
    console.log('Current user:', current[0].user);
    
    // 查看所有 ssm 用户
    const [users] = await connection.execute("SELECT user, host FROM mysql.user WHERE user = 'ssm'");
    console.log('ssm users found:', users);
    
  } catch (e) {
    console.error('Error:', e.message);
  } finally {
    await connection.end();
  }
}

check();
