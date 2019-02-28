package com.tairanchina.csp.avm.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.AppWhiteList;
import com.tairanchina.csp.avm.mapper.AppWhiteListMapper;
import com.tairanchina.csp.avm.service.AppWhiteListService;
import com.tairanchina.csp.avm.service.BasicService;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppWhiteListServiceImpl implements AppWhiteListService {

    @Autowired
    private AppWhiteListMapper appWhiteListMapper;
    
    @Autowired
    private BasicService basicService;

    @Override
    public ServiceResult checkBatchName(String batchName) {
        EntityWrapper<AppWhiteList> wrapper = new EntityWrapper<>();
        wrapper.and().eq("app_id", ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        wrapper.and().eq("batch_name", batchName);
        wrapper.and().eq("del_flag", 0);
        List<AppWhiteList> result = appWhiteListMapper.selectList(wrapper);
        return ServiceResult.ok(result);
    }

    @Override
    public ServiceResult getAppWhiteListPageByIpOrPhone(int page, int pageSize, EntityWrapper<AppWhiteList> wrapper) {
        wrapper.andNew().eq("app_id", ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        List<AppWhiteList> result = appWhiteListMapper.selectList(wrapper);
        return ServiceResult.ok(result);
    }

    @Override
    public ServiceResult createOne(AppWhiteList appWhiteList) {
        AppWhiteList batch = appWhiteListMapper.selectById(appWhiteList.getBatchId());
        if (null == batch || 1 == batch.getDelFlag()) {
            return ServiceResultConstants.WHITE_LIST_NOT_EXISITS;
        }
        appWhiteList.setId(null);
        appWhiteList.setDelFlag(null);
        appWhiteList.setBatchId(batch.getId());
        appWhiteList.setBatchName(batch.getBatchName());
        appWhiteList.setWhiteType(batch.getWhiteType());
        appWhiteList.setCreatedBy(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
        appWhiteList.setAppId(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        Integer result = appWhiteListMapper.insert(appWhiteList);
        if (result > 0) {
            return ServiceResult.ok(appWhiteList);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult createOneByBatch(AppWhiteList appWhiteList) {
        appWhiteList.setId(null);
        appWhiteList.setDelFlag(null);
        appWhiteList.setCreatedBy(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
        appWhiteList.setAppId(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        Integer result = appWhiteListMapper.insert(appWhiteList);
        if (result > 0) {
            return ServiceResult.ok(appWhiteList);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult createBatch(AppWhiteList appWhiteList) {
        appWhiteList.setId(null);
        appWhiteList.setBatchId(null);
        appWhiteList.setDelFlag(null);
        appWhiteList.setCreatedBy(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
        appWhiteList.setAppId(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        appWhiteList.setIsBatch(1);
        appWhiteListMapper.insert(appWhiteList);
        Integer batchId = appWhiteList.getId();
        appWhiteList.setBatchId(batchId);
        Integer result = appWhiteListMapper.updateById(appWhiteList);
        if (result > 0) {
            return ServiceResult.ok(appWhiteList);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult getAppWhiteBatchPage(int page, int pageSize, EntityWrapper<AppWhiteList> wrapper) {
        wrapper.andNew().eq("is_batch", 1);
        Page<AppWhiteList> pageEntity = new Page<>();
        pageEntity.setSize(pageSize);
        pageEntity.setCurrent(page);
        pageEntity.setRecords(appWhiteListMapper.selectPage(pageEntity, wrapper));
        basicService.formatCreatedBy(pageEntity.getRecords());
        return ServiceResult.ok(pageEntity);
    }

    @Override
    public ServiceResult getAppWhiteListByBatch(Integer batchId) {
        EntityWrapper<AppWhiteList> wrapper = new EntityWrapper<>();
        wrapper.andNew().eq("batch_id", batchId);
        wrapper.andNew().eq("app_id", ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        wrapper.andNew().eq("is_batch", 0);
        wrapper.andNew().eq("del_flag", 0);
        wrapper.orderBy("created_time", false);
        List<AppWhiteList> result = appWhiteListMapper.selectList(wrapper);
        return ServiceResult.ok(result);
    }

    @Override
    public ServiceResult getAppWhiteListPageByBatch(int page, int pageSize, Integer batchId, EntityWrapper<AppWhiteList> wrapper) {
        wrapper.andNew().eq("batch_id", batchId);
        wrapper.andNew().eq("app_id", ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        wrapper.andNew().eq("del_flag", 0);
        wrapper.orderBy("created_time", false);
        Page<AppWhiteList> pageEntity = new Page<>();
        pageEntity.setSize(pageSize);
        pageEntity.setCurrent(page);
        pageEntity.setRecords(appWhiteListMapper.selectPage(pageEntity, wrapper));
        basicService.formatCreatedBy(pageEntity.getRecords());
        return ServiceResult.ok(pageEntity);
    }

    @Override
    public ServiceResult deleteOne(Integer id) {
        AppWhiteList appWhiteList = appWhiteListMapper.selectById(id);
        if (null == appWhiteList || 1 == appWhiteList.getDelFlag()) {
            return ServiceResultConstants.WHITE_LIST_NOT_EXISITS;
        }
        if(!appWhiteList.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())){
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        appWhiteList.setUpdatedBy(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
        appWhiteList.setDelFlag(1);
        appWhiteList.setUpdatedTime(null);
        Integer result = appWhiteListMapper.updateById(appWhiteList);
        if (result > 0) {
            return ServiceResult.ok(id);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult deleteOneForever(Integer id) {
        AppWhiteList appWhiteList = appWhiteListMapper.selectById(id);
        if(appWhiteList!=null && !appWhiteList.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())){
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        Integer result = appWhiteListMapper.deleteById(id);
        if (result > 0) {
            return ServiceResult.ok(id);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult deleteBatch(Integer batchId) {
        EntityWrapper<AppWhiteList> wrapper = new EntityWrapper<>();
        wrapper.and().eq("batch_id", batchId);
        wrapper.and().eq("del_flag", 0);
        List<AppWhiteList> update = appWhiteListMapper.selectList(wrapper);
        for (AppWhiteList a : update) {
            a.setDelFlag(1);
            a.setUpdatedBy(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
            a.setUpdatedTime(null);
            appWhiteListMapper.updateById(a);
        }
        return ServiceResult.ok(batchId);
    }

    @Override
    public ServiceResult deleteBatchForever(Integer batchId) {
        EntityWrapper<AppWhiteList> wrapper = new EntityWrapper<>();
        wrapper.and().eq("batch_id", batchId);
        Integer result = appWhiteListMapper.delete(wrapper);
        if (result > 0) {
            return ServiceResult.ok(batchId);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }
}
