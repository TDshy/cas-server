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
    // 查看表结构
    const [columns] = await connection.execute('DESCRIBE sys_user');
    console.log('Table structure:');
    console.table(columns);
    
    // 查看表数据
    const [data] = await connection.execute('SELECT * FROM sys_user');
    console.log('\nTable data:', data);
    
  } catch (e) {
    console.error('Error:', e.message);
  } finally {
    await connection.end();
  }
}

check();
