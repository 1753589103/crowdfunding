package com.nyf.mvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration            // 设置为配置类
@EnableWebSecurity        // 开启web环境下的权限控制功能
// 需要继承WebSecurityConfigurerAdapter
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {


}
