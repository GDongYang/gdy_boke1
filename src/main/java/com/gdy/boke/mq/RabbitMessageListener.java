//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gdy.boke.mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

public interface RabbitMessageListener {
    int ACK_MODE_AUTO = 0;
    int ACK_MODE_MANUAL = 1;
    int ACK_MODE_NONE = 2;
    int DEFAULT_RECEIVE_NUM = 1;

    String getTargetQueueName();

    void handleMessage(Object messageData);

    void handleMessage(Object messageData, Message message, Channel channel);

    int getAcknowledgeMode();

    int getConcurrent();
}
