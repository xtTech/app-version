package com.tairanchina.csp.avm.dto;

import com.ecfront.dew.common.$;

/**
 * Created by hzlizx on 2019/2/21
 */
public class UserLoginStatus {

    private String jwt;
    private String userId;
    private String NickName;
    private String phone;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
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
