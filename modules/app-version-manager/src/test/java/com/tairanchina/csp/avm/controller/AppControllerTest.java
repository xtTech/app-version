package com.tairanchina.csp.avm.controller;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AppControllerTest extends BaseControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(AppControllerTest.class);

    private String uri = "/app";

    @Test
    public void app() throws Exception {
        String contentAsString = get(uri);
        logger.info(contentAsString);
    }

    @Test
    public void myApp() throws Exception {
        uri = uri + "/bind";
        String contentAsString = get(uri);
        logger.info(contentAsString);
    }

}