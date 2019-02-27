package com.tairanchina.csp.avm.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.CustomApi;
import com.tairanchina.csp.avm.enums.ChatBotEventType;
import com.tairanchina.csp.avm.mapper.CustomApiMapper;
import com.tairanchina.csp.avm.service.BasicService;
import com.tairanchina.csp.avm.service.ChatBotService;
import com.tairanchina.csp.avm.service.CustomApiService;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomApiServiceImpl implements CustomApiService {

    @Autowired
    private BasicService basicService;

    @Autowired
    private CustomApiMapper customApiMapper;

    @Autowired
    private ChatBotService chatBotService;

    @Override
    public ServiceResult createCustomApi(CustomApi customApi) {
        customApi.setCreatedBy(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
        customApi.setAppId(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        Integer result = customApiMapper.insert(customApi);
        if (result > 0) {
            chatBotService.sendMarkdown(ChatBotEventType.CUSTOM_API_CREATED, "创建自定义接口提醒", makeMarkdown(customApi));
            return ServiceResult.ok(customApi);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult updateCustomApi(CustomApi customApi) {
        CustomApi checkExisit = customApiMapper.selectById(customApi.getId());
        if (null == checkExisit) {
            return ServiceResultConstants.CUSTOM_API_NOT_EXISTS;
        }
        if (!checkExisit.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        customApi.setUpdatedBy(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
        customApi.setUpdatedTime(null);
        Integer result = customApiMapper.updateById(customApi);
        if (result > 0) {
            return ServiceResult.ok(customApi);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult deleteCustomApi(int id) {
        CustomApi customApi = customApiMapper.selectById(id);
        if (null == customApi) {
            return ServiceResultConstants.CUSTOM_API_NOT_EXISTS;
        }
        if (!customApi.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        customApi.setDelFlag(1);
        customApi.setUpdatedBy(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
        customApi.setUpdatedTime(null);
        Integer result = customApiMapper.updateById(customApi);
        if (result > 0) {
            return ServiceResult.ok(customApi);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult deleteCustomApiForver(int id) {
        CustomApi customApi = customApiMapper.selectById(id);
        if (null == customApi) {
            return ServiceResultConstants.CUSTOM_API_NOT_EXISTS;
        }
        if (!customApi.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        Integer result = customApiMapper.deleteById(id);
        if (result > 0) {
            return ServiceResult.ok(customApi);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult getCustomApiByOne(CustomApi customApi) {
        CustomApi result = customApiMapper.selectOne(customApi);
        if (result == null) {
            return ServiceResultConstants.CUSTOM_API_NOT_EXISTS;
        }
        if (!result.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        return ServiceResult.ok(result);
    }

    @Override
    public ServiceResult getCustomApiByKeyAndAppId(String customKey) {
        CustomApi customApi = new CustomApi();
        customApi.setCustomKey(customKey);
        customApi.setAppId(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        customApi.setDelFlag(0);
        CustomApi result = customApiMapper.selectOne(customApi);
        if (result == null) {
            return ServiceResultConstants.CUSTOM_API_NOT_EXISTS;
        }
        if (!result.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        return ServiceResult.ok(result);
    }

    @Override
    public ServiceResult list(int page, int pageSize, EntityWrapper<CustomApi> wrapper) {
        Page<CustomApi> pageEntity = new Page<>();
        pageEntity.setSize(pageSize);
        pageEntity.setCurrent(page);
        pageEntity.setRecords(customApiMapper.selectPage(pageEntity, wrapper));
        basicService.formatCreatedBy(pageEntity.getRecords());
        return ServiceResult.ok(pageEntity);
    }

    private String makeMarkdown(CustomApi customApi) {
        StringBuilder sb = new StringBuilder();
        sb.append("#### 发布了自定义接口[ " + customApi.getCustomName() + " ]\n\n");
        sb.append("> **接口地址** ：" + customApi.getCustomKey() + "\n\n");

        Integer customStatus = customApi.getCustomStatus();
        switch (customStatus) {
            case 0:
                sb.append("> **接口状态** ：关闭\n\n");
                break;
            case 1:
                sb.append("> **接口状态** ：线上开启\n\n");
                break;
            case 2:
                sb.append("> **接口状态** ：测试需要\n\n");
                break;
        }
        ArrayList arrayList = new ArrayList<>();
        String androidRange = null;
        String iosRange = null;
        if (customApi.getAndroidEnabled() == 1) {
            arrayList.add("Android");
            androidRange = customApi.getAndroidMin() + " - " + customApi.getAndroidMax();
        }

        if (customApi.getIosEnabled() == 1) {
            arrayList.add("iOS");
            iosRange = customApi.getIosMin() + " - " + customApi.getIosMax();
        }
        sb.append("> **开启设备** ：" + StringUtils.join(arrayList, ',') + "\n\n");
        if (StringUtils.isNotEmpty(androidRange)) {
            sb.append("> **Android范围** ：" + androidRange + "\n\n");
        }
        if (StringUtils.isNotEmpty(iosRange)) {
            sb.append("> **iOS范围** ：" + iosRange + "\n\n");
        }
        return sb.toString();
    }
}
