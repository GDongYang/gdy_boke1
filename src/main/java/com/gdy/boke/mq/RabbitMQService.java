//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gdy.boke.mq;

public interface RabbitMQService {
    void send(String queueName, Object data);

    void send(RabbitMQMessageTarget target, Object data);

    void listen(RabbitMessageListener l, Integer concurrentConsumers);
}
