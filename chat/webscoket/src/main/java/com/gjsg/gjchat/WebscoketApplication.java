package com.gjsg.gjchat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
@MapperScan("com.gjsg.gjchat.webscoket.dao")
public class WebscoketApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
//    System.setProperty("kms.url","ws://36.111.44.141:8888/kurento");
//    System.setProperty("DNS_NAME","app141.fromfuture.cn:9999");
		SpringApplication.run(WebscoketApplication.class, args);
	}
}
