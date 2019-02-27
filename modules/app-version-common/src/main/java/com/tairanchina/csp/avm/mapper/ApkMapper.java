package com.tairanchina.csp.avm.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.tairanchina.csp.avm.dto.ApkExt;
import com.tairanchina.csp.avm.entity.Apk;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by hzlizx on 2018/5/17 0017
 */
@Mapper
public interface ApkMapper extends BaseMapper<Apk> {

    List<ApkExt> selectApkWithChannelCode(Pagination page , @Param("versionId") Integer versionId, @Param("channelCode") String channelCode, @Param("md5") String md5, @Param("deliveryStatus") Integer deliveryStatus);

}
