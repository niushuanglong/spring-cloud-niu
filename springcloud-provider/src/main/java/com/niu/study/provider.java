package com.niu.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableEurekaClient
public class provider extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(provider.class, args);
    }

    @Bean
    public MultipartConfigElement getMultipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 当前项目中，无论上传的是什么，都不允许超过100M，否则直接报错，控制器根本就不执行
        DataSize dataSize = DataSize.ofMegabytes(100);
        factory.setMaxFileSize(dataSize);
        factory.setMaxRequestSize(dataSize);
        return factory.createMultipartConfig();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(provider.class);
    }
}
