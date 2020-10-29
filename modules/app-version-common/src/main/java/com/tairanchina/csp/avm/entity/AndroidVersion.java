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


    /**
     [
     {
     label: '不更新',
     value: -1
     },
     {
     label: '强制更新',
     value: 0
     },
     {
     label: '一般更新',
     value: 1
     },
     {
     label: '静默更新',
     value: 2
     },
     {
     label: '可忽略更新',
     value: 3
     },
     {
     label: '静默可忽略更新',
     value: 4
     }
     **/
    public enum UpdateType {
        UPDATETYPE_NO(-1, "不更新"),
        UPDATETYPE_FORCE_UPDATE(0, "强制更新"),
        UPDATETYPE_GENERAL_UPDATE(1, "一般更新"),
        UPDATETYPE_SILENT_UPDATE(2, "静默更新"),
        UPDATETYPE_NEGLIGIBLE_UPDATES(3, "可忽略更新"),
        UPDATETYPE_SILENT_IGNORE_UPDATES(4, "静默可忽略更新");


        private Integer code;
        private String info;

        private UpdateType(Integer code, String info) {
            this.code = code;
            this.info = info;
        }

        public Integer code() {
            return code;
        }

        public String info() {
            return info;
        }

        public static UpdateType codeOf(Integer code) {
            switch (code) {
                case -1:
                    return UpdateType.UPDATETYPE_NO;
                case 0:
                    return UpdateType.UPDATETYPE_FORCE_UPDATE;
                case 1:
                    return UpdateType.UPDATETYPE_GENERAL_UPDATE;
                case 2:
                    return UpdateType.UPDATETYPE_SILENT_UPDATE;
                case 3:
                    return UpdateType.UPDATETYPE_NEGLIGIBLE_UPDATES;
                case 4:
                    return UpdateType.UPDATETYPE_SILENT_IGNORE_UPDATES;
                default:
                      return UpdateType.UPDATETYPE_NO;
            }
        }
    }

}
