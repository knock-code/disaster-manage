package org.gaohui.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gaohui  2022/01/24
 */
@Configuration
public class GatewayRouteConfiguration {
    @Bean
    public RouteLocator externalRouteLocator(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();
        routes
                .route("cms", (r) ->
                        r.path("/cms/**")
                                .filters(f -> f.stripPrefix(1))  // stripPrefix的意思是设置将转发的URL地址进行取消第一个地址
                                .uri("http://124.223.97.154:9999"))
                .route("fdfs", (r) ->
                        r.path("/fdfs/**")
                                .filters(f -> f.stripPrefix(1))
                                .uri("http://124.223.97.154:10000"))
                .route("disaster", (r) ->
                        r.path("/disaster/**")
                                .filters(f -> f.stripPrefix(1))
                                .uri("http://124.223.97.154:10001"))
                .route("message", (r) ->
                        r.path("/message/**")
                                .filters(f -> f.stripPrefix(1))
                                .uri("http://124.223.97.154:10002"))
                .route("analyse", (r) ->
                        r.path("/analyse/**")
                                .filters(f -> f.stripPrefix(1))
                                .uri("http://124.223.97.154:10003")
                        )
        ;
        return routes.build();
    }
}
