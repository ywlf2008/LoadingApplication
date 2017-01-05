package com.example.yhuan.loadingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gastudio.gabottleloading.library.GABottleLoadingView;

import java.util.Timer;
import java.util.TimerTask;

public class LoadingActivity extends AppCompatActivity {

    GABottleLoadingView mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
//            }
//        }.start();

        initView();

    }

    private void initView() {
        mLoadingView = (GABottleLoadingView) findViewById(R.id.ga_bottle_loading_view);
        mLoadingView.performAnimation();
        mLoadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
