package com.example.medireminder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medireminder.R;
import com.example.medireminder.helper.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            Intent intent;

            if (sessionManager.isLogin()) {

                intent = new Intent(
                        SplashActivity.this,
                        DashboardActivity.class
                );

            } else {

                intent = new Intent(
                        SplashActivity.this,
                        LoginActivity.class
                );

            }

            startActivity(intent);

            finish();

        },2000);

    }

}