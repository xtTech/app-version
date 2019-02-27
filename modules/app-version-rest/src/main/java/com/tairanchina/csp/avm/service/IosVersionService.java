package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.dto.ServiceResult;

/**
 * Created by hzlizx on 2018/6/21 0021
 */
public interface IosVersionService {

    /**
     * 找到当前最新的版本
     * @param tenantAppId   应用ID
     * @param version       当前APP版本
     * @return              版本信息
     */
    ServiceResult findNewestVersion(String tenantAppId, String version);

}
