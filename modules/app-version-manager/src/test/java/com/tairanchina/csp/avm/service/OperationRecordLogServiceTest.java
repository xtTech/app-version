package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.OperationRecordLog;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class OperationRecordLogServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(OperationRecordLogServiceTest.class);

    @Autowired
    private OperationRecordLogService operationRecordLogService;

    @Test
    public void createOperationRecordLog() throws Exception {
        OperationRecordLog operationRecordLog = new OperationRecordLog();
        operationRecordLog.setOperationContent("'a':'b'")
                .setAppId(24)
                .setOperationResult(OperationRecordLog.OperationResult.OTHER)
                .setOperationType(OperationRecordLog.OperationType.CREATE)
                .setOperationResource(OperationRecordLog.OperationResource.ANDROID_VERSION)
                .setOperator("test")
                .setResultMessage("test")
                .setCreatedBy("test");
        ServiceResult result = operationRecordLogService.createOperationRecordLog(operationRecordLog);
        if (result.getData() != null) {
            System.out.println(result.getData());
        }
    }

    @Test
    public void deleteOperationRecordLog() throws Exception {
        Integer id = 50;
        ServiceResult result = operationRecordLogService.deleteOperationRecordLogForever(id);
        if (result.getData() != null) {
            if (result.getData() != null) {
                System.out.println(result.getData());
            }
        }
    }

    @Test
    public void deleteOperationRecordLogForever() throws Exception {
        Integer id = 50;
        ServiceResult result = operationRecordLogService.deleteOperationRecordLogForever(id);
        if (result.getData() != null) {
            System.out.println(result.getData());
        }
    }

    @Test
    public void getOperationRecordLogById() throws Exception {
        Integer id = 54;
        ServiceResult result = operationRecordLogService.getOperationRecordLogById(id);
        OperationRecordLog operationRecordLog = (OperationRecordLog) result.getData();
        Object jsonObject = operationRecordLog.getOperationContent();
        System.out.println(jsonObject);
    }

    @Test
    public void list() throws Exception {
        String operator = "";
        String operationType = "DELETE";
        String operationResource = "";
        Integer appId = 24;
        Integer page = 1;
        Integer pageSize = 10;

        EntityWrapper<OperationRecordLog> wrapper = new EntityWrapper<>();
        wrapper.and().eq("app_id", appId);

        if (StringUtils.isNotBlank(operator)) {
            wrapper.andNew().eq("operator", operator);
        }
        if (StringUtils.isNotBlank(operationType)) {
            wrapper.andNew().eq("operation_type", operationType);
        }
        if (StringUtils.isNotBlank(operationResource)) {
            wrapper.andNew().eq("operation_resource", operationResource);
        }
        wrapper.andNew().eq("del_flag", 0);
        wrapper.orderBy("created_time", false);
        ServiceResult result = operationRecordLogService.list(page, pageSize, wrapper);
        if (result.getData() != null) {
            System.out.println(result.getData());
        }
    }

    @Test
    public void getListByQuery() throws Exception {
        ServiceResult result = operationRecordLogService.getListByQuery(1, 10, null, null, null, null, null, null,null,null);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

}