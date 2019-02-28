package com.tairanchina.csp.avm.enums;

import com.ecfront.dew.common.$;

import java.util.HashMap;

/**
 * Created by hzlizx on 2018/9/27 0027
 */
public enum ChatBotEventType {
    ANDROID_VERSION_CREATED("创建Android版本"),
    IOS_VERSION_CREATED("创建iOS版本"),
    CUSTOM_API_CREATED("创建自定义接口"),
    ;
    private String message;

    ChatBotEventType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("event", this.name());
        hashMap.put("message", this.message);
        return $.json.toJsonString(hashMap);
    }

}
