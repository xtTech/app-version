package com.tairanchina.csp.avm.dto;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class AdminUpdateNickNameRequestDTO {

    @ApiModelProperty(value = "用户Id", required = true)
    @NotBlank(message = "userId不能为空")
    private String userId;

    @ApiModelProperty(value = "用户昵称", required = true)
    @NotBlank(message = "nickName不能为空")
    private String nickName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
