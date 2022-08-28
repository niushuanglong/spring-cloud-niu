package com.niu.study.web.api;

import com.niu.study.service.LoginService;
import com.niu.study.utils.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/login")
@Api(value = "LoginController", tags = "登录接口")
public class LoginController {
    @Autowired
    public LoginService loginService;

    //暂时未添加记住我功能remember
    @RequestMapping(value = "/chunkLogin",method = RequestMethod.GET)
    @ApiOperation(value = "校验登陆用户名密码",httpMethod = "GET")
    public JsonResult chunkLogin(
                                 HttpServletResponse response,
                                 HttpServletRequest request) {
        return loginService.chunkLoginInfo("admin","123456",request);
    }





}
