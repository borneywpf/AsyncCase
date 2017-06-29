package com.think.asyncase;

import android.util.Log;

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

    <R extends Case.ResponseValue> void notifySuccess(final R response,
            final Case.CaseCallback<R> caseCallback) {
        mCaseScheduler.notifyReponse(response, caseCallback);
    }

    <R extends Case.ResponseValue> void notifyError(final Case.CaseCallback<R> caseCallback) {
        mCaseScheduler.notifyError(caseCallback);
    }

    private static class UiCallbackWrapper<R extends Case.ResponseValue> implements
            Case.CaseCallback<R> {
        private Case.CaseCallback<R> mCaseCallback;
        private CaseHandler mCaseHandler;

        UiCallbackWrapper(Case.CaseCallback<R> caseCallback, CaseHandler caseHandler) {
            mCaseCallback = caseCallback;
            mCaseHandler = caseHandler;
        }

        @Override
        public void onSuccess(R response) {
            mCaseHandler.notifySuccess(response, mCaseCallback);
        }

        @Override
        public void onError() {
            mCaseHandler.notifyError(mCaseCallback);
        }
    }
}
