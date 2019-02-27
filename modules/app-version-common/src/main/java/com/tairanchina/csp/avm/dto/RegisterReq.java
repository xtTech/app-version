package com.tairanchina.csp.avm.dto;

/**
 * Created by hzlizx on 2019/2/21
 */
public class RegisterReq {
    private String phone;
    private String password;
    private String passwordConfirm;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
