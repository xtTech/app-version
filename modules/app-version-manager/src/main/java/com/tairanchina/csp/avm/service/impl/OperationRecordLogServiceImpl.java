package com.tairanchina.csp.avm.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.OperationRecordLogExt;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.OperationRecordLog;
import com.tairanchina.csp.avm.mapper.OperationRecordLogMapper;
import com.tairanchina.csp.avm.service.BasicService;
import com.tairanchina.csp.avm.service.OperationRecordLogService;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationRecordLogServiceImpl implements OperationRecordLogService {

    @Autowired
    private OperationRecordLogMapper operationRecordLogMapper;

    @Autowired
    private BasicService basicService;

    @Override
    public ServiceResult createOperationRecordLog(OperationRecordLog operationRecordLog) {
        operationRecordLog.setCreatedBy(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
        operationRecordLog.setAppId(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        int result = operationRecordLogMapper.insert(operationRecordLog);
        if (result > 0) {
            return ServiceResult.ok(operationRecordLog);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult deleteOperationRecordLog(Integer id) {
        OperationRecordLog operationRecordLog = operationRecordLogMapper.selectById(id);
        if (null == operationRecordLog) {
            return ServiceResultConstants.OPERATION_LOG_NOT_EXISTS;
        }
        operationRecordLog.setDelFlag(1);
        operationRecordLog.setUpdatedBy(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
        operationRecordLog.setUpdatedTime(null);
        int result = operationRecordLogMapper.updateById(operationRecordLog);
        if (result > 0) {
            return ServiceResult.ok(operationRecordLog);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult deleteOperationRecordLogForever(Integer id) {
        OperationRecordLog operationRecordLog = operationRecordLogMapper.selectById(id);
        if (null == operationRecordLog) {
            return ServiceResultConstants.OPERATION_LOG_NOT_EXISTS;
        }
        int result = operationRecordLogMapper.deleteById(operationRecordLog);
        if (result > 0) {
            return ServiceResult.ok(operationRecordLog);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult getOperationRecordLogById(Integer id) {
        OperationRecordLog operationRecordLog = operationRecordLogMapper.selectById(id);
        return ServiceResult.ok(operationRecordLog);
    }

    @Override
    public ServiceResult list(int page, int pageSize, EntityWrapper<OperationRecordLog> wrapper) {
        Page<OperationRecordLog> pageEntity = new Page<>();
        pageEntity.setSize(pageSize);
        pageEntity.setCurrent(page);
        pageEntity.setRecords(operationRecordLogMapper.selectPage(pageEntity, wrapper));
        basicService.formatCreatedBy(pageEntity.getRecords());
        return ServiceResult.ok(pageEntity);
    }

    @Override
    public ServiceResult getListByQuery(int page, int pageSize, String phone, String nickName, Integer appId, String operationResource, String operationDescription, String operationType, String startDate, String endDate) {
        Page<OperationRecordLogExt> logPage = new Page<>();
        logPage.setCurrent(page);
        logPage.setSize(pageSize);
        List<OperationRecordLogExt> list = operationRecordLogMapper.selectLogExtByQuery(logPage, phone, nickName, appId, operationResource, operationDescription, operationType, startDate, endDate);
        logPage.setRecords(list);
        return ServiceResult.ok(logPage);
    }


}
