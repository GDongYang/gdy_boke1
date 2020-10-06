package com.gdy.boke.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

public class SimpleRabbitService implements RabbitMQService {

    private static final Logger logger = LoggerFactory.getLogger(SimpleRabbitService.class);
    protected static final long DEFAUL_SLEEP_MILLIS = 30L;
    private ConnectionFactory rabbitMQConnectionFactory;
    private RabbitTemplate rabbitTemplate;
    private RabbitAdmin rabbitAdmin;
    private Set<String> declaredQueues;
    private Set<String> declaredExchangeAndQueues;
    private MessageConverter messageConverter;

    public SimpleRabbitService() {
        this.declaredQueues = new HashSet();
        this.declaredExchangeAndQueues = new HashSet();
    }

    public SimpleRabbitService(ConnectionFactory cf, RabbitTemplate rt, RabbitAdmin admin) {
        this(cf, rt, admin, (MessageConverter)null);
    }

    public SimpleRabbitService(ConnectionFactory cf, RabbitTemplate rt, RabbitAdmin admin, MessageConverter mc) {
        this.declaredQueues = new HashSet();
        this.declaredExchangeAndQueues = new HashSet();
        this.rabbitMQConnectionFactory = cf;
        this.rabbitTemplate = rt;
        this.rabbitAdmin = admin;
        this.messageConverter = mc;
        if (this.messageConverter != null) {
            this.rabbitTemplate.setMessageConverter(this.messageConverter);
        }

    }

    @Override
    public void send(String queueName, Object data) {
        this.send(queueName, queueName, SimpleRabbitService.ExchangeType.DIRECT, data, queueName);
    }
    @Override
    public void send(RabbitMQMessageTarget target, Object data) {
        this.send(target.getExchangeName(), target.getRoutingKey(), target.getExchangeType(), data, target.getQueueNames());
    }
    protected void send(String exchangeName, String routingKey, SimpleRabbitService.ExchangeType exchangeType, Object data, String... queueNames) {
        if (StringUtils.isEmpty(exchangeName)) {
            throw new IllegalArgumentException("exchange or routingKey must not be null");
        } else {
            this.declareExchangeAndQueue(exchangeName, exchangeType, routingKey, queueNames);

            try {
                Message message = null;
                MessageProperties messageProperties = new MessageProperties();
                messageProperties.setMessageId(UUID.randomUUID().toString());
                if (this.messageConverter == null) {
                    SimpleMessageConverter defaultConverter = new SimpleMessageConverter();
                    message = defaultConverter.toMessage(data, messageProperties);
                } else {
                    message = this.messageConverter.toMessage(data, messageProperties);
                }

                this.rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
            } catch (AmqpException var9) {
                logger.error("RabbitMQ send exception" + var9.getMessage(), var9);
                throw var9;
            }
        }
    }

    private void declareExchangeAndQueue(String exchangeName, SimpleRabbitService.ExchangeType ExchangeType, String routingKey, String... queueNames) {
        if (queueNames != null && queueNames.length > 0) {
            String[] var5 = queueNames;
            int var6 = queueNames.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String queueName = var5[var7];
                if (!this.declaredExchangeAndQueues.contains(exchangeName + "|" + queueName)) {
                    Queue queue = new Queue(queueName);
                    queue.setAdminsThatShouldDeclare(new Object[]{this.rabbitAdmin});
                    this.rabbitAdmin.declareQueue(queue);
                    String deadQueueName = queueName + "_DEAD";
                    Queue deadQueue = new Queue(deadQueueName);
                    deadQueue.setAdminsThatShouldDeclare(new Object[]{this.rabbitAdmin});
                    this.rabbitAdmin.declareQueue(deadQueue);
                    switch(ExchangeType) {
                        case TOPIC:
                            TopicExchange topicExchange = new TopicExchange(exchangeName);
                            this.rabbitAdmin.declareExchange(topicExchange);
                            this.rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(topicExchange).with(routingKey));
                            break;
                        case DIRECT:
                            DirectExchange directExchange = new DirectExchange(exchangeName);
                            this.rabbitAdmin.declareExchange(directExchange);
                            this.rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(directExchange).with(routingKey));
                            DirectExchange deadDirectExchange = new DirectExchange(exchangeName);
                            this.rabbitAdmin.declareExchange(deadDirectExchange);
                            this.rabbitAdmin.declareBinding(BindingBuilder.bind(deadQueue).to(deadDirectExchange).with(deadQueueName));
                            break;
                        case FANOUT:
                            FanoutExchange fanoutExchange = new FanoutExchange(exchangeName);
                            this.rabbitAdmin.declareExchange(fanoutExchange);
                            this.rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(fanoutExchange));
                            break;
                        default:
                            FanoutExchange exchange = new FanoutExchange(exchangeName);
                            this.rabbitAdmin.declareExchange(exchange);
                            this.rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange));
                    }

                    this.declaredExchangeAndQueues.add(exchangeName + "|" + queueName);
                }
            }
        }

    }

    @Override
    public void listen(final RabbitMessageListener listener, Integer concurrentConsumers) {
        String targetQueue = listener.getTargetQueueName();
        this.ensureQueueDeclared(targetQueue);
        ChannelAwareMessageListener messageHandler = new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                String consumerQueue = message.getMessageProperties().getConsumerQueue();
                Integer count = (Integer)message.getMessageProperties().getHeaders().get("count");
                Object object = null;
                if (SimpleRabbitService.this.messageConverter == null) {
                    SimpleMessageConverter defaultConverter = new SimpleMessageConverter();
                    object = defaultConverter.fromMessage(message);
                } else {
                    object = SimpleRabbitService.this.messageConverter.fromMessage(message);
                }

                if (listener.getAcknowledgeMode() == 1) {
                    listener.handleMessage(object, message, channel);
                } else if (listener.getAcknowledgeMode() == 2) {
                    listener.handleMessage(object);
                } else if (count == null) {
                    try {
                        listener.handleMessage(object);
                    } catch (Exception var8) {
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        message.getMessageProperties().setHeader("count", 1);
                        SimpleRabbitService.this.rabbitTemplate.convertAndSend(consumerQueue, consumerQueue, message);
                        SimpleRabbitService.logger.error("MQ listener handle method exception " + var8.getMessage(), var8);
                    }
                } else if (count > 0 && count <= 5) {
                    try {
                        listener.handleMessage(object);
                    } catch (Exception var7) {
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        message.getMessageProperties().setHeader("count", count + 1);
                        SimpleRabbitService.this.rabbitTemplate.convertAndSend(consumerQueue, consumerQueue, message);
                        SimpleRabbitService.logger.error("MQ listener handle method exception " + var7.getMessage(), var7);
                    }
                } else if (count > 5) {
                    String deadQueue = consumerQueue + "_DEAD";
                    SimpleRabbitService.this.rabbitTemplate.convertAndSend(deadQueue, deadQueue, message);
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    System.out.println("消息已经加入到私信队列-" + deadQueue + "-" + JSON.toJSONString(object));
                }

            }
        };
        MessageListenerAdapter adapter = new MessageListenerAdapter(messageHandler);
        if (this.messageConverter != null) {
            adapter.setMessageConverter(this.messageConverter);
        }

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer((org.springframework.amqp.rabbit.connection.ConnectionFactory) this.rabbitMQConnectionFactory);
        container.setMessageListener(adapter);
        container.setQueueNames(new String[]{listener.getTargetQueueName()});
        if (concurrentConsumers != null && concurrentConsumers > 1) {
            container.setConcurrentConsumers(concurrentConsumers);
        }

        switch(listener.getAcknowledgeMode()) {
            case 0:
                container.setAcknowledgeMode(AcknowledgeMode.AUTO);
                break;
            case 1:
                container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
                break;
            case 2:
                container.setAcknowledgeMode(AcknowledgeMode.NONE);
                break;
            default:
                container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        }

        container.start();
    }

    private void ensureQueueDeclared(String queueName) {
        if (!this.declaredQueues.contains(queueName)) {
            Queue queue = new Queue(queueName);
            queue.setAdminsThatShouldDeclare(new Object[]{this.rabbitAdmin});
            this.rabbitAdmin.declareQueue(queue);
            this.declaredQueues.add(queueName);
        }

    }

    protected static enum ExchangeType {
        TOPIC,
        DIRECT,
        FANOUT;

        private ExchangeType() {
        }
    }

}
