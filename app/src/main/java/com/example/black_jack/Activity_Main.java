package com.example.black_jack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.Objects;

public class Activity_Main extends AppCompatActivity {
    Button startBtn, ruleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        startBtn = findViewById(R.id.main_gameStartBtn);
        ruleBtn = findViewById(R.id.main_gameRuleBtn);

        startBtn.setOnClickListener(v -> {
            changeLayout(0);
        });
        ruleBtn.setOnClickListener(v -> {
            changeLayout(1);
        });
    }

    private void changeLayout(int cnd) {
        Intent intent = null;
        if(cnd == 0) {
            intent = new Intent(Activity_Main.this, Activity_Game.class);
        } else if(cnd == 1) {
            intent = new Intent(Activity_Main.this, Activity_Rules.class);
        }
        startActivity(intent);
    }
}