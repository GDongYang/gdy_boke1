package com.gdy.boke.service;

public interface DataCacheService {

    boolean setTokenLock(String requestId);

    String getToken();

    void unlock(String requestId);
}
