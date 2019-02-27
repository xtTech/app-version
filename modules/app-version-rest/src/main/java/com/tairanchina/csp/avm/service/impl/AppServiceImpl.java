package com.tairanchina.csp.avm.service.impl;

import com.tairanchina.csp.avm.constants.RedisKey;
import com.tairanchina.csp.avm.entity.App;
import com.tairanchina.csp.avm.mapper.AppMapper;
import com.tairanchina.csp.avm.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by hzlizx on 2018/6/25 0025
 */
@Service
public class AppServiceImpl implements AppService {

    private static final Logger logger = LoggerFactory.getLogger(AndroidVersionServiceImpl.class);

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public App findAppByTenantAppId(String tenantAppId) {
        App appSelected;
        logger.debug("[app]查询缓存...");
        BoundHashOperations operations = redisTemplate.boundHashOps(RedisKey.APP_HASH);
        try {
            appSelected = (App) operations.get(tenantAppId);
            if(appSelected==null){
                logger.debug("[app]缓存不存在该数据，查询数据库...");
                App app = new App();
                app.setTenantAppId(tenantAppId);
                app.setDelFlag(0);
                appSelected = appMapper.selectOne(app);
                if (appSelected != null) {
                    logger.debug("[app]命中数据库");
                    operations.put(tenantAppId, appSelected);
                }
            } else {
                logger.debug("[app]命中缓存");
            }
            return appSelected;
        } catch (Exception e) {
            logger.error("查找缓存出错",e);logger.debug("[app]缓存不存在该数据，查询数据库...");
            App app = new App();
            app.setTenantAppId(tenantAppId);
            app.setDelFlag(0);
            appSelected = appMapper.selectOne(app);
            if (appSelected != null) {
                logger.debug("[app]命中数据库");
                operations.put(tenantAppId, appSelected);
            }
            return appSelected;
        }
    }
}
