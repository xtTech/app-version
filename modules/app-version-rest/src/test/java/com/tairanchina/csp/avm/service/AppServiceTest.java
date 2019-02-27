package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.entity.App;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class AppServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(AppServiceTest.class);

    @Autowired
    AppService appService;

    @Test
    public void findAppByTenantAppId() throws Exception {
        String tenantAppId = "app";
        App app = appService.findAppByTenantAppId(tenantAppId);
        logger.info(app.getAppName());
    }

}