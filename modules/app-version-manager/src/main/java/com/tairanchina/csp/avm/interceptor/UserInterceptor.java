package com.tairanchina.csp.avm.interceptor;

import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.entity.LoginInfo;
import com.tairanchina.csp.avm.entity.User;
import com.tairanchina.csp.avm.service.UserService;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import com.tairanchina.csp.avm.dto.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户登录拦截器
 * Created by hzlizx on 2018/4/25 0025
 */
@Component
@PropertySource("classpath:application.yml")
public class UserInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(UserInterceptor.class);

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Access-Control-Allow-Origin, Authorization, appId, serviceId");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            this.print(response, ServiceResult.failed(1003, "没有权限访问该地址，请先登录").toString());
            return false;
        }
        if (authorization.startsWith("Bearer ")) {
            String substring = authorization.substring(7);
            try {
                ServiceResult validate = userService.validate(substring);
                if (validate.getCode() == 200) {
                    Integer appId = null;
                    String appIdFromHeader = request.getHeader("appId");
                    if (StringUtils.isNotBlank(appIdFromHeader)) {
                        try {
                            appId = Integer.valueOf(appIdFromHeader);
                        } catch (NumberFormatException e) {
                            logger.error("AppID转换错误", e);
                        }
                    }
                    User userInDb = (User) validate.getData();
                    String userId = userInDb.getUserId();
                    String nickName = userInDb.getNickName();
                    String phone = userInDb.getPhone();
                    boolean isAdmin = userInDb.getIsAdmin() == 1;
                    logger.info("appId = {},userId = {}", appId, userId);
                    logger.info("method = {},requestUri = {}", request.getMethod(), request.getRequestURI() + "?" + request.getQueryString());
                    logger.info("用户名：[{}]，手机号：[{}]", nickName, phone);
                    LoginInfo loginInfo = new LoginInfo(appId, userId, isAdmin);
                    loginInfo.setNickName(nickName);
                    loginInfo.setPhone(phone);
                    ThreadLocalUtils.USER_THREAD_LOCAL.set(loginInfo);
                    return true;
                } else {
                    this.print(response, validate.toString());
                }
            } catch (Exception e) {
                logger.info("检测授权失败：{}", e.getMessage());
                this.print(response, ServiceResult.failed(1002, e.getMessage()).toString());
            }
        } else {
            logger.info("没有找到用户信息");
            this.print(response, ServiceResultConstants.NO_LOGIN.toString());
        }
        return false;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtils.USER_THREAD_LOCAL.remove();
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
