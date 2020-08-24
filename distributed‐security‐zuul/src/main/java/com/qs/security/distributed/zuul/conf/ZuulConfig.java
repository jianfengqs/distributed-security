package com.qs.security.distributed.zuul.conf;

import com.qs.security.distributed.zuul.filter.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * zuul配置
 *
 * @author jianfeng
 * @date 2020/08/24
 */
@Configuration
public class ZuulConfig {

    /**
     * 预过滤器
     *
     * @return {@link AuthFilter}
     */
    @Bean
    public AuthFilter preFilter(){
        return new AuthFilter();
    }

    /**
     * 过滤器
     *
     * @return {@link FilterRegistrationBean}
     */
    @Bean
    public FilterRegistrationBean corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setMaxAge(18000L);
        source.registerCorsConfiguration("/**", config);
        CorsFilter corsFilter = new CorsFilter(source);
        FilterRegistrationBean bean = new FilterRegistrationBean(corsFilter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }


}
