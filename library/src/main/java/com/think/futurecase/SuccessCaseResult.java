package com.think.futurecase;

/**
 * Created by borney on 1/11/18.
 */

public class SuccessCaseResult<V> implements CaseResult<V> {
    private V value;

    public SuccessCaseResult(V value) {
        this.value = value;
    }

    @Override
    public V get() {
        return value;
    }

    @Override
    public CaseException exception() {
        throw new RuntimeException("Not support exception!!!");
    }

    @Override
    public final boolean isSuccess() {
        return true;
    }

    @Override
    public String toString() {
        return "SuccessCaseResult{" +
                "value=" + value +
                '}';
    }
}
