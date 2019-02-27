package com.tairanchina.csp.avm.controller;

import com.tairanchina.csp.avm.entity.RnRoute;
import com.tairanchina.csp.avm.service.RnRouteServiceTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RnRouteControllerTest extends BaseControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(RnRouteServiceTest.class);

    private String uri = "/route";
    private String url = "";

    @Test
    public void list() throws Exception {
        String routeName = "";
        String routeKey = "";
        String routeValue = "";
        Integer routeStatus = 1;
        url = uri + "?routeName=" + routeName + "&routeKey=" + routeKey + "&routeValue=" + routeValue + "&routeStatus=" + routeStatus;
        String contentAsString = get(url);
        logger.info(contentAsString);

        routeName = "name";
        routeKey = "";
        routeValue = "";
        routeStatus = 1;
        url = uri + "?routeName=" + routeName + "&routeKey=" + routeKey + "&routeValue=" + routeValue + "&routeStatus=" + routeStatus;
        contentAsString = get(url);
        logger.info(contentAsString);

        routeKey = "key";
        routeValue = "";
        routeStatus = 1;
        url = uri + "?routeName=" + routeName + "&routeKey=" + routeKey + "&routeValue=" + routeValue + "&routeStatus=" + routeStatus;
        contentAsString = get(url);
        logger.info(contentAsString);

        routeValue = "value";
        routeStatus = 1;
        url = uri + "?routeName=" + routeName + "&routeKey=" + routeKey + "&routeValue=" + routeValue + "&routeStatus=" + routeStatus;
        contentAsString = get(url);
        logger.info(contentAsString);

        routeStatus = 0;
        url = uri + "?routeName=" + routeName + "&routeKey=" + routeKey + "&routeValue=" + routeValue + "&routeStatus=" + routeStatus;
        contentAsString = get(url);
        logger.info(contentAsString);

    }

    @Test
    public void create() throws Exception {
        RnRoute rnRoute = new RnRoute();
        String contentAsString = post(uri, rnRoute);
        logger.info(contentAsString);

        rnRoute.setAndroidEnabled(1);
        rnRoute.setAndroidMax("2.0.0.2");
        rnRoute.setAndroidMin("2.0.0.0");
        rnRoute.setAppId(24);
        rnRoute.setRouteKey("key");
        rnRoute.setRouteValue("value");
        rnRoute.setRouteName("name");
        rnRoute.setRouteStatus(1);
        contentAsString = post(uri, rnRoute);
        logger.info(contentAsString);

        rnRoute.setAndroidMax("2.0.0.0");
        contentAsString = post(uri, rnRoute);
        logger.info(contentAsString);
    }

    @Test
    public void update() throws Exception {
        Integer id = 0;
        RnRoute rnRoute = new RnRoute();
        url = uri + "/" + id;
        String contentAsString = put(url, rnRoute);
        logger.info(contentAsString);

        id = 24;
        url = uri + "/" + id;
        contentAsString = put(url, rnRoute);
        logger.info(contentAsString);

        rnRoute.setAndroidEnabled(1);
        rnRoute.setAndroidMax("2.0.0.2");
        rnRoute.setAndroidMin("2.0.0.0");
        rnRoute.setAppId(24);
        rnRoute.setRouteKey("key");
        rnRoute.setRouteValue("value");
        rnRoute.setRouteName("name");
        rnRoute.setRouteStatus(1);
        url = uri + "/" + id;
        contentAsString = put(url, rnRoute);
        logger.info(contentAsString);


        rnRoute.setAndroidMin("2.0.0.2");
        url = uri + "/" + id;
        contentAsString = put(url, rnRoute);
        logger.info(contentAsString);
    }

    @Test
    public void delete() throws Exception {
        Integer id = 17;
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
        Integer id = 17;
        url = uri + "/" + id;
        String contentAsString = get(url);
        logger.info(contentAsString);

        id = 0;
        url = uri + "/" + id;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

}