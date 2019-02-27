package com.tairanchina.csp.avm.entity;

import com.ecfront.dew.common.$;

/**
 * 当前登录用户的ID，APPID，以及是否为超级管理员
 * Created by hzlizx on 2018/5/17 0017
 */
public class LoginInfo {
    private Integer appId;
    private String userId;
    private String nickName;
    private String phone;
    private boolean admin;

    public LoginInfo(Integer appId, String userId) {
        this.appId = appId;
        this.userId = userId;
    }

    public LoginInfo(Integer appId, String userId, boolean admin) {
        this.appId = appId;
        this.userId = userId;
        this.admin = admin;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public boolean isAdmin() {
        return admin;
    }

    public LoginInfo setAdmin(boolean admin) {
        this.admin = admin;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return $.json.toJsonString(this);
    }
}
