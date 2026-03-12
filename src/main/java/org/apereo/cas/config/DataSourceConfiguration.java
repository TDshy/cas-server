package org.apereo.cas.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * 数据源配置 - 使用 Druid 连接池
 * 为白名单自动登录提供数据库连接
 */
@AutoConfiguration
public class DataSourceConfiguration {

    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mariadb://shuheyu.cn:3306/ruoyi?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8");
        dataSource.setUsername("ssm");
        dataSource.setPassword("Ssm@12345678");
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        
        // Druid 连接池配置
        dataSource.setInitialSize(2);
        dataSource.setMinIdle(2);
        dataSource.setMaxActive(5);
        dataSource.setMaxWait(30000);
        
        // 监控配置
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setValidationQuery("SELECT 1");
        
        return dataSource;
    }
}
