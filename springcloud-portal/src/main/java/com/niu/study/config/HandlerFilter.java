package com.niu.study.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niu.study.domain.AccessToken;
import com.niu.study.domain.User;
import com.niu.study.service.LoginService;
import com.niu.study.utils.JWTTokenUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandlerFilter implements Filter {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HandlerFilter.class);

    private LoginService loginService;
    private List<String> ignoredUrls;

    public HandlerFilter(LoginService loginService, List<String> ignoredUrls) {
        this.loginService=loginService;
        this.ignoredUrls=ignoredUrls;
    }
    private String getAccessUrl(HttpServletRequest req) {
        String origUrl = req.getRequestURI();
        String accessUrl = origUrl.replaceFirst(req.getContextPath(),"")
                .replaceAll("/{1,}","/");
        return accessUrl;
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse hres = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession();
        AccessToken accessToken=null;
        if (null!=(AccessToken)session.getAttribute("accessToken")){
            accessToken=(AccessToken)session.getAttribute("accessToken");
            JWTTokenUtil.verifyTokenAndGetClaims(accessToken.getAccessToken());
        }else if (null!=hres.getHeader("accessToken")){
            String token=hres.getHeader("accessToken");
            JWTTokenUtil.verifyTokenAndGetClaims(token);
        }else if (null!=req.getParameter("accessToken")){
            String token=req.getParameter("accessToken");
            JWTTokenUtil.verifyTokenAndGetClaims(token);
        }
        String accessUrl = getAccessUrl(req);
//        if (accessUrl.contains("api/doLogin")) {
//            chain.doFilter(req, hres);
//            hres.sendRedirect("login");
//        }
        if (accessToken!=null) {
            AccessToken token=loginService.queryAccessToken(accessToken.getAccessToken());
            if (token==null){
                throw new RuntimeException("用户不存在!");
            }
            if (new Date().compareTo(token.getExpireTime())==1){
                throw new RuntimeException("登陆超时!");
            }
            Map<String, Object> map = JWTTokenUtil.verifyTokenAndGetClaims(token.getAccessToken());
            String username = map.get("username").toString();
            User user=loginService.findUserByUsername(username);
            if (user==null){
                throw new RuntimeException("密码错误!");
            }
            //更新超时时间 如果token在数据库不为空  刷新token时长
            token.updateExpireTime(new Date(24 * 60 * 60));
            loginService.refreshToken(token);
        } else if(ignoredUrls.contains(accessUrl)){
            hres.setContentType("application/json;charset=UTF-8");
            hres.setHeader("Cache-Control", "no-store");
            hres.setHeader("Pragma", "no-cache");
            hres.setDateHeader("Expires", 0);
            hres.getWriter().write(new ObjectMapper().writeValueAsString("登录超时！"));
            hres.getWriter().flush();
            hres.getWriter().close();
            return;
        }
        chain.doFilter(req, hres);
        return;
    }

    @Override
    public void destroy() {

    }

}