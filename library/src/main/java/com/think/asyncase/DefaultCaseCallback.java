package com.think.asyncase;

/**
 * Created by borney on 6/29/17.
 */

public class DefaultCaseCallback<R extends Case.ResponseValue> implements Case.CaseCallback<R> {

    @Override
    public void onSuccess(R response) {

    }

    @Override
    public <E extends Case.FailureValue> void onFailure(E ex) {

    }
}
