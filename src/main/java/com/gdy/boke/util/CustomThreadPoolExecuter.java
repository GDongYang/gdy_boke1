package com.gdy.boke.util;

import java.util.concurrent.*;

public class CustomThreadPoolExecuter extends ThreadPoolExecutor{

    private CustomThreadPoolExecuter(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public static ExecutorService newFixedThreadPoolExecutor(int nThreads){

        return new CustomThreadPoolExecuter(nThreads,nThreads,0, TimeUnit.MILLISECONDS,new LinkedBlockingDeque<>(2));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(task);
    }

    @Override
    public void execute(Runnable command) {
        super.execute(command);
    }
}

