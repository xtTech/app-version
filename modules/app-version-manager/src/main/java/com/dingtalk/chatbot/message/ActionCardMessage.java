package com.dingtalk.chatbot.message;

import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class ActionCardMessage implements Message {
    public static final int MAX_ACTION_BUTTON_CNT = 5;
    public static final int MIN_ACTION_BUTTON_CNT = 1;
    private String title;
    private String bannerURL;
    private String briefTitle;
    private String briefText;
    private boolean hideAvatar;
    private ActionButtonStyle actionButtonStyle;
    private List<ActionCardAction> actions;

    public ActionCardMessage() {
        this.actionButtonStyle = ActionButtonStyle.VERTICAL;
        this.actions = new ArrayList();
    }

    public boolean isHideAvatar() {
        return this.hideAvatar;
    }

    public void setHideAvatar(boolean hideAvatar) {
        this.hideAvatar = hideAvatar;
    }

    public String getBriefTitle() {
        return this.briefTitle;
    }

    public void setBriefTitle(String briefTitle) {
        this.briefTitle = briefTitle;
    }

    public ActionButtonStyle getActionButtonStyle() {
        return this.actionButtonStyle;
    }

    public void setActionButtonStyle(ActionButtonStyle actionButtonStyle) {
        this.actionButtonStyle = actionButtonStyle;
    }

    public String getBannerURL() {
        return this.bannerURL;
    }

    public void setBannerURL(String bannerURL) {
        this.bannerURL = bannerURL;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBriefText() {
        return this.briefText;
    }

    public void setBriefText(String briefText) {
        this.briefText = briefText;
    }

    public void addAction(ActionCardAction action) {
        if (this.actions.size() >= 5) {
            throw new IllegalArgumentException("number of actions can't more than 5");
        } else {
            this.actions.add(action);
        }
    }

    public String toJsonString() {
        Map<String, Object> items = new HashMap();
        items.put("msgtype", "actionCard");
        Map<String, Object> actionCardContent = new HashMap();
        actionCardContent.put("title", this.title);
        StringBuffer text = new StringBuffer();
        if (StringUtils.isNotBlank(this.bannerURL)) {
            text.append(MarkdownMessage.getImageText(this.bannerURL) + "\n");
        }

        if (StringUtils.isNotBlank(this.briefTitle)) {
            text.append(MarkdownMessage.getHeaderText(3, this.briefTitle) + "\n");
        }

        if (StringUtils.isNotBlank(this.briefText)) {
            text.append(this.briefText + "\n");
        }

        actionCardContent.put("text", text.toString());
        if (this.hideAvatar) {
            actionCardContent.put("hideAvatar", "1");
        }

        if (this.actions.size() < 1) {
            throw new IllegalArgumentException("number of actions can't less than 1");
        } else {
            actionCardContent.put("btns", this.actions);
            if (this.actions.size() == 2 && this.actionButtonStyle == ActionButtonStyle.HORIZONTAL) {
                actionCardContent.put("btnOrientation", "1");
            }

            items.put("actionCard", actionCardContent);
            return JSON.toJSONString(items);
        }
    }
}
