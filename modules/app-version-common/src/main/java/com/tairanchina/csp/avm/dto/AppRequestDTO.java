package com.tairanchina.csp.avm.dto;


import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class AppRequestDTO {

    @ApiModelProperty(value = "APP名称，应用名")
    @NotBlank(message = "应用名称不能为空")
    private String appName;
    @ApiModelProperty(value = "appId,应用ID，请和用户中心的应用ID保持一致，eg: uc28ec7f8870a6e785 ")
    @NotBlank(message = "应用id不能为空")
    private String tenantAppId;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTenantAppId() {
        return tenantAppId;
    }

    public void setTenantAppId(String tenantAppId) {
        this.tenantAppId = tenantAppId;
    }
}
