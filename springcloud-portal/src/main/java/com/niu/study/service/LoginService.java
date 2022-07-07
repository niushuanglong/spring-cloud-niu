package com.niu.study.service;

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
}
