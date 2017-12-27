package com.think.asyncase;

/**
 * Created by borney on 6/29/17.
 */

public interface CaseScheduler {
    void execute(Runnable runnable);

    <P extends Case.ResponseValue> void notifyReponse(P reponse, Case.CaseCallback<P> caseCallback);

    <P extends Case.ResponseValue, E extends Case.FailureValue> void notifyError(E ex, Case.CaseCallback<P> caseCallback);
}
