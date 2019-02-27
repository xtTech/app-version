package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.IosVersion;

/**
 * Created by hzlizx on 2018/6/11 0011
 */
public interface IosVersionService {

    /**
     * 创建iOS版本
     * @param iosVersion
     * @return
     */
    ServiceResult create(IosVersion iosVersion);

    /**
     * 更新iOS版本
     * @param iosVersion
     * @return
     */
    ServiceResult update(IosVersion iosVersion);

    /**
     * 删除一个版本
     * @param id
     * @return
     */
    ServiceResult delete(int id);

    /**
     * 列表
     * @param page
     * @param pageSize
     * @param wrapper
     * @return
     */
    ServiceResult list(int page, int pageSize, EntityWrapper<IosVersion> wrapper);

    /**
     * 列表
     * @param page
     * @param pageSize
     * @param wrapper
     * @return
     */
    ServiceResult listSort(int page, int pageSize, EntityWrapper<IosVersion> wrapper);

    /**
     * 根据条件获取两版本之间的所有版本
     * @param version1
     * @param version2
     * @param wrapper       查询条件
     * @return
     */
    ServiceResult findBetweenVersionList(String version1, String version2, EntityWrapper<IosVersion> wrapper);

    /**
     * 列出当前所有版本
     * @return
     */
    ServiceResult listAllVersion();

    /**
     * 上架iOS
     * @param id
     * @return
     */
    ServiceResult delivery(int id);

    /**
     * 下架iOS
     * @param id
     * @return
     */
    ServiceResult undelivery(int id);

    /**
     * 获取一个版本
     * @param id
     * @return
     */
    ServiceResult get(int id);

}
