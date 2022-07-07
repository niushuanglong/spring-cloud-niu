package com.niu.study.config;

import com.niu.study.domain.AccessToken;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandlerFilter implements Filter {
        private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HandlerFilter.class);


        /**
         * 封装，不需要过滤的list列表
         */
        protected static List<Pattern> patterns = new ArrayList<Pattern>();
        static {
            patterns.add(Pattern.compile("api"));
            patterns.add(Pattern.compile("static"));
        }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            HttpServletResponse res = (HttpServletResponse) servletResponse;
        String origin = req.getHeader("Origin");

            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json; charset=utf-8");
            res.setHeader("Access-Control-Allow-Origin", origin);
            res.setHeader("Access-Control-Allow-Credentials", "true");
            res.setHeader("Access-Control-Allow-Methods", "*");
            res.setHeader("Access-Control-Allow-Headers", "Content-Type,Authorization,token");
            res.setHeader("Access-Control-Expose-Headers", "*");

            String url = req.getRequestURI().substring(req.getContextPath().length());
            String[] temp;
            if (url.startsWith("/") && url.length() > 1) {
                url = url.substring(1);
            }
            if(url.contains("swagger-")||url.contains("v2/api-docs")){
                chain.doFilter(req, res);
                return;
            }
            temp = url.split("/");
            if (isInclude(temp[0])) {
                chain.doFilter(req, res);//相当于放行  去执行下一个过滤器或者Servlet 就相当于不拦截了
                return;
            }
            HttpSession session = req.getSession();
            AccessToken accessToken=null;
            if (null!=(AccessToken)session.getAttribute("accessToken")){
                accessToken=(AccessToken)session.getAttribute("accessToken");
                JWTTokenUtil.verifyTokenAndGetClaims(accessToken.getAccessToken());
            }else if (null!=res.getHeader("accessToken")){
                String token=res.getHeader("accessToken");////暂时不写忘了
                JWTTokenUtil.verifyTokenAndGetClaims(token);
            }else if (null!=req.getParameter("accessToken")){
                String token=req.getParameter("accessToken");
                JWTTokenUtil.verifyTokenAndGetClaims(token);
            }
            //accessToken.getUserName(); //可以这样写
            if (accessToken== null|| StringUtils.isBlank(accessToken.getAccessToken())) {
                session.invalidate();
                res.sendRedirect(req.getContextPath()+"/api/doLogin");
                return;
            }

        chain.doFilter(req, res);
        }

        @Override
        public void destroy() {

        }


        /**
         * 是否需要过滤
         *
         * @param url
         * @return
         */
        private boolean isInclude(String url) {
            for (Pattern pattern : patterns) {
                Matcher matcher = pattern.matcher(url);
                if (matcher.matches()) {
                    return true;
                }
            }
            return false;
        }

}