package com.think.futurecase;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by borney on 1/17/18.
 */

public class CaseHandler {
    private volatile static CaseHandler sInstance;
    private final Scheduler scheduler;

    private CaseHandler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public static CaseHandler create() {
        if (sInstance == null) {
            sInstance = new CaseHandler(new CaseScheduler());
        }
        return sInstance;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public <T> void execute(final Case<T> uCase, Case.CaseCallback<T> callback) {
        Future<T> future = scheduler.execute(new Scheduler.AsyncGetRunnable<T>() {
            @Override
            public void run(Scheduler.AsyncGetNotify<T> notify) {
                uCase.runCase(notify);
            }
        });

        boolean attachOnLooperThread = uCase.attachOnLooperThread(future, scheduler,
                new CasebackWapper<>(uCase, callback, this));

        if (!attachOnLooperThread) {
            T obj = null;
            try {
                obj = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (callback != null) {
                callback.onSuccess(obj);
            }
        }
    }

    private <T> void notifySuccess(final Case<T> uCase, final Case.CaseCallback<T> callback, final T obj) {
        Scheduler.AsyncGetNotify<T> notify = uCase.getNotify();
        notify.notify(obj);
        if (uCase.isAttachOnLooperThread()) {
            uCase.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        callback.onSuccess(obj);
                    }
                }
            });
        }
    }

    private static class CasebackWapper<T> implements Case.CaseCallback<T> {
        private Case.CaseCallback<T> callback;
        private CaseHandler caseHandler;
        private Case<T> uCase;

        public CasebackWapper(Case<T> uCase, Case.CaseCallback<T> callback, CaseHandler caseHandler) {
            this.uCase = uCase;
            this.callback = callback;
            this.caseHandler = caseHandler;
        }

        @Override
        public void onSuccess(T obj) {
            caseHandler.notifySuccess(uCase, callback, obj);
        }
    }
}
