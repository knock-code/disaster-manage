package org.gaohui.message;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author gaohui  2022/02/21
 */
@ComponentScans(value = {
        @ComponentScan(basePackages = "org.gaohui.common.config"),
        @ComponentScan(basePackages = "org.gaohui.common.exception"),
        @ComponentScan(basePackages = "org.gaohui.common.aspect")
})
@MapperScan(value = "org.gaohui.message.mapper")
@EnableDiscoveryClient
@EnableFeignClients
@EnableAsync
@SpringBootApplication
public class MessageServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageServerApplication.class, args);
    }
}
