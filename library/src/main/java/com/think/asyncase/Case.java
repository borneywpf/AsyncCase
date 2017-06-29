package com.think.asyncase;

/**
 * Created by borney on 6/29/17.
 */

public abstract class Case<P extends Case.RequestValue, R extends Case.ResponseValue> {

    private P mRequestValues;
    private CaseCallback<R> mCaseCallback;

    void setCaseCallback(CaseCallback<R> callback) {
        mCaseCallback = callback;
    }

    void setRequestValues(P values) {
        mRequestValues = values;
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

    public interface RequestValue {
    }

    public interface ResponseValue {
    }

    public interface CaseCallback<R> {
        void onSuccess(R response);

        void onError();
    }
}
