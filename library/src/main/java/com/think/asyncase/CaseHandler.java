package com.think.asyncase;

import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by borney on 6/29/17.
 */

public class CaseHandler {
    private static final String TAG = "CaseHandler";

    private static CaseHandler INSTANCE;

    private CaseScheduler mCaseScheduler;

    private final Object mLockObject = new Object();

    private volatile boolean isPause = false;

    public CaseHandler(CaseScheduler scheduler) {
        mCaseScheduler = scheduler;
    }

    public static CaseHandler create() {
        if (INSTANCE == null) {
            INSTANCE = new CaseHandler(new CaseThreadPoolScheduler());
        }
        return INSTANCE;
    }

    public void pauseRequests() {
        isPause = true;
    }

    public void resumeRequests() {
        isPause = false;
        synchronized (mLockObject) {
            mLockObject.notifyAll();
        }
    }

    public <P extends Case.RequestValue, R extends Case.ResponseValue> void execute(
            final Case<P, R> uCase, final P values, final Case.CaseCallback<R> caseCallback) {
        uCase.setRequestValues(values);
        uCase.setCaseCallback(new UiCallbackWrapper<>(caseCallback, this));

        mCaseScheduler.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (mLockObject) {
                    while (isPause) {
                        try {
                            Log.v(TAG, "wait " + uCase);
                            mLockObject.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                uCase.run();
            }
        });
    }

    <R extends Case.ResponseValue> void notifySuccess(R response, Case.CaseCallback<R> caseCallback) {
        mCaseScheduler.notifyReponse(response, caseCallback);
    }

    <R extends Case.ResponseValue, E extends Case.FailureValue> void notifyError(E ex, Case.CaseCallback<R> caseCallback) {
        mCaseScheduler.notifyError(ex, caseCallback);
    }

    private static class UiCallbackWrapper<R extends Case.ResponseValue> implements
            Case.CaseCallback<R> {
        private WeakReference<Case.CaseCallback<R>> mCaseCallback;
        private CaseHandler mCaseHandler;

        UiCallbackWrapper(Case.CaseCallback<R> caseCallback, CaseHandler caseHandler) {
            mCaseCallback = new WeakReference<>(caseCallback);
            mCaseHandler = caseHandler;
        }

        @Override
        public void onSuccess(R response) {
            Case.CaseCallback<R> callback = mCaseCallback.get();
            if (callback != null) {
                mCaseHandler.notifySuccess(response, callback);
            }
        }

        @Override
        public <E extends Case.FailureValue> void onFailure(E ex) {
            Case.CaseCallback<R> callback = mCaseCallback.get();
            if (callback != null) {
                mCaseHandler.notifyError(ex, callback);
            }
        }
    }
}
