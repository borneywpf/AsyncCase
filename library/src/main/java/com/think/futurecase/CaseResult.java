package com.think.futurecase;

/**
 * Created by borney on 1/11/18.
 */

public interface CaseResult<V> {
    V get();

    CaseException exception();

    boolean isSuccess();
}
