package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.Apk;

/**
 * Created by hzlizx on 2018/6/14 0014
 */
public interface ApkService {

    ServiceResult create(Apk apk);

    ServiceResult list(int page, int pageSize, EntityWrapper<Apk> wrapper, int versionId);

    ServiceResult delivery(int apkId);

    ServiceResult undelivery(int apkId);

    ServiceResult delete(int apkId);

    /**
     * 查询某个版本某个渠道码是否已经存在APK包
     *
     * @param channelCode
     * @param versionId
     * @return
     */
    ServiceResult exists(String channelCode, int versionId);

    /**
     * 分页查询apk,查询条件有channelCode
     */
    ServiceResult getApkPageWithChannelCode(int page, int pageSize, Integer versionId, String channelCode, String md5, Integer deliveryStatus);
}
