package com.tairanchina.csp.avm.mq;

import com.dingtalk.chatbot.DingtalkChatbotClient;
import com.dingtalk.chatbot.SendResult;
import com.dingtalk.chatbot.message.MarkdownMessage;
import com.dingtalk.chatbot.message.TextMessage;
import com.ecfront.dew.common.$;
import com.tairanchina.csp.avm.entity.App;
import com.tairanchina.csp.avm.entity.User;
import com.tairanchina.csp.avm.mapper.AppMapper;
import com.tairanchina.csp.avm.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * ChatBot发送端
 * Created by hzlizx on 2018/9/27 0027
 */
@Component
public class ChatBotSender implements Consumer<String> {
    private static final Logger logger = LoggerFactory.getLogger(ChatBotSender.class);

    @Autowired
    private DingtalkChatbotClient dingtalkChatbotClient;

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void accept(String s) {
        ChatBotMQEvent chatBotMQEvent = $.json.toObject(s, ChatBotMQEvent.class);
        logger.info("接受到机器人消息发送事件：" + chatBotMQEvent.toString());
        App app = appMapper.selectById(chatBotMQEvent.getAppId());

        User user = new User();
        user.setUserId(chatBotMQEvent.getTriggeredBy());
        User userInDb = userMapper.selectOne(user);
        if (app != null && userInDb != null) {
            try {
                if("MARKDOWN".equalsIgnoreCase(chatBotMQEvent.getType())){
                    MarkdownMessage markdownMessage = new MarkdownMessage();
                    markdownMessage.setTitle(chatBotMQEvent.getTitle());
                    markdownMessage.add(chatBotMQEvent.getMessage());

                    markdownMessage.add("> **操作人** ：" + userInDb.getNickName() + "\n\n");
                    markdownMessage.add("> **操作应用** ：" + app.getAppName() + "\n\n");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);

                    markdownMessage.add("> **操作时间** ：" + simpleDateFormat.format(chatBotMQEvent.getEventTime()) + "\n\n");
                    SendResult send = dingtalkChatbotClient.send(chatBotMQEvent.getWebhook(), markdownMessage);
                    printResult(send);
                }else {
                    String text = "用户[ " + userInDb.getNickName() + " ]在应用[ " + app.getAppName() + " ]下执行了操作[ " + chatBotMQEvent.getMessage() + " ]";
                    SendResult send = dingtalkChatbotClient.send(chatBotMQEvent.getWebhook(), new TextMessage(text));
                    printResult(send);
                }
            } catch (IOException e) {
                logger.error("发送机器人信息出错", e);
            }
        }
    }

    private void printResult(SendResult sendResult){
        logger.info("ChatBot send result : {}", $.json.toJsonString(sendResult));
    }
}
