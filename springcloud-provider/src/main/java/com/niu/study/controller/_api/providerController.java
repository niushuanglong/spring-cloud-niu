package com.niu.study.controller._api;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/_api/provider")
@Api(tags = "服务提供者接口")
public class providerController {

    @GetMapping("/test")
    public String testProviderRest() {
        System.err.println(".002222");
        return "success";
    }
}
