package com.tairanchina.csp.avm.aspect;

import com.ecfront.dew.common.$;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.annotation.AccessRecord;
import com.tairanchina.csp.avm.mq.AccessRecordEvent;
import com.tairanchina.csp.avm.mq.MQTopic;
import com.tairanchina.csp.avm.utils.NetworkUtil;
import com.tairanchina.csp.dew.Dew;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Aspect
@Component
public class AccessRecordAspect {

    private static final Logger logger = LoggerFactory.getLogger(AccessRecordAspect.class);


    @AfterReturning(value = "@annotation(accessRecord)", returning = "serviceResult")
    public void recordOperationLog(JoinPoint joinPoint, AccessRecord accessRecord, ServiceResult serviceResult) {
        logger.info("开始记录[{}]接口访问信息...", accessRecord.api());
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String ipAddress = NetworkUtil.getIpAddress(request);
            String userAgent = request.getHeader("User-Agent");

            AccessRecordEvent accessRecordEvent = new AccessRecordEvent();
            accessRecordEvent.setApi(accessRecord.api())
                    .setApiDescription(accessRecord.apiDescription().getName())
                    .setRequestArgs(joinPoint.getArgs())
                    .setIpAddress(ipAddress)
                    .setUserAgent(userAgent)
                    .setReturnCode(serviceResult.getCode())
                    .setReturnMessage(serviceResult.getMessage());
            Dew.cluster.mq.request(MQTopic.ACCESS_RECORD, $.json.toJsonString(accessRecordEvent));
        } catch (IOException e) {
            logger.error("记录访问日志时，解析IP地址失败", e);
        } catch (Exception e) {
            logger.error("记录访问日志时，出现异常", e);
        }
    }

}
