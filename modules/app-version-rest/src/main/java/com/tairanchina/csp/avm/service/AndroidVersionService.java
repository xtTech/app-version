package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.dto.ServiceResult;

/**
 * Created by hzlizx on 2018/6/21 0021
 */
public interface AndroidVersionService {

    /**
     * 找到当前最新的版本
     *
     * @param tenantAppId 应用ID
     * @param version     当前APP版本
     * @param channelCode 渠道码
     * @return 版本信息
     */
    ServiceResult findNewestVersion(String tenantAppId, String version, String channelCode);

    /**
     * 获取下载地址
     *
     * @param apkId apk的ID
     * @return Apk实体
     */
    ServiceResult getDownloadUrl(int apkId);

    /**
     * 获取最新APK包
     *
     * @param tenantAppId
     * @param channelCode
     * @return
     */
    ServiceResult findNewestApk(String tenantAppId, String channelCode);
}
