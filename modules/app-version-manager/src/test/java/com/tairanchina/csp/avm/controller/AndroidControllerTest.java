package com.tairanchina.csp.avm.controller;

import com.tairanchina.csp.avm.entity.AndroidVersion;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AndroidControllerTest extends BaseControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(AndroidControllerTest.class);

    private String uri = "/android";

    private String url = "";

    @Test
    public void list() throws Exception {
        String appVersion = "";
        Integer updateType = 1;
        Integer versionStatus = 1;
        url = uri + "?appVersion=" + appVersion + "&updateType=" + updateType + "&versionStatus=" + versionStatus;
        String contentAsString = get(url);
        logger.info(contentAsString);

        //ppVersion = "1.0.0.0";
        updateType = 1;
        versionStatus = 1;
        url = uri + "?appVersion=" + appVersion + "&updateType=" + updateType + "&versionStatus=" + versionStatus;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

    @Test
    public void create() throws Exception {
        AndroidVersion androidVersion = new AndroidVersion();
        androidVersion.setId(null); //使用数据库自增ID
        String contentAsString = post(uri, androidVersion);
        logger.info(contentAsString);

        androidVersion.setAllowLowestVersion("1.0.0.3");
        androidVersion.setAppVersion("2.0.0.2");
        androidVersion.setVersionDescription("desc");
        androidVersion.setId(null); //使用数据库自增ID
        contentAsString = post(uri, androidVersion);
        logger.info(contentAsString);

        androidVersion.setUpdateType(1);
        contentAsString = post(uri, androidVersion);
        logger.info(contentAsString);

        androidVersion.setAllowLowestVersion("2.0.0.3");
        androidVersion.setAppVersion("2.0.0.2");
        contentAsString = post(uri, androidVersion);
        logger.info(contentAsString);

        androidVersion.setAllowLowestVersion("2.0.0.321111111111111111111111111111111111111444444444444444444444444444444444444444444444444444444444444444444444444444444444444");
        androidVersion.setAppVersion("2.0.0.4");
        contentAsString = post(uri, androidVersion);
        logger.info(contentAsString);

        androidVersion.setAllowLowestVersion("2.0.0.3");
        androidVersion.setAppVersion("2.0.0.321111111111111111111111111111111111111444444444444444444444444444444444444444444444444444444444444444444444444444444444444");
        contentAsString = post(uri, androidVersion);
        logger.info(contentAsString);
    }

    @Test
    public void update() throws Exception {
        Integer id = 0;
        url = uri + "/" + id;
        AndroidVersion androidVersion = new AndroidVersion();
        String contentAsString = put(url, androidVersion);
        logger.info(contentAsString);

        id = 14;
        url = uri + "/" + id;
        contentAsString = put(url, androidVersion);
        logger.info(contentAsString);

        androidVersion.setUpdateType(1);
        androidVersion.setVersionDescription("desc");
        url = uri + "/" + id;
        contentAsString = put(url, androidVersion);
        logger.info(contentAsString);

        androidVersion.setAllowLowestVersion("3.0.0.3");
        androidVersion.setAppVersion("2.0.0.2");
        androidVersion.setUpdateType(1);
        androidVersion.setVersionDescription("desc");
        url = uri + "/" + id;
        contentAsString = put(url, androidVersion);
        logger.info(contentAsString);

        androidVersion.setAllowLowestVersion("1.0.0.3");
        androidVersion.setAppVersion("2.0.0.2");
        androidVersion.setUpdateType(1);
        androidVersion.setVersionDescription("desc");
        url = uri + "/" + id;
        contentAsString = put(url, androidVersion);
        logger.info(contentAsString);
    }

    @Test
    public void delete() throws Exception {
        Integer id = 14;
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
        Integer id = 14;
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
        Integer id = 14;
        uri = uri + "/" + id + "/delivery";
        String contentAsString = put(uri, null);
        logger.info(contentAsString);
    }

    @Test
    public void undelivery() throws Exception {
        Integer id = 14;
        uri = uri + "/" + id + "/undelivery";
        String contentAsString = put(uri, null);
        logger.info(contentAsString);
    }

}