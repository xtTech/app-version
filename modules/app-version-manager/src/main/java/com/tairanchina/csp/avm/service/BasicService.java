package com.tairanchina.csp.avm.service;


import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.AndroidVersion;
import com.tairanchina.csp.avm.entity.BasicEntity;
import com.tairanchina.csp.avm.entity.IosVersion;
import com.tairanchina.csp.avm.entity.RnPackage;

import java.util.List;

/**
 * Created by hzlizx on 2018/6/11 0011
 */
public interface BasicService {

    /**
     * 将createdBy中的userId转换为用户名
     *
     * @param source
     */
    void formatCreatedBy(List<? extends BasicEntity> source);


    /**
     * 将createdBy中的userId转换为用户名
     *
     * @param basicEntity
     */
    void formatCreatedBy(BasicEntity basicEntity);

    /**
     * 检查版本号大小
     */
    ServiceResult checkVersion(Object o);

    /**
     *比较版本大小
     * 前者大则返回一个正数,后者大返回一个负数,相等则返回0
     */
    int compareVersion(String version1, String version2);

    /**
     * 根据版本号list排序
     * @param source list对象
     * @param isLowToHigh 是否顺序排序
     * @return
     */
    List<AndroidVersion> sortAndroidVersion(List<AndroidVersion> source, boolean isLowToHigh);

    List<IosVersion> sortIosVersion(List<IosVersion> source, boolean isLowToHigh);

    List<RnPackage> sortRnPackageVersion(List<RnPackage> source, boolean isLowToHigh);
}
