package com.think.asyncase.demo;

import android.util.Log;

import com.think.futurecase.Case;

import java.util.concurrent.ExecutionException;

/**
 * Created by borney on 1/18/18.
 */

public class TestGetFetureCase extends Case<String> {

    public TestGetFetureCase() {
        super("TestGetFetureCase");
    }

    @Override
    public void executeCase() {
        Log.d("wpf", getDescription("exec"));
        TestFutureCase futureCase = new TestFutureCase();

        try {
            String s = get(futureCase);
            Log.i("wpf", getDescription("depend back (" + s + ")"));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notifySuccess("xxxxxxxxxxxx");
    }

    public String getDescription(String s) {
        return String.format("%s case[" + getName() + "] thread:" + Thread.currentThread().getName(), s);
    }
}
