package com.tairanchina.csp.avm.mq;


import java.io.Serializable;

public class AccessRecordEvent implements Serializable {

    private String api;

    private String apiDescription;

    private Object[] requestArgs;

    private Integer returnCode;

    private String returnMessage;

    private String ipAddress;

    private String userAgent;


    public String getApi() {
        return api;
    }

    public AccessRecordEvent setApi(String api) {
        this.api = api;
        return this;
    }

    public String getApiDescription() {
        return apiDescription;
    }

    public AccessRecordEvent setApiDescription(String apiDescription) {
        this.apiDescription = apiDescription;
        return this;
    }

    public Object[] getRequestArgs() {
        return requestArgs;
    }

    public AccessRecordEvent setRequestArgs(Object[] requestArgs) {
        this.requestArgs = requestArgs;
        return this;
    }

    public Integer getReturnCode() {
        return returnCode;
    }

    public AccessRecordEvent setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
        return this;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public AccessRecordEvent setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
        return this;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public AccessRecordEvent setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public AccessRecordEvent setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

}
