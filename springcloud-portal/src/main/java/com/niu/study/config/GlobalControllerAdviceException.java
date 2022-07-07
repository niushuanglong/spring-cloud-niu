package com.niu.study.config;

import com.niu.study.ExceptionDealWth.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

import static com.niu.study.domain.SYSCONSTANT.STATUS_ERR_SERVER;

@ControllerAdvice
public class GlobalControllerAdviceException {
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleException(RuntimeException e){
        ModelAndView modelAndView = new ModelAndView("404");
        modelAndView.addObject("message", e.getMessage());
        ModelAndView modelAndView1 = modelAndView.addObject("state", STATUS_ERR_SERVER.getId());
        modelAndView.addObject("data", null);
        return modelAndView;
    }
    //不穿参数以返回值map为key 返回
    @ModelAttribute()
    public Map<String, String> presetParam(Model model){
        model.addAttribute("mymap","这是我设置的全局数据map Controller可通过model拿到 ");
        Map<String, String> map = new HashMap<String, String>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        return map;
    }
}
