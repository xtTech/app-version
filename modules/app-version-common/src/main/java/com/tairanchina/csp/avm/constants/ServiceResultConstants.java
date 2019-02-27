package com.tairanchina.csp.avm.constants;

import com.tairanchina.csp.avm.dto.ServiceResult;

/**
 * Created by hzlizx on 2018/5/4 0004
 */
public class ServiceResultConstants {

    /**
     * 登录错误
     */
    public static final ServiceResult NO_LOGIN = ServiceResult.failed(1001, "没有登录，请先登录");
    public static final ServiceResult NO_ADMIN = ServiceResult.failed(1002, "当前用户非管理员，没有该接口的权限");
    public static final ServiceResult NO_APP_PM = ServiceResult.failed(1003, "当前用户没有管理该应用的权限");
    public static final ServiceResult NO_APP_ID = ServiceResult.failed(1004, "请带上APPID");
    public static final ServiceResult RESOURCE_NOT_BELONG_APP = ServiceResult.failed(1005, "所操作的资源不属于该应用");
    public static final ServiceResult USER_NOT_FOUND = ServiceResult.failed(1006, "找不到该用户");
    public static final ServiceResult WRONG_PHONE_PASSWORD = ServiceResult.failed(1007, "手机号或者密码错误");
    public static final ServiceResult JWT_ERROR = ServiceResult.failed(1008, "令牌不合法，请重新登录");

    /**
     * 注册错误
     */
    public static final ServiceResult USER_EXISTS = ServiceResult.failed(2001, "用户已经存在");
    public static final ServiceResult PASSWORD_CONFIRM_ERROR = ServiceResult.failed(2002, "两次输入的密码不一致");
    public static final ServiceResult PASSWORD_ERROR = ServiceResult.failed(2003, "密码格式错误");

    /**
     * 公共错误
     */
    public static final ServiceResult ERROR_500 = ServiceResult.failed(500, "系统错误，请联系管理员");
    public static final ServiceResult NEED_PARAMS = ServiceResult.failed(10001, "缺少参数，请带上正确的参数进行请求，如有疑问请查看开发文档");
    public static final ServiceResult DB_ERROR = ServiceResult.failed(10002, "数据库插入错误");
    public static final ServiceResult SERVICE_NOT_SUPPORT = ServiceResult.failed(10003, "当前版本暂不支持该操作，详情请联系管理员");
    public static final ServiceResult PARAMS_TYPE_ERROR = ServiceResult.failed(10004, "参数格式错误，请带上正确的参数进行请求，如有疑问请查看开发文档");
    public static final ServiceResult DATA_TOO_LARGE = ServiceResult.failed(10005, "参数长度过长");

    /**
     * 业务错误
     */
    public static final ServiceResult APP_NOT_EXISTS = ServiceResult.failed(20001, "找不到应用");
    public static final ServiceResult APP_EXISTS = ServiceResult.failed(20002, "应用已经存在，无法重复创建");
    public static final ServiceResult APP_NOT_SOFT_DELETE = ServiceResult.failed(20003, "应用没有在回收站中");
    public static final ServiceResult REL_EXISTS = ServiceResult.failed(20004, "关系已经存在");
    public static final ServiceResult CHANNEL_NOT_EXISTS = ServiceResult.failed(20005, "该渠道不存在");
    public static final ServiceResult VERSION_NOT_EXISTS = ServiceResult.failed(20006, "该版本不存在");
    public static final ServiceResult VERSION_NOT_SOFT_DELETE = ServiceResult.failed(20007, "版本没有在回收站中");
    public static final ServiceResult VERSION_EXISTS = ServiceResult.failed(20008, "该版本已经存在");
    public static final ServiceResult APK_NOT_EXISTS = ServiceResult.failed(20009, "APK包不存在");
    public static final ServiceResult RN_ROUTE_NOT_EXISTS = ServiceResult.failed(20010, "Rn路由不存在");
    public static final ServiceResult APP_DELETED = ServiceResult.failed(20011, "该APP已经被删除");
    public static final ServiceResult CHANNEL_CODE_EXISTS = ServiceResult.failed(20012, "渠道码重复");
    public static final ServiceResult TENANT_APP_ID_EXISTS = ServiceResult.failed(20013, "应用用户中心AppId已经存在");
    public static final ServiceResult CHANNEL_OFFICIAL = ServiceResult.failed(20014, "该渠道是官方渠道，无法被废弃或者删除");
    public static final ServiceResult CHANNEL_OFFICIAL_NOT_EXISTS = ServiceResult.failed(20015, "该版本暂未创建官方渠道，请创建渠道码为[official]的渠道，名称为[官方渠道]");
    public static final ServiceResult CHANNEL_OFFICIAL_NOT_UPLOADED = ServiceResult.failed(20016, "请在该版本的官方渠道[official]上传对应的apk包，并且上架，否则无法发布该版本");
    public static final ServiceResult APK_SAVE_ERROR = ServiceResult.failed(20017, "APK包保存失败");
    public static final ServiceResult ALLOWLOWESTVERSION_BIG_THAN_APPVERSION = ServiceResult.failed(20018, "允许最低版本号大于设定的版本号");
    public static final ServiceResult CUSTOM_API_NOT_EXISTS = ServiceResult.failed(20019, "该自定义接口不存在");
    public static final ServiceResult OPERATION_LOG_NOT_EXISTS = ServiceResult.failed(20020, "该操作记录不存在");
    public static final ServiceResult APK_EXISTS = ServiceResult.failed(20021, "APK包已经存在");
    public static final ServiceResult WHITE_LIST_BATCH_EXISTS = ServiceResult.failed(20022, "该白名单组的名称已存在");
    public static final ServiceResult WHITE_LIST_BATCH_ERROR = ServiceResult.failed(20023, "白名单组对应的名称存在多个");
    public static final ServiceResult WHITE_LIST_NOT_EXISITS = ServiceResult.failed(20024, "白名单（组）不存在");
    public static final ServiceResult MIN_BIG_THAN_MAX = ServiceResult.failed(20025, "最小版本应该小于最大版本");
    public static final ServiceResult TENANT_APP_ID_OR_APP_NAME_EXISTS = ServiceResult.failed(20026, "App名称或AppId已经存在");
    public static final ServiceResult CHANNEL_CODE_OR_NAME_EXISTS = ServiceResult.failed(20027, "渠道名称或渠道码重复");
    public static final ServiceResult VERSION_TOO_LONG = ServiceResult.failed(20028, "版本号最大长度为32");
    public static final ServiceResult RN_PACKAGE_NOT_EXISTS = ServiceResult.failed(20029, "Rn包不存在");
    public static final ServiceResult APP_NAME_TOO_LONG = ServiceResult.failed(20030, "App名称过长，请不要超过32个字符");
    public static final ServiceResult TENANT_APP_ID_TOO_LONG = ServiceResult.failed(20031, "AppId过长，请不要超过32个字符");
    public static final ServiceResult CHANNEL_STATUS_2_3 = ServiceResult.failed(20032, "渠道被废弃或停用");
    public static final ServiceResult CHANNEL_NAME_TOO_LONG = ServiceResult.failed(20033, "渠道名称过长");
    public static final ServiceResult CHANNEL_CODE_TOO_LONG = ServiceResult.failed(20034, "渠道码过长");
    public static final ServiceResult APK_CHANNEL_NOT_EXISTS = ServiceResult.failed(20035, "APK下载渠道不存在");
    public static final ServiceResult CHAT_BOT_MSG_NOT_EXISTS = ServiceResult.failed(20036,"找不到该机器人文本信息");
    public static final ServiceResult CHAT_BOT_EXISTS = ServiceResult.failed(20037,"机器人已经存在");
    public static final ServiceResult CHAT_BOT_EVENT_NOT_EXIST = ServiceResult.failed(20038,"机器人未绑定任何事件");
    public static final ServiceResult APP_NOT_BIND_BOT = ServiceResult.failed(20039,"该应用未绑定任何机器人");
    public static final ServiceResult CHAT_BOT_NOT_EXISTS = ServiceResult.failed(20040,"找不到该应用绑定的机器人");


    /**
     * 版本查询
     */
    public static final ServiceResult PLATFORM_ERROR = ServiceResult.failed(30001, "请指定查询的版本iOS还是Andorid版本");
    public static final ServiceResult NO_NEW_VERSION = ServiceResult.failed(30002, "查询不到新版本或者新版本未上架，没有更新的版本");
    public static final ServiceResult CHANNEL_STATUS_2 = ServiceResult.failed(30003, "当前渠道已经被废弃，请联系管理员");
    public static final ServiceResult CHANNEL_STATUS_3 = ServiceResult.failed(30004, "当前渠道已经被停用，请联系管理员");
    public static final ServiceResult APK_NOT_EXISTS_EXT = ServiceResult.failed(30005, "该渠道还没有上传APK包，请联系管理员");
    public static final ServiceResult APK_STATUS_0 = ServiceResult.failed(30006, "APK包暂未上架，请联系管理员");
    public static final ServiceResult APK_STATUS_2 = ServiceResult.failed(30007, "APK包暂已下架，请联系管理员");
    public static final ServiceResult APK_NOT_UPLOADED = ServiceResult.failed(30008, "暂未查询到该渠道或者是官网渠道已上传的APK包，请联系管理员");
    public static final ServiceResult STARTED_DOWNLOAD = ServiceResult.failed(200, "已经开始下载");


    /**
     * RN查询
     */
    public static final ServiceResult RN_STATUS_ERROR = ServiceResult.failed(40001, "RN状态必须为1（线上开启）或者2（测试需要）");

    /**
     * 自定义接口查询
     */
    public static final ServiceResult CUSTOM_API_NOT_FOUND = ServiceResult.failed(50001, "找不到该自定义接口信息");

}
