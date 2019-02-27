package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.dto.ServiceResult;

/**
 * Created by hzlizx on 2018/6/28 0028
 */
public interface CustomApiService {

    ServiceResult getCustomContent(String tenantAppId, String key, String platfrom, String version);

}
