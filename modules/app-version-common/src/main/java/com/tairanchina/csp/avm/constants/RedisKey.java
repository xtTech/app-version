package com.tairanchina.csp.avm.constants;

/**
 * Redis的Key
 * Created by hzlizx on 2018/4/11 0011
 */
public class RedisKey {

    // 长链接对应短链接的hash缓存
    public static final String LONG_SHORT_HASH = "long_short_hash:";
    // 短链接ID计数
    public static final String SHORT_COUNT = "short_count:";


    public static final String FILE_LAST = "file_last:";
    public static final String E_TAG = "e_tag:";

    public static final String APP_HASH = "app_hash:";

    public static final String APP_LOCK = "app_lock:";

    public static final String USER_LOGIN_INFO = "user_login_info";
}
