package com.think.asyncase.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.think.futurecase.Case;
import com.think.futurecase.CaseHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CaseHandler handler = CaseHandler.create();
        final TestFutureCase aCase = new TestFutureCase();
//        handler.execute(aCase, new Case.CaseCallback<String>() {
//            @Override
//            public void onSuccess(String obj) {
//                Log.d("wpf", aCase.getDescription("onSuccess") + " result:" + obj);
//            }
//        });
//        Log.w("wpf", "run in main...." + aCase);
//
//        new Thread("sync-main") {
//            @Override
//            public void run() {
//                super.run();
//                final TestFutureCase syncCase = new TestFutureCase();
//                handler.execute(syncCase, new Case.CaseCallback<String>() {
//                    @Override
//                    public void onSuccess(String obj) {
//                        Log.d("wpf", syncCase.getDescription("onSuccess") + " result:" + obj);
//                    }
//                });
//                Log.e("wpf", "run in " + getName() + " ..." + syncCase);
//            }
//        }.start();

//        final TestGetFetureCase getFetureCase = new TestGetFetureCase();
//        handler.execute(getFetureCase, new Case.CaseCallback<String>() {
//            @Override
//            public void onSuccess(String obj) {
//                Log.d("wpf", getFetureCase.getDescription("onSuccess") + " result:" + obj);
//            }
//        });
//        Log.w("wpf", "run in main...." + getFetureCase);

        new Thread("get-sync-main") {
            @Override
            public void run() {
                super.run();
                final TestGetFetureCase syncGetFetureCase = new TestGetFetureCase();
                handler.execute(syncGetFetureCase, new Case.CaseCallback<String>() {
                    @Override
                    public void onSuccess(String obj) {
                        Log.d("wpf", syncGetFetureCase.getDescription("onSuccess") + " result:" + obj);
                    }
                });
                Log.e("wpf", "run in " + getName() + " ..." + syncGetFetureCase);
            }
        }.start();

    }
}
