package com.tairanchina.csp.avm.dto;

import io.swagger.annotations.ApiModelProperty;

public class AndroidVersionRequestDTO {

    @ApiModelProperty(value = "主键ID，非必填，可能部分编辑操作时需要")
    private String id;
    @ApiModelProperty(value = "版本号")
    private String appVersion;
    @ApiModelProperty(value = "更新类型，0：强制更新 1：一般更新 2：静默更新 3：可忽略更新 4：静默可忽略更新")
    private Integer updateType;
    @ApiModelProperty(value = "更新描述")
    private String versionDescription;
    @ApiModelProperty(value = "允许最低版本（低于这个要强制更新）")
    private String allowLowestVersion;
    @ApiModelProperty(value = "发布状态（0-未上架；1-已上架）")
    private Integer versionStatus;
    @ApiModelProperty(value = "灰度发布（0-无；1-白名单发布；2-IP发布）")
    private Integer grayReleased;
    @ApiModelProperty(value = "白名单ID")
    private Integer whiteListId;
    @ApiModelProperty(value = "IP段发布的list ID")
    private Integer ipListId;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
