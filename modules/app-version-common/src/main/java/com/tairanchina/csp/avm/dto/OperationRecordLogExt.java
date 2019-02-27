package com.tairanchina.csp.avm.dto;

import com.tairanchina.csp.avm.entity.OperationRecordLog;

public class OperationRecordLogExt extends OperationRecordLog {
    private String phone;
    private String nickName;

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
}
