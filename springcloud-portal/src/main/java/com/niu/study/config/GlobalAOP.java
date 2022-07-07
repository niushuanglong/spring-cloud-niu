package com.niu.study.config;

import com.niu.study.utils.JsonResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;

@Aspect
@Component
public class GlobalAOP {
    @Around("execution(* com.niu.study.web.*.*(..))")
    public Object around(ProceedingJoinPoint jp) {
        JsonResult<Object> jsonResult = new JsonResult<>();
        try {
            String name = jp.getSignature().getName();//获取调用该类的方法名
            try {
                Object [] args = jp.getArgs();//获取参数
                System.out.println("Before->The "+name+" method begins");
                System.out.println("Before->The params of the "+name+" method are "+args[0]+","+args[1]);
                Object result = jp.proceed();// 执行目标对象内的方法
            } catch (Exception e){
                e.printStackTrace();
                StringWriter writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer, true));
                String exceptionMsg = writer.toString();
                System.err.println("exceptionMsg 异常为:->"+exceptionMsg);
                jsonResult.setMessage(e.getMessage()).setState(400).setData(null);
            }finally {
                System.out.println("After->"+"The "+name+" method ends"+" 桃李不言下自成蹊!牛双龙");// @After注解所修饰的方法
            }
        } catch (Throwable e) {
            System.out.println("AfterThrowing->"+e);// @AfterThrowing注解所修饰的方法
        }
        return jsonResult;
    }
}
