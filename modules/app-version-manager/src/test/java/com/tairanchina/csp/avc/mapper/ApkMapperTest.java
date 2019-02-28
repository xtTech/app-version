package com.tairanchina.csp.avc.mapper;


import com.baomidou.mybatisplus.plugins.Page;
import com.tairanchina.csp.avm.dto.ApkExt;
import com.tairanchina.csp.avm.mapper.ApkMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ApkMapperTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(ApkMapperTest.class);

    @Autowired
    ApkMapper apkMapper;

    @Test
    public void selectApkWithChannelCode() throws Exception {
        Integer versionId = 14;
        String channelCode = "";
        String md5 = "";
        Integer deliveryStatus = null;
        Page<ApkExt> apkPage = new Page<>();
        apkPage.setCurrent(1);
        apkPage.setSize(10);
        List<ApkExt> apkExts = apkMapper.selectApkWithChannelCode(apkPage, versionId, channelCode, md5, deliveryStatus);
        logger.info("apkExts..." + apkExts.size());

        apkExts = apkMapper.selectApkWithChannelCode(apkPage, null, "", "", null);
        logger.info("apkExts..." + apkExts.size());
        assert apkExts.size() > 0;
    }

}