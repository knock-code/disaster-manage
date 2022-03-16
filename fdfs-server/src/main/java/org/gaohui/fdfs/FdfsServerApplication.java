package org.gaohui.fdfs;

import org.gaohui.common.aspect.JwtUtils;
import org.gaohui.common.aspect.RequestCheckAspect;
import org.gaohui.common.aspect.RoleAuthCheckAspect;
import org.gaohui.common.config.RedisConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.FilterType;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author gaohui  2021/10/22
 */
@ComponentScans(value = {
        @ComponentScan(basePackages = "org.gaohui.common.config"),
        @ComponentScan(basePackages = "org.gaohui.common.exception"),
        @ComponentScan(basePackages = "org.gaohui.common.aspect")
})
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("org.gaohui.fdfs.mapper")
@SpringBootApplication
@EnableSwagger2
public class FdfsServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FdfsServerApplication.class, args);
    }
}


