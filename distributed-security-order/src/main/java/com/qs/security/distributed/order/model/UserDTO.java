package com.qs.security.distributed.order.model;

import lombok.Data;


/**
 * 用户dto
 *
 * @author jianfeng
 * @date 2020/08/24
 */
@Data
public class UserDTO {

    /**
     * 用户id
     */
    private String id;
    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 姓名
     */
    private String fullname;

    /**
     * 用户信息
     */
    private String principal;




}
