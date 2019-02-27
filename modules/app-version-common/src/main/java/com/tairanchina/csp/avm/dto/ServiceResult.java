package com.tairanchina.csp.avm.dto;

import com.ecfront.dew.common.$;

/**
 * Service返回
 * Created by hzlizx on 2018/4/11 0011
 */
public class ServiceResult {

    /**
     * Code     错误码，200为正常
     * message  文字消息
     * data     需要封装往前台传的对象
     */
    private int code;
    private String message;
    private Object data;

    public ServiceResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ServiceResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public ServiceResult setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ServiceResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ServiceResult setData(Object data) {
        this.data = data;
        return this;
    }

    public static ServiceResult ok(Object data) {
        ServiceResult serviceResult = new ServiceResult(200, "请求成功");
        serviceResult.setData(data);
        return serviceResult;
    }

    public static ServiceResult failed(int code, String message) {
        ServiceResult serviceResult = new ServiceResult(code, message);
        return serviceResult;
    }


    @Override
    public String toString() {
        return $.json.toJsonString(this);
    }

}
