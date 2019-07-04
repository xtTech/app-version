package com.tairanchina.csp.avm.service.impl;

import com.ecfront.dew.common.$;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.App;
import com.tairanchina.csp.avm.entity.IosVersion;
import com.tairanchina.csp.avm.mapper.IosVersionMapper;
import com.tairanchina.csp.avm.service.AppService;
import com.tairanchina.csp.avm.service.IosVersionService;
import com.tairanchina.csp.avm.utils.VersionCompareUtils;
import com.tairanchina.csp.avm.wapper.ExtWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hzlizx on 2018/6/21 0021
 */
@Service
public class IosVersionServiceImpl implements IosVersionService {
    private static final Logger logger = LoggerFactory.getLogger(IosVersionServiceImpl.class);

    @Autowired
    private IosVersionMapper iosVersionMapper;

    @Autowired
    private AppService appService;

    @Override
    public ServiceResult findNewestVersion(String tenantAppId, String version) {
        logger.debug("查询tenantAppId为{}的应用...", tenantAppId);
        App appSelected = appService.findAppByTenantAppId(tenantAppId);
        if (appSelected == null) {
            return ServiceResultConstants.APP_NOT_EXISTS;
        }
        logger.debug("找到应用:{}", appSelected.getAppName());

        ExtWrapper<IosVersion> iosVersionEntityWrapper = new ExtWrapper<>();
        iosVersionEntityWrapper.and().eq("app_id", appSelected.getId());
        iosVersionEntityWrapper.and().eq("del_flag", 0);
        iosVersionEntityWrapper.and().eq("version_status", 1);
        iosVersionEntityWrapper.setVersionSort("app_version", false);
        List<IosVersion> iosVersions = iosVersionMapper.selectList(iosVersionEntityWrapper);
        if (iosVersions.isEmpty()) {
            logger.debug("查询不到新版本，当前版本为最新");
            return ServiceResultConstants.NO_NEW_VERSION;
        }
        // 将查询结果再次进行筛选，选出大于传入version的版本，然后再找出最新版本
        List<IosVersion> iosVersionsResult = new LinkedList<>();
        iosVersionsResult.addAll(iosVersions);
        iosVersions.forEach(iosVersion -> logger.debug(iosVersion.getAppVersion()));
        if (VersionCompareUtils.compareVersion(version, iosVersions.get(iosVersions.size() - 1).getAppVersion()) > 0) {
            Collections.reverse(iosVersions);
            for (IosVersion i : iosVersions) {
                String iosVersionMin = i.getAppVersion();
                if (VersionCompareUtils.compareVersion(version, iosVersionMin) >= 0) {
                    iosVersionsResult.remove(i);
                }
            }
        }
        if (iosVersionsResult.isEmpty()) {
            logger.debug("查询不到新版本或者新版本未上架，当前版本为最新");
            return ServiceResultConstants.NO_NEW_VERSION;
        }
        logger.debug("查询到的版本：");
        iosVersionsResult.forEach(iosVersion -> logger.debug(iosVersion.getAppVersion()));

        IosVersion iosVersion = iosVersionsResult.get(0);
        logger.debug("当前最新版本为：{}", iosVersion.getAppVersion());

        logger.debug("找到新版本，开始组装返回值...");
        HashMap<String, Object> map = new HashMap<>();
        map.put("allowLowestVersion", iosVersion.getAllowLowestVersion());
        map.put("appStoreUrl", iosVersion.getAppStoreUrl());
        map.put("description", iosVersion.getVersionDescription());
        map.put("forceUpdate", iosVersion.getUpdateType());
        map.put("version", iosVersion.getAppVersion());
        ServiceResult ok = ServiceResult.ok(map);
        logger.debug("结果：{}", $.json.toJsonString(ok));
        return ok;
    }
}
