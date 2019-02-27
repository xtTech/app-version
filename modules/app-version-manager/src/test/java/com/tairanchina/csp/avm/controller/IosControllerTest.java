package com.tairanchina.csp.avm.controller;

import com.tairanchina.csp.avm.entity.IosVersion;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IosControllerTest extends BaseControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(IosControllerTest.class);

    private String uri = "/ios";

    private String url = "";

    @Test
    public void list() throws Exception {
        String appVersion = "";
        Integer updateType = 1;
        Integer versionStatus = 1;
        url = uri + "?appVersion=" + appVersion + "&updateType=" + updateType + "&versionStatus=" + versionStatus;
        String contentAsString = get(url);
        logger.info(contentAsString);

        appVersion = "1.0.0.0.3";
        updateType = 1;
        versionStatus = 1;
        url = uri + "?appVersion=" + appVersion + "&updateType=" + updateType + "&versionStatus=" + versionStatus;
        contentAsString = get(url);
        logger.info(contentAsString);

    }

    @Test
    public void create() throws Exception {
        IosVersion iosVersion = new IosVersion();
        iosVersion.setAppId(24);
        String contentAsString = post(uri, iosVersion);
        logger.info(contentAsString);

        iosVersion.setAppId(24);
        iosVersion.setGrayReleased(1);
        iosVersion.setAllowLowestVersion("1.0.0.0");
        iosVersion.setAppVersion("1.0.0.2");
        iosVersion.setAppStoreUrl("url");
        iosVersion.setVersionDescription("desc");
        iosVersion.setUpdateType(null);
        contentAsString = post(uri, iosVersion);
        logger.info(contentAsString);

        iosVersion.setUpdateType(1);
        contentAsString = post(uri, iosVersion);
        logger.info(contentAsString);

        iosVersion.setAllowLowestVersion("2.0.0.0");
        iosVersion.setAppVersion("1.0.0.2");
        contentAsString = post(uri, iosVersion);
        logger.info(contentAsString);

        iosVersion.setAllowLowestVersion("2.0.0.02dsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        iosVersion.setAppVersion("3.0.0.2dsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        contentAsString = post(uri, iosVersion);
        logger.info(contentAsString);

        iosVersion.setAllowLowestVersion("2.0.0.02dsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        iosVersion.setAppVersion("3.0.0.2");
        contentAsString = post(uri, iosVersion);
        logger.info(contentAsString);

    }

    @Test
    public void update() throws Exception {
        Integer id = 0;
        url = uri + "/" + id;
        IosVersion iosVersion = new IosVersion();
        String contentAsString = put(url, iosVersion);
        logger.info(contentAsString);

        id = 20;
        url = uri + "/" + id;
        iosVersion.setAppId(24);
        iosVersion.setGrayReleased(1);
        iosVersion.setAppStoreUrl("url");
        contentAsString = put(url, iosVersion);
        logger.info(contentAsString);

        iosVersion.setAllowLowestVersion("2.0.0.0");
        iosVersion.setAppVersion("1.0.0.0");
        contentAsString = put(url, iosVersion);
        logger.info(contentAsString);

        iosVersion.setAllowLowestVersion("2.0.0.0");
        iosVersion.setAppVersion("3.0.0.0");
        contentAsString = put(url, iosVersion);
        logger.info(contentAsString);
    }

    @Test
    public void delete() throws Exception {
        Integer id = 20;
        url = uri + "/" + id;
        String contentAsString = delete(url);
        logger.info(contentAsString);

        id = 0;
        url = uri + "/" + id;
        contentAsString = delete(url);
        logger.info(contentAsString);
    }

    @Test
    public void versions() throws Exception {
        uri = uri + "/versions";
        String contentAsString = get(uri);
        logger.info(contentAsString);
    }

    @Test
    public void get() throws Exception {
        Integer id = 20;
        url = uri + "/" + id;
        String contentAsString = get(url);
        logger.info(contentAsString);

        id = 0;
        url = uri + "/" + id;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

    @Test
    public void delivery() throws Exception {
        Integer id = 20;
        uri = uri + "/" + id + "/delivery";
        String contentAsString = put(uri, null);
        logger.info(contentAsString);
    }

    @Test
    public void undelivery() throws Exception {
        Integer id = 20;
        uri = uri + "/" + id + "/undelivery";
        String contentAsString = put(uri, null);
        logger.info(contentAsString);
    }

}