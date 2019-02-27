package com.tairanchina.csp.avm.utils;

import com.ecfront.dew.common.$;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 根据IP查询相关信息
 * Created by hzlizx on 2018/4/27 0027
 */
public class IpQueryer {
    private static final Logger logger = LoggerFactory.getLogger(IpQueryer.class);

    private static final String HOST = "https://api01.aliyun.venuscn.com";
    private static final String PATH = "/ip";
    private static final String APPCODE = "e3fc47cfc3fd43dc81e90be2f5f70eef";

    private static final String TAOBAO_HOST ="http://ip.taobao.com/service/getIpInfo.php?ip=";
    public static ObjectNode getIpInfo(String ip) {

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(TAOBAO_HOST + ip);
        //httpGet.setHeader("Authorization", "APPCODE " + APPCODE);
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity);
            logger.debug(body);
            return $.json.getMapper().readValue(body, ObjectNode.class);
        } catch (IOException e) {
            logger.error("请求接口出错", e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                logger.error("关闭client出错", e);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        ObjectNode ipInfo = IpQueryer.getIpInfo("115.236.161.67");
        JsonNode data = ipInfo.get("data");
        System.out.println(data);
    }
}
