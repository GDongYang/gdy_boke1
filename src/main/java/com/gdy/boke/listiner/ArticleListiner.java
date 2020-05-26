package com.gdy.boke.listiner;

public class ArticleListiner extends AbstractRabbitMessageListener {
    public ArticleListiner(String queueName) {
        super(queueName,0,0);
    }

    @Override
    public void handleMessage(Object object) {
        System.out.println("收到消息");
    }
}
