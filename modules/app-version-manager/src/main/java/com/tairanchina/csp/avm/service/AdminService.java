package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.App;
import com.tairanchina.csp.avm.entity.User;

/**
 * 管理员操作
 * 该类只有管理员可以操作
 * Created by hzlizx on 2018/6/6 0006
 */
public interface AdminService {

    /**
     * 绑定一个用户和APP，使该用户可以操作该APP的版本管理系统
     * @param userId    用户ID
     * @param appId     应用ID
     * @return          ServiceResult
     */
    ServiceResult bindUserAndApp(String userId, int appId);

    /**
     * 解绑一个用户和APP
     * @param userId    用户ID
     * @param appId     应用ID
     * @return          ServiceResult
     */
    ServiceResult unbindUserAndApp(String userId, int appId);

    /**
     * 创建一个应用
     * @param appName   应用名称
     * @return          ServiceResult（有应用ID）
     */
    ServiceResult createApp(String appName,String tenantAppId);

    /**
     * 修改一个应用
     * @param appId     应用ID
     * @param appName   应用名称
     * @return          ServiceResult
     */
    ServiceResult editApp(int appId, String appName,String tenantAppId);

    /**
     * 删除一个应用（软删除，加入回收站，防止误删，可以恢复）
     * @param appId     应用ID
     * @return          ServiceResult
     */
    ServiceResult deleteApp(int appId);

    /**
     * 得到一个应用
     * @param appId     应用ID
     * @return          ServiceResult
     */
    ServiceResult getApp(int appId);

    /**
     * 从回收站删除一个应用，永久删除，无法恢复，且删除所有版本信息和APK信息
     * @param appId     应用ID
     * @return          ServiceResult
     */
    ServiceResult deleteAppForever(int appId);

    /**
     * 列出所有APP
     * @param page      页码
     * @param pageSize  页码大小
     * @param wrapper   查询条件
     * @return          列表
     */
    ServiceResult listApp(int page, int pageSize, EntityWrapper<App> wrapper);

    /**
     * 列出所有APP（带绑定信息）
     * @param page      页码
     * @param pageSize  页码大小
     * @param wrapper   查询条件
     * @return          列表
     */
    ServiceResult listAppWithBindInfo(int page, int pageSize, EntityWrapper<App> wrapper, String userId);

    /**
     * 列出某用户所有绑定的APP
     * @return          列表
     */
    ServiceResult listBindApp(String userId);

    /**
     * 列出所有登录过的用户
     * @param page      页码
     * @param pageSize  页码大小
     * @param wrapper   查询条件
     * @return          列表
     */
    ServiceResult listUser(int page, int pageSize, EntityWrapper<User> wrapper);

    /**
     * 判断user是否为管理员
     * @param userId    用户ID
     * @return          是否为管理员
     */
    ServiceResult isAdmin(String userId);
}
