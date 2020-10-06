//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gdy.boke.config;

import java.util.Collection;
import java.util.Iterator;

import com.gdy.boke.mq.RabbitMQService;
import com.gdy.boke.mq.RabbitMessageListener;
import com.gdy.boke.mq.SimpleRabbitService;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQAutoConfiguration implements ApplicationContextAware {
    private static final long RESOURCE_OWNER_ID = 1404053829324700L;
    private ApplicationContext applicationContext;
    private RabbitMQService rabbitMQService;
    @Value("${calorie.rabbitmq.enter:false}")
    private boolean enter;
    @Value("${calorie.rabbitmq.isAli:false}")
    private boolean isAli;
    @Value("${calorie.rabbitmq.autoListen:false}")
    private boolean autoListen;
    @Value("${calorie.rabbitmq.host:127.0.0.1}")
    private String host;
    @Value("${calorie.rabbitmq.port:5672}")
    private int port;
    @Value("${calorie.rabbitmq.userName:guest}")
    private String userName;
    @Value("${calorie.rabbitmq.password:guest}")
    private String password;
    @Value("${calorie.rabbitmq.metrics.report.interval:30}")
    private int reportInterval;
    @Value("${calorie.rabbitmq.serialize.type:2}")
    private int serializationType;
    @Value("${calorie.rabbitmq.addresses:}")
    private String addresses;
    @Value("${calorie.rabbitmq.vhost:/}")
    private String vhost;
    @Value("${calorie.rabbitmq.maxChannels:25}")
    private int maxChannels;
    @Value("${calorie.rabbitmq.connectionTimeout:60000}")
    private int connectionTimeout;
    @Value("${calorie.rabbitmq.requestedHeartbeat:60}")
    private int requestedHeartbeat;
    @Value("${calorie.rabbitmq.automaticRecoveryEnabled:false}")
    private boolean automaticRecoveryEnabled;
    @Value("${calorie.rabbitmq.topologyRecoveryEnabled:true}")
    private boolean topologyRecoveryEnabled;

    public RabbitMQAutoConfiguration() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    @ConditionalOnExpression("'${calorie.rabbitmq.enter:false}'=='true'")
    public ConnectionFactory rabbitConnectionFactory() {
        if (this.isAli) {
           // AliyunCredentialsProvider credentialsProvider = new AliyunCredentialsProvider(this.userName, this.password, 1404053829324700L);
            com.rabbitmq.client.ConnectionFactory rabbitConnectionFactory = new com.rabbitmq.client.ConnectionFactory();
            rabbitConnectionFactory.setHost(this.host);
            rabbitConnectionFactory.setPort(this.port);
            rabbitConnectionFactory.setVirtualHost(this.vhost);
           // rabbitConnectionFactory.setCredentialsProvider(credentialsProvider);
            rabbitConnectionFactory.setAutomaticRecoveryEnabled(true);
            rabbitConnectionFactory.setNetworkRecoveryInterval(5000);
            ConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitConnectionFactory);
            ((CachingConnectionFactory)connectionFactory).setChannelCacheSize(this.maxChannels);
            ((CachingConnectionFactory)connectionFactory).getRabbitConnectionFactory().setTopologyRecoveryEnabled(this.topologyRecoveryEnabled);
            ((CachingConnectionFactory)connectionFactory).getRabbitConnectionFactory().setAutomaticRecoveryEnabled(this.automaticRecoveryEnabled);
            ((CachingConnectionFactory)connectionFactory).setConnectionTimeout(this.connectionTimeout);
            ((CachingConnectionFactory)connectionFactory).setRequestedHeartBeat(this.requestedHeartbeat);
            return (CachingConnectionFactory)connectionFactory;
        } else {
            CachingConnectionFactory cf = new CachingConnectionFactory();
            cf.setHost(this.host);
            cf.setPassword(this.password);
            cf.setUsername(this.userName);
            cf.setPort(this.port);
            cf.setVirtualHost(this.vhost);
            cf.setAddresses(this.addresses);
            cf.setChannelCacheSize(this.maxChannels);
            cf.getRabbitConnectionFactory().setTopologyRecoveryEnabled(this.topologyRecoveryEnabled);
            cf.getRabbitConnectionFactory().setAutomaticRecoveryEnabled(this.automaticRecoveryEnabled);
            cf.setConnectionTimeout(this.connectionTimeout);
            cf.setRequestedHeartBeat(this.requestedHeartbeat);
            return cf;
        }
    }

    @Bean
    @ConditionalOnExpression("'${calorie.rabbitmq.enter:false}'=='true'")
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
        RabbitTemplate template = new RabbitTemplate(cf);
        return template;
    }

    @Bean
    @ConditionalOnExpression("'${calorie.rabbitmq.enter:false}'=='true'")
    public RabbitAdmin rabbitAdmin(ConnectionFactory cf) {
        RabbitAdmin admin = new RabbitAdmin(cf);
        return admin;
    }

    @Bean
    @ConditionalOnExpression("'${calorie.rabbitmq.enter:false}'=='true'")
    public RabbitMQService rabbitMQService(ConnectionFactory rabbitConnectionFactory, RabbitTemplate rabbitTemplate, RabbitAdmin admin) {
        RabbitMQService rabbitMQService = null;
        if (this.serializationType == 1) {
            rabbitMQService = new SimpleRabbitService(rabbitConnectionFactory, rabbitTemplate, admin);
        } else if (this.serializationType == 2) {
            rabbitMQService = new SimpleRabbitService(rabbitConnectionFactory, rabbitTemplate, admin, new Jackson2JsonMessageConverter());
        } else {
            rabbitMQService = new SimpleRabbitService(rabbitConnectionFactory, rabbitTemplate, admin);
        }

        this.rabbitMQService = rabbitMQService;
        return rabbitMQService;
    }

    @Bean
    public AutoConsumerBean autoConsumerBean() {
        if (this.autoListen) {
            Collection<RabbitMessageListener> rabbitListeners = this.applicationContext.getBeansOfType(RabbitMessageListener.class).values();
            Iterator it = rabbitListeners.iterator();

            while(it.hasNext()) {
                RabbitMessageListener next = (RabbitMessageListener)it.next();
                int concurrent = next.getConcurrent();
                if (concurrent > 1) {
                    this.rabbitMQService.listen(next, concurrent);
                } else {
                    this.rabbitMQService.listen(next, 1);
                }
            }
        }

        return new AutoConsumerBean();
    }
}
