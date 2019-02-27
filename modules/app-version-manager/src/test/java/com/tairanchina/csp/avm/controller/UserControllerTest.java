package com.tairanchina.csp.avm.controller;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserControllerTest extends BaseControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    private String uri = "/user/";

    @Test
    public void set() throws Exception {
        String password = "";
        String phone = "";
        uri = uri + "/set?password=" + password + "&phone=" + phone;
        String contentAsString = get(uri);
        logger.info(contentAsString);
    }

    @Test
    public void change() throws Exception {
        String password = "";
        String phone = "";
        String nickName = "";
        uri = uri + "/nick?password=" + password + "&phone=" + phone + "&nickName=" + nickName;
        String contentAsString = get(uri);
        logger.info(contentAsString);
    }

    @Test
    public void changeNickName() throws Exception{
        String url = "";
        String nickname = "syl";
        url = uri + "/update/"+ nickname;
        String contentAsString = put(url,null);
        logger.info(contentAsString);

        nickname = "aaaaa";
        url = uri + "/update/"+ nickname;
        contentAsString = put(url,null);
        logger.info(contentAsString);

        nickname = "sylt";
        url = uri + "/update/"+ nickname;
        contentAsString = put(url,null);
        logger.info(contentAsString);
    }


}