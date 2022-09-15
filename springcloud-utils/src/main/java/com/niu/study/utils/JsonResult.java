package com.niu.study.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

/**
 * 基于此对象封装控制层对象
 * 的响应结果,在此对象中应该
 * 包含返回到客户端的数据以及
 * 一个状态码和状态信息
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class JsonResult<T> implements Serializable {
    private static final long serialVersionUID = -5766977494287555486L;
    /**
     * 状态码
     */
    private int state = 100;//100 ok,400 error
    /**
     * 状态码对应的信息
     */
    private String message = "ok";
    /**
     * 正常数据
     */
    private Object data;

    public JsonResult(String message) {
        this.message = message;
    }

    public JsonResult(Object data) {
        this.data = data;
    }

    public JsonResult(Throwable e) {
        this.state = 0;
        this.message = e.getMessage();
    }

    public JsonResult(int state, String message, Object data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }

    public static final <T> JsonResult<T> restapi(Object logic){
        JsonResult<T> result = new JsonResult<T>();
        try {

        } catch (Exception e) {
            e.printStackTrace();
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer, true));
            String exceptionMsg = writer.toString();
            result.setMessage(e.getMessage());
        }
        return result;
    }
}



