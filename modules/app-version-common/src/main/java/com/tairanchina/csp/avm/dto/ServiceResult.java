package com.tairanchina.csp.avm.dto;

//import com.ecfront.dew.common.$;

import cn.hutool.json.JSONUtil;

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
    private String info;
    private Object record;

    public ServiceResult(int code, String info, Object record) {
        this.code = code;
        this.info = info;
        this.record = record;
    }

    public ServiceResult(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public ServiceResult setCode(int code) {
        this.code = code;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public ServiceResult setInfo(String info) {
        this.info = info;
        return this;
    }

    public Object getRecord() {
        return record;
    }

    public ServiceResult setRecord(Object record) {
        this.record = record;
        return this;
    }

    public static ServiceResult ok() {
        ServiceResult serviceResult = new ServiceResult(200, "请求成功");
        return serviceResult;
    }
    public static ServiceResult ok(Object record) {
        ServiceResult serviceResult = new ServiceResult(200, "请求成功");
        serviceResult.setRecord(record);
        return serviceResult;
    }

    public static ServiceResult failed(int code, String message) {
        ServiceResult serviceResult = new ServiceResult(code, message);
        return serviceResult;
    }
    public static ServiceResult failed() {
        ServiceResult serviceResult = new ServiceResult(-1, "请求失败");
        return serviceResult;
    }


    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }

}
