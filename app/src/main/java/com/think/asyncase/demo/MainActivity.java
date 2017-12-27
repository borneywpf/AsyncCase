package com.think.asyncase.demo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.think.asyncase.Case;
import com.think.asyncase.CaseHandler;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CaseHandler handler = CaseHandler.create();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("wangpf", "pauseRequests handler");
                handler.pauseRequests();
            }
        }, 5000);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("wangpf", "resumeRequests handler");
                handler.resumeRequests();
            }
        }, 12000);
        addCase(handler);
    }

    private void addCase(CaseHandler handler) {
        for (int i = 1; i <= 10; i++) {
            TestCase aCase = new TestCase(new Random().nextInt(10) * 1000);
            Log.w("wangpf", "submit " + aCase.getName());
            handler.execute(aCase,
                    new TestCase.TestRequestValue(aCase.getName()),
                    new Case.CaseCallback<TestCase.TestResponseValue>() {

                        @Override
                        public void onSuccess(TestCase.TestResponseValue response) {
                            Log.d("wangpf",
                                    "onSuccess thread: " + Thread.currentThread().getName());
                            Log.e("wangpf", response.toString());
                        }

                        @Override
                        public <E extends Case.FailureValue> void onFailure(E ex) {
                            Log.d("wangpf", "onFailure thread: " + Thread.currentThread().getName());
                        }
                    });
        }
    }
}
