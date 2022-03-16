package org.gaohui.message.config;

import org.gaohui.message.websocket.MessageWebSocketHandler;
import org.gaohui.message.websocket.MessageWebSocketShakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 开启 Spring WebSocket
 * @author GH
 */
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 配置处理器
        registry.addHandler(this.webSocketHandler(), "/information/")
                // 配置拦截器
                .addInterceptors(new MessageWebSocketShakeInterceptor())
                // 解决跨域问题
                .setAllowedOrigins("*");
    }

    @Bean
    public MessageWebSocketHandler webSocketHandler() {
        return new MessageWebSocketHandler();
    }

    @Bean
    public MessageWebSocketShakeInterceptor webSocketShakeInterceptor() {
        return new MessageWebSocketShakeInterceptor();
    }

}
