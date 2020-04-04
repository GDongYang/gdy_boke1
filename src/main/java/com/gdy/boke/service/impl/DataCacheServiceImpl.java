package com.gdy.boke.service.impl;

import com.gdy.boke.constant.Constant;
import com.gdy.boke.service.DataCacheService;
import com.gdy.boke.service.RedisMgmtService;
import org.springframework.stereotype.Service;

@Service
public class DataCacheServiceImpl implements DataCacheService {

    private RedisMgmtService redisMgmtService;

    @Override
    public boolean setTokenLock(String requestId) {
        String result = redisMgmtService.getJedisCluster().set(Constant.LOCK_TOKEN_KEY, requestId, "nx", "px", 2 * 60 * 1000);
        if(result.equals("OK")){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public String getToken() {
        Object o = redisMgmtService.get(Constant.TOKEN_KEY);
        return o==null ? null : o.toString();
    }

    @Override
    public void unlock(String requestId) {
        redisMgmtService.del(Constant.LOCK_TOKEN_KEY);
    }
}
