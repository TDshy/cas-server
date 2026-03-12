package org.apereo.cas.config;

import org.apereo.cas.web.ProtocolEndpointWebSecurityConfigurer;
import org.apereo.cas.web.WhitelistController;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import java.util.List;

/**
 * 白名单安全配置
 * 放行 /api/public/** 路径，允许匿名访问并自动登录
 */
@AutoConfiguration
public class WhitelistSecurityConfiguration {

    @Bean
    public WhitelistController whitelistController() {
        System.out.println("[WhitelistSecurityConfiguration] Creating WhitelistController bean");
        return new WhitelistController();
    }

    @Bean
    public ProtocolEndpointWebSecurityConfigurer<HttpSecurity> whitelistProtocolEndpointConfigurer() {
        return new ProtocolEndpointWebSecurityConfigurer<>() {
            @Override
            public List<String> getIgnoredEndpoints() {
                // 这些路径将被 Spring Security 忽略（完全绕过过滤器链）
                // /api/public 包含所有子路径如 /hello, /info, /health, /session-credentials
                return List.of("/api/public");
            }

            @Override
            public ProtocolEndpointWebSecurityConfigurer<HttpSecurity> configure(HttpSecurity http) throws Exception {
                // 或者使用 permitAll 允许匿名访问（经过过滤器链但不需要认证）
                http.authorizeHttpRequests(customizer -> 
                    customizer.requestMatchers("/api/public/**").permitAll()
                );
                // 禁用 CSRF 保护，避免 POST 请求被拦截
                http.csrf(AbstractHttpConfigurer::disable);
                return this;
            }
        };
    }
}
