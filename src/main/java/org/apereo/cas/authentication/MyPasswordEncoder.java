package org.apereo.cas.authentication;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义密码编码器
 * 默认实现明文密码验证
 */
public class MyPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        // 明文返回
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 明文比较
        return rawPassword.toString().equals(encodedPassword);
    }
}
