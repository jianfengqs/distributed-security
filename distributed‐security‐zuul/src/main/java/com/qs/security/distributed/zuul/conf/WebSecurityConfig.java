package com.qs.security.distributed.zuul.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全配置
 *
 * @author jianfeng
 * @date 2020/08/21
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // 放行所有请求
                .antMatchers("/**").permitAll()
                .and().csrf().disable();
    }


}