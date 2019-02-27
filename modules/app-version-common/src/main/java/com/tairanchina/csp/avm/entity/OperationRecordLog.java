package com.tairanchina.csp.avm.entity;

public class OperationRecordLog extends BasicEntity {

    private Integer id;
    private String operator;
    private Integer appId;
    private OperationResource operationResource;
    private OperationType operationType;
    private String operationDescription;
    private String operationContent;
    private OperationResult operationResult;
    private String resultMessage;
    private Integer delFlag;


    public enum OperationType {
        CREATE("添加"),
        UPDATE("修改"),
        DELETE("删除"),
        DELETE_FOREVER("硬删"),
        SELECT("查询"),
        DELIVERY("上架"),           //apk,android,ios
        UNDELIVERY("下架"),         //apk,android,ios
        SCRAP("废弃"),              //Channel
        OPEN("开启");               //Channel
//        OTHER("其他");

        private String name;

        private OperationType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum OperationResource {
        APK("APK"),
        APP("APP"),
        CHANNEL("渠道"),
        CUSTOM_API("自定义接口"),
        IOS_VERSION("IOS版本"),
        ANDROID_VERSION("安卓版本"),
        RN_PACKAGE("RN包"),
        RN_ROUTE("RN路由"),
        USER("用户信息"),
        USER_APP_REL("用户和APP关系");
//        OPETATION_RECORD_LOG("操作记录"),
//        APP_WHITE_LIST_ONE("单个白名单"),
//        APP_WHITE_LIST_BATCH("白名单组"),
//        OTHER("其他");

        private String name;

        private OperationResource(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum OperationResult {
        FAILD,
        SUCCESS,
        OTHER
    }

    public enum OperationDescription {
        CREATE_APK("添加APK包"),
        CREATE_APP("添加APP"),
        CREATE_CHANNEL("添加渠道"),
        CREATE_CUSTOM_API("添加自定义接口"),
        CREATE_IOS_VERSION("添加IOS版本"),
        CREATE_ANDROID_VERSION("添加安卓版本"),
        CREATE_RN_PACKAGE("添加RN包"),
        CREATE_RN_ROUTE("添加RN路由"),
        CREATE_USER("创建用户"),
        CREATE_USER_APP_REL("绑定用户和APP"),
//        CREATE_OPETATION_RECORD_LOG("添加操作日志"),
//        CREATE_APP_WHITE_LIST_ONE("添加单个白名单"),
//        CREATE_APP_WHITE_LIST_BATCH("添加一组白名单"),

        UPDATE_APK("修改APK包"),
        UPDATE_APP("修改APP"),
        UPDATE_CHANNEL("修改渠道"),
        UPDATE_CUSTOM_API("修改自定义接口"),
        UPDATE_IOS_VERSION("修改IOS版本"),
        UPDATE_ANDROID_VERSION("修改安卓版本"),
        UPDATE_RN_PACKAGE("修改RN包"),
        UPDATE_RN_ROUTE("修改RN路由"),
        UPDATE_USER("修改用户信息"),
//        UPDATE_USER_APP_REL("修改用户和APP关联"),
//        UPDATE_OPETATION_RECORD_LOG("修改操作日志"),
//        UPDATE_APP_WHITE_LIST_ONE("修改单个白名单"),
//        UPDATE_APP_WHITE_LIST_BATCH("修改一组白名单"),

        DELETE_APK("删除APK包"),
        DELETE_APP("删除APP"),
        DELETE_CHANNEL("删除渠道"),
        DELETE_CUSTOM_API("删除自定义接口"),
        DELETE_IOS_VERSION("删除IOS版本"),
        DELETE_ANDROID_VERSION("删除安卓版本"),
        DELETE_RN_PACKAGE("删除RN包"),
        DELETE_RN_ROUTE("删除RN路由"),
        DELETE_USER("删除用户信息"),
//        DELETE_USER_APP_REL("删除用户和APP关联"),
//        DELETE_OPETATION_RECORD_LOG("删除操作日志"),
//        DELETE_APP_WHITE_LIST_ONE("删除单个白名单"),
//        DELETE_APP_WHITE_LIST_BATCH("删除一组白名单"),

        DELIVERY_APK("APK上架"),
        DELIVERY_IOS_VERSION("IOS版本上架"),
        DELIVERY_ANDROID_VERSION("安卓版本上架"),

        UNDELIVERY_APK("APK下架"),
        UNDELIVERY_IOS_VERSION("IOS版本下架"),
        UNDELIVERY_ANDROID_VERSION("安卓版本下架"),

        SCRAP_CHANNEL("废弃渠道"),
        OPEN_CHANNEL("开启渠道"),

        DELETE_FOREVER_APK("永久删除APK包"),
//        DELETE_FOREVER_APP("永久删除APP"),
        DELETE_FOREVER_CHANNEL("永久删除渠道"),
        DELETE_FOREVER_CUSTOM_API("永久删除自定义接口"),
        DELETE_FOREVER_IOS_VERSION("永久删除IOS版本"),
        DELETE_FOREVER_ANDROID_VERSION("永久删除安卓版本"),
        DELETE_FOREVER_RN_PACKAGE("永久删除RN包"),
        DELETE_FOREVER_RN_ROUTE("永久删除RN路由"),
        DELETE_FOREVER_USER("永久删除用户信息"),
        DELETE_FOREVER_USER_APP_REL("删除用户和APP关联(解绑APP)");
//        DELETE_FOREVER_OPETATION_RECORD_LOG("永久删除操作日志"),
//        DELETE_FOREVER_APP_WHITE_LIST_ONE("永久删除单个白名单"),
//        DELETE_FOREVER_APP_WHITE_LIST_BATCH("永久删除一组白名单");

        private String description;

        private OperationDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public Integer getId() {
        return id;
    }

    public OperationRecordLog setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public OperationRecordLog setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public Integer getAppId() {
        return appId;
    }

    public OperationRecordLog setAppId(Integer appId) {
        this.appId = appId;
        return this;
    }

    public OperationResource getOperationResource() {
        return operationResource;
    }

    public OperationRecordLog setOperationResource(OperationResource operationResource) {
        this.operationResource = operationResource;
        return this;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public OperationRecordLog setOperationType(OperationType operationType) {
        this.operationType = operationType;
        return this;
    }

    public String getOperationDescription() {
        return operationDescription;
    }

    public OperationRecordLog setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
        return this;
    }

    public String getOperationContent() {
        return operationContent;
    }

    public OperationRecordLog setOperationContent(String operationContent) {
        this.operationContent = operationContent;
        return this;
    }

    public OperationResult getOperationResult() {
        return operationResult;
    }

    public OperationRecordLog setOperationResult(OperationResult operationResult) {
        this.operationResult = operationResult;
        return this;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public OperationRecordLog setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
        return this;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public OperationRecordLog setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
        return this;
    }
}
