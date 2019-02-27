package com.tairanchina.csp.avm.config;

import com.tairanchina.csp.avm.constants.MQKey;
import com.tairanchina.csp.avm.mq.ChatBotSender;
import com.tairanchina.csp.dew.Dew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by hzlizx on 2018/9/27 0027
 */
@Configuration
public class MqConfig {

    @Autowired
    private ChatBotSender chatBotSender;

    /**
     * 机器人消息消费者
     */
    @PostConstruct
    public void chatBotResponse(){
        Dew.cluster.mq.response(MQKey.CHAT_BOT_MQ, chatBotSender);
    }

}
