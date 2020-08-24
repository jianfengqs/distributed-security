package com.qs.security.distributed.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * 订购服务器
 *
 * @author jianfeng
 * @date 2020/08/21
 */
@SpringBootApplication
@EnableDiscoveryClient
public class OrderServer {

    public static void main(String[] args) {
        SpringApplication.run(OrderServer.class,args);
    }

}
