package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.IosVersion;
import com.tairanchina.csp.avm.wapper.ExtWrapper;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


public class IosVersionServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(IosVersionServiceTest.class);

    @Autowired
    private IosVersionService iosVersionService;

    @Test
    public void create() throws Exception {

        IosVersion iosVersion = new IosVersion();
        iosVersion.setAppId(24);
        iosVersion.setGrayReleased(1);
        iosVersion.setAllowLowestVersion("1.0.0.0");
        iosVersion.setAppVersion("1.0.0.0");
        iosVersion.setAppStoreUrl("url");

        String appVersion = iosVersion.getAppVersion();
        String allowLowestVersion = iosVersion.getAllowLowestVersion();

        if (allowLowestVersion.compareTo(appVersion) > 0) {
            logger.info("ALLOWLOWESTVERSION_BIG_THAN_APPVERSION");
        } else {
            ServiceResult result = iosVersionService.create(iosVersion);
            if (result.getData() != null) {
                logger.info(result.getData().toString());
            }
        }
    }

    @Test
    public void update() throws Exception {
        IosVersion iosVersion = new IosVersion();
        iosVersion.setId(20);
        iosVersion.setAppId(24);
        iosVersion.setGrayReleased(1);
        iosVersion.setAllowLowestVersion("2.0.0.0");
        iosVersion.setAppVersion("1.0.0.0");
        iosVersion.setAppStoreUrl("updateurl");

        String appVersion = iosVersion.getAppVersion();
        String allowLowestVersion = iosVersion.getAllowLowestVersion();

        if (allowLowestVersion.compareTo(appVersion) > 0) {
            logger.info("ALLOWLOWESTVERSION_BIG_THAN_APPVERSION");
        } else {
            ServiceResult result = iosVersionService.update(iosVersion);
            if (result.getData() != null) {
                logger.info(result.getData().toString());
            }
        }
    }

    @Test
    public void delete() throws Exception {
        Integer id = 20;
        ServiceResult result = iosVersionService.delete(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void list() throws Exception {
        String appVersion = "2.0.0.0";
        Integer updateType = 1;
        Integer versionStatus = 1;
        ExtWrapper<IosVersion> wrapper = new ExtWrapper<>();
        wrapper.and().eq("app_id", ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        wrapper.and().eq("del_flag", 0);
        wrapper.setVersionSort("app_version", false);
        if (StringUtils.hasLength(appVersion)) {
            wrapper.andNew().like("app_version", "%" + appVersion + "%");
        }
        if (updateType != null) {
            wrapper.and().eq("update_type", updateType);
        }
        if (versionStatus != null) {
            wrapper.and().eq("version_status", versionStatus);
        }
        ServiceResult result = iosVersionService.list(1, 10, wrapper);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void listAllVersion() throws Exception {
        ServiceResult result = iosVersionService.listAllVersion();
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void delivery() throws Exception {
        Integer id = 20;
        ServiceResult result = iosVersionService.delivery(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void undelivery() throws Exception {
        Integer id = 20;
        ServiceResult result = iosVersionService.undelivery(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void get() throws Exception {
        Integer id = 20;
        ServiceResult result = iosVersionService.get(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

}