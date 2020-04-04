package com.gdy.boke.util;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqProducer {

    private static final String EXCHANGE_NAME = "producer_confirm";
    private final static String ROUTE_KEY = "error";


    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        //创建一个连接
        Connection conn = factory.newConnection();

        //创建一个信道
        Channel channel = conn.createChannel();
        //指定转发
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //将信道确认为发送方确认
        channel.confirmSelect();
        //添加异步监听器
        channel.addConfirmListener(new ConfirmListener() {
            /**
             * 当rabbitMQ队列返回确认的时候调用的方法
             * @param l
             * @param b
             * @throws IOException
             */
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                System.out.println("sendMsg succss:"+l);
            }

            /**
             * 发生数据丢失调用的方法
             * @param l
             * @param b
             * @throws IOException
             */
            @Override
            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("send msg failed:"+l);
            }
        });

        /**
         * 服务端返回的方法
         */
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("replyText:"+replyText);
                System.out.println("exchange:"+exchange);
                System.out.println("routingKey:"+routingKey);
                System.out.println("msg:"+msg);
            }
        });

        String[] serverties = {"error","info","warning"};
        for(int  i = 0;i<3;i++){
            String serverity = serverties[i%3];
            //发送消息
            String msg = "hello rabbitmq"+(i+1)+("_"+System.currentTimeMillis());
            channel.basicPublish(EXCHANGE_NAME,serverity,true,null,msg.getBytes());
            System.out.println("----------------------------------");
            System.out.println(" Sent Message: [" + serverity +"]:'"+ msg + "'");
            Thread.sleep(200);
        }
        //关闭通道和连接
        channel.close();
        conn.close();

    }

    /**
     * 发送消息
     */
    public static void sendMsg(Channel channel,String msg){

    }

}

