package com.tairanchina.csp.avm.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ecfront.dew.common.$;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.App;
import com.tairanchina.csp.avm.entity.Channel;
import com.tairanchina.csp.avm.entity.UserAppRel;
import com.tairanchina.csp.avm.mapper.AppMapper;
import com.tairanchina.csp.avm.mapper.ChannelMapper;
import com.tairanchina.csp.avm.mapper.UserAppRelMapper;
import com.tairanchina.csp.avm.service.AdminService;
import com.tairanchina.csp.avm.service.AppService;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hzlizx on 2018/6/8 0008
 */
@Service
public class AppServiceImpl implements AppService{
    private static final Logger logger = LoggerFactory.getLogger(AppServiceImpl.class);

    @Autowired
    private UserAppRelMapper userAppRelMapper;

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ChannelMapper channelMapper;

    @Override
    public ServiceResult getAppListWithUserId(int page, int pageSize, String userId) {
        EntityWrapper<UserAppRel> entityWrapper = new EntityWrapper<>();
        entityWrapper.and().eq("user_id", userId);
        Page<UserAppRel> userAppRelPage = new Page<>();
        userAppRelPage.setCurrent(page);
        userAppRelPage.setSize(pageSize);
        userAppRelPage.setRecords(userAppRelMapper.selectPage(userAppRelPage, entityWrapper));
        List<App> collect = userAppRelPage.getRecords().stream().map(mapper -> {
            Integer appId = mapper.getAppId();
            App app = new App();
            app.setId(appId);
            app.setDelFlag(0);
            return appMapper.selectOne(app);
        }).collect(Collectors.toList());

        collect.removeAll(Collections.singleton(null));
        Page<App> appPage = new Page<>();
        try {
            $.bean.copyProperties(appPage, userAppRelPage);
            appPage.setRecords(collect);
            return ServiceResult.ok(appPage);
        } catch (InvocationTargetException | IllegalAccessException e) {
            logger.error("App参数转换出错", e);
        }
        return ServiceResultConstants.ERROR_500;
    }

    @Override
    @Transactional
    public ServiceResult createApp(String appName, String tenantAppId) {
        EntityWrapper<App> wrapper = new EntityWrapper<>();
        wrapper.andNew().eq("app_name", appName).or().eq("tenant_app_id", tenantAppId);
        wrapper.andNew().eq("del_flag", 0);
        if (!appMapper.selectList(wrapper).isEmpty()) {
            return ServiceResultConstants.TENANT_APP_ID_OR_APP_NAME_EXISTS;
        }
        App app = new App();
        app.setDelFlag(0);
        app.setTenantAppId(tenantAppId);
        app.setAppName(appName);
        app.setCreatedBy(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
        Integer insert = appMapper.insert(app);

        UserAppRel userAppRel = new UserAppRel();
        userAppRel.setAppId(app.getId());
        userAppRel.setUserId(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
        Integer insert2 = userAppRelMapper.insert(userAppRel);
        logger.info("创建App[{}]成功", appName);
        //创建官方渠道
        Channel channel = new Channel();
        channel.setAppId(app.getId());
        channel.setCreatedBy(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
        channel.setChannelCode("official");
        channel.setChannelName("官方渠道");
        channel.setChannelStatus(1);
        Integer insert1 = channelMapper.insert(channel);
        if (insert > 0 && insert2>0 && insert1>0) {
            return ServiceResult.ok(app);
        } else {
            logger.error("创建App[{}]失败", appName);
            throw new RuntimeException("创建App失败");
        }
    }

    @Override
    public ServiceResult getMyApp() {
        return adminService.listBindApp(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
    }
}
