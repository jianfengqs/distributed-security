package com.qs.security.distributed.uaa.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 令牌配置
 *
 * @author jianfeng
 * @date 2020/08/20
 */
@Configuration
public class TokenConfig {

    private String SIGNING_KEY = "uaa123";


    @Bean
    public TokenStore tokenStore() {
        // JWT令牌存储方案
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * JWT令牌加密配置
     *
     * @return {@link JwtAccessTokenConverter}
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 对称秘钥，资源服务器使用该秘钥来验证
        converter.setSigningKey(SIGNING_KEY);
        return converter;
    }

//    /**
//     * 令牌存储策略
//     *
//     * @return {@link TokenStore}
//     */
//    @Bean
//    public TokenStore tokenStore() {
//        // 内存方式，生成普通令牌
//        return new InMemoryTokenStore();
//    }
}
