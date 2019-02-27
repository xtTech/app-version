package com.tairanchina.csp.avm.entity;

import java.util.Date;

/**
 * rest接口访问日志
 */
public class AccessLog {

    private Integer id;

    /**
     * 访问的接口
     */
    private String api;

    /**
     * 接口中文描述
     */
    private String apiDescription;

    /**
     * 访问的接口入参
     */
    private String apiArgs;

    /**
     * 返回结果的code
     */
    private Integer returnCode;

    /**
     * 返回结果的内容
     */
    private String ReturnMessage;

    /**
     * 访问IP
     */
    private String reqIp;

    /**
     * 访问时间
     */
    private Date reqTime;

    /**
     * 访问运营商
     */
    private String reqIsp;

    /**
     * 城市
     */
    private String reqCity;

    /**
     * 城市ID
     */
    private String reqCityId;

    /**
     * 访问省份
     */
    private String reqRegion;

    /**
     * 省份ID
     */
    private String reqRegionId;

    /**
     * 访问国家
     */
    private String reqCountry;

    /**
     * 国家ID
     */
    private String reqCountryId;

    /**
     * 访问区域
     */
    private String reqArea;

    /**
     * 操作系统类型
     */
    private String reqOs;

    /**
     * 浏览器类型
     */
    private String reqBrowser;

    /**
     * 设备型号
     */
    private String reqDevice;

    /**
     * 请求头
     */
    private String reqUa;

    public Integer getId() {
        return id;
    }

    public AccessLog setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getApi() {
        return api;
    }

    public AccessLog setApi(String api) {
        this.api = api;
        return this;
    }

    public String getApiDescription() {
        return apiDescription;
    }

    public AccessLog setApiDescription(String apiDescription) {
        this.apiDescription = apiDescription;
        return this;
    }

    public String getApiArgs() {
        return apiArgs;
    }

    public AccessLog setApiArgs(String apiArgs) {
        this.apiArgs = apiArgs;
        return this;
    }

    public Integer getReturnCode() {
        return returnCode;
    }

    public AccessLog setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
        return this;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public AccessLog setReturnMessage(String returnMessage) {
        ReturnMessage = returnMessage;
        return this;
    }

    public String getReqIp() {
        return reqIp;
    }

    public AccessLog setReqIp(String reqIp) {
        this.reqIp = reqIp;
        return this;
    }

    public Date getReqTime() {
        return reqTime;
    }

    public AccessLog setReqTime(Date reqTime) {
        this.reqTime = reqTime;
        return this;
    }

    public String getReqIsp() {
        return reqIsp;
    }

    public AccessLog setReqIsp(String reqIsp) {
        this.reqIsp = reqIsp;
        return this;
    }

    public String getReqCity() {
        return reqCity;
    }

    public AccessLog setReqCity(String reqCity) {
        this.reqCity = reqCity;
        return this;
    }

    public String getReqCityId() {
        return reqCityId;
    }

    public AccessLog setReqCityId(String reqCityId) {
        this.reqCityId = reqCityId;
        return this;
    }

    public String getReqRegion() {
        return reqRegion;
    }

    public AccessLog setReqRegion(String reqRegion) {
        this.reqRegion = reqRegion;
        return this;
    }

    public String getReqRegionId() {
        return reqRegionId;
    }

    public AccessLog setReqRegionId(String reqRegionId) {
        this.reqRegionId = reqRegionId;
        return this;
    }

    public String getReqCountry() {
        return reqCountry;
    }

    public AccessLog setReqCountry(String reqCountry) {
        this.reqCountry = reqCountry;
        return this;
    }

    public String getReqCountryId() {
        return reqCountryId;
    }

    public AccessLog setReqCountryId(String reqCountryId) {
        this.reqCountryId = reqCountryId;
        return this;
    }

    public String getReqArea() {
        return reqArea;
    }

    public AccessLog setReqArea(String reqArea) {
        this.reqArea = reqArea;
        return this;
    }

    public String getReqOs() {
        return reqOs;
    }

    public AccessLog setReqOs(String reqOs) {
        this.reqOs = reqOs;
        return this;
    }

    public String getReqBrowser() {
        return reqBrowser;
    }

    public AccessLog setReqBrowser(String reqBrowser) {
        this.reqBrowser = reqBrowser;
        return this;
    }

    public String getReqDevice() {
        return reqDevice;
    }

    public AccessLog setReqDevice(String reqDevice) {
        this.reqDevice = reqDevice;
        return this;
    }

    public String getReqUa() {
        return reqUa;
    }

    public AccessLog setReqUa(String reqUa) {
        this.reqUa = reqUa;
        return this;
    }

    public enum ApiDescription {
        SELECT_CUSTON_API("查询自定义接口"),
        SELECT_RN_ROUTE("查询RN路由"),
        SELECT_RN_PACKAGE("查询RN包"),
        SELECT_NEW_VERSION("查询新版本"),
        DOWNLOAD_APK("下载APK");

        String name;

        private ApiDescription(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
