package com.qs.security.distributed.zuul.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.qs.security.distributed.zuul.comm.util.EncryptUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.*;

/**
 * 过滤器
 *
 * @author jianfeng
 * @date 2020/08/24
 */
public class AuthFilter extends ZuulFilter {

    /**
     * 过滤器类型(pre-之前)
     *
     * @return {@link String}
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 0表示优先级最高
     *
     * @return int
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 转发解析后的token
     *
     * @return {@link Object}* @throws ZuulException zuul例外
     */
    @Override
    public Object run() throws ZuulException {
        // 获取当前请求
        RequestContext ctx=RequestContext.getCurrentContext();
        // 上下文中拿到用户身份对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof OAuth2Authentication)){
            // 无token访问网关内资源的情况，目前仅有uua服务直接暴露
            return null;
        }

        OAuth2Authentication oAuth2Authentication= (OAuth2Authentication) authentication;
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        // 取出用户身份
        String principal = userAuthentication.getName();

        List<String> authorities=new ArrayList<>();
        // 从userAuthentication取出权限，放在authorities中
        userAuthentication.getAuthorities().forEach(c->authorities.add((c).getAuthority()));

        // 其它信息
        OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();

        Map<String, String> requestParameters = oAuth2Request.getRequestParameters();
        Map<String,Object> jsonToken=new HashMap<>(requestParameters);
        if (userAuthentication!=null){
            jsonToken.put("principal",principal);
            jsonToken.put("authorities",authorities);
        }

        // 把身份信息和权限信息放在json中，加入http的header中(转成Base64),转发微服务
        ctx.addZuulRequestHeader("json-token", EncryptUtils.encodeUTF8StringBase64(JSON.toJSONString(jsonToken)));

        return null;
    }
}
