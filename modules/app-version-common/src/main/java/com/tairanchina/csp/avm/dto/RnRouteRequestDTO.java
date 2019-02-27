package com.tairanchina.csp.avm.dto;


import io.swagger.annotations.ApiModelProperty;

public class RnRouteRequestDTO {

//    @ApiModelProperty(value = "主键ID，非必填，可能部分编辑操作时需要")
//    private String id;
    @ApiModelProperty(value = "路由通用昵称")
    private String routeName;
    @ApiModelProperty(value = "被拦截URL（约定）")
    private String routeKey;
    @ApiModelProperty(value = "目标URL")
    private String routeValue;
    @ApiModelProperty(value = "是否iOS开启")
    private Integer iosEnabled;
    @ApiModelProperty(value = "是否android开启")
    private Integer androidEnabled;
    @ApiModelProperty(value = "路由状态 0:关闭 1:线上开启 2:测试需要")
    private Integer routeStatus;
    @ApiModelProperty(value = "ios最小版本")
    private String iosMin;
    @ApiModelProperty(value = "ios最大版本")
    private String iosMax;
    @ApiModelProperty(value = "android最小版本")
    private String androidMin;
    @ApiModelProperty(value = "android最大版本")
    private String androidMax;

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public String getRouteValue() {
        return routeValue;
    }

    public void setRouteValue(String routeValue) {
        this.routeValue = routeValue;
    }

    public Integer getIosEnabled() {
        return iosEnabled;
    }

    public void setIosEnabled(Integer iosEnabled) {
        this.iosEnabled = iosEnabled;
    }

    public Integer getAndroidEnabled() {
        return androidEnabled;
    }

    public void setAndroidEnabled(Integer androidEnabled) {
        this.androidEnabled = androidEnabled;
    }

    public Integer getRouteStatus() {
        return routeStatus;
    }

    public void setRouteStatus(Integer routeStatus) {
        this.routeStatus = routeStatus;
    }

    public String getIosMin() {
        return iosMin;
    }

    public void setIosMin(String iosMin) {
        this.iosMin = iosMin;
    }

    public String getIosMax() {
        return iosMax;
    }

    public void setIosMax(String iosMax) {
        this.iosMax = iosMax;
    }

    public String getAndroidMin() {
        return androidMin;
    }

    public void setAndroidMin(String androidMin) {
        this.androidMin = androidMin;
    }

    public String getAndroidMax() {
        return androidMax;
    }

    public void setAndroidMax(String androidMax) {
        this.androidMax = androidMax;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
}
