package com.niu.study.web.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/api")
public class HomeController {
    @RequestMapping(value = "/home",method = RequestMethod.GET)
    @ApiOperation("首页")
    public String home() {
        return "page/home";
    }

}
