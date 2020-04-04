package com.gdy.boke.service;

public interface RedisService {

    String getArticle(String articleName) throws InterruptedException;
}
