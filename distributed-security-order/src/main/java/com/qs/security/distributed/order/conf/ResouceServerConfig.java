package com.qs.security.distributed.order.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 资源服务器配置
 *
 * @author jianfeng
 * @date 2020/08/21
 */
@Configuration
@EnableResourceServer
public class ResouceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * 资源id(uua配置的资源)
     */
    public static final String RESOURCE_ID = "res1";

    @Autowired
    private TokenStore tokenStore;


    /**
     * 资源配置
     *
     * @param resources 资源
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
                // 资源id
                .resourceId(RESOURCE_ID)
                // 验证令牌服务,自己验证
                .tokenStore(tokenStore)
                // 验证令牌服务,调用授权服务验证
//                .tokenServices(tokenService())
                .stateless(true);
    }

    /**
     * 安全配置
     *
     * @param http http
     * @throws Exception 异常
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // 如果scope的授权范围不是ROLE_ADMIN，那么就不让它访问
                .antMatchers("/**").access("#oauth2.hasScope('ROLE_ADMIN')")
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


//    /**
//     * 资源服务令牌解析配置
//     *
//     * @return {@link ResourceServerTokenServices}
//     */
//    @Bean
//    public ResourceServerTokenServices tokenService() {
//        // 使用远程服务请求授权服务器校验token,必须指定校验token 的url、client_id，client_secret
//        RemoteTokenServices service=new RemoteTokenServices();
//        service.setCheckTokenEndpointUrl("http://localhost:53020/uaa/oauth/check_token");
//        service.setClientId("c1");
//        service.setClientSecret("secret");
//        return service;
//    }




}
