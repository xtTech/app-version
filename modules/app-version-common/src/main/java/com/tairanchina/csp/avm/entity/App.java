package com.tairanchina.csp.avm.entity;


import io.swagger.annotations.ApiModelProperty;

/**
 * 应用
 * Created by hzlizx on 2018/6/6 0006
 */
public class App extends BasicEntity {
    private Integer id;
    @ApiModelProperty(value = "APP名称，应用名")
    private String appName;
    @ApiModelProperty(value = "appId,应用ID，请和用户中心的应用ID保持一致，eg: uc28ec7f8870a6e785 ")
    private String tenantAppId;
    private Integer delFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }


    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getTenantAppId() {
        return tenantAppId;
    }

    public void setTenantAppId(String tenantAppId) {
        this.tenantAppId = tenantAppId;
    }
}
