package com.tairanchina.csp.avm.controller;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OperationRecordLogControllerTest extends BaseControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(OperationRecordLogControllerTest.class);

    private String uri = "/log";

    @Test
    public void getOperationRecordLog() throws Exception {
        Integer id = 20;
        uri = uri + "/" + id;
        String contentAsString = get(uri);
        logger.info(contentAsString);
    }

    @Test
    public void list() throws Exception {
        String url = "";
        String operationType = "IOS版本下架";
        String endDate = "2018-06-27 19:10:27";
        Integer appId = 24;
        url = uri + "?" +
//                "phone=" + phone + "&" +
                "operationType=" + operationType + "&" +
//                "operationResource=" + operationResource +
                "endDate=" + endDate
                + "&appId=" + appId
        ;
        String contentAsString = get(url);
        logger.info(contentAsString);
    }

//    @Test
//    public void create() throws Exception {
//        OperationRecordLog operationRecordLog = new OperationRecordLog();
//        operationRecordLog.setOperationContent("'a':'b'")
//                .setAppId(24)
//                .setOperationResult(OperationRecordLog.OperationResult.OTHER)
//                .setOperationType(OperationRecordLog.OperationType.CREATE)
//                .setOperationResource(OperationRecordLog.OperationResource.OPETATION_RECORD_LOG)
//                .setOperator("test")
//                .setResultMessage("test")
//                .setCreatedBy("test");
//        String contentAsString = post(uri, operationRecordLog);
//        logger.info(contentAsString);
//    }

    @Test
    public void delete() throws Exception {
        Integer id = 20;
        uri = uri + "/delete/" + id;
        String contentAsString = put(uri, null);
        logger.info(contentAsString);
    }

    @Test
    public void deleteForever() throws Exception {
        Integer id = 20;
        uri = uri + "/" + id;
        String contentAsString = delete(uri);
        logger.info(contentAsString);
    }

    @Test
    public void getOperationType() throws Exception {
        uri = uri + "/type";
        String contentAsString = get(uri);
        logger.info(contentAsString);
    }

    @Test
    public void getOperationResource() throws Exception {
        uri = uri + "/resource";
        String contentAsString = get(uri);
        logger.info(contentAsString);
    }

    @Test
    public void getOperationResourceType() throws Exception {
        String resource = "user";
        uri = uri + "/" + resource + "/type";
        String contentAsString = get(uri);
        logger.info(contentAsString);
    }

}