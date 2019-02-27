package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.dto.ServiceResult;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class CustomApiServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(CustomApiServiceTest.class);

    @Autowired
    CustomApiService customApiService;

    @Test
    public void getCustomContent() throws Exception {
        String tenantAppId = "app";
        String key = "test";
        String platfrom = "ios";
        String version = "1.0.0.0.1";
        ServiceResult result = customApiService.getCustomContent(tenantAppId, key, platfrom, version);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        platfrom = "android";
        result = customApiService.getCustomContent(tenantAppId, key, platfrom, version);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

}