package com.tairanchina.csp.avm.controller;

import com.tairanchina.csp.avm.entity.Channel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ChannelControllerTest extends BaseControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(ChannelControllerTest.class);

    private String uri = "/channel";
    private String url = "";

    @Test
    public void list() throws Exception {
        String channelName = "";
        String channelCode = "code";
        Integer channelStatus = 1;
        url = uri + "?channelStatus=" + channelStatus + "&channelName=" + channelName + "&channelCode=" + channelCode;
        String contentAsString = get(url);
        logger.info(contentAsString);

        channelName = "name";
        channelCode = "";
        channelStatus = 1;
        url = uri + "?channelStatus=" + channelStatus + "&channelName=" + channelName + "&channelCode=" + channelCode;
        contentAsString = get(url);
        logger.info(contentAsString);

        channelName = "name";
        channelCode = "code";
        channelStatus = 0;
        url = uri + "?channelStatus=" + channelStatus + "&channelName=" + channelName + "&channelCode=" + channelCode;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

    @Test
    public void create() throws Exception {
        Channel channel = new Channel();
        channel.setChannelName("name");
        channel.setChannelCode("code");
        channel.setChannelType(1);
        String contentAsString = post(uri, channel);
        logger.info(contentAsString);

        channel.setChannelName(null);
        channel.setChannelCode("code");
        channel.setChannelType(1);
        contentAsString = post(uri, channel);
        logger.info(contentAsString);

        channel.setChannelName("name");
        channel.setChannelCode(null);
        channel.setChannelType(1);
        contentAsString = post(uri, channel);
        logger.info(contentAsString);
    }

    @Test
    public void delete() throws Exception {
        Integer id = 30;
        url = uri + "/" + id;
        String contentAsString = delete(url);
        logger.info(contentAsString);

        id = 0;
        url = uri + "/" + id;
        contentAsString = delete(url);
        logger.info(contentAsString);
    }

    @Test
    public void scrap() throws Exception {
        Integer id = 30;
        url = uri + "/" + id + "/scrap";
        String contentAsString = put(url, null);
        logger.info(contentAsString);

        id = 0;
        url = uri + "/" + id + "/scrap";
        contentAsString = put(url, null);
        logger.info(contentAsString);
    }

    @Test
    public void open() throws Exception {
        Integer id = 30;
        url = uri + "/" + id + "/open";
        String contentAsString = put(url, null);
        logger.info(contentAsString);

        id = 0;
        url = uri + "/" + id + "/open";
        contentAsString = put(url, null);
        logger.info(contentAsString);
    }

    @Test
    public void edit() throws Exception {
        Channel channel = new Channel();
        channel.setChannelName("nameedit");
        channel.setChannelCode("code");
        channel.setChannelType(1);
        Integer id = 30;
        url = uri + "/" + id + "/edit";
        String contentAsString = put(url, channel);
        logger.info(contentAsString);

        id = 0;
        url = uri + "/" + id + "/edit";
        contentAsString = put(url, channel);
        logger.info(contentAsString);
    }

    @Test
    public void find() throws Exception {
        Integer id = 30;
        url = uri + "/" + id;
        String contentAsString = get(url);
        logger.info(contentAsString);

        id = 0;
        url = uri + "/" + id;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

}