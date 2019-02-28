package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.RnRoute;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


public class RnRouteServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(RnRouteServiceTest.class);

    @Autowired
    private RnRouteService rnRouteService;

    @Autowired
    private BasicService basicService;

    @Test
    public void create() throws Exception {
        RnRoute rnRoute = new RnRoute();
        rnRoute.setAndroidEnabled(1);
        rnRoute.setAndroidMax("2.0.0.0");
        rnRoute.setAndroidMin("2.0.0.0");
        rnRoute.setAppId(24);
        rnRoute.setRouteKey("key");
        rnRoute.setRouteValue("value");
        rnRoute.setRouteName("name");
        rnRoute.setRouteStatus(1);
        ServiceResult serviceResult = basicService.checkVersion(rnRoute);
        if (serviceResult.getCode() != 200) {
            logger.info("code is not 200..............");
        } else {
            ServiceResult result = rnRouteService.create(rnRoute);
            if (result.getData() != null) {
                logger.info(result.getData().toString());
            }
        }
    }

    @Test
    public void delete() throws Exception {
        Integer id = 17;
        ServiceResult result = rnRouteService.delete(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 9;
        result = rnRouteService.delete(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 999999;
        result = rnRouteService.delete(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void update() throws Exception {
        RnRoute rnRoute = new RnRoute();
        rnRoute.setId(17);
        rnRoute.setAndroidEnabled(1);
        rnRoute.setAndroidMax("2.0.0.1");
        rnRoute.setAndroidMin("2.0.0.0");
        rnRoute.setAppId(24);
        rnRoute.setRouteKey("key");
        rnRoute.setRouteValue("value");
        rnRoute.setRouteName("update");
        rnRoute.setRouteStatus(1);
        ServiceResult serviceResult = basicService.checkVersion(rnRoute);
        if (serviceResult.getCode() != 200) {
            logger.info("code is not 200..............");
        } else {
            ServiceResult result = rnRouteService.update(rnRoute);
            if (result.getData() != null) {
                logger.info(result.getData().toString());
            }
        }

        rnRoute.setId(null);
        ServiceResult result = rnRouteService.update(rnRoute);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        rnRoute.setId(0);
        result = rnRouteService.update(rnRoute);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        rnRoute.setId(9);
        result = rnRouteService.update(rnRoute);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        rnRoute.setId(999999);
        result = rnRouteService.update(rnRoute);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        rnRoute.setId(17);
        rnRoute.setAppId(2222);
        result = rnRouteService.update(rnRoute);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void list() throws Exception {
        String routeName = "";
        String routeKey = "";
        String routeValue = "";
        Integer routeStatus = 1;
        EntityWrapper<RnRoute> wrapper = new EntityWrapper<>();
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

        if (routeStatus != null) {
            wrapper.and().eq("route_status", routeStatus);
        }
        wrapper.orderBy("created_time", false);
        ServiceResult result = rnRouteService.list(1, 10, wrapper);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void find() throws Exception {
        Integer id = 17;
        ServiceResult result = rnRouteService.find(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 999999;
        result = rnRouteService.find(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 7;
        result = rnRouteService.find(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

}