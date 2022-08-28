package com.niu.study.web.api;

import com.niu.study.service.UserService;
import com.niu.study.service.dto.UserDto;
import com.niu.study.service.impl.UserServiceImpl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
@ApiModel("用户接口集")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("创建用户接口")
    @RequestMapping(value = "/createUser",method = RequestMethod.POST)
    public void createUser(UserDto dto){
        userService.createUser(dto);
    }

    @ApiOperation(value = "查询用户接口",httpMethod = "GET")
    @RequestMapping(value = "/queryUser",method = RequestMethod.GET)
    public UserDto queryUser(HttpServletRequest request){
        String accessToken = request.getAttribute("accessToken").toString();
        return userService.queryUser(accessToken);
    }

}
