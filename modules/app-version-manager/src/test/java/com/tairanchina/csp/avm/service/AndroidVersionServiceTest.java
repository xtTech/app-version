package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.AndroidVersion;
import com.tairanchina.csp.avm.utils.StringUtilsExt;
import com.tairanchina.csp.avm.wapper.ExtWrapper;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


public class AndroidVersionServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(AndroidVersionServiceTest.class);

    @Autowired
    AndroidVersionService androidVersionService;


    @Test
    public void createAndroidVersion() throws Exception {

        AndroidVersion androidVersion = new AndroidVersion();
        androidVersion.setAllowLowestVersion("1.0.0.0");
        androidVersion.setAppVersion("version");
        androidVersion.setUpdateType(1);
        androidVersion.setVersionDescription("desc");

        androidVersion.setId(null); //使用数据库自增ID
        String appVersion = androidVersion.getAppVersion();
        String allowLowestVersion = androidVersion.getAllowLowestVersion();
        String versionDescription = androidVersion.getVersionDescription();
        Integer updateType = androidVersion.getUpdateType();
        if (StringUtilsExt.hasEmpty(appVersion, allowLowestVersion, versionDescription) || updateType == null) {
            logger.info("NEED_PARAMS");
        }
        if (allowLowestVersion.compareTo(appVersion) > 0) {
            logger.info("ALLOWLOWESTVERSION_BIG_THAN_APPVERSION");
        }

        if (appVersion.length() > 32) {
            logger.info("VERSION_TOO_LONG");
        }
        if (versionDescription.length() > 32) {
            logger.info("VERSION_TOO_LONG");
        }
        ServiceResult result = androidVersionService.createAndroidVersion(androidVersion);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

    }

    @Test
    public void deleteAndroidVersion() throws Exception {
        Integer id = 14;
        ServiceResult result = androidVersionService.deleteAndroidVersion(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void deleteAndroidVersionForever() throws Exception {
        Integer id = 14;
        ServiceResult result = androidVersionService.deleteAndroidVersionForever(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void updateAndroidVersion() throws Exception {
        AndroidVersion androidVersion = new AndroidVersion();
        androidVersion.setId(14);
        androidVersion.setAllowLowestVersion("1.0.0.0");
        androidVersion.setAppVersion("1.0.0.0");
        androidVersion.setUpdateType(1);
        androidVersion.setVersionDescription("updatedesc");
        String appVersion = androidVersion.getAppVersion();
        String allowLowestVersion = androidVersion.getAllowLowestVersion();
        if (!StringUtilsExt.hasEmpty(appVersion, allowLowestVersion) && allowLowestVersion.compareTo(appVersion) > 0) {
            logger.info("ALLOWLOWESTVERSION_BIG_THAN_APPVERSION");
        }
        ServiceResult result = androidVersionService.updateAndroidVersion(androidVersion);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void list() throws Exception {
        String appVersion = "";
        Integer updateType = null;
        Integer versionStatus = null;
        ExtWrapper<AndroidVersion> wrapper = new ExtWrapper<>();
        wrapper.and().eq("app_id", ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        wrapper.and().eq("del_flag", 0);
        wrapper.setVersionSort("app_version", false);
        if (StringUtils.hasLength(appVersion)) {
            wrapper.and().like("app_version", "%" + appVersion + "%");
        }
        if (updateType != null) {
            wrapper.and().eq("update_type", updateType);
        }
        if (versionStatus != null) {
            wrapper.and().eq("version_status", versionStatus);
        }
        ServiceResult result = androidVersionService.list(1, 1, wrapper);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void findById() throws Exception {
        Integer id = 14;
        ServiceResult result = androidVersionService.findById(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void listAllVersion() throws Exception {
        ServiceResult result = androidVersionService.listAllVersion();
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void delivery() throws Exception {
        Integer id = 14;
        ServiceResult result = androidVersionService.delivery(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void undelivery() throws Exception {
        Integer id = 14;
        ServiceResult result = androidVersionService.undelivery(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 44444;
        result = androidVersionService.undelivery(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 8;
        result = androidVersionService.undelivery(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

}