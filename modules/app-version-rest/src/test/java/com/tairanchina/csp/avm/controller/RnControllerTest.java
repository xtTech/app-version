package com.tairanchina.csp.avm.controller;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RnControllerTest extends BaseControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(RnControllerTest.class);

    @Test
    public void route() throws Exception {

        String version = "";
        String tenantAppId = "";
        String platform = "";
        Integer status = 1;
        String url = "/route/" + version + "-" + tenantAppId + "-" + platform + "-" + status;
        String contentAsString = get(url);
        logger.info(contentAsString);

        status = 0;
        url = "/route/" + version + "-" + tenantAppId + "-" + platform + "-" + status;
        contentAsString = get(url);
        logger.info(contentAsString);

        version = "1.0.0.0.3";
        tenantAppId = "app";
        platform = "ios";
        status = 1;
        url = "/route/" + version + "-" + tenantAppId + "-" + platform + "-" + status;
        contentAsString = get(url);
        logger.info(contentAsString);

        status = 2;
        url = "/route/" + version + "-" + tenantAppId + "-" + platform + "-" + status;
        contentAsString = get(url);
        logger.info(contentAsString);

        status = 0;
        url = "/route/" + version + "-" + tenantAppId + "-" + platform + "-" + status;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

    @Test
    public void bundles() throws Exception {

        String version = "";
        String tenantAppId = "";
        String platform = "";
        Integer status = 1;
        String url = "/bundles/" + version + "-" + tenantAppId + "-" + platform + "-" + status;
        String contentAsString = get(url);
        logger.info(contentAsString);

        status = 0;
        url = "/bundles/" + version + "-" + tenantAppId + "-" + platform + "-" + status;
        contentAsString = get(url);
        logger.info(contentAsString);

        version = "1.0.0.0";
        tenantAppId = "app";
        platform = "ios";
        status = 1;
        url = "/bundles/" + version + "-" + tenantAppId + "-" + platform + "-" + status;
        contentAsString = get(url);
        logger.info(contentAsString);

        status = 2;
        url = "/bundles/" + version + "-" + tenantAppId + "-" + platform + "-" + status;
        contentAsString = get(url);
        logger.info(contentAsString);

        status = 0;
        url = "/bundles/" + version + "-" + tenantAppId + "-" + platform + "-" + status;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

}