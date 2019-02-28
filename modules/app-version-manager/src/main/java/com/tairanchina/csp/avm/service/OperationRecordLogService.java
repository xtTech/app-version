package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.OperationRecordLog;

public interface OperationRecordLogService {

    ServiceResult createOperationRecordLog(OperationRecordLog operationRecordLog);

    ServiceResult deleteOperationRecordLog(Integer id);

    ServiceResult deleteOperationRecordLogForever(Integer id);

    ServiceResult getOperationRecordLogById(Integer id);

    ServiceResult list(int page, int pageSize, EntityWrapper<OperationRecordLog> wrapper);

    ServiceResult getListByQuery(int page,
                                 int pageSize,
                                 String phone,
                                 String nickName,
                                 Integer appId,
                                 String operationResource,
                                 String operationDescription,
                                 String operationType,
                                 String startDate,
                                 String endDate);
}
