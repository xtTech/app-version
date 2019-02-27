package com.tairanchina.csp.avm.controller;

import com.tairanchina.csp.avm.entity.RnPackage;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RnPackageControllerTest extends BaseControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(RnPackageControllerTest.class);

    private String uri = "/package";
    private String url = "";

    @Test
    public void list() throws Exception {
        String routeName = "";
        String routeKey = "";
        String routeValue = "";
        url = uri + "?routeName=" + routeName + "&routeKey=" + routeKey + "&routeValue=" + routeValue;
        String contentAsString = get(url);
        logger.info(contentAsString);

        routeName = "name";
        routeKey = "";
        routeValue = "";
        url = uri + "?routeName=" + routeName + "&routeKey=" + routeKey + "&routeValue=" + routeValue;
        contentAsString = get(url);
        logger.info(contentAsString);

        routeKey = "key";
        routeValue = "";
        url = uri + "?routeName=" + routeName + "&routeKey=" + routeKey + "&routeValue=" + routeValue;
        contentAsString = get(url);
        logger.info(contentAsString);

        routeValue = "value";
        url = uri + "?routeName=" + routeName + "&routeKey=" + routeKey + "&routeValue=" + routeValue;
        contentAsString = get(url);
        logger.info(contentAsString);

        url = uri + "?routeName=" + routeName + "&routeKey=" + routeKey + "&routeValue=" + routeValue;
        contentAsString = get(url);
        logger.info(contentAsString);

    }

    @Test
    public void create() throws Exception {
        RnPackage rnPackage = new RnPackage();
        String contentAsString = post(uri, rnPackage);
        logger.info(contentAsString);

        rnPackage.setAppId(24);
        rnPackage.setRnType(1);
        rnPackage.setVersionMax("2.0.0.2");
        rnPackage.setVersionMin("2.0.0.0");
        rnPackage.setRnName("rnname");
        rnPackage.setRnNickName("nickname");
        rnPackage.setRnVersion("rnversion");
        rnPackage.setRnUpdateLog("log");
        rnPackage.setResourceUrl("url");
        contentAsString = post(uri, rnPackage);
        logger.info(contentAsString);


        rnPackage.setVersionMin("2.0.0.2");
        contentAsString = post(uri, rnPackage);
        logger.info(contentAsString);
    }

    @Test
    public void update() throws Exception {
        Integer id = 1;
        RnPackage rnPackage = new RnPackage();
        url = uri + "/" + id;
        String contentAsString = put(url, rnPackage);
        logger.info(contentAsString);

        id = 8;
        rnPackage.setAppId(24);
        rnPackage.setRnType(1);
        rnPackage.setVersionMax("2.0.0.2");
        rnPackage.setVersionMin("2.0.0.0");
        rnPackage.setRnName("rnname");
        rnPackage.setRnNickName("nickname");
        rnPackage.setRnVersion("rnversion");
        rnPackage.setRnUpdateLog("log");
        url = uri + "/" + id;
        contentAsString = put(url, rnPackage);
        logger.info(contentAsString);

        rnPackage.setVersionMin("2.0.0.2");
        url = uri + "/" + id;
        contentAsString = put(url, rnPackage);
        logger.info(contentAsString);
    }

    @Test
    public void delete() throws Exception {
        Integer id = 8;
        url = uri + "/" + id;
        String contentAsString = delete(url);
        logger.info(contentAsString);

        id = 0;
        url = uri + "/" + id;
        contentAsString = delete(url);
        logger.info(contentAsString);
    }

    @Test
    public void find() throws Exception {
        Integer id = 8;
        url = uri + "/" + id;
        String contentAsString = get(url);
        logger.info(contentAsString);

        id = 0;
        url = uri + "/" + id;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

}