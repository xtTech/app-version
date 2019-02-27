package com.tairanchina.csp.avm.controller;

import com.tairanchina.csp.avm.dto.UploadFileEntity;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ApkControllerTest extends BaseControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(ApkControllerTest.class);

    private String uri = "/apk";

    private String url = "";

    @Test
    public void upload() throws Exception {
        url = uri + "/upload";
        String contentAsString = post(url,null);
        logger.info(contentAsString);
    }

    @Test
    public void list() throws Exception {
        Integer versionId = 14;
        String channelCode = "qudao";
        String md5 = "d41d8c";
        Integer deliveryStatus = 1;
        url = uri + "?versionId=" + versionId + "&channelCode=" + channelCode + "&md5=" + md5 + "&deliveryStatus=" + deliveryStatus;
        String contentAsString = get(url);
        logger.info(contentAsString);

        versionId = 0;
        url = uri + "?versionId=" + versionId + "&channelCode=" + channelCode + "&md5=" + md5 + "&deliveryStatus=" + deliveryStatus;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

    @Test
    public void create() throws Exception {
        UploadFileEntity uploadFileEntity = new UploadFileEntity();
        uploadFileEntity.setChannel("official");
        uploadFileEntity.setFileName("filename");
        uploadFileEntity.setOssUrl("ossurl");
        uploadFileEntity.setMd5("md5");
        uploadFileEntity.setVersionId(14);
        String contentAsString = post(uri, uploadFileEntity);
        logger.info(contentAsString);

        uploadFileEntity.setChannel("sssssss");
        contentAsString = post(uri, uploadFileEntity);
        logger.info(contentAsString);
    }

    @Test
    public void delivery() throws Exception {
        Integer id = 32;
        url = uri + "/" + id + "/delivery";
        String contentAsString = put(url, null);
        logger.info(contentAsString);

        id = 0;
        url = uri + "/" + id + "/delivery";
        contentAsString = put(url, null);
        logger.info(contentAsString);
    }

    @Test
    public void undelivery() throws Exception {
        Integer id = 32;
        url = uri + "/" + id + "/undelivery";
        String contentAsString = put(url, null);
        logger.info(contentAsString);

        id = 0;
        url = uri + "/" + id + "/undelivery";
        contentAsString = put(url, null);
        logger.info(contentAsString);
    }

    @Test
    public void delete() throws Exception {
        Integer id = 32;
        url = uri + "/" + id;
        String contentAsString = delete(url);
        logger.info(contentAsString);

        id = 0;
        url = uri + "/" + id;
        contentAsString = delete(url);
        logger.info(contentAsString);
    }

    @Test
    public void checkChannel() throws Exception {
        String channelCode = "qudao";
        url = uri + "/channel/check?channelCode=" + channelCode;
        String contentAsString = get(url);
        logger.info(contentAsString);

        channelCode = "dfdff";
        url = uri + "/channel/check?channelCode=" + channelCode;
        contentAsString = get(url);
        logger.info(contentAsString);

        channelCode = "";
        url = uri + "/channel/check?channelCode=" + channelCode;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

    @Test
    public void exists() throws Exception {
        String channelCode = "qudao";
        Integer versionId = 9;
        url = uri + "/exists?channelCode=" + channelCode + "&versionId=" + versionId;
        String contentAsString = get(url);
        logger.info(contentAsString);

        channelCode = "";
        versionId = 9;
        url = uri + "/exists?channelCode=" + channelCode + "&versionId=" + versionId;
        contentAsString = get(url);
        logger.info(contentAsString);

        channelCode = "qudao";
        versionId = 0;
        url = uri + "/exists?channelCode=" + channelCode + "&versionId=" + versionId;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

}