package com.tairanchina.csp.avm.controller;

import com.tairanchina.csp.avm.entity.App;
import com.tairanchina.csp.avm.entity.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AdminControllerTest extends BaseControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(AdminControllerTest.class);

    private String uri = "/admin/";
    private String url = "";

    @Test
    public void isAdmin() throws Exception {
        url = uri + "isAdmin";
        String contentAsString = get(url);
        logger.info(contentAsString);
    }

    @Test
    public void listUser() throws Exception {
        Integer admin = 0;
        String phone = "";
        url = uri + "user/list?admin=" + admin + "&phone=" + phone;
        String contentAsString = get(url);
        logger.info(contentAsString);

        admin = 1;
        url = uri + "user/list?admin=" + admin + "&phone=" + phone;
        contentAsString = get(url);
        logger.info(contentAsString);

        phone = "1";
        url = uri + "user/list?admin=" + admin + "&phone=" + phone;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

    @Test
    public void listApp() throws Exception {
        String appName = "syl";
        url = uri + "app/list?appName=" + appName;
        String contentAsString = get(url);
        logger.info(contentAsString);

        appName = "";
        url = uri + "app/list?appName=" + appName;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

    @Test
    public void app() throws Exception {
        Integer id = 1;
        url = uri + "app/" + id;
        String contentAsString = get(url);
        logger.info(contentAsString);

        id = 0;
        url = uri + "app/" + id;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

    @Test
    public void listAppWithBindInfo() throws Exception {
        String uerId = "b9e980c1495e4d0582c257901d86b4ff";
        url = uri + "app/list/bind?userId=" + uerId;
        String contentAsString = get(url);
        logger.info(contentAsString);

        uerId = "";
        url = uri + "app/list/bind?userId=" + uerId;
        contentAsString = get(url);
        logger.info(contentAsString);
    }

    @Test
    public void createApp() throws Exception {
        App app = new App();
        url = uri + "app";
        String contentAsString = post(url, app);
        logger.info(contentAsString);

        app.setTenantAppId("testTenantAppId");
        contentAsString = post(url, app);
        logger.info(contentAsString);

        app.setAppName("testAPP");
        app.setTenantAppId("");
        contentAsString = post(url, app);
        logger.info(contentAsString);

        app.setAppName("testAPP");
        app.setTenantAppId("testTenantAppId");
        contentAsString = post(url, app);
        logger.info(contentAsString);

        app.setAppName("dfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        app.setTenantAppId("testTenantAppId");
        contentAsString = post(url, app);
        logger.info(contentAsString);

        app.setAppName("testAPP");
        app.setTenantAppId("dfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        contentAsString = post(url, app);
        logger.info(contentAsString);

        assert contentAsString.length() > 0;
    }

    @Test
    public void editApp() throws Exception {
        App app = new App();
        Integer id = 0;
        url = uri + "app/" + id;
        String contentAsString = put(url, app);
        logger.info(contentAsString);

        app.setAppName("");
        app.setTenantAppId("testTenantAppId");
        url = uri + "app/" + id;
        contentAsString = put(url, app);
        logger.info(contentAsString);

        app.setAppName("testAPP");
        app.setTenantAppId("");
        url = uri + "app/" + id;
        contentAsString = put(url, app);
        logger.info(contentAsString);

        app.setAppName("testAPP");
        app.setTenantAppId("testTenantAppId");
        url = uri + "app/" + id;
        contentAsString = put(url, app);
        logger.info(contentAsString);

        id = 24;
        url = uri + "app/" + id;
        contentAsString = put(url, app);
        logger.info(contentAsString);

        app.setAppName("dfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        app.setTenantAppId("testTenantAppId");
        url = uri + "app/" + id;
        contentAsString = put(url, app);
        logger.info(contentAsString);

        app.setAppName("testAPP");
        app.setTenantAppId("dfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfdfsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        url = uri + "app/" + id;
        contentAsString = put(url, app);
        logger.info(contentAsString);

        app.setAppName("testAPP");
        app.setTenantAppId("testTenantAppId");
        url = uri + "app/" + id;
        contentAsString = put(url, app);
        logger.info(contentAsString);
    }

    @Test
    public void deleteApp() throws Exception {
        Integer appId = 24;
        url = uri + "app/" + appId;
        String contentAsString = delete(url);
        logger.info(contentAsString);

        appId = 0;
        url = uri + "app/" + appId;
        contentAsString = delete(url);
        logger.info(contentAsString);
    }

    @Test
    public void bind() throws Exception {
        Integer appId = 0;
        String userId = " ";
        url = uri + "/" + userId + "/" + appId + "/bind";
        String contentAsString = put(url, null);
        logger.info(contentAsString);

        appId = 24;
        url = uri + "/" + userId + "/" + appId + "/bind";
        contentAsString = put(url, null);
        logger.info(contentAsString);

        userId = "ttt";
        url = uri + "/" + userId + "/" + appId + "/bind";
        contentAsString = put(url, null);
        logger.info(contentAsString);

        appId = 0;
        url = uri + "/" + userId + "/" + appId + "/bind";
        contentAsString = put(url, null);
        logger.info(contentAsString);

        appId = 24;
        userId = "ttt";
        url = uri + "/" + userId + "/" + appId + "/bind";
        contentAsString = put(url, null);
        logger.info(contentAsString);
    }

    @Test
    public void listBindApp() throws Exception {
        String userId = "b9e980c1495e4d0582c257901d86b4ff";
        url = uri + "app/list/only/bind?userId=" + userId;
        String contentAsString = get(url);
        logger.info(contentAsString);

        userId = "";
        url = uri + "app/list/only/bind?userId=" + userId;
        contentAsString = get(url);
        logger.info(contentAsString);

        assert contentAsString.length() > 0;
    }

    @Test
    public void unBind() throws Exception {
        Integer appId = 0;
        String userId = " ";
        url = uri + userId + "/" + appId + "/unBind";
        String contentAsString = put(url, null);
        logger.info(contentAsString);

        userId = "b9e980c1495e4d0582c257901d86b4ff";
        url = uri + userId + "/" + appId + "/unBind";
        contentAsString = put(url, null);
        logger.info(contentAsString);

        appId = 24;
        url = uri + userId + "/" + appId + "/unBind";
        contentAsString = put(url, null);
        logger.info(contentAsString);
    }

    @Test
    public void changeNickName() throws Exception {
        String userId = "";
        String nickName = "";

        User user = new User();
        user.setUserId(userId);
        user.setNickName(nickName);
        url = uri + "user";
        String contentAsString = put(url, user);
        logger.info(contentAsString);

        nickName = "tttt";
        user.setNickName(nickName);
        contentAsString = put(url, user);
        logger.info(contentAsString);

        userId = "a2d452868a8b46348706bcb987e51935";
        user.setUserId(userId);
        contentAsString = put(url, user);
        logger.info(contentAsString);

        userId = "a9e885e153b04fc095ed805c468e2d40";
        nickName = "ttffffffffffftt";
        user.setNickName(nickName);
        user.setUserId(userId);
        contentAsString = put(url, user);
        logger.info(contentAsString);

        assert contentAsString.length() > 0;
    }

}