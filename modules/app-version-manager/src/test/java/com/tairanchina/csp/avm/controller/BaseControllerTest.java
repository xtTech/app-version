package com.tairanchina.csp.avm.controller;

import com.ecfront.dew.common.$;
import com.tairanchina.csp.avm.entity.LoginInfo;
import com.tairanchina.csp.avm.AppVersionManagerApplication;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AppVersionManagerApplication.class})
@Transactional
@ComponentScan("com.tairanchina.csp.avm.controller")
public class BaseControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(BaseControllerTest.class);


    private static MockMvc mockMvc;
    private static HttpHeaders httpHeaders;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        String authorization = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiOWU5ODBjMTQ5NWU0ZDA1ODJjMjU3OTAxZDg2YjRmZiIsImF1ZCI6WyJ1Y2NjYzFkNjQ1ZjA4OGZlYmQiLCJ1Y2VudGVyIl0sImlfdiI6MTUyOTY1MTQ4NTAwMCwiaWRlbnRfaWQiOjMxMjI0NTksIm5iZiI6MTUzNzk0OTMzOSwicF92IjoxNTM3NjcyOTM0MDAwLCJpc3MiOiJ1Y2VudGVyIiwiZXhwIjoxNTM4MDM1NzM5LCJ0eXBlIjoxLCJpYXQiOjE1Mzc5NDkzMzksImp0aSI6IjMzNjY3MCJ9.loopoWwJ65MhfF4mKWdItwiaDDa6MPwWSXM5rFh2Hl3IktJBg1tGdb_ABCBAYxyf7LOR2ckkR_raX1gW3A64y6S7oQf27K1JdWKrkinKmK3Ak_N9klGuGFdoo4zS9MWr7p6aSjB3nJpSUM3ZI3GZdTpEllZMiWjZU_ulwadzjNk";

        LoginInfo loginInfo = new LoginInfo(24, "b9e980c1495e4d0582c257901d86b4ff");
        ThreadLocalUtils.USER_THREAD_LOCAL.set(loginInfo);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", authorization);
        httpHeaders.set("appId", "24");
    }

    @Test
    public void test() {
        logger.info("BaseControllerTest........");
    }


    public String post(String uri, Object requestBody) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content($.json.toJsonString(requestBody)).headers(httpHeaders)).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        return contentAsString;
    }

    public String put(String uri, Object requestBody) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content($.json.toJsonString(requestBody)).headers(httpHeaders)).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        return contentAsString;
    }

    public String get(String uri) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri).headers(httpHeaders)).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        return contentAsString;
    }

    public String delete(String uri) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).headers(httpHeaders)).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        return contentAsString;
    }

}
