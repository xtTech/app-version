package com.dingtalk.chatbot.message;

public class ActionCardAction {
    private String title;
    private String actionURL;

    public ActionCardAction(String text, String actionURL) {
        this.title = text;
        this.actionURL = actionURL;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActionURL() {
        return this.actionURL;
    }

    public void setActionURL(String actionURL) {
        this.actionURL = actionURL;
    }
}
