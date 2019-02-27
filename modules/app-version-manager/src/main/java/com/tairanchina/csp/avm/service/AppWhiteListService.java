package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.AppWhiteList;

public interface AppWhiteListService {

    /**
     * 判断白名单BatchName是否存在
     */
    ServiceResult checkBatchName(String batchName);

    /**
     * 查询白名单所在的组（分页）
     */
    ServiceResult getAppWhiteListPageByIpOrPhone(int page, int pageSize, EntityWrapper<AppWhiteList> wrapper);

    /**
     * 单个添加白名单
     */
    ServiceResult createOne(AppWhiteList appWhiteList);

    /**
     * 单个添加白名单(通过上传Excel的方式)
     */
    ServiceResult createOneByBatch(AppWhiteList appWhiteList);

    /**
     * 添加白名单组
     * 返回组Id
     */
    ServiceResult createBatch(AppWhiteList appWhiteList);

    /**
     * 查询白名单组列表（分页）
     */
    ServiceResult getAppWhiteBatchPage(int page, int pageSize, EntityWrapper<AppWhiteList> wrapper);

    /**
     * 查询某一组内全部白名单
     */
    ServiceResult getAppWhiteListByBatch(Integer batchId);

    /**
     * 查询某一组内全部白名单（分页）
     */
    ServiceResult getAppWhiteListPageByBatch(int page, int pageSize, Integer batchId, EntityWrapper<AppWhiteList> wrapper);

    /**
     * 单个删除白名单（软删）
     */
    ServiceResult deleteOne(Integer id);

    /**
     * 单个删除白名单（硬删）
     */
    ServiceResult deleteOneForever(Integer id);

    /**
     * 删除整个白名单组（软删）
     */
    ServiceResult deleteBatch(Integer batchId);

    /**
     * 删除整个白名单组（硬删）
     */
    ServiceResult deleteBatchForever(Integer batchId);


}
