package com.tairanchina.csp.avm.controller;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomApiControllerTest extends BaseControllerTest {

    private static Logger logger = LoggerFactory.getLogger(CustomApiControllerTest.class);

    private static String uri = "/c/";

    @Test
    public void hello() throws Exception {
        String tenantAppId = "app";
        String key = "test";
        String platform = "ios";
        String version = "1.0.0.0.0";
        String url = uri + tenantAppId + "/" + key + "/" + version + "/" + platform;
        String contentAsString = get(url);
        logger.info(contentAsString);

        tenantAppId = "app";
        key = " ";
        platform = "ios";
        version = "1.0.0.0.0";
        url = uri + tenantAppId + "/" + key + "/" + version + "/" + platform;
        contentAsString = get(url);
        logger.info(contentAsString);

        tenantAppId = "app";
        key = "test";
        platform = "fdfsdf";
        version = "1.0.0.0.0";
        url = uri + tenantAppId + "/" + key + "/" + version + "/" + platform;
        contentAsString = get(url);
        logger.info(contentAsString);

        tenantAppId = "app";
        key = "test";
        platform = "android";
        version = "1.0.0.0";
        url = uri + tenantAppId + "/" + key + "/" + version + "/" + platform;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

}