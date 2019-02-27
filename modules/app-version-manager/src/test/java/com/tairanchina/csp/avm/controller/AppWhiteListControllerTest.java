package com.tairanchina.csp.avm.controller;

import com.tairanchina.csp.avm.entity.AppWhiteList;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;


public class AppWhiteListControllerTest extends BaseControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(AppWhiteListControllerTest.class);

    private String uri = "/white";

    private String url = "";

    @Test
    public void generateTemplet() throws Exception {
        String type = "ip";
        url = uri + "/templet/" + type;
        String contentAsString = get(url);
        logger.info(contentAsString);

        type = "phone";
        url = uri + "/templet/" + type;
        contentAsString = get(url);
        logger.info(contentAsString);

        type = "dsdsa";
        url = uri + "/templet/" + type;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

    @Test
    public void batchUpdate() throws Exception {
        String batchName = "batchName";
        String type = "ip";
        MultipartFile file = new MockMultipartFile("运营监测系统APP白名单批量添加模板.xls", new FileInputStream(new File("C:\\Users\\hzsyl\\Desktop\\运营监测系统APP白名单批量添加模板.xls")));
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("batchName", batchName);
        map.put("type", type);
        map.put("file", file);
        url = uri + "add_batch";
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.MULTIPART_FORM_DATA).content($.json.toJsonString(map)).headers(httpHeaders)).andReturn();
//        String contentAsString = mvcResult.getResponse().getContentAsString();
//        assertThat(contentAsString).isNotEmpty();
    }

    @Test
    public void checkBatchNameExisit() throws Exception {
        String name = "name";
        uri = uri + "/check_name/" + name;
        String contentAsString = get(uri);
        logger.info(contentAsString);
    }

    @Test
    public void getBatch() throws Exception {
        String batchName = "test";
        String operatorId = "b9e980c1495e4d0582c257901d86b4ff";
        uri = uri + "/batch?batchName=" + batchName + "&operatorId=" + operatorId;
        String contentAsString = get(uri);
        logger.info(contentAsString);
    }

    @Test
    public void getWhiteListByBatch() throws Exception {
        Integer batchId = 77;
        uri = uri + "/" + batchId + "/list";
        String contentAsString = get(uri);
        logger.info(contentAsString);
    }

    @Test
    public void getWhiteListPageByBatch() throws Exception {
        Integer batchId = 77;
        String query = "1";
        uri = uri + "/" + batchId + "/page?query=" + query;
        String contentAsString = get(uri);
        logger.info(contentAsString);
    }

    @Test
    public void getWhiteListBatchByQuery() throws Exception {
        String query = "1";
        String type = "phone";
        uri = uri + "/page?query=" + query + "&type=" + type;
        String contentAsString = get(uri);
        logger.info(contentAsString);
    }

    @Test
    public void deleteOne() throws Exception {
        Integer id = 90;
        uri = uri + "/" + id;
        String contentAsString = delete(uri);
        logger.info(contentAsString);
    }

    @Test
    public void deleteBatch() throws Exception {
        Integer batchId = 77;
        uri = uri + "/" + batchId + "/delete";
        String contentAsString = delete(uri);
        logger.info(contentAsString);
    }

    @Test
    public void createBatch() throws Exception {
        AppWhiteList appWhiteList = new AppWhiteList();
        url = uri + "/add/batch";
        String contentAsString = post(url, appWhiteList);
        logger.info(contentAsString);


        appWhiteList.setBatchName("test");
        contentAsString = post(url, appWhiteList);
        logger.info(contentAsString);


        appWhiteList.setBatchName("");
        appWhiteList.setWhiteType(AppWhiteList.WhiteType.ip.name());
        contentAsString = post(url, appWhiteList);
        logger.info(contentAsString);

        appWhiteList.setBatchName("add1234");
        contentAsString = post(url, appWhiteList);
        logger.info(contentAsString);


        appWhiteList.setBatchName("test1234");
        contentAsString = post(url, appWhiteList);
        logger.info(contentAsString);

    }

    @Test
    public void createOne() throws Exception {
        AppWhiteList appWhiteList = new AppWhiteList();
        url = uri + "/add/one";
        String contentAsString = post(url, appWhiteList);
        logger.info(contentAsString);

        appWhiteList.setWhiteValue("10.2.3.2");
        contentAsString = post(url, appWhiteList);
        logger.info(contentAsString);

//        appWhiteList.setWhiteValue(" afafad ");
//        appWhiteList.setBatchId(77);
//        contentAsString = post(url, appWhiteList);
//        logger.info(contentAsString);

        appWhiteList.setWhiteValue("10.2.3.2");
        contentAsString = post(url, appWhiteList);
        logger.info(contentAsString);
    }

    @Test
    public void exportBatch() throws Exception {
        Integer batchId = 77;
        url = uri + "/export/" + batchId;
        String contentAsString = get(url);
        logger.info(contentAsString);

        batchId = 99999;
        url = uri + "/export/" + batchId;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

}