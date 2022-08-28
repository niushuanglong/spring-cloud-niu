package com.niu.study;

import java.util.ArrayList;
import java.util.List;

import com.niu.study.config.HandlerFilter;
import com.niu.study.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FiltersConfiguration {
    @Autowired
    private LoginService loginService;
    @Value("#{'${config.loginTokenFilter.ignoredUrls:}'.split(',')}")
    private List<String> ignoredUrls = new ArrayList<String>();
//    @Value("#{'${config.accessIPFilter.passOverIps:}'.split(',')}")
//    private List<String> passOverIps = new ArrayList<String>();
    
    //前后端交互处理-拦截所有api访问请求
    @Bean
    public FilterRegistrationBean apiFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new HandlerFilter(loginService, ignoredUrls));
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(5);
        return registrationBean;
    }
}
