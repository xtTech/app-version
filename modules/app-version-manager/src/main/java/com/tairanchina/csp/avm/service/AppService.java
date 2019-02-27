package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.dto.ServiceResult;

/**
 * Created by hzlizx on 2018/6/8 0008
 */
public interface AppService {

    /**
     * 获取指定用户绑定的应用列表
     * @param page          页码
     * @param pageSize      页码大小
     * @param userId        用户ID
     * @return              列表
     */
    ServiceResult getAppListWithUserId(int page, int pageSize, String userId);

    /**
     * 创建一个应用
     * @param appName   应用名称
     * @return          ServiceResult（有应用ID）
     */
    ServiceResult createApp(String appName,String tenantAppId);

    /**
     * 获取当前登录用户绑定的应用列表
     * @return              列表
     */
    ServiceResult getMyApp();
}
