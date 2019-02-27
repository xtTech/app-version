package com.dingtalk.chatbot.message;

public class FeedCardMessageItem {
    private String title;
    private String picURL;
    private String messageURL;

    public FeedCardMessageItem() {
    }

    public String getMessageURL() {
        return this.messageURL;
    }

    public void setMessageURL(String messageURL) {
        this.messageURL = messageURL;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicURL() {
        return this.picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }
}
