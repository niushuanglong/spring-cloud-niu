package com.niu.study.web.api;

import com.niu.study.service.UserService;
import com.niu.study.service.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
@Api(value = "用户接口集合", tags = "用户接口")
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
