package com.hik.practicedemo.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by wangJinChang on 2019/12/26 20:54
 * 密码加密配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder loadBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
