package com.think.asyncase.demo;

import android.util.Log;

import com.think.asyncase.Case;


/**
 * Created by borney on 6/29/17.
 */

public class TestCase extends Case<TestCase.TestRequestValue, TestCase.TestResponseValue> {

    @Override
    protected void executeCase(TestRequestValue requestValues) {
        Log.d("wangpf", "TestCase execute thread: " + Thread.currentThread().getName());
        Log.d("wangpf", requestValues.toString());
        Log.d("wangpf", "start sleep...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("wangpf", "end sleep!!!");

        notifySuccess(new TestResponseValue());

        notifyError();
    }

    public static class TestRequestValue implements Case.RequestValue {
        @Override
        public String toString() {
            return "this is TestRequestValue";
        }
    }

    public static class TestResponseValue implements Case.ResponseValue {
        @Override
        public String toString() {
            return "this is TestResponseValue";
        }
    }
}
