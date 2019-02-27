package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.AndroidVersion;

/**
 * 安卓应用版本管理
 * Created by hzlizx on 2018/6/6 0006
 */
public interface AndroidVersionService {

    /**
     * 创建一个安卓版本
     * @param androidVersion    安卓版本信息
     * @return                  是否成功
     */
    ServiceResult createAndroidVersion(AndroidVersion androidVersion);

    /**
     * 删除一个安卓版本（软删除）
     * @param versionId         安卓版本ID
     * @return                  是否成功
     */
    ServiceResult deleteAndroidVersion(int versionId);

    /**
     * 删除一个安卓版本（硬删除）
     * @param versionId         安卓版本ID（该版本应该已经被软删除了）
     * @return                  是否成功
     */
    ServiceResult deleteAndroidVersionForever(int versionId);

    /**
     * 更新一个安卓版本
     * @param androidVersion    带ID的android版本
     * @return                  是否成功
     */
    ServiceResult updateAndroidVersion(AndroidVersion androidVersion);

    /**
     * 列出所有的安卓版本
     * @param page          页码
     * @param pageSize      每页大小
     * @param wrapper       查询条件
     * @return              列表
     */
    ServiceResult list(int page, int pageSize, EntityWrapper<AndroidVersion> wrapper);

    /**
     * 分页排序
     * @param page          页码
     * @param pageSize      每页大小
     * @param wrapper       查询条件
     * @return              列表
     */
    ServiceResult listSort(int page, int pageSize, EntityWrapper<AndroidVersion> wrapper);

    /**
     * 根据条件获取两版本之间的所有版本
     * @param version1
     * @param version2
     * @param wrapper       查询条件
     * @return
     */
    ServiceResult findBetweenVersionList(String version1, String version2, EntityWrapper<AndroidVersion> wrapper);

    /**
     * 根据ID找到版本
     * @param id
     * @return
     */
    ServiceResult findById(int id);


    /**
     * 列出当前所有版本
     * @return
     */
    ServiceResult listAllVersion();



    /**
     * 上架
     * @param id
     * @return
     */
    ServiceResult delivery(int id);

    /**
     * 下架
     * @param id
     * @return
     */
    ServiceResult undelivery(int id);

}
