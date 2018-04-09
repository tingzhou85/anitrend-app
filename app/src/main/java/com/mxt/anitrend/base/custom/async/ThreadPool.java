package com.mxt.anitrend.base.custom.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by max on 2018/02/20.
 */

public class ThreadPool {

    private ExecutorService executorService;

    private ThreadPool() {

    }

    private void createThreadPool() {
        executorService = Executors.newCachedThreadPool();
    }

    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    public static class Builder {

        public static ThreadPool create() {
            ThreadPool threadPool = new ThreadPool();
            threadPool.createThreadPool();
            return threadPool;
        }
    }
}
