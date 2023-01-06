package com.example.black_jack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Objects;

public class Activity_Intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // 액션바 숨기기
        Objects.requireNonNull(getSupportActionBar()).hide();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(Activity_Intro.this, Activity_Main.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}