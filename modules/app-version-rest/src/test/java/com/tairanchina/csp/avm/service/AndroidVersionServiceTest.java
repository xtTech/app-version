package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.dto.ServiceResult;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;


public class AndroidVersionServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(AndroidVersionServiceTest.class);

    @Value("${rest.base.url}")
    private String baseUrl;

    @Autowired
    private AndroidVersionService androidVersionService;


    @Test
    public void findNewestVersion() throws Exception {
        String tenantAppId = "app";
        String version = "0.0.0.1";
        String channelCode = "qudaoncode";
        String platform = "android";
        if ("android".equalsIgnoreCase(platform)) {
            ServiceResult serviceResult = androidVersionService.findNewestVersion(tenantAppId, version, channelCode);
            if (serviceResult.getCode() == 200) {
                HashMap<String, Object> data = (HashMap<String, Object>) serviceResult.getData();
                String downloadUrl = (String) data.get("downloadUrl");
                downloadUrl = baseUrl + downloadUrl;
                data.put("downloadUrl", downloadUrl);
            }
            if (serviceResult.getData() != null) {
                logger.info(serviceResult.getData().toString());
            }
        }

        channelCode = "aaaa2";
        ServiceResult serviceResult = androidVersionService.findNewestVersion(tenantAppId, version, channelCode);
        if (serviceResult.getData() != null) {
            logger.info(serviceResult.getData().toString());
        }

        channelCode = "aaaa3";
        serviceResult = androidVersionService.findNewestVersion(tenantAppId, version, channelCode);
        if (serviceResult.getData() != null) {
            logger.info(serviceResult.getData().toString());
        }

        channelCode = "";
        serviceResult = androidVersionService.findNewestVersion(tenantAppId, version, channelCode);
        if (serviceResult.getData() != null) {
            logger.info(serviceResult.getData().toString());
        }

        channelCode = "qudaotest";
        serviceResult = androidVersionService.findNewestVersion(tenantAppId, version, channelCode);
        if (serviceResult.getData() != null) {
            logger.info(serviceResult.getData().toString());
        }
    }

    @Test
    public void getDownloadUrl() throws Exception {
        Integer apkId = 38;
        ServiceResult serviceResult = androidVersionService.getDownloadUrl(apkId);
        if (serviceResult.getData() != null) {
            logger.info(serviceResult.getData().toString());
        }

        apkId = 9999999;
        serviceResult = androidVersionService.getDownloadUrl(apkId);
        if (serviceResult.getData() != null) {
            logger.info(serviceResult.getData().toString());
        }
    }

}