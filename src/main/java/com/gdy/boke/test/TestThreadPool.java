package com.gdy.boke.test;

import java.util.concurrent.*;

public class TestThreadPool extends ThreadPoolExecutor {

    private static final int CORE_SIZE = 5;

    private static final int MAX_MUM_POOL_SIZE = 20;

    private static final long ALIVE_TIME = 0;

    public static final ExecutorService EXECUTOR_SERVICE = new TestThreadPool();

    private TestThreadPool() {
        super(CORE_SIZE, MAX_MUM_POOL_SIZE, ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(5));
    }

    @Override
    public void execute(Runnable command) {
        super.execute(command);
    }

    @Override
    public Future<?> submit(Callable task) {
        return super.submit(task);
    }
}
