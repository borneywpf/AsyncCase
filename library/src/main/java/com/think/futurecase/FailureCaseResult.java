package com.think.futurecase;

/**
 * Created by borney on 1/11/18.
 */

public class FailureCaseResult<V> implements CaseResult<V> {
    private CaseException value;

    public FailureCaseResult(CaseException value) {
        this.value = value;
    }

    @Override
    public V get() {
        throw new RuntimeException("Not support exception!!!");
    }

    @Override
    public CaseException exception() {
        return value;
    }

    @Override
    public final boolean isSuccess() {
        return false;
    }

    @Override
    public String toString() {
        return "FailureCaseResult{" +
                "value=" + value +
                '}';
    }
}
