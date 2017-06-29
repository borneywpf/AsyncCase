package com.think.asyncase;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by borney on 6/29/17.
 */

public abstract class Case<P extends Case.RequestValue, R extends Case.ResponseValue> {
    private static final AtomicInteger caseNumber = new AtomicInteger(1);
    private String mName;
    private int mId;
    private P mRequestValues;
    private CaseCallback<R> mCaseCallback;

    public Case() {
        this(null);
    }

    public Case(String name) {
        mId = caseNumber.getAndIncrement();
        mName = name == null ? "Case-" + mId : name;
    }

    void setCaseCallback(CaseCallback<R> callback) {
        mCaseCallback = callback;
    }

    void setRequestValues(P values) {
        mRequestValues = values;
    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return mId;
    }

    public P getRequestValues() {
        return mRequestValues;
    }

    public void notifySuccess(R reponse) {
        mCaseCallback.onSuccess(reponse);
    }

    public void notifyError() {
        mCaseCallback.onError();
    }

    void run() {
        executeCase(mRequestValues);
    }

    protected abstract void executeCase(P requestValues);

    @Override
    public String toString() {
        return "Case[" + mId + "," + mName + "]";
    }

    public interface RequestValue {
    }

    public interface ResponseValue {
    }

    public interface CaseCallback<R> {
        void onSuccess(R response);

        void onError();
    }
}
