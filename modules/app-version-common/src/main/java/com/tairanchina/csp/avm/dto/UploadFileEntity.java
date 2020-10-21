package com.tairanchina.csp.avm.dto;

/**
 * Created by hzlizx on 2018/6/14 0014
 */
public class UploadFileEntity {

    private String fileName;
    private String channel;
    private Integer versionId;

    private String apkName;
    private String ossUrl;
    private String downloadUrl;
    private String downloadApkUrl;
    private String downloadFileName;

    private String md5;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getOssUrl() {
        return ossUrl;
    }

    public void setOssUrl(String ossUrl) {
        this.ossUrl = ossUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadApkUrl() {
        return downloadApkUrl;
    }

    public void setDownloadApkUrl(String downloadApkUrl) {
        this.downloadApkUrl = downloadApkUrl;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
