package com.tairanchina.csp.avm.aspect;

import com.ecfront.dew.common.$;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.App;
import com.tairanchina.csp.avm.entity.OperationRecordLog;
import com.tairanchina.csp.avm.mapper.OperationRecordLogMapper;
import com.tairanchina.csp.avm.annotation.OperationRecord;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
public class OperationAspect {

    private static final Logger logger = LoggerFactory.getLogger(OperationAspect.class);

    @Autowired
    private OperationRecordLogMapper operationRecordLogMapper;

    @AfterReturning(value = "@annotation(operationRecord)", returning = "serviceResult")
    public void recordOperationLog(JoinPoint joinPoint, OperationRecord operationRecord, ServiceResult serviceResult) {
        logger.info("开始记录[{}]操作日志...", operationRecord.resource());
        OperationRecordLog log = new OperationRecordLog();
        if (200 == serviceResult.getCode()) {
            if (null != serviceResult.getData()) {
                logger.info("记录操作成功内容...");
                String content = $.json.toJsonString(serviceResult.getData());
                log.setOperationContent(content);
            }
            log.setOperationResult(OperationRecordLog.OperationResult.SUCCESS);
        } else if (200 != serviceResult.getCode()) {
            logger.info("记录操作失败内容...");
            log.setOperationContent($.json.toJsonString(joinPoint.getArgs()));
            log.setOperationResult(OperationRecordLog.OperationResult.FAILD);
        }
        String resultMessage = serviceResult.getMessage();
        String operator = ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId();
        Integer appId = ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId();
        if (null != appId) {
            log.setAppId(appId);
        }

        log.setOperator(operator)
                .setOperationResource(operationRecord.resource())
                .setOperationType(operationRecord.type())
                .setOperationDescription(operationRecord.description().getDescription())
                .setResultMessage(resultMessage)
                .setCreatedBy(operator);

        //如果被操作的资源为“APP”和“USER_APP_REL”时，需要单独设置被操作的appId。
        if (operationRecord.resource().equals(OperationRecordLog.OperationResource.APP)) {
            Object[] args = joinPoint.getArgs();
            if (operationRecord.type().equals(OperationRecordLog.OperationType.DELETE)
                    || operationRecord.type().equals(OperationRecordLog.OperationType.UPDATE)) {
                Integer id = (Integer) args[0];
                log.setAppId(id);
            } else if (operationRecord.type().equals(OperationRecordLog.OperationType.CREATE)) {
                App app = (App) serviceResult.getData();
                if(Objects.nonNull(app)){
                    log.setAppId(app.getId());
                }
            }
        }

        if (operationRecord.resource().equals(OperationRecordLog.OperationResource.USER_APP_REL)) {
            Object[] args = joinPoint.getArgs();
            Integer id = (Integer) args[1];
            log.setAppId(id);
        }

        operationRecordLogMapper.insert(log);
        logger.info("操作日志记录成功，操作人：{}", operator);
    }

}
