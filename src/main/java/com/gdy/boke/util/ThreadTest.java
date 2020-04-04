package com.gdy.boke.util;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadTest {

    private static final Integer TOTAL_THREAD = 2;
    private static final Integer MAX_ALIVE = 4;
    private static final Integer ALIVE_TIME = 20;

    static class StyleFactory implements ThreadFactory{

        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("i am style thread"+mThreadNum);
            System.out.println("new Thread is created"+t.getName());
            return t;

        }
    }

    static class StyleHandler implements RejectedExecutionHandler{
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            this.dolog(r,executor);
        }

        private void dolog(Runnable r, ThreadPoolExecutor executor) {
            System.out.println(r.toString()+"is rejected");
        }
    }

    static class NameThread extends Thread{

        private NameThread(String threadName){
            this.setName(threadName);
        }

        @Override
        public void run() {
            System.out.println("this is new NameThread"+currentThread().getName());
        }
    }

    public static void main(String[] args) {

        StyleFactory factory = new StyleFactory();
        //创建线程池处理对象
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(TOTAL_THREAD,MAX_ALIVE,ALIVE_TIME, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(2),new StyleHandler());
        threadPoolExecutor.prestartAllCoreThreads();
        for(int i = 0;i<600;i++){
            Thread t = factory.newThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            threadPoolExecutor.execute(t);
        }
    }

}
