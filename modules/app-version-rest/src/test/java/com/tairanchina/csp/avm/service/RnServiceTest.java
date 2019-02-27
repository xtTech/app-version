package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.dto.ServiceResult;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class RnServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(RnServiceTest.class);

    @Autowired
    RnService rnService;

    @Test
    public void route() throws Exception {
        String version = "1.0.0.0.3";
        String tenantAppId = "app";
        String platform = "ios";
        Integer status = 1;
        ServiceResult result = rnService.route(version, tenantAppId, platform, status);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        platform = "android";
        result = rnService.route(version, tenantAppId, platform, status);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
        platform = "2323";
        result = rnService.route(version, tenantAppId, platform, status);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void bundles() throws Exception {
        String version = "1.0.0.0.2";
        String tenantAppId = "app";
        String platform = "ios";
        Integer status = 1;
        ServiceResult result = rnService.bundles(version, tenantAppId, platform, status);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        tenantAppId = "ghgfhfgdhg";
        result = rnService.bundles(version, tenantAppId, platform, status);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        platform = "android";
        result = rnService.bundles(version, tenantAppId, platform, status);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

}