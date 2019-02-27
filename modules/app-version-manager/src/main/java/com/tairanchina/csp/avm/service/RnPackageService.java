package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.RnPackage;

/**
 * Created by hzlizx on 2018/6/20 0020
 */
public interface RnPackageService {


    /**
     * 创建包
     * @param rnPackage
     * @return
     */
    ServiceResult create(RnPackage rnPackage);

    /**
     * 删除路由
     * @param id
     * @return
     */
    ServiceResult delete(int id);

    /**
     * 更新路由
     * @param rnPackage
     * @return
     */
    ServiceResult update(RnPackage rnPackage);

    /**
     * 列表
     * @param page
     * @param pageSize
     * @param wrapper
     * @return
     */
    ServiceResult list(int page, int pageSize, EntityWrapper<RnPackage> wrapper);

    /**
     *
     * @param page
     * @param pageSize
     * @param wrapper
     * @return
     */
    ServiceResult listSort(int page, int pageSize, EntityWrapper<RnPackage> wrapper);

    /**
     * 根据条件获取两版本之间的所有版本
     * @param version1
     * @param version2
     * @param wrapper       查询条件
     * @return
     */
    ServiceResult findBetweenVersionList(String version1, String version2, EntityWrapper<RnPackage> wrapper);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    ServiceResult find(int id);

}
