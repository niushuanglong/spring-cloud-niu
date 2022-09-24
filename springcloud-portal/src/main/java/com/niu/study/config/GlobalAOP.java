package com.niu.study.config;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import com.google.gson.JsonObject;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Aspect
@Component
public class GlobalAOP {
    private static Logger logger = LoggerFactory.getLogger(GlobalAOP.class);
    @Pointcut("execution(* com.niu.study.web.api.LoginController.*(..))")
    public void pointCut() {
    }


    @Around("pointCut()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        assert sra != null;
        HttpServletRequest request = sra.getRequest();
        HttpServletResponse response = sra.getResponse();
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        long startTime = System.currentTimeMillis();
        logger.info("{url:{}, method:{}, queryString:{}}", url, method, queryString);
        Object rs;
        boolean successAble = false;
        JsonObject paramsJson = new JsonObject();
        Cookie cookie = new Cookie("aaa", "aaaa");
        cookie.setMaxAge(24 * 60 * 60 * 2);
        response.addCookie(cookie);
        try {
            Object[] params = jp.getArgs();
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof BindingResult || params[i] instanceof HttpRequest || params[i] instanceof HttpResponse){
                    continue;
                }
                paramsJson.addProperty("param-" + i, JSONUtil.toJsonPrettyStr(params[i]));
            }
            rs = jp.proceed();
            successAble = true;
        } finally {
            logger.info("{url:{}, method:{}, success-able:{}, exe-time:{}, params:{}}", url, method, successAble, System.currentTimeMillis() - startTime, paramsJson);
        }
        return rs;
    }
}
