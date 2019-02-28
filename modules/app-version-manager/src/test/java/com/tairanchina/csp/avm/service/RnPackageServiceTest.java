package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.RnPackage;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


public class RnPackageServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(RnPackageServiceTest.class);

    @Autowired
    private RnPackageService rnPackageService;

    @Autowired
    private BasicService basicService;

    @Test
    public void create() throws Exception {
        RnPackage rnPackage = new RnPackage();
        rnPackage.setAppId(24);
        rnPackage.setRnType(1);
        rnPackage.setVersionMax("2.0.0.0");
        rnPackage.setVersionMin("2.0.0.0");
        rnPackage.setRnName("rnname");
        rnPackage.setRnNickName("nickname");
        rnPackage.setRnVersion("rnversion");
        rnPackage.setRnUpdateLog("log");
//        ServiceResult serviceResult = basicService.checkVersion(rnPackage);

        ServiceResult result = rnPackageService.create(rnPackage);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

    }

    @Test
    public void delete() throws Exception {
        Integer id = 17;
        ServiceResult result = rnPackageService.delete(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 3;
        result = rnPackageService.delete(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void update() throws Exception {
        RnPackage rnPackage = new RnPackage();
        rnPackage.setAppId(24);
        rnPackage.setRnType(1);
        rnPackage.setVersionMax("2.0.0.0");
        rnPackage.setVersionMin("2.0.0.0");
        rnPackage.setRnName("update");
        rnPackage.setRnNickName("nickname");
        rnPackage.setRnVersion("rnversion");
        rnPackage.setRnUpdateLog("log");
        ServiceResult result = rnPackageService.update(rnPackage);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        rnPackage.setId(null);
        result = rnPackageService.update(rnPackage);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        rnPackage.setId(0);
        result = rnPackageService.update(rnPackage);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        rnPackage.setId(17);
        result = rnPackageService.update(rnPackage);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        rnPackage.setAppId(333);
        result = rnPackageService.update(rnPackage);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

    }

    @Test
    public void list() throws Exception {
        String routeName = "";
        String routeKey = "";
        String routeValue = "";
        EntityWrapper<RnPackage> wrapper = new EntityWrapper<>();
        wrapper.and().eq("del_flag", 0);
        if (StringUtils.hasLength(routeName)) {
            wrapper.and().like("route_name", "%" + routeName + "%");
        }
        if (StringUtils.hasLength(routeKey)) {
            wrapper.and().like("route_key", "%" + routeKey + "%");
        }

        if (StringUtils.hasLength(routeValue)) {
            wrapper.and().like("route_value", "%" + routeValue + "%");
        }

        wrapper.orderBy("created_time", false);
        ServiceResult result = rnPackageService.list(1, 10, wrapper);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void find() throws Exception {
        Integer id = 17;
        ServiceResult result = rnPackageService.find(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 3333;
        result = rnPackageService.find(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

}