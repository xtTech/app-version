package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.dto.ServiceResult;

/**
 * Created by hzlizx on 2018/6/22 0022
 */
public interface RnService {
    /**
     * 获取Rn路由列表
     * @param version       版本号
     * @param appId         应用
     * @param platform      平台（ios\android）
     * @param routeStatus   路由状态 0:关闭 1:线上开启 2:测试需要
     * @return              路由列表
     */
    ServiceResult route(String version,String appId,String platform, int routeStatus);

    /**
     * 获取Rn包信息
     * @param version   版本号
     * @param appId     应用
     * @param platform  平台（ios\android）
     * @param rnStatus  路由状态 0:关闭 1:线上开启 2:测试需要
     * @return          包列表
     */
    ServiceResult bundles(String version,String appId,String platform, int rnStatus);
}
