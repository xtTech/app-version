package com.tairanchina.csp.avm.entity;

/**
 * Created by hzlizx on 2018/6/20 0020
 */
public class RnPackage extends BasicEntity {

    private Integer id;
    private Integer appId;
    private String rnName;
    private String rnNickName;
    private Integer rnType;
    private String resourceUrl;
    private Integer rnSize;
    private String rnVersion;
    private String rnUpdateLog;
    private Integer rnStatus;
    private Integer delFlag;
    private String versionMin;
    private String versionMax;

    public String getVersionMin() {
        return versionMin;
    }

    public void setVersionMin(String versionMin) {
        this.versionMin = versionMin;
    }

    public String getVersionMax() {
        return versionMax;
    }

    public void setVersionMax(String versionMax) {
        this.versionMax = versionMax;
    }

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

    public String getRnName() {
        return rnName;
    }

    public void setRnName(String rnName) {
        this.rnName = rnName;
    }

    public String getRnNickName() {
        return rnNickName;
    }

    public void setRnNickName(String rnNickName) {
        this.rnNickName = rnNickName;
    }

    public Integer getRnType() {
        return rnType;
    }

    public void setRnType(Integer rnType) {
        this.rnType = rnType;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public Integer getRnSize() {
        return rnSize;
    }

    public void setRnSize(Integer rnSize) {
        this.rnSize = rnSize;
    }

    public String getRnVersion() {
        return rnVersion;
    }

    public void setRnVersion(String rnVersion) {
        this.rnVersion = rnVersion;
    }

    public String getRnUpdateLog() {
        return rnUpdateLog;
    }

    public void setRnUpdateLog(String rnUpdateLog) {
        this.rnUpdateLog = rnUpdateLog;
    }

    public Integer getRnStatus() {
        return rnStatus;
    }

    public void setRnStatus(Integer rnStatus) {
        this.rnStatus = rnStatus;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
