package com.think.futurecase;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by borney on 1/17/18.
 */

public interface Scheduler {
    <T> Future<T> submit(Callable<T> callable);

    Future<?> submit(Runnable runnable);

    <T> Future<T> execute(AsyncGetRunnable<T> asyncGetRunnable);

    <T> T get(AsyncGetRunnable<T> asyncGetRunnable) throws ExecutionException, InterruptedException;

    interface AsyncGetRunnable<T> {
        void run(AsyncGetNotify<T> notify);
    }

    interface AsyncGetNotify<T> {
        void notify(T result);
    }
}
