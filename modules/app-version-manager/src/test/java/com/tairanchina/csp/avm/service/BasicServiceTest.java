package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.AndroidVersion;
import com.tairanchina.csp.avm.entity.BasicEntity;
import com.tairanchina.csp.avm.entity.CustomApi;
import com.tairanchina.csp.avm.mapper.AndroidVersionMapper;
import com.tairanchina.csp.avm.mapper.CustomApiMapper;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class BasicServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(BasicServiceTest.class);

    @Autowired
    BasicService basicService;

    @Autowired
    CustomApiMapper customApiMapper;

    @Autowired
    AndroidVersionMapper androidVersionMapper;

    @Test
    public void formatCreatedBy() throws Exception {
        EntityWrapper<CustomApi> wrapper = new EntityWrapper<>();
        wrapper.and().eq("app_id", ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        wrapper.andNew().eq("id", 48);
        wrapper.andNew().eq("del_flag", 0);
        wrapper.orderBy("created_time", false);

        Page<CustomApi> pageEntity = new Page<>();
        pageEntity.setSize(10);
        pageEntity.setCurrent(1);
        pageEntity.setRecords(customApiMapper.selectPage(pageEntity, wrapper));
        logger.info("formatCreatedBy前：" + pageEntity.getRecords().toString());
        basicService.formatCreatedBy(pageEntity.getRecords());
        logger.info("formatCreatedBy后：" + pageEntity.getRecords().toString());
    }

    @Test
    public void formatCreatedByBasicEntity() throws Exception {
        BasicEntity basicEntity = new BasicEntity();
        basicEntity.setCreatedBy("b9e980c1495e4d0582c257901d86b4ff");
        logger.info("formatCreatedBy前：" + basicEntity.getCreatedBy());
        basicService.formatCreatedBy(basicEntity);
        logger.info("formatCreatedBy后：" + basicEntity.getCreatedBy());
    }

    @Test
    public void checkVersion() throws Exception {
        CustomApi customApi = new CustomApi();
        customApi.setCustomName("test");
        customApi.setAndroidEnabled(1);
        customApi.setAndroidMin("2.0.0");
        customApi.setAndroidMax("2.0.0");
        customApi.setCustomContent("content");
        customApi.setCustomKey("key");
        ServiceResult result = basicService.checkVersion(customApi);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void sortAndroidVersion() throws Exception{
        EntityWrapper<AndroidVersion> androidVersionEntityWrapper = new EntityWrapper<>();
        androidVersionEntityWrapper.and().eq("app_id", 24);
        androidVersionEntityWrapper.and().eq("del_flag", 0);
        androidVersionEntityWrapper.orderBy("app_version", false);

        List<AndroidVersion> list  = androidVersionMapper.selectList(androidVersionEntityWrapper);
        List<AndroidVersion> result = basicService.sortAndroidVersion(list,true);
        System.out.println(result);
    }

}