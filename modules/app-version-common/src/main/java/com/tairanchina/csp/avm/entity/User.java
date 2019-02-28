package com.tairanchina.csp.avm.entity;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 用户表
 * Created by hzlizx on 2018/6/6 0006
 */
public class User {

    private String userId;
    private String phone;
    private String username;
    private String password;
    @ApiModelProperty(value = "用户昵称")
    private String nickName;
    private Date firstLoginTime;
    private Integer isAdmin;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Date getFirstLoginTime() {
        return firstLoginTime;
    }

    public void setFirstLoginTime(Date firstLoginTime) {
        this.firstLoginTime = firstLoginTime;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
