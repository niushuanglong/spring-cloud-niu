package com.niu.study.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/provider")
@Api(tags = "服务提供者接口")
public class providerController {

    @GetMapping("/test")
    public String testProviderRest() {
        System.err.println(".002222");
        return "success";
    }

    @ApiOperation("测试map传递参数")
    @RequestMapping(value = "/sendMapParam", method = RequestMethod.GET)
    public String sendMapParam(@RequestParam("phoneNumbers") String phoneNumbers) {
        //String phoneNumbers = map.get("phoneNumbers");
        System.err.println(phoneNumbers);
        System.err.println("sss");
        return "success";
    }


}
