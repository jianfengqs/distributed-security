package com.qs.security.distributed.uaa.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Collections;


/**
 * 授权服务器
 *
 * @author jianfeng
 * @date 2020/08/20
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;



//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    /**
     * 客户详细信息配置（将客户端的信息存储到数据库,授权码存入到数据库）
     *
     * @param dataSource 数据源
     * @return {@link ClientDetailsService}
     */
    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService)
                clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }

    /**
     * 配置客户端详细信息服务
     *
     * @param clients 客户
     * @throws Exception 异常
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        // 使用in‐memory存储（内存存储）
//        clients.inMemory()
//                // client_id
//                .withClient("c1")
//                // 客户端密钥
//                .secret(new BCryptPasswordEncoder().encode("secret"))
//                // 客户端可以访问的资源列表
//                .resourceIds("res1")
//                // 该client允许的授权类型authorization_code,password,refresh_token,implicit,client_credentials
//                .authorizedGrantTypes("authorization_code",
//                        "password", "client_credentials", "implicit", "refresh_token")
//                // 允许的授权范围
//                .scopes("all")
//                .autoApprove(false)
//                // 加上验证回调地址
//                .redirectUris("http://www.baidu.com");
        // 数据库读取
        clients
                .withClientDetails(clientDetailsService);

    }

    /**
     * 令牌管理服务配置
     *
     * @return {@link AuthorizationServerTokenServices}
     */
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service = new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService);
        // 是否产生刷新令牌
        service.setSupportRefreshToken(true);
        // 令牌存储策略
        service.setTokenStore(tokenStore);

        // 设置令牌增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Collections.singletonList(accessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);

        // 令牌默认有效期2小时
//        service.setAccessTokenValiditySeconds(7200);
//        // 刷新令牌默认有效期3天
//        service.setRefreshTokenValiditySeconds(259200);
        return service;
    }

//    /**
//     * 令牌访问端点配置
//     *
//     * @return {@link AuthorizationCodeServices}
//     */
//    @Bean
//    public AuthorizationCodeServices authorizationCodeServices() {
//        // 设置授权码模式的授权码如何存取，暂时采用内存方式
//        return new InMemoryAuthorizationCodeServices();
//    }


    /**
     * 令牌访问端点配置
     *
     * @param dataSource 数据源
     * @return {@link AuthorizationCodeServices}
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        // 设置授权码模式的授权码如何存取
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * 令牌访问端点配置
     *
     * @param endpoints 端点
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                // 密码模式需要
                .authenticationManager(authenticationManager)
                // 授权码模式需要
                .authorizationCodeServices(authorizationCodeServices)
                // 令牌管理服务
                .tokenServices(tokenService())
                // 允许post方式访问
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }


    /**
     * 令牌访问端点安全策略
     *
     * @param security 安全
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                // /oauth/token_key公开，提供公有密匙的端点，如果你使用JWT令牌的话
                .tokenKeyAccess("permitAll()")
                // /oauth/check_token公开，检测令牌
                .checkTokenAccess("permitAll()")
                // 表单认证，申请令牌
                .allowFormAuthenticationForClients()
        ;
    }


}
