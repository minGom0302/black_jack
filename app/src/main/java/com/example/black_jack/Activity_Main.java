package com.example.black_jack;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.text.DecimalFormat;
import java.util.Objects;

public class Activity_Main extends AppCompatActivity {
    Button startBtn, ruleBtn, resetBtn;
    TextView moneyTv;
    SharedPreferences sp;
    SharedPreferences.Editor sp_e;
    DecimalFormat format = new DecimalFormat("###,###");
    backspaceHandler bsHandler = new backspaceHandler(Activity_Main.this);
    private InterstitialAd ads;

    String money;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sp_e = sp.edit();

        Objects.requireNonNull(getSupportActionBar()).hide();

        moneyTv = findViewById(R.id.main_moneyTv);
        startBtn = findViewById(R.id.main_gameStartBtn);
        ruleBtn = findViewById(R.id.main_gameRuleBtn);
        resetBtn = findViewById(R.id.main_resetBtn);

        // 광고를 보여주기 위한 객체 생성
        MobileAds.initialize(Activity_Main.this, "ca-app-pub-3940256099942544~3347511713"); // 앱 ID > 테스트 ID
        ads = new InterstitialAd(Activity_Main.this);
        ads.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); // 광고 ID > 앱을 등록해야지만 나오기 때문에 테스트 ID 넣어둠
        ads.loadAd(new AdRequest.Builder().build());

        // 광고 끝나고 실행할 것들 작성
        ads.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                sp_e.putString("money", "100");
                sp_e.commit();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                ads.loadAd(new AdRequest.Builder().build());
            }
        });

        startBtn.setOnClickListener(v -> {
            if(money.equals("0")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Main.this);
                builder.setTitle("경고").setMessage("보유하신 금액이 없습니다.\n초기화 후 진행해주세요.");
                builder.setPositiveButton("예", (dialogInterface, i) -> { });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                changeLayout(0);
            }
        });
        ruleBtn.setOnClickListener(v -> changeLayout(1));
        resetBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Main.this);
            builder.setTitle("경고").setMessage("보유하신 금액을 100만원으로 초기화하시겠습니까?");
            builder.setPositiveButton("예", (dialogInterface, i) -> {
                // 광고 실행
                if(ads.isLoaded()) {
                    ads.show();
                } else {
                    Log.i("TAG", "The interstitial wasn't loaded yet.");
                }
            });
            builder.setNeutralButton("아니오", (dialogInterface, i) -> {

            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        money = sp.getString("money", "100");
        // 돈 초기화는 돈이 0원일 때 보여지게 한다.
        if(money.equals("0")) {
            resetBtn.setVisibility(View.VISIBLE);
        } else {
            resetBtn.setVisibility(View.GONE);
        }


        int money01 = Integer.parseInt(money);
        if(money01 < 10000) {
            String s1 = format.format(money01);
            moneyTv.setText("보유금액 : " + s1 + "만원");
        } else if(money01%10000 == 0) {
            int i1 = money01 / 10000;
            moneyTv.setText("보유금액 : " + i1 + "억원");
        } else {
            int i1 = money01 / 10000;
            int i2 = money01 % 10000;
            String s1 = format.format(i1);
            String s2 = format.format(i2);
            moneyTv.setText("보유금액 : " + s1 + "억 " + s2 + "만원");
        }
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

    @Override
    public void onBackPressed() {
        bsHandler.onBackPressed("'뒤로가기' 버튼을 한번 더 누르면 종료됩니다.", 0);
    }
}