package com.tairanchina.csp.avm.entity;

import java.util.Date;

/**
 * 用户和应用绑定关系
 * Created by hzlizx on 2018/6/6 0006
 */
public class UserAppRel {
    private String userId;
    private Integer appId;
    private Date createdTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
