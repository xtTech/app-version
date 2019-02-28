package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.App;
import com.tairanchina.csp.avm.entity.LoginInfo;
import com.tairanchina.csp.avm.entity.User;
import com.tairanchina.csp.avm.mapper.AppMapper;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


public class AdminServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceTest.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private AppMapper appMapper;


    @Test
    public void bindUserAndApp() throws Exception {
        String userId = "test";
        int appId = 31;
        ServiceResult result = adminService.bindUserAndApp(userId, appId);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        appId = 44444444;
        result = adminService.bindUserAndApp(userId, appId);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void unbindUserAndApp() throws Exception {
        String userId = "test";
        int appId = 31;
        ServiceResult result = adminService.unbindUserAndApp(userId, appId);
        if (result.getData() != null) {

            logger.info(result.getData().toString());

        }//todo ?  用户和APP未绑定，调用方法提示 {"code":10002,"message":"数据库插入错误","data":null}
    }


    @Test
    public void createApp() throws Exception {
        App app = new App();
        app.setAppName("testAPP");
        app.setTenantAppId("testTenantAppId");
        ServiceResult result = adminService.createApp(app.getAppName(), app.getTenantAppId());
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void editApp() throws Exception {
        App app = new App();
        app.setAppName("testEditAPP");
        app.setTenantAppId("testEditTenantAppId");
        ServiceResult result = adminService.editApp(app.getId(), app.getAppName(), app.getTenantAppId());
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        } //todo ?  如果要编辑的APP不存在，调用方法提示 {"code":10002,"message":"数据库插入错误","data":null}
    }

    @Test
    public void deleteApp() throws Exception {
        Integer appId = 24;
        ServiceResult result = adminService.deleteApp(appId);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void getApp() throws Exception {
        Integer id = 24;
        ServiceResult result = adminService.getApp(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void deleteAppForever() throws Exception {
//        Integer appId = 24;
        Integer appId = 20;
        ServiceResult result = adminService.deleteAppForever(appId);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void listApp() throws Exception {
        String appName = "syl";
        EntityWrapper<App> wrapper = new EntityWrapper<>();
        wrapper.and().eq("del_flag", 0);
        if (StringUtils.hasLength(appName)) {
            wrapper.andNew().like("app_name", "%" + appName + "%");
        }
        ServiceResult result = adminService.listApp(1, 10, wrapper);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void listAppWithBindInfo() throws Exception {
        EntityWrapper<App> wrapper = new EntityWrapper<>();
        String userId = "b9e980c1495e4d0582c257901d86b4ff";
        wrapper.and().eq("del_flag", 0);
        ServiceResult result = adminService.listAppWithBindInfo(1, 10, wrapper, userId);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void listBindApp() throws Exception {
        String userId = "b9e980c1495e4d0582c257901d86b4ff";
        ServiceResult result = adminService.listBindApp(userId);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void listUser() throws Exception {
        Integer admin = 1;
        String phone = "15670950484";
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        if (admin == 1) {
            wrapper.and().eq("is_admin", 1);
        }
        if (StringUtils.hasLength(phone)) {
            wrapper.and().like("phone", "%" + phone + "%");
        }
        ServiceResult result = adminService.listUser(1, 10, wrapper);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void isAdmin() throws Exception {
        LoginInfo loginInfo = ThreadLocalUtils.USER_THREAD_LOCAL.get();
        ServiceResult result = adminService.isAdmin(loginInfo.getUserId());
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

}