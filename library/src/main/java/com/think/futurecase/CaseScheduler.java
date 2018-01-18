package com.think.futurecase;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by borney on 1/17/18.
 */

public class CaseScheduler implements Scheduler {
    private ExecutorService executor = Executors.newCachedThreadPool(new SchedulerThreadFactory());


    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        FutureTask<T> futureTask = new FutureTask<>(callable);
        executor.submit(futureTask);
        return futureTask;
    }

    @Override
    public Future<?> submit(final Runnable runnable) {
        return executor.submit(runnable);
    }

    @Override
    public <T> Future<T> execute(final AsyncGetRunnable<T> asyncGetRunnable) {
        return submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                final AtomicReference<T> atomResult = new AtomicReference<>();
                final Object lock = new Object();
                AsyncGetNotify<T> asyncGetNotify = new AsyncGetNotify<T>() {
                    @Override
                    public void notify(T result) {
                        atomResult.set(result);
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                    }
                };
                asyncGetRunnable.run(asyncGetNotify);
                while (atomResult.get() == null) {
                    synchronized (lock) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                return atomResult.get();
            }
        });
    }

    @Override
    public <T> T get(final AsyncGetRunnable<T> asyncGetRunnable)
            throws ExecutionException, InterruptedException {
        Future<T> future = execute(asyncGetRunnable);
        return future.get();
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
            namePrefix = "case-pool-" +
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
