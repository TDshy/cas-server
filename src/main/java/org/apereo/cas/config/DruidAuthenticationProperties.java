package org.apereo.cas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Druid 认证配置属性
 */
@ConfigurationProperties(prefix = "cas.authn.druid")
public class DruidAuthenticationProperties {

    /**
     * 是否启用 Druid 认证
     */
    private boolean enabled = true;

    /**
     * 认证处理器名称
     */
    private String name = "DruidAuthenticationHandler";

    /**
     * 认证处理器顺序（优先级）
     */
    private int order = 0;

    /**
     * SQL 查询语句
     */
    private String sql = "SELECT password, status, del_flag FROM sys_user WHERE user_name = ?";

    /**
     * 密码字段名
     */
    private String fieldPassword = "password";

    /**
     * 禁用状态字段名（若依框架 status: 0=正常, 1=停用）
     */
    private String fieldDisabled = "status";

    /**
     * 删除标记字段名（若依框架 del_flag: 0=正常, 1=删除）
     */
    private String fieldExpired = "del_flag";

    // Getters and Setters

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getFieldPassword() {
        return fieldPassword;
    }

    public void setFieldPassword(String fieldPassword) {
        this.fieldPassword = fieldPassword;
    }

    public String getFieldDisabled() {
        return fieldDisabled;
    }

    public void setFieldDisabled(String fieldDisabled) {
        this.fieldDisabled = fieldDisabled;
    }

    public String getFieldExpired() {
        return fieldExpired;
    }

    public void setFieldExpired(String fieldExpired) {
        this.fieldExpired = fieldExpired;
    }
}
