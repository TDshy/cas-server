package org.apereo.cas.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 白名单控制器 - 完整自动登录实现
 * 从数据库获取 admin 密码，自动提交登录表单
 */
@Controller
@RequestMapping("/api/public")
public class WhitelistController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/hello")
    public String hello(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("[WhitelistController] /hello called, storing credentials in session");
        
        // 从数据库获取 admin 密码，存入 Session
        String adminPassword = getAdminPasswordFromDB();
        request.getSession().setAttribute("AUTO_LOGIN_PASSWORD", adminPassword);
        request.getSession().setAttribute("AUTO_LOGIN_USERNAME", "admin");
        request.getSession().setAttribute("AUTO_LOGIN_ENABLED", "true");
        
        // 重定向到普通登录页面，URL 中不显示任何参数
        String redirectUrl = "http://localhost:8080/cas_server_war_exploded/login";
        
        return "redirect:" + redirectUrl;
    }
    
    /**
     * 从数据库获取 admin 用户密码
     */
    private String getAdminPasswordFromDB() {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            String sql = "SELECT password FROM sys_user WHERE user_name = ? AND status = '0' AND del_flag = '0'";
            String password = jdbcTemplate.queryForObject(sql, String.class, "admin");
            System.out.println("[WhitelistController] Got admin password from DB");
            return password != null ? password : "admin";
        } catch (Exception e) {
            System.err.println("[WhitelistController] Failed to get admin password: " + e.getMessage());
            return "admin"; // 默认密码
        }
    }

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> info() {
        Map<String, Object> result = new HashMap<>();
        result.put("service", "CAS Server");
        result.put("version", "7.0.0");
        result.put("message", "Access /api/public/hello for auto-login");
        return result;
    }

    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("time", System.currentTimeMillis());
        return result;
    }

    /**
     * 从 Session 获取自动登录凭证
     */
    @GetMapping(value = "/session-credentials", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> getSessionCredentials(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        
        String autoLogin = (String) request.getSession().getAttribute("AUTO_LOGIN_ENABLED");
        String username = (String) request.getSession().getAttribute("AUTO_LOGIN_USERNAME");
        String password = (String) request.getSession().getAttribute("AUTO_LOGIN_PASSWORD");
        
        result.put("autoLogin", autoLogin != null ? autoLogin : "false");
        
        if ("true".equals(autoLogin) && username != null && password != null) {
            result.put("username", username);
            result.put("password", password);
            // 读取后立即清除，避免重复使用
            request.getSession().removeAttribute("AUTO_LOGIN_ENABLED");
            request.getSession().removeAttribute("AUTO_LOGIN_USERNAME");
            request.getSession().removeAttribute("AUTO_LOGIN_PASSWORD");
            System.out.println("[WhitelistController] Credentials retrieved from session and cleared");
        }
        
        return result;
    }

}
