package com.think.asyncase;

import android.os.Handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by borney on 6/29/17.
 */

class CaseThreadPoolScheduler implements CaseScheduler {

    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private static final int MAX_POOL_SIZE = POOL_SIZE * 2 + 1;

    private static final int TIMEOUT = 30;

    private ThreadPoolExecutor mThreadPoolExecutor;

    private Handler mHandler = new Handler();

    CaseThreadPoolScheduler() {
        mThreadPoolExecutor = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, TIMEOUT,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(POOL_SIZE),
                new SchedulerThreadFactory());
    }

    @Override
    public void execute(Runnable runnable) {
        mThreadPoolExecutor.execute(runnable);
    }

    @Override
    public <P extends Case.ResponseValue> void notifyReponse(final P reponse,
            final Case.CaseCallback<P> caseCallback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                caseCallback.onSuccess(reponse);
            }
        });
    }

    @Override
    public <P extends Case.ResponseValue> void notifyError(
            final Case.CaseCallback<P> caseCallback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                caseCallback.onError();
            }
        });
    }

    private static class SchedulerThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        private SchedulerThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "tcase-pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}
