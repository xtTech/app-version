package com.tairanchina.csp.avm.interceptor;

import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.entity.App;
import com.tairanchina.csp.avm.entity.LoginInfo;
import com.tairanchina.csp.avm.entity.UserAppRel;
import com.tairanchina.csp.avm.mapper.AppMapper;
import com.tairanchina.csp.avm.mapper.UserAppRelMapper;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 管理员拦截器
 * Created by hzlizx on 2018/4/25 0025
 */
@Component
public class AppInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AppInterceptor.class);

    @Autowired
    private UserAppRelMapper userAppRelMapper;

    @Autowired
    private AppMapper appMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LoginInfo loginInfo = ThreadLocalUtils.USER_THREAD_LOCAL.get();
        String userId = loginInfo.getUserId();
        Integer appId = ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId();
        UserAppRel userAppRel = new UserAppRel();
        userAppRel.setAppId(appId);
        userAppRel.setUserId(userId);
        if(appId==null){
            this.print(response, ServiceResultConstants.NO_APP_ID.toString());
            return false;
        }
        logger.info("appId={},userId={}", appId, userId);
        UserAppRel userAppRel1 = userAppRelMapper.selectOne(userAppRel);
        if (userAppRel1 != null) {
            //查询该APP是否可用
            App app = appMapper.selectById(appId);
            if (app != null) {
                if (app.getDelFlag() == 0) {
                    return true;
                } else {
                    this.print(response, ServiceResultConstants.APP_DELETED.toString());
                    return false;
                }
            } else {
                this.print(response, ServiceResultConstants.APP_NOT_EXISTS.toString());
                return false;
            }
        } else {
            this.print(response, ServiceResultConstants.NO_APP_PM.toString());
            return false;
        }
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    private void print(HttpServletResponse response, String json) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            response.getWriter().print(json);
        } catch (IOException e) {
            logger.error("打印出错", e);
        }
    }
}
