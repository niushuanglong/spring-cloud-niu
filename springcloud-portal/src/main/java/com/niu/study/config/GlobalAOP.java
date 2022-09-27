package com.niu.study.config;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.niu.study.domain.AccessToken;
import com.niu.study.utils.enums.JsonResult;
import io.swagger.models.auth.In;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import com.google.gson.JsonObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;


@Aspect
@Component
public class GlobalAOP {

    private static Logger logger = LoggerFactory.getLogger(GlobalAOP.class);
    public static HttpServletResponse response=null;
    public static HttpServletRequest request=null;


    @Pointcut("execution(* com.niu.study.web.api.LoginController.*(..))")
    public void pointCut() {
    }


    @Around("pointCut()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        if (response==null||request==null){
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            assert sra != null;
            request = sra.getRequest();
            response = sra.getResponse();
        }
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        long startTime = System.currentTimeMillis();
        logger.info("{url:{}, method:{}, queryString:{}}", url, method, queryString);
        Object rs;
        boolean successAble = false;
        JsonObject paramsJson = new JsonObject();
        try {
            Object[] params = jp.getArgs();
            MethodSignature target = (MethodSignature)jp.getSignature();
            target.getMethod();
            System.err.println(target.getMethod());
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
    @AfterReturning(value = "execution(* com.niu.study.web.api.LoginController.*(..))",returning = "methodResult")
    public void afterReturning(JoinPoint joinPoint,Object methodResult){
        MethodSignature target = (MethodSignature)joinPoint.getSignature();
        Method method = target.getMethod();
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        assert sra != null;
        HttpServletRequest request = sra.getRequest();
        HttpServletResponse response = sra.getResponse();
        if (methodResult!=null){
            JsonResult result = (JsonResult) methodResult;
            if (result.getData()!=null){
                AccessToken token=(AccessToken)result.getData();
                Cookie cookie = new Cookie("token", token.getId());
                //cookie.setMaxAge(maxTimeOut);
                response.addCookie(cookie);
            }

        }
    }


}
