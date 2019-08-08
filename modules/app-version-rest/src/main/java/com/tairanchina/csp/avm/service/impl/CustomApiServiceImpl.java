package com.tairanchina.csp.avm.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ecfront.dew.common.$;
import com.fasterxml.jackson.databind.JsonNode;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.App;
import com.tairanchina.csp.avm.entity.CustomApi;
import com.tairanchina.csp.avm.mapper.CustomApiMapper;
import com.tairanchina.csp.avm.service.AppService;
import com.tairanchina.csp.avm.service.CustomApiService;
import com.tairanchina.csp.avm.utils.VersionCompareUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by hzlizx on 2018/6/28 0028
 */
@Service
public class CustomApiServiceImpl implements CustomApiService {
    private static final Logger logger = LoggerFactory.getLogger(CustomApiServiceImpl.class);

    @Autowired
    private CustomApiMapper customApiMapper;

    @Autowired
    private AppService appService;

    @Override
    public ServiceResult getCustomContent(String tenantAppId, String key, String platform, String version) {
        App app = appService.findAppByTenantAppId(tenantAppId);
        if (app == null) {
            return ServiceResultConstants.APP_NOT_EXISTS;
        }
        CustomApi customApi = new CustomApi();
        EntityWrapper<CustomApi> wrapper = new EntityWrapper<>();
        customApi.setAppId(app.getId());
        customApi.setCustomKey(key);
        customApi.setCustomStatus(1);
        customApi.setDelFlag(0);
        if ("ios".equalsIgnoreCase(platform)) {
            customApi.setIosEnabled(1);
        } else if ("android".equalsIgnoreCase(platform)) {
            customApi.setAndroidEnabled(1);
        }
        wrapper.setEntity(customApi);
        wrapper.orderBy("created_time", false);
        List<CustomApi> customApis = customApiMapper.selectList(wrapper);
        if (customApis.isEmpty()) {
            return ServiceResultConstants.CUSTOM_API_NOT_FOUND;
        }

        CustomApi customApiNew = null;
        if ("ios".equalsIgnoreCase(platform)) {
            for (CustomApi c : customApis) {
                if (VersionCompareUtils.compareVersion(c.getIosMax(), version) > 0 && VersionCompareUtils.compareVersion(version, c.getIosMin()) >= 0) {
                    customApiNew = c;
                    break;
                }
            }
        } else if ("android".equalsIgnoreCase(platform)) {
            for (CustomApi c : customApis) {
                if (VersionCompareUtils.compareVersion(c.getAndroidMax(), version) > 0 && VersionCompareUtils.compareVersion(version, c.getAndroidMin()) >= 0) {
                    customApiNew = c;
                    break;
                }
            }
        }
        if (null == customApiNew) {
            return ServiceResultConstants.CUSTOM_API_NOT_FOUND;
        }

        String customContent = customApiNew.getCustomContent();
        try {
            JsonNode jsonNode = $.json.getMapper().readTree(customContent);
            return ServiceResult.ok(jsonNode);
        } catch (IOException e) {
            logger.error("渲染JSON出错", e);
            return ServiceResultConstants.ERROR_500;
        }
    }
}
