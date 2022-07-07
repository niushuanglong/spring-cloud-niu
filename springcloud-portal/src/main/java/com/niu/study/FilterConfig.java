package com.niu.study;

import com.niu.study.config.HandlerFilter;
import com.niu.study.config.JWTInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.Filter;

@Configuration
public class FilterConfig implements WebMvcConfigurer  {
    /**
     * 配置过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HandlerFilter());
        registration.addUrlPatterns("/*");
        registration.addUrlPatterns("/api/**");
        registration.setOrder(10);
        //registration.addInitParameter("portal");
        //registration.addInitParameter("ERR_URL", "login.html");
        registration.setName("sessionFilter");
        return registration;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new JWTInterceptor())
                .addPathPatterns("/**") //拦截的地址
                .excludePathPatterns("/doc.html") //不需要拦截的地
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/api/**")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/swagger-ui.html/**");
        registry.addInterceptor(new JWTInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }
    /**
     * 这是原来就写的用来解决跨域的，不过好像没有用（狗头保命）
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
