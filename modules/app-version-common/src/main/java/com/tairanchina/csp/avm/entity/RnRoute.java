package com.tairanchina.csp.avm.entity;

/**
 * Created by hzlizx on 2018/6/20 0020
 */
public class RnRoute extends BasicEntity{

    private Integer id;
    private Integer appId;
    private String routeName;
    private String routeKey;
    private String routeValue;
    private Integer iosEnabled;
    private Integer androidEnabled;
    private Integer routeStatus;
    private String iosMin;
    private String iosMax;
    private String androidMin;
    private String androidMax;
    private Integer delFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

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

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
