package com.qs.security.distributed.zuul.conf;

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
public class ResouceServerConfig {

    /**
     * 资源id
     */
    public static final String RESOURCE_ID = "res1";


    /**
     * uaa授权服务器server配置
     *
     * @author jianfeng
     * @date 2020/08/21
     */
    @Configuration
    @EnableResourceServer
    public class UAAServerConfig extends ResourceServerConfigurerAdapter{

        @Autowired
        private TokenStore tokenStore;

        /**
         * 资源配置
         *
         * @param resources 资源
         */
        @Override
        public void configure(ResourceServerSecurityConfigurer resources){
            resources
                    // 验证令牌服务,自己验证
                    .tokenStore(tokenStore)
                    // 资源id
                    .resourceId(RESOURCE_ID)
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
            http.authorizeRequests()
                    // 授权服务全部放行
                    .antMatchers("/uaa/**").permitAll();
        }

    }



    /**
     * 订单服务器server配置
     *
     * @author jianfeng
     * @date 2020/08/21
     */
    @Configuration
    @EnableResourceServer
    public class OrderServerConfig extends ResourceServerConfigurerAdapter{

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
                    // 验证令牌服务,自己验证
                    .tokenStore(tokenStore)
                    // 资源id
                    .resourceId(RESOURCE_ID)
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
                    // order服务只放行scope授权范围为ROLE_API的请求
                    .antMatchers("/order/**").access("#oauth2.hasScope('ROLE_API')");
        }



    }


}
