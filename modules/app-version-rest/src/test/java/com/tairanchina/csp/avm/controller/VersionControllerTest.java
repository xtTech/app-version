package com.tairanchina.csp.avm.controller;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class VersionControllerTest extends BaseControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(VersionControllerTest.class);

    @Test
    public void version() throws Exception {
        String tenantAppId = "app";
        String version = "1.0.0.0";
        String channelCode = "qudao";
        String platform = "ios";
        String url = "/v/" + tenantAppId + "-" + version + "-" + channelCode + "-" + platform;
        String contentAsString = get(url);
        logger.info(contentAsString);

        tenantAppId = "app";
        version = "1.0.0.0";
        channelCode = "qudao";
        platform = "android";
        url = "/v/" + tenantAppId + "-" + version + "-" + channelCode + "-" + platform;
        contentAsString = get(url);
        logger.info(contentAsString);

        tenantAppId = "app";
        version = "0.0.0.1";
        channelCode = "official";
        platform = "android";
        url = "/v/" + tenantAppId + "-" + version + "-" + channelCode + "-" + platform;
        contentAsString = get(url);
        logger.info(contentAsString);

        platform = "FFDFSFSFDS";
        url = "/v/" + tenantAppId + "-" + version + "-" + channelCode + "-" + platform;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

    @Test
    public void version1() throws Exception {
        int apkId = 0;
        String url = "/v/download/" + apkId;
        String contentAsString = get(url);
        logger.info(contentAsString);

        apkId = 39;
        url = "/v/download/" + apkId;
        contentAsString = get(url);
        logger.info(contentAsString);

        apkId = 999999;
        url = "/v/download/" + apkId;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

}