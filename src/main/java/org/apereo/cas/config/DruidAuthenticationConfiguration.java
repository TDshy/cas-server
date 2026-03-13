package org.apereo.cas.config;

import org.apereo.cas.adaptors.jdbc.DruidQueryAuthenticationHandler;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.authentication.principal.PrincipalFactoryUtils;
import org.apereo.cas.services.ServicesManager;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * Druid 数据源认证配置
 * 注册自定义 JDBC 认证处理器，使用 Druid 数据源
 */
@AutoConfiguration
@EnableConfigurationProperties(DruidAuthenticationProperties.class)
public class DruidAuthenticationConfiguration implements AuthenticationEventExecutionPlanConfigurer {

    private final DruidAuthenticationProperties properties;
    private final DataSource dataSource;
    private final ServicesManager servicesManager;

    public DruidAuthenticationConfiguration(
            final DruidAuthenticationProperties properties,
            @Qualifier("dataSource") final DataSource dataSource,
            @Qualifier("servicesManager") final ServicesManager servicesManager) {
        this.properties = properties;
        this.dataSource = dataSource;
        this.servicesManager = servicesManager;
    }

    /**
     * 创建 PrincipalFactory
     */
    @Bean
    public PrincipalFactory druidPrincipalFactory() {
        return PrincipalFactoryUtils.newPrincipalFactory();
    }

    /**
     * 创建 Druid JDBC 认证处理器
     */
    @Bean
    public AuthenticationHandler druidAuthenticationHandler(
            @Qualifier("druidPrincipalFactory") final PrincipalFactory principalFactory) {
        
        final DruidQueryAuthenticationHandler handler = new DruidQueryAuthenticationHandler(
                properties.getName(),
                servicesManager,
                principalFactory,
                properties.getOrder(),
                dataSource,
                properties.getSql(),
                properties.getFieldPassword(),
                properties.getFieldDisabled(),
                properties.getFieldExpired()
        );
        
        // 设置自定义密码编码器（使用 MyPasswordEncoder）
        handler.setPasswordEncoder(new org.apereo.cas.authentication.MyPasswordEncoder());
        
        return handler;
    }

    @Override
    public void configureAuthenticationExecutionPlan(final AuthenticationEventExecutionPlan plan) {
        // 如果启用了 Druid 认证，则注册到认证计划
        if (properties.isEnabled()) {
            plan.registerAuthenticationHandler(druidAuthenticationHandler(druidPrincipalFactory()));
        }
    }
}
