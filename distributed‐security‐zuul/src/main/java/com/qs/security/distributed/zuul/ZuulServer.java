package com.qs.security.distributed.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;


/**
 * zuul服务器启动类
 *
 * @author jianfeng
 * @date 2020/08/21
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class ZuulServer {

    public static void main(String[] args) {
        SpringApplication.run(ZuulServer.class,args);
    }

}
