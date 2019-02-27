package com.tairanchina.csp.avm.utils;


import com.tairanchina.csp.avm.entity.LoginInfo;

/**
 * 每次请求登录用户的信息保存在这里
 * Created by hzlizx on 2018/4/25 0025
 */
public class ThreadLocalUtils {
    public static final ThreadLocal<LoginInfo> USER_THREAD_LOCAL = new ThreadLocal<>();

}
