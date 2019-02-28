package com.tairanchina.csp.avm.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ecfront.dew.common.$;
import com.tairanchina.csp.avm.constants.RedisKey;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.JWTSubject;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.User;
import com.tairanchina.csp.avm.mapper.UserMapper;
import com.tairanchina.csp.avm.utils.StringUtilsExt;
import com.tairanchina.csp.avm.service.TokenService;
import com.tairanchina.csp.avm.service.UserService;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import com.tairanchina.csp.dew.Dew;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String ALGORITHM = "md5";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TokenService tokenService;

    @Override
    public ServiceResult register(String phone, String password) {
        if (StringUtilsExt.hasEmpty(password, phone)) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        userEntityWrapper.eq("phone", phone);
        Integer integer = userMapper.selectCount(userEntityWrapper);
        if (integer > 0) {
            return ServiceResultConstants.USER_EXISTS;
        }
        Integer count = userMapper.selectCount(new EntityWrapper<>());
        User user = new User();
        if (count > 0) {
            user.setIsAdmin(0);
        } else {
            user.setIsAdmin(1);
        }
        user.setUserId($.field.createUUID());
        user.setFirstLoginTime(new Date());
        user.setPhone(phone);
        user.setNickName(phone);
        try {
            String md5 = $.security.digest.digest(password + phone, ALGORITHM); // 加盐算法
            user.setPassword(md5);
            Integer insert = userMapper.insert(user);
            if (insert > 0) {
                return ServiceResult.ok(null);
            } else {
                return ServiceResultConstants.DB_ERROR;
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error("密码加密出错", e);
            return ServiceResultConstants.ERROR_500;
        }
    }

    @Override
    public ServiceResult login(String phone, String password) {
        if (StringUtilsExt.hasEmpty(password, phone)) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        User userQuery = new User();
        userQuery.setPhone(phone);
        User user = userMapper.selectOne(userQuery);
        if (Objects.isNull(user)) {
            return ServiceResultConstants.WRONG_PHONE_PASSWORD;
        }
        try {
            String md5 = $.security.digest.digest(password + phone, ALGORITHM); // 加盐算法
            if (StringUtils.isNotEmpty(user.getPassword()) && user.getPassword().equals(md5)) {
                // 登录成功，签发token
                JWTSubject jwtSubject = new JWTSubject();
                jwtSubject.setUserId(user.getUserId());
                String jwt = tokenService.signJWT(jwtSubject); // 签发JWTtoken
                Dew.cluster.cache.hset(RedisKey.USER_LOGIN_INFO, user.getUserId(), jwt); //将JWT保存至Redis
                HashMap<String, Object> map = new HashMap<>();
                map.put("token", jwt);
                map.put("ident", phone);
                map.put("isNew", false);
                map.put("nickName", user.getNickName());
                map.put("userId", user.getUserId());
                map.put("username", user.getPhone());
                map.put("warning", false);
                map.put("isAdmin", user.getIsAdmin());
                return ServiceResult.ok(map);
            } else {
                return ServiceResultConstants.WRONG_PHONE_PASSWORD;
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error("密码加密出错", e);
            return ServiceResultConstants.ERROR_500;
        }
    }

    @Override
    public ServiceResult validate(String jwt) {
        JWTSubject jwtSubject = tokenService.validateJWT(jwt);
        if (Objects.isNull(jwtSubject)) {
            return ServiceResultConstants.JWT_ERROR;
        }
        String userId = jwtSubject.getUserId();
        String jwtFromRedis = Dew.cluster.cache.hget(RedisKey.USER_LOGIN_INFO, userId);
        if (!jwt.equals(jwtFromRedis)) {
            return ServiceResultConstants.JWT_ERROR;
        }
        User userQuery = new User();
        userQuery.setUserId(userId);
        User user = userMapper.selectOne(userQuery);
        if (Objects.isNull(user)) {
            Dew.cluster.cache.hdel(RedisKey.USER_LOGIN_INFO, userId);
            return ServiceResultConstants.USER_NOT_FOUND;
        }
        return ServiceResult.ok(user);
    }

    @Override
    public ServiceResult changePassword(String oldPassword, String password) {
        if (StringUtilsExt.hasEmpty(password, oldPassword)) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        String userId = ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId();
        User userQuery = new User();
        userQuery.setUserId(userId);
        User user = userMapper.selectOne(userQuery);
        if (Objects.isNull(user)) {
            return ServiceResultConstants.USER_NOT_FOUND;
        }
        String phone = user.getPhone();
        try {
            String oldPass = $.security.digest.digest(oldPassword + phone, ALGORITHM);
            if (!oldPass.equals(user.getPassword())) {
                return ServiceResultConstants.WRONG_PHONE_PASSWORD;
            } else {
                String newPass = $.security.digest.digest(password + phone, ALGORITHM);
                user.setPassword(newPass);
                EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
                userEntityWrapper.eq("user_id", user.getUserId());
                Integer integer = userMapper.update(user, userEntityWrapper);
                if (integer > 0) {
                    return ServiceResult.ok(null);
                } else {
                    return ServiceResultConstants.DB_ERROR;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error("密码加密出错", e);
            return ServiceResultConstants.ERROR_500;
        }
    }

    @Override
    public ServiceResult updateUserNickNameByUserId(String userId, String nickName) {
        User user = new User();
        user.setUserId(userId);
        User u = userMapper.selectOne(user);
        if (StringUtils.isNotBlank(u.getNickName()) && nickName.equals(u.getNickName())) {
            return ServiceResult.failed(40002, "请输入不一样的用户名");
        }

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.and().eq("nick_name", nickName);
        List<User> check = userMapper.selectList(wrapper);
        if (!check.isEmpty()) {
            return ServiceResult.failed(40001, "该用户名已存在");
        }
        User update = new User();
        update.setNickName(nickName);
        Integer result = userMapper.update(update, new EntityWrapper<>(user));
        return ServiceResult.ok(result);
    }

}
