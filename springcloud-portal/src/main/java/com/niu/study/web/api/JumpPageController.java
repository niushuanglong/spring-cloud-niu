package com.niu.study.web.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 所有页面跳转用
 */

@Controller
@RequestMapping("/home")
public class JumpPageController {
    @RequestMapping(value = "/doLogin",method = RequestMethod.GET)
    @ApiOperation("登陆页面")
    public String doLogin() {
        return "login";
    }

}
