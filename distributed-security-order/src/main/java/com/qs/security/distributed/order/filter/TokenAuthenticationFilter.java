package com.qs.security.distributed.order.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qs.security.distributed.order.comm.util.EncryptUtils;
import com.qs.security.distributed.order.model.UserDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 令牌的身份验证过滤器
 *
 * @author jianfeng
 * @date 2020/08/24
 */
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 解析请求头的token
        String token = httpServletRequest.getHeader("json-token");
        if (StringUtils.isNotBlank(token)) {
            // 解码
            String json = EncryptUtils.decodeUTF8StringBase64(token);

            // 将token转成json对象
            JSONObject jsonObject = JSON.parseObject(json);
            // 取出用户信息
//            String principal = jsonObject.getString("principal");
//            UserDTO user=new UserDTO();
//            user.setUsername(principal);
            UserDTO user = JSON.parseObject(jsonObject.getString("principal"), UserDTO.class);


            // 取出用户权限
            JSONArray authoritiesArray = jsonObject.getJSONArray("authorities");
            String[] authorities = authoritiesArray.toArray(new String[authoritiesArray.size()]);

            // 将用户信息和权限填充到用户身份token对象中
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, AuthorityUtils.createAuthorityList(authorities));
            // 创建details
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

            // 将authenticationToken填充到上下文中
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
