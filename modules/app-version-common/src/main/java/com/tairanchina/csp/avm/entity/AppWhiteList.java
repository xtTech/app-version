package com.tairanchina.csp.avm.entity;

import javax.validation.constraints.Pattern;

public class AppWhiteList extends BasicEntity {

    private Integer id;
    private Integer appId;
    //白名单组名
    private String batchName;
    //白名单类型：ip,phone
    private String whiteType;
    @Pattern(regexp = "(^((?:,\\s*)?\\d{1,3}(?:\\.\\d{1,3}){3})+$)|(^1[3|4|5|6|7|8|9]\\d{9}$)", message = "白名单应为手机号或IP")
    private String whiteValue;
    //所属白名单组的ID
    private Integer batchId;
    //是否是白名单组
    private Integer isBatch;
    private Integer delFlag;

    public enum WhiteType {
        ip,
        phone
    }

    public Integer getId() {
        return id;
    }

    public AppWhiteList setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getAppId() {
        return appId;
    }

    public AppWhiteList setAppId(Integer appId) {
        this.appId = appId;
        return this;
    }


    public String getWhiteType() {
        return whiteType;
    }

    public AppWhiteList setWhiteType(String whiteType) {
        this.whiteType = whiteType;
        return this;
    }

    public String getWhiteValue() {
        return whiteValue;
    }

    public AppWhiteList setWhiteValue(String whiteValue) {
        this.whiteValue = whiteValue;
        return this;
    }

    public String getBatchName() {
        return batchName;
    }

    public AppWhiteList setBatchName(String batchName) {
        this.batchName = batchName;
        return this;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public AppWhiteList setBatchId(Integer batchId) {
        this.batchId = batchId;
        return this;
    }

    public Integer getIsBatch() {
        return isBatch;
    }

    public AppWhiteList setIsBatch(Integer isBatch) {
        this.isBatch = isBatch;
        return this;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public AppWhiteList setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
        return this;
    }
}
