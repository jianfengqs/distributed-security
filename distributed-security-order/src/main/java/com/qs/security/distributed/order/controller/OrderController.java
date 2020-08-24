package com.qs.security.distributed.order.controller;

import com.qs.security.distributed.order.model.UserDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资源服务器控制器
 *
 * @author jianfeng
 * @date 2020/08/21
 */
@RestController
public class OrderController {


    /**
     * 拥有p1权限方可访问此url
     *
     * @return {@link String}
     */
    @GetMapping(value = "/r1")
    @PreAuthorize("hasAuthority('p1')")
    public String r1(){
        UserDTO principal= (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getFullname()+"访问资源1";
    }

}
