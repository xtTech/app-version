package com.tairanchina.csp.avm.controller;

import com.tairanchina.csp.avm.entity.CustomApi;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CustomApiControllerTest extends BaseControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(CustomApiControllerTest.class);

    private String uri = "/capi";

    @Test
    public void list() throws Exception {
        String type = "ios";
        uri = uri + "?type=" + type;
        String contentAsString = get(uri);
        logger.info(contentAsString);
    }

    @Test
    public void findCustomApi() throws Exception {
        Integer id = 16;
        uri = uri + "/" + id;
        String contentAsString = get(uri);
        logger.info(contentAsString);
    }

    @Test
    public void addCustomApi() throws Exception {
        CustomApi customApi = new CustomApi();
        customApi.setCustomName("test");
        customApi.setAndroidEnabled(1);
        customApi.setAndroidMin("2.0.0");
        customApi.setAndroidMax("2.0.2");
        customApi.setCustomContent("content");
        customApi.setCustomKey("key");
        uri = uri + "/add";
        String contentAsString = post(uri, customApi);
        logger.info(contentAsString);
    }

    @Test
    public void updateCustomApi() throws Exception {
        Integer id = 16;
        CustomApi customApi = new CustomApi();
        customApi.setCustomName("test");
        customApi.setAndroidEnabled(1);
        customApi.setAndroidMin("2.0.0");
        customApi.setAndroidMax("2.0.3");
        customApi.setCustomContent("content");
        customApi.setCustomKey("key");
        uri = uri + "/update/" + id;
        String contentAsString = put(uri, customApi);
        logger.info(contentAsString);
    }

    @Test
    public void deleteCustomApiForver() throws Exception {
        Integer id = 16;
        uri = uri + "/" + id;
        String contentAsString = delete(uri);
        logger.info(contentAsString);
    }

    @Test
    public void deleteCustomApi() throws Exception {
        Integer id = 16;
        uri = uri + "/" + id;
        String contentAsString = put(uri, null);
        logger.info(contentAsString);
    }

}