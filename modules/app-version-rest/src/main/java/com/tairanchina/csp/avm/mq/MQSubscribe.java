package com.tairanchina.csp.avm.mq;

import com.ecfront.dew.common.$;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tairanchina.csp.avm.entity.AccessLog;
import com.tairanchina.csp.avm.mapper.AccessLogMapper;
import com.tairanchina.csp.avm.utils.IpQueryer;
import com.tairanchina.csp.avm.utils.UserAgentUtils;
import com.tairanchina.csp.dew.Dew;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MQSubscribe {

    private static final Logger logger = LoggerFactory.getLogger(MQSubscribe.class);

    @Autowired
    AccessLogMapper accessLogMapper;

    public void subscribeAccessRecord() {
        Dew.cluster.mq.response(MQTopic.ACCESS_RECORD, meassage -> {
            logger.debug("mq开始记录访问日志...");
            try {
                AccessRecordEvent event = $.json.toObject(meassage, AccessRecordEvent.class);

                AccessLog log = new AccessLog();
                log.setApi(event.getApi());
                log.setApiDescription(event.getApiDescription());
                log.setApiArgs($.json.toJsonString(event.getRequestArgs()));
                log.setReturnCode(event.getReturnCode());
                log.setReturnMessage(event.getReturnMessage());
                String ipAddress = event.getIpAddress();

                ObjectNode ipInfo = IpQueryer.getIpInfo(ipAddress);
                if (ipInfo != null && ipInfo.get("code").asInt() == 0) {
                    JsonNode data = ipInfo.get("data");
                    if (data != null) {
                        log.setReqIsp(data.get("isp").asText());
                        log.setReqCity(data.get("city").asText());
                        log.setReqCityId(data.get("city_id").asText());
                        log.setReqRegion(data.get("region").asText());
                        log.setReqRegionId(data.get("region_id").asText());
                        log.setReqCountry(data.get("country").asText());
                        log.setReqCountryId(data.get("country_id").asText());
                        log.setReqArea(data.get("area").asText());
                    }
                }
                String userAgent = event.getUserAgent();
                logger.info("MQ接收到的useragent：" + userAgent);
                String device;
                String browser;
                //传统的useragent
                if (userAgent.startsWith("Mozilla")) {
                    device = UserAgentUtils.getDeviceType(userAgent);
                    UserAgent ua = UserAgent.parseUserAgentString(userAgent);
                    OperatingSystem os = ua.getOperatingSystem();
                    browser = UserAgentUtils.getBrowser(userAgent);
                    log.setReqBrowser(browser);
                    log.setReqOs(os.getName());
                    log.setReqDevice(device);
                }
                //APP接入方自定义的useragent useragent格式不规范，无法准确记录，容易报错
                if (userAgent.startsWith("OS/")) {
                    String os = userAgent.substring(userAgent.indexOf("OS/") + 3, userAgent.indexOf(" "));
                    log.setReqOs(os);
                }

                log.setReqUa(userAgent);
                accessLogMapper.insert(log);
                logger.debug("mq访问日志记录成功");
            } catch (Exception e) {
                logger.error("解析useragent时出错，"  + " 原因：" + e.getMessage());
            }
        });
    }


}
