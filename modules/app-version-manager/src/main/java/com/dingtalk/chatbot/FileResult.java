
package com.dingtalk.chatbot;

/**
 * 文件结果集
 */
public class FileResult {

    String fileName;
    String filePath;
    Long fileSize;
    String requestUrl; //下载地址(适用于网页的文件流下载)
    String requestApkUrl; //Apk文件直接下载地址

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestApkUrl() {
        return requestApkUrl;
    }

    public void setRequestApkUrl(String requestApkUrl) {
        this.requestApkUrl = requestApkUrl;
    }
}
