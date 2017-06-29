package com.think.asyncase.demo;

import android.util.Log;

import com.think.asyncase.Case;


/**
 * Created by borney on 6/29/17.
 */

public class TestCase extends Case<TestCase.TestRequestValue, TestCase.TestResponseValue> {
    private long mSleep;

    public TestCase(long sleep) {
        mSleep = sleep;
    }

    @Override
    protected void executeCase(TestRequestValue requestValues) {
        Log.d("wangpf", getName() + " TestCase execute thread: " + Thread.currentThread().getName());
        Log.v("wangpf", getName() + " " + requestValues.toString());
        Log.d("wangpf", getName() + " start sleep..." + mSleep);
        try {
            Thread.sleep(mSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("wangpf", getName() + " end sleep!!!");

        notifySuccess(new TestResponseValue(getName()));

        notifyError();
    }

    public static class TestRequestValue implements Case.RequestValue {
        private String mName;

        public TestRequestValue(String name) {
            mName = name;
        }

        @Override
        public String toString() {
            return mName + "-this is TestRequestValue";
        }
    }

    public static class TestResponseValue implements Case.ResponseValue {
        private String mName;
        public TestResponseValue(String name) {
            mName = name;
        }

        @Override
        public String toString() {
            return mName + "-this is TestResponseValue";
        }
    }
}
