package com.gjsg.gjchat.webscoket.config;

import com.gjsg.gjchat.webscoket.ChatWebScoket;
import com.gjsg.gjchat.webscoket.vo.AppGroupVo;
import org.kurento.client.KurentoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dlf on 2018/2/5 0005.
 * Email 1429264916@qq.com
 */
@Configuration
public class WebSocketConfig  implements WebSocketConfigurer {
    @Autowired
    ChatWebScoket  chatwebscoket;

//    @Bean
//    public KurentoClient kurentoClient() {
//        return KurentoClient.create();
//    }
    /**
     * 保存webscoketSession，由于WebSocketSession被设计成无法序列化，所以保存在各自
     * 独立系统的内存中，集群的时候，通过ip定位到具体的项目获取session
     * string  -用户guid
     */
    public static final Map<String,WebSocketSession> WEBSCOKETSESSION=new ConcurrentHashMap<>();
    //session id
    public static final Map<String,AppGroupVo> SESSIONID=new ConcurrentHashMap<>();

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //跨域设置，白名单设置
        registry.addHandler(chatwebscoket, "/v1/chat").setAllowedOrigins("*");
    }

}
