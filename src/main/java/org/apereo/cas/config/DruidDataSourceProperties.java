package org.apereo.cas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Druid 数据源配置属性
 */
@ConfigurationProperties(prefix = "cas.datasource")
public class DruidDataSourceProperties {

    /**
     * 数据库连接 URL
     */
    private String url;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 数据库驱动类名
     */
    private String driverClassName;

    /**
     * 初始连接数
     */
    private int initialSize = 2;

    /**
     * 最小空闲连接数
     */
    private int minIdle = 2;

    /**
     * 最大活跃连接数
     */
    private int maxActive = 5;

    /**
     * 获取连接最大等待时间（毫秒）
     */
    private long maxWait = 30000;

    /**
     * 空闲时是否检测连接
     */
    private boolean testWhileIdle = true;

    /**
     * 获取连接时是否检测
     */
    private boolean testOnBorrow = false;

    /**
     * 归还连接时是否检测
     */
    private boolean testOnReturn = false;

    /**
     * 连接验证 SQL
     */
    private String validationQuery = "SELECT 1";

    // Getters and Setters

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }
}
