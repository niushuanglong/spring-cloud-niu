package com.niu.study.config;

import com.niu.study.ExceptionDealWth.CustomizeException;
import com.niu.study.utils.enums.JsonResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller层处理异常
 */


@RestControllerAdvice
public class GlobalControllerAdviceException {
    @ExceptionHandler(CustomizeException.class)
    public JsonResult<Void> success(CustomizeException e){
        JsonResult<Void> jsonResult=new JsonResult<>(e);
        if(e instanceof CustomizeException) {
            jsonResult.setMessage(e.getMessage());
            jsonResult.setData(null);
            jsonResult.setCode(500);
        }
        return jsonResult;
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
