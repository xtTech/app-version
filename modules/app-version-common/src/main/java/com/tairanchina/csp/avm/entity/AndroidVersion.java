package com.tairanchina.csp.avm.entity;

/**
 * 安卓版本
 * Created by hzlizx on 2018/6/6 0006
 */
public class AndroidVersion extends BasicEntity{
    private Integer id;
    private Integer appId;
    private String appVersion;
    private Integer updateType;
    private String versionDescription;
    private String allowLowestVersion;
    private Integer versionStatus;
    private Integer grayReleased;
    private Integer whiteListId;
    private Integer ipListId;
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

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Integer getUpdateType() {
        return updateType;
    }

    public void setUpdateType(Integer updateType) {
        this.updateType = updateType;
    }

    public String getVersionDescription() {
        return versionDescription;
    }

    public void setVersionDescription(String versionDescription) {
        this.versionDescription = versionDescription;
    }

    public String getAllowLowestVersion() {
        return allowLowestVersion;
    }

    public void setAllowLowestVersion(String allowLowestVersion) {
        this.allowLowestVersion = allowLowestVersion;
    }

    public Integer getVersionStatus() {
        return versionStatus;
    }

    public void setVersionStatus(Integer versionStatus) {
        this.versionStatus = versionStatus;
    }

    public Integer getGrayReleased() {
        return grayReleased;
    }

    public void setGrayReleased(Integer grayReleased) {
        this.grayReleased = grayReleased;
    }

    public Integer getWhiteListId() {
        return whiteListId;
    }

    public void setWhiteListId(Integer whiteListId) {
        this.whiteListId = whiteListId;
    }

    public Integer getIpListId() {
        return ipListId;
    }

    public void setIpListId(Integer ipListId) {
        this.ipListId = ipListId;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
