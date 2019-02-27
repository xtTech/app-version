package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.dto.ServiceResult;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class IosVersionServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(IosVersionServiceTest.class);

    @Autowired
    IosVersionService iosVersionService;

    @Test
    public void findNewestVersion() throws Exception {
        String tenantAppId = "app";
        String version = "0.0.0.1";
        String platform = "ios";
        if ("ios".equalsIgnoreCase(platform)) {
            ServiceResult serviceResult = iosVersionService.findNewestVersion(tenantAppId, version);
            if (serviceResult.getData() != null) {
                logger.info(serviceResult.getData().toString());
            }
        }

        version = "0.0.0.1.1111";
        ServiceResult serviceResult = iosVersionService.findNewestVersion(tenantAppId, version);
        if (serviceResult.getData() != null) {
            logger.info(serviceResult.getData().toString());
        }

        tenantAppId = "appdsfsddd";
        serviceResult = iosVersionService.findNewestVersion(tenantAppId, version);
        if (serviceResult.getData() != null) {
            logger.info(serviceResult.getData().toString());
        }
    }

}