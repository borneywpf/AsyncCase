package com.think.asyncase;

/**
 * Created by borney on 6/29/17.
 */

public interface CaseScheduler {
    void execute(Runnable runnable);

    <P extends Case.ResponseValue> void notifyReponse(final P reponse,
            final Case.CaseCallback<P> caseCallback);

    <P extends Case.ResponseValue> void notifyError(final Case.CaseCallback<P> caseCallback);
}
