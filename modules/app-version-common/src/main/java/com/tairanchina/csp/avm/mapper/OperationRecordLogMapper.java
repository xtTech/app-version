package com.tairanchina.csp.avm.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.tairanchina.csp.avm.dto.OperationRecordLogExt;
import com.tairanchina.csp.avm.entity.OperationRecordLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OperationRecordLogMapper extends BaseMapper<OperationRecordLog> {

    List<OperationRecordLogExt> selectLogExtByQuery(Pagination page, @Param("phone") String phone, @Param("nickName") String nickName, @Param("appId") Integer appId, @Param("operationResource") String operationResource, @Param("operationDescription") String operationDescription, @Param("operationType") String operationType, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
