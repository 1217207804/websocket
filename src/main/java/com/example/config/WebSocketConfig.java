package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author gxq
 * @Description
 * @create 2020-08-19 9:48
 */
@Configuration
public class WebSocketConfig {

    /**
     *  @author: gxq
     *  @Date: 2020/8/19 9:50
     *  @Description:ServerEndpointExporter 作用
     *  这个Bean会自动注册使用@ServerEndpoint注解声明的websocket endpoint
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
