package org.apereo.cas.adaptors.jdbc;

import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.credential.UsernamePasswordCredential;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.sql.DataSource;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 自定义 JDBC 查询认证处理器 - 使用 Druid 数据源
 * 适配若依框架 sys_user 表结构
 */
public class DruidQueryAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DruidQueryAuthenticationHandler.class);

    private final String sql;
    private final String fieldPassword;
    private final String fieldDisabled;
    private final String fieldExpired;

    public DruidQueryAuthenticationHandler(final String name,
                                           final ServicesManager servicesManager,
                                           final PrincipalFactory principalFactory,
                                           final Integer order,
                                           final DataSource dataSource,
                                           final String sql,
                                           final String fieldPassword,
                                           final String fieldDisabled,
                                           final String fieldExpired) {
        super(name, servicesManager, principalFactory, order, dataSource);
        this.sql = sql;
        this.fieldPassword = fieldPassword;
        this.fieldDisabled = fieldDisabled;
        this.fieldExpired = fieldExpired;
    }

    @Override
    protected AuthenticationHandlerExecutionResult authenticateUsernamePasswordInternal(
            final UsernamePasswordCredential credential,
            final String originalPassword)
            throws GeneralSecurityException, PreventedException {

        final String username = credential.getUsername();

        LOG.debug("Authenticating user [{}] using Druid DataSource", username);

        try {
            // 查询数据库获取用户信息
            final Map<String, Object> dbFields = getJdbcTemplate().queryForMap(sql, username);
            LOG.debug("Found user record for [{}]", username);

            // 验证密码
            if (dbFields.containsKey(fieldPassword)) {
                final String dbPassword = (String) dbFields.get(fieldPassword);
                
                // 使用父类的 matches 方法验证密码（支持 BCrypt 等编码器）
                if (!matches(originalPassword, dbPassword)) {
                    LOG.warn("Password mismatch for user [{}]", username);
                    throw new FailedLoginException("密码不正确");
                }
                LOG.debug("Password verified successfully for user [{}]", username);
            } else {
                LOG.error("Password field [{}] not found for user [{}]", fieldPassword, username);
                throw new FailedLoginException("数据库配置错误");
            }

            // 检查账号是否被禁用（若依框架的 status 字段，0=正常，1=停用）
            if (fieldDisabled != null && dbFields.containsKey(fieldDisabled)) {
                final Object disabledValue = dbFields.get(fieldDisabled);
                final String disabledStr = disabledValue != null ? disabledValue.toString() : "0";
                if ("1".equals(disabledStr) || "true".equalsIgnoreCase(disabledStr)) {
                    LOG.warn("Account [{}] is disabled", username);
                    throw new FailedLoginException("账号已被禁用");
                }
            }

            // 检查账号是否过期（若依框架的 del_flag 字段，0=正常，1=删除）
            if (fieldExpired != null && dbFields.containsKey(fieldExpired)) {
                final Object expiredValue = dbFields.get(fieldExpired);
                final String expiredStr = expiredValue != null ? expiredValue.toString() : "0";
                if ("1".equals(expiredStr) || "true".equalsIgnoreCase(expiredStr)) {
                    LOG.warn("Account [{}] is deleted", username);
                    throw new FailedLoginException("账号已被删除");
                }
            }

            // 创建认证结果
            final var principal = getPrincipalFactory().createPrincipal(username);
            LOG.info("Successfully authenticated user [{}]", username);
            return createHandlerResult(credential, principal, new ArrayList<>(0));

        } catch (final IncorrectResultSizeDataAccessException e) {
            if (e.getActualSize() == 0) {
                LOG.warn("User [{}] not found in database", username);
                throw new AccountNotFoundException("用户不存在");
            }
            LOG.error("Multiple records found for user [{}]", username);
            throw new FailedLoginException("找到多个用户记录");
        } catch (final DataAccessException e) {
            LOG.error("Database access error for user [{}]", username, e);
            throw new PreventedException(e);
        }
    }
}
