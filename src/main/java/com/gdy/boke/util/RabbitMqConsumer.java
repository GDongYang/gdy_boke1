package com.gdy.boke.util;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqConsumer {

    private static final String EXCHANGE_NAME = "producer_confirm";
    public static void main(String[] argv) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        Connection connection = factory.newConnection();//连接
        Channel channel = connection.createChannel();//信道
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);//交换器
        //声明队列
        String queueName = "consumer_confirm";
        //创建队列，参数分别为：队列名，durable是否进行持久化，exlusive是否私有，auto-delete没有消费者是自动删除，arguments相关参数
        channel.queueDeclare(queueName,false,false,false,null);
        String server = "error";
        channel.queueBind(queueName,EXCHANGE_NAME,server);
        System.out.println("Waiting message.......");
        Consumer consumerB = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body,"UTF-8");
                System.out.println("Accept:"+envelope.getRoutingKey()+":"+message);
                this.getChannel().basicAck(envelope.getDeliveryTag(),false);//参数：投递的标记符，b:是否进行批量回复
            }
        };
        //三个参数：queueName队列名，autoAck是否自动确认，callback消息监听回调
        channel.basicConsume(queueName,false,consumerB);
    }

}
