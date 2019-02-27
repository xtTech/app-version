package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.CustomApi;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import static org.junit.Assert.assertTrue;


public class CustomApiServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(CustomApiServiceTest.class);

    @Autowired
    BasicService basicService;

    @Autowired
    CustomApiService customApiService;

    @Test
    public void createCustomApi() throws Exception {
        CustomApi customApi = new CustomApi();
        customApi.setCustomName("test");
        customApi.setAndroidEnabled(1);
        customApi.setAndroidMin("2.0.0");
        customApi.setAndroidMax("2.0.0");
        customApi.setCustomContent("content");
        customApi.setCustomKey("key");

        ServiceResult serviceResult = basicService.checkVersion(customApi);
        if (serviceResult.getCode() != 200) {
            logger.info("code is not 200..............");
        } else {
            ServiceResult result = customApiService.createCustomApi(customApi);
            if (result.getData() != null) {
                CustomApi a = (CustomApi) result.getData();
                logger.info("createCustomApi:" + a.getCustomKey());
                assertTrue(a != null);
            }
        }
    }


    @Test
    public void updateCustomApi() throws Exception {
        CustomApi customApi = new CustomApi();
        customApi.setId(2);
        customApi.setCustomName("ipdatetest");
        customApi.setAndroidEnabled(1);
        customApi.setAndroidMin("1.0.0");
        customApi.setAndroidMax("2.0.0");
        customApi.setCustomContent("content");
        customApi.setCustomKey("key");
        ServiceResult serviceResult = basicService.checkVersion(customApi);
        if (serviceResult.getCode() != 200) {
            logger.info("code is not 200..............");
        } else {
            ServiceResult result = customApiService.updateCustomApi(customApi);
            if (result.getData() != null) {
                CustomApi a = (CustomApi) result.getData();
                logger.info("updateCustomApi:" + a.getCustomName());
                assertTrue(a != null);
            }
        }
    }

    @Test
    public void deleteCustomApi() throws Exception {
        ServiceResult result = customApiService.deleteCustomApi(2);
        if (result.getData() != null) {
            String a = (String) result.getData();
            logger.info("软删" + a);
        }

        result = customApiService.deleteCustomApi(47);
        if (result.getData() != null) {
            String a = (String) result.getData();
            logger.info("软删" + a);
        }
    }

    @Test
    public void deleteCustomApiForver() throws Exception {
        ServiceResult result = customApiService.deleteCustomApiForver(2);
        if (result.getData() != null) {
            Integer a = (Integer) result.getData();
            logger.info("硬删" + a);
        }

        result = customApiService.deleteCustomApiForver(47);
        if (result.getData() != null) {
            Integer a = (Integer) result.getData();
            logger.info("硬删" + a);
        }
    }

    @Test
    public void getCustomApiByOne() throws Exception {
        CustomApi customApi = new CustomApi();
        customApi.setId(16);
        customApi.setDelFlag(0);
        ServiceResult result = customApiService.getCustomApiByOne(customApi);
        if (result.getData() != null) {
            CustomApi a = (CustomApi) result.getData();
            logger.info("getCustomApiById:" + a.getCustomName());
        }

        customApi.setId(47);
        result = customApiService.getCustomApiByOne(customApi);
        if (result.getData() != null) {
            CustomApi a = (CustomApi) result.getData();
            logger.info("getCustomApiById:" + a.getCustomName());
        }
    }

    @Test
    public void list() throws Exception {
        EntityWrapper<CustomApi> wrapper = new EntityWrapper<>();
        String type = "ios";
//        type = "android";
        if ("android".equals(type)) {
            wrapper.andNew().eq("android_enabled", 1);
        } else if ("ios".equals(type)) {
            wrapper.andNew().eq("ios_enabled", 1);
        }
        ServiceResult result = customApiService.list(1, 10, wrapper);

    }

    @Test
    public void getCustomApiByKeyAndAppId() throws Exception {
        String customKey = "key";
        ServiceResult result = customApiService.getCustomApiByKeyAndAppId(customKey);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        customKey = "test21";
        result = customApiService.getCustomApiByKeyAndAppId(customKey);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

}