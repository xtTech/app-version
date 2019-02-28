package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.enums.ChatBotEventType;

import java.util.List;

/**
 * 钉钉机器人管理
 * https://open-doc.dingtalk.com/docs/doc.htm?spm=a219a.7629140.0.0.21364a972AacKR&treeId=257&articleId=105735&docType=1
 * Created by hzlizx on 2018/9/27 0027
 */
public interface ChatBotService {

    /**
     * 创建一个钉钉机器人
     * @param appId     应用ID
     * @param webhook   WebHook
     * @param name      机器人名称
     * @param events    事件
     * @return          是否成功
     */
    ServiceResult createChatBot(Integer appId, String webhook, String name, List<ChatBotEventType> events);

    /**
     * 编辑钉钉机器人信息
     * @param appId     应用ID
     * @param webhook   WebHook
     * @param name      机器人名称
     * @param events    事件
     * @return          是否成功
     */
    ServiceResult editChatBot(Integer appId, String webhook, String name, List<ChatBotEventType> events);

    /**
     * 删除某个机器人
     * @param appId
     * @return
     */
    ServiceResult deleteChatBot(Integer appId);

    /**
     * 根据APPID找到绑定的机器人
     * @param appId
     * @return
     */
    ServiceResult getByAppId(int appId);

    /**
     * 发送钉钉消息
     * @param event
     * @param text
     */
    void send(ChatBotEventType event, String text);

    /**
     * 发送钉钉消息
     * @param event
     * @param title
     * @param text
     */
    void sendMarkdown(ChatBotEventType event,String title, String text);

}
