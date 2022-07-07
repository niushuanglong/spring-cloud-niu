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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/api")
//页面跳转的时候 也会带上api 问题  就是api/jQuery/jquery.min.js /jQuery/jquery.min.js
@Api(value = "LoginController", tags = "登录接口")
public class LoginController {
    @Autowired
    public LoginService loginService;


    @RequestMapping(value = "/doLogin",method = RequestMethod.GET)
    @ApiOperation("登陆页面")
    public String doLogin() {
        return "login";
    }
    //暂时未添加记住我功能remember
    @RequestMapping(value = "/chunkLogin",method = RequestMethod.GET)
    @ApiOperation("远程调用校验登陆用户名密码")
    public JsonResult chunkLogin(@RequestParam("username") String username,
                                     @RequestParam("password") String password,
                                     HttpServletResponse response,
                                     HttpServletRequest request) {
        return loginService.chunkLoginInfo(username,password,request);
    }





}
