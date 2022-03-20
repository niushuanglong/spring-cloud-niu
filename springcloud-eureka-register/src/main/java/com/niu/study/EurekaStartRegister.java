package com.niu.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer //Ribbon 和 Eureka 整合以后，客户端可以直接调用，不用关心IP地址和端口号
// feign客户端注解,并指定要扫描的包以及配置接口DeptClientService
public class EurekaStartRegister {
  public static void main(String[] args) {
    SpringApplication.run(EurekaStartRegister.class, args);
  }
}
