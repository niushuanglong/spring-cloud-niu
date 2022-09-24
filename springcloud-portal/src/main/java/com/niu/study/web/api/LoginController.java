package com.niu.study.web.api;

import com.niu.study.service.LoginService;
import com.niu.study.service.dto.UserDto;
import com.niu.study.utils.enums.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/login")
@Api(value = "令牌接口集合", tags = "登录接口")
public class LoginController {
    @Autowired
    public LoginService loginService;

    //暂时未添加记住我功能remember
    @RequestMapping(value = "/chunkLogin",method = RequestMethod.POST)
    @ApiOperation(value = "校验登陆用户名密码",httpMethod = "POST")
    public JsonResult chunkLogin(@RequestBody UserDto dto, HttpServletResponse response, HttpServletRequest request) {
        return loginService.chunkLoginInfo(dto.getUsername(),dto.getPassword(),response);
    }





}
