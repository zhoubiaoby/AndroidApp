package com.example.zhoubiao.cxcourses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;

/**
 * Created by zb-1 on 2015/9/21.
 */
public class SplashActivity extends Activity {
    private CountDownTimer waitTimer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        waitTimer = new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}
