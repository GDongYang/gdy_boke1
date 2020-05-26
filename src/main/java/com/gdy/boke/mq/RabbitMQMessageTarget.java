//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gdy.boke.mq;

public class RabbitMQMessageTarget {

    private String[] queueNames;
    private String exchangeName;
    private String routingKey;
    private SimpleRabbitService.ExchangeType exchangeType;

    public static final RabbitMQMessageTarget createDirectTarget(String queueName) {
        return new RabbitMQMessageTarget(queueName, queueName, SimpleRabbitService.ExchangeType.DIRECT, new String[]{queueName});
    }

    public static final RabbitMQMessageTarget createFanoutTarget(String exchangeName, String... queueNames) {
        return new RabbitMQMessageTarget(exchangeName, (String)null, SimpleRabbitService.ExchangeType.FANOUT, queueNames);
    }

    public static final RabbitMQMessageTarget createTopicTarget(String exchangeName, String routingKey, String... queueNames) {
        return new RabbitMQMessageTarget(exchangeName, routingKey, SimpleRabbitService.ExchangeType.TOPIC, queueNames);
    }

    protected RabbitMQMessageTarget() {
    }

    protected RabbitMQMessageTarget(String exchangeName, String routingKey, SimpleRabbitService.ExchangeType exchangeType, String... queueNames) {
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.exchangeType = exchangeType;
        this.queueNames = queueNames;
    }

    public String[] getQueueNames() {
        return this.queueNames;
    }

    public void setQueueNames(String[] queueNames) {
        this.queueNames = queueNames;
    }

    public String getExchangeName() {
        return this.exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getRoutingKey() {
        return this.routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public SimpleRabbitService.ExchangeType getExchangeType() {
        return this.exchangeType;
    }

    public void setExchangeType(SimpleRabbitService.ExchangeType exchangeType) {
        this.exchangeType = exchangeType;
    }
}
