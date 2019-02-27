package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.entity.App;

/**
 * Created by hzlizx on 2018/6/25 0025
 */
public interface AppService {

    /**
     * 根据应用ID获取应用
     * @param tenantAppId   应用ID
     * @return              应用
     */
    App findAppByTenantAppId(String tenantAppId);
}
