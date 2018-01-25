package com.think.futurecase;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by borney on 1/17/18.
 */

public abstract class Case<T> {
    private static final AtomicInteger caseNumber = new AtomicInteger(1);
    private final AtomicReference<Scheduler.AsyncGetNotify<T>> atomicNotify = new AtomicReference<>();
    private String name;
    private int id;
    private CaseCallback<T> callback;
    private Scheduler scheduler;
    private Future<T> future;
    private boolean isAttachOnLooperThread = false;
    private Handler handler;
    private T response;

    public interface CaseCallback<T> {
        void onSuccess(T obj);
    }

    public Case() {
        this(null);
    }

    public Case(String name) {
        id = caseNumber.getAndIncrement();
        this.name = name == null ? "Case-" + id : name;
    }

    boolean attachOnLooperThread(Future<T> future, Scheduler scheduler, CaseCallback<T> callback) {
        this.future = future;
        this.scheduler = scheduler;
        this.callback = callback;
        Looper looper = Looper.myLooper();
        if (looper != null) {
            handler = new Handler(looper);
            isAttachOnLooperThread = true;
        }
        return isAttachOnLooperThread;
    }

    Scheduler.AsyncGetNotify<T> getNotify() {
        return atomicNotify.get();
    }

    Future<T> getFuture() {
        return future;
    }

    Handler getHandler() {
        return handler;
    }

    boolean isAttachOnLooperThread() {
        return isAttachOnLooperThread;
    }

    synchronized final void runCase(Scheduler.AsyncGetNotify<T> notify) {
        atomicNotify.set(notify);
        executeCase();
    }

    public boolean isDone() {
        return future != null ? future.isDone() : false;
    }

    public boolean isCancelled() {
        return future != null ? future.isCancelled() : true;
    }

    public boolean cancel() {
        return future != null ? future.cancel(true) : false;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public abstract void executeCase();

    public void notifySuccess(T obj) {
        Scheduler.AsyncGetNotify<T> notify = getNotify();
        if (notify == null) {
            throw new IllegalStateException("notify before case runCase.");
        }
        if (callback == null) {
            notify.notify(obj);
        } else {
            callback.onSuccess(obj);
        }
        response = obj;
    }

    public T get() {
        return response;
    }

    public final <V> V get(final Case<V> dependCase) throws ExecutionException, InterruptedException {
        return scheduler.get(new Scheduler.AsyncGetRunnable<V>() {
            @Override
            public void run(Scheduler.AsyncGetNotify<V> notify) {
                dependCase.runCase(notify);
            }
        });
    }

    public final T get(Scheduler scheduler) throws ExecutionException, InterruptedException {
        return scheduler.get(new Scheduler.AsyncGetRunnable<T>() {
            @Override
            public void run(Scheduler.AsyncGetNotify<T> notify) {
                runCase(notify);
            }
        });
    }

    @Override
    public String toString() {
        return "Case{" +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
