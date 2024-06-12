package com.techprimers.springbootwebsocketexample;

import com.techprimers.springbootwebsocketexample.config.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringBootWebsocketExampleApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(SpringBootWebsocketExampleApplication.class, args);
		SpringContextUtil.setApplicationContext(applicationContext);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SpringBootWebsocketExampleApplication.class);
	}
}
