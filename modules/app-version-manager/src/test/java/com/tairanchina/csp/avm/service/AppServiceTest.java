package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class AppServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(AppServiceTest.class);

    @Autowired
    private AppService appService;

    @Test
    public void getAppListWithUserId() throws Exception {
        ServiceResult result = appService.getAppListWithUserId(1, 10, ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
        if (result.getData() != null) {
            if (result.getData() != null) {
                logger.info(result.getData().toString());
            }
        }
    }

    @Test
    public void getMyApp() throws Exception {
        ServiceResult result = appService.getMyApp();
        if (result.getData() != null) {
            if (result.getData() != null) {
                logger.info(result.getData().toString());
            }
        }
    }

}