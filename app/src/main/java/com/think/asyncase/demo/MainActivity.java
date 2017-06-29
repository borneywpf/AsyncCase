package com.think.asyncase.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.think.asyncase.Case;
import com.think.asyncase.CaseHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CaseHandler handler = CaseHandler.create();

        handler.execute(new TestCase(), new TestCase.TestRequestValue(),
                new Case.CaseCallback<TestCase.TestResponseValue>() {

                    @Override
                    public void onSuccess(TestCase.TestResponseValue response) {
                        Log.d("wangpf", "onSuccess thread: " + Thread.currentThread().getName());
                        Log.d("wangpf", response.toString());
                    }

                    @Override
                    public void onError() {
                        Log.d("wangpf", "onError thread: " + Thread.currentThread().getName());
                    }
                });
    }
}
