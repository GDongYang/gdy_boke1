package com.gdy.boke.listiner;

import com.gdy.boke.mq.RabbitMessageListener;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

public class AbstractRabbitMessageListener implements RabbitMessageListener {
    private String targetQueueName;
    private int concurrent;
    private int acknowledgeMode;

    public AbstractRabbitMessageListener(String queueName) {
        this(queueName, 0, 1);
    }

    public AbstractRabbitMessageListener(String queueName, int ackMode) {
        this(queueName, ackMode, 1);
    }

    public AbstractRabbitMessageListener(String queueName, int ackMode, int concurrent) {
        this.concurrent = 1;
        this.acknowledgeMode = 0;
        this.targetQueueName = queueName;
        this.acknowledgeMode = ackMode;
        this.concurrent = concurrent;
    }

    @Override
    public int getAcknowledgeMode() {
        return this.acknowledgeMode;
    }
    @Override
    public String getTargetQueueName() {
        return this.targetQueueName;
    }
    @Override
    public void handleMessage(Object object) {
    }
    @Override
    public void handleMessage(Object object, Message message, Channel channel) {
    }
    @Override
    public int getConcurrent() {
        return this.concurrent;
    }
}
