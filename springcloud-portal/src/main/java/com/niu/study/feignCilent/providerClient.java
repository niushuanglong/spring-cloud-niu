package com.niu.study.feignCilent;

import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "provider8001",
        path = "/_api/provider",
        configuration = UTF8JsonGenerator.class)
public interface providerClient {
        @RequestMapping(value="/test",method = RequestMethod.POST)
        String test();
}
