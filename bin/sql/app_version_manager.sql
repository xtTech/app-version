use app_version_manager;

DROP TABLE IF EXISTS `access_log`;
CREATE TABLE `access_log`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `api` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '接口名',
  `api_description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '接口中文描述',
  `api_args` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '接口入参',
  `return_code` int(8) NOT NULL DEFAULT 0 COMMENT '接口返回code',
  `return_message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '接口返回信息',
  `req_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
  `req_isp` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '访问运营商',
  `req_city` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '城市',
  `req_city_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '城市ID',
  `req_region` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '访问省份',
  `req_region_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '省份ID',
  `req_country` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '访问国家',
  `req_country_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '国家ID',
  `req_area` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '访问区域',
  `req_os` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '访问的操作系统',
  `req_browser` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '访问的浏览器',
  `req_device` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '设备型号',
  `req_ua` varchar(516) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '请求时的userAgent',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `android_version`;
CREATE TABLE `android_version`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'APPID',
  `app_version` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '版本号',
  `update_type` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT '0：强制更新 1：一般更新 2：静默更新 3：可忽略更新 4：静默可忽略更新 ',
  `version_description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '更新描述',
  `allow_lowest_version` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '允许最低版本（低于这个要强制更新）',
  `version_status` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发布状态（0-未上架；1-已上架）',
  `gray_released` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '灰度发布（0-无；1-白名单发布；2-IP发布）',
  `white_list_id` int(11) NOT NULL DEFAULT 0 COMMENT '白名单ID',
  `ip_list_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'IP段发布的list ID',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `del_flag` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否被删除',
  `updated_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `apk`;
CREATE TABLE `apk`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `channel_id` int(11) NOT NULL DEFAULT 0 COMMENT '渠道ID',
  `app_id` int(11) NOT NULL DEFAULT 0 COMMENT 'AppId',
  `version_id` int(11) NOT NULL DEFAULT 0 COMMENT '版本ID',
  `md5` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'APK文件的MD5值',
  `oss_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'apk文件oss存储地址',
  `delivery_status` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '标记渠道版本的安装包是否已上架（0-未上架；1-已上架）',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `del_flag` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `updated_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `apk_download_record`;
CREATE TABLE `apk_download_record`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` int(11) NOT NULL DEFAULT 0 COMMENT 'AppId',
  `version_id` int(11) NOT NULL DEFAULT 0 COMMENT '版本ID',
  `channel_id` int(11) NOT NULL DEFAULT 0 COMMENT '渠道ID',
  `apk_id` int(11) NOT NULL DEFAULT 0 COMMENT 'apkId',
  `download_num` int(11) NOT NULL DEFAULT 0 COMMENT 'apk下载量',
  `download_date` date NOT NULL,
  `created_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `apkid_idx`(`apk_id`) USING BTREE COMMENT 'apkid的索引',
  INDEX `date_idx`(`download_date`) USING BTREE COMMENT '下载日期的索引',
  INDEX `version_idx`(`app_id`, `version_id`) USING BTREE COMMENT '版本的索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `app`;
CREATE TABLE `app`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'APP名称',
  `tenant_app_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '租户应用ID',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `del_flag` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否被删除',
  `updated_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `channel`;
CREATE TABLE `channel`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` int(11) NOT NULL DEFAULT 0 COMMENT '关联的APPID',
  `channel_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '渠道名称',
  `channel_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '渠道码',
  `channel_type` tinyint(2) UNSIGNED NOT NULL DEFAULT 1 COMMENT '渠道类型 （1:应用市场 2:推广渠道）',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `del_flag` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否被删除',
  `updated_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `channel_status` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '1：正常 2：废弃 3：停用 ',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_app_channel_code`(`app_id`, `channel_code`) USING BTREE COMMENT '某个app不可以有重复的渠道码'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `chat_bot`;
CREATE TABLE `chat_bot`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '机器人名称',
  `webhook` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '钉钉群机器人Webhook地址',
  `app_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联的APP',
  `active_event` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '绑定的事件',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `updated_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `del_flag` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `custom_api`;
CREATE TABLE `custom_api`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'APPID',
  `custom_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '自定义配置的名称',
  `ios_enabled` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'iOS是否开启',
  `android_enabled` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '安卓是否开启',
  `custom_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置内容',
  `custom_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '接口的key',
  `custom_status` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态 0:关闭 1:线上开启 2:测试需要',
  `ios_min` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'iOS最小版本',
  `ios_max` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'iOS最大版本',
  `android_min` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '安卓最小版本',
  `android_max` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '安卓最大版本',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `del_flag` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `updated_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `ios_version`;
CREATE TABLE `ios_version`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'APPID',
  `app_version` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '版本号',
  `update_type` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT '0：强制更新 1：一般更新 2：静默更新 3：可忽略更新  4：静默可忽略更新 ',
  `version_description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '更新描述',
  `allow_lowest_version` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '允许最低版本（低于这个要强制更新）',
  `version_status` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发布状态（0-未上架；1-已上架）',
  `app_store_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'APP STORE的地址',
  `gray_released` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '灰度发布（0-无；1-白名单发布；2-IP发布）',
  `white_list_id` int(11) NOT NULL DEFAULT 0 COMMENT '白名单ID',
  `ip_list_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'IP段发布的list ID',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `del_flag` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否被删除',
  `updated_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `operation_record_log`;
CREATE TABLE `operation_record_log`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '操作记录id主键',
  `operator` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作者',
  `app_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '被操作的appid',
  `operation_resource` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '被操作的资源，通常为表名',
  `operation_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作类型：增删改查',
  `operation_description` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '操作内容的中文描述',
  `operation_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作的数据内容',
  `operation_result` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '操作结果：成功、失败',
  `result_message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '操作返回内容',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `updated_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `del_flag` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作记录日志表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `rn_package`;
CREATE TABLE `rn_package`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'APPID',
  `rn_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模块名称（约定）',
  `rn_nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'RN包模块通用昵称',
  `rn_type` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '1-android ; 2-ios',
  `resource_url` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'RN包资源地址',
  `rn_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'RN包大小(KB)',
  `rn_version` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'RN包版本号',
  `rn_update_log` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '包更新日志',
  `rn_status` tinyint(2) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'RN包状态（0-关闭；1-线上开启；2-测试需要）',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `del_flag` tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
  `version_min` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '最小版本',
  `version_max` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '最大版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'RN包版本明细表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `rn_route`;
CREATE TABLE `rn_route`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '路由id主键',
  `app_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'appid',
  `route_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '路由通用昵称',
  `route_key` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '被拦截URL（约定）',
  `route_value` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '目标URL',
  `ios_enabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否iOS开启',
  `android_enabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否android开启',
  `route_status` tinyint(2) UNSIGNED NOT NULL DEFAULT 0 COMMENT '路由状态 0:关闭 1:线上开启 2:测试需要',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `updated_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `updated_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `ios_min` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'ios最小版本',
  `ios_max` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'ios最大版本',
  `android_min` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'android最小版本',
  `android_max` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'android最大版本',
  `del_flag` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'RN路由地址明细表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户ID',
  `phone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码',
  `nick_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '昵称',
  `first_login_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `is_admin` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否是超级管理员',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `idx_phone`(`phone`) USING BTREE COMMENT '手机号唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `user_app_rel`;
CREATE TABLE `user_app_rel`  (
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户ID',
  `app_id` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'APPID',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`, `app_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

