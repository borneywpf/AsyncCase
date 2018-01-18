package com.think.asyncase.demo;

import android.util.Log;

import com.think.futurecase.Case;

/**
 * Created by borney on 1/17/18.
 */

public class TestFutureCase extends Case<String> {
    private static final String TAG = "wpf";

    public TestFutureCase() {
        super("TestFutureCase");
    }

    @Override
    public void executeCase() {
        Log.d(TAG, getDescription("exec"));

        new Thread(getName()) {
            @Override
            public void run() {
                super.run();
                Log.d("wpf", getDescription("start sleep"));
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                notifySuccess("success!!!");
            }
        }.start();

    }


    public String getDescription(String s) {
        return String.format("%s case[" + getName() + "] thread:" + Thread.currentThread().getName(), s);
    }
}
