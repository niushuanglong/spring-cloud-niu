package com.niu.study.service;

import com.niu.study.domain.AccessToken;
import com.niu.study.domain.User;
import com.niu.study.utils.JsonResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginService {

    /**
     * 校验用户名密码  登陆
     * @param username
     * @param password
     * @return
     */
    JsonResult chunkLoginInfo(String username, String password, HttpServletRequest request);

    /**
     * 根据令牌查询
     * @param token
     * @return
     */
    AccessToken queryAccessToken(String token);
    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 刷新令牌时间
     * @param token
     */
    void refreshToken(AccessToken token);
}
