package com.example.black_jack;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Objects;

public class Activity_Popup_Insurance extends Activity {
    EditText moneyEt;
    TextView nowMoneyTv;

    DecimalFormat format = new DecimalFormat("#,###");
    String result = "";
    String strNowMoney = "";
    String nowMoney = "";

    SharedPreferences sp;
    SharedPreferences.Editor sp_e;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_insurance);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sp_e = sp.edit();

        Button okBtn = findViewById(R.id.insurance_okBtn);
        Button allInBtn = findViewById(R.id.insurance_AllInBtn);
        Button passBtn = findViewById(R.id.insurance_passBtn);
        Button plus10Btn = findViewById(R.id.insurance_plus10Btn);
        Button plus50Btn = findViewById(R.id.insurance_plus50Btn);
        Button plus100Btn = findViewById(R.id.insurance_plus100Btn);
        moneyEt = findViewById(R.id.insurance_moneyEt);
        nowMoneyTv = findViewById(R.id.insurance_nowMoneyTv);

        nowMoney = sp.getString("money", "100");

        int money01 = Integer.parseInt(nowMoney);
        if(money01 < 10000) {
            strNowMoney = format.format(money01);
            nowMoneyTv.setText("보유금액 : " + strNowMoney + "만원");
        } else if(money01%10000 == 0) {
            int i1 = money01 / 10000;
            strNowMoney = format.format(money01);
            nowMoneyTv.setText("보유금액 : " + i1 + "억원");
        } else {
            strNowMoney = format.format(money01);
            int i1 = money01 / 10000;
            int i2 = money01 % 10000;
            String s1 = format.format(i1);
            String s2 = format.format(i2);

            nowMoneyTv.setText("보유금액 : " + s1 + "억 " + s2 + "만원");
        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence.toString()) && !charSequence.toString().equals(result)){
                    result = format.format(Double.parseDouble(charSequence.toString().replaceAll(",","")));
                    moneyEt.setText(result);
                    moneyEt.setSelection(result.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        moneyEt.addTextChangedListener(textWatcher);

        okBtn.setOnClickListener(v -> battingEnd(0));
        passBtn.setOnClickListener(v -> battingEnd(0));

        plus10Btn.setOnClickListener(v -> plusMoney(10));
        plus50Btn.setOnClickListener(v -> plusMoney(50));
        plus100Btn.setOnClickListener(v -> plusMoney(100));

        allInBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Popup_Insurance.this);
            builder.setTitle("안내").setMessage("보유한 금액을 전부 배팅하시겠습니까?");
            builder.setPositiveButton("예", (dialogInterface, i) -> battingEnd(1));
            builder.setNeutralButton("아니오", (dialogInterface, i) -> { });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    private void plusMoney(int value) {
        String money = String.valueOf(moneyEt.getText()).replaceAll(",", "");
        int beforeMoney = Integer.parseInt(money);
        int afterMoney = beforeMoney + value;
        if(afterMoney <= Integer.parseInt(nowMoney)) {
            money = format.format(afterMoney);
            moneyEt.setText(money);
        } else {
            Toast.makeText(this, "보유한 금액보다 많이 배팅할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void battingEnd(int cnd) {
        if(cnd == 0) {
            String money = String.valueOf(moneyEt.getText()).replaceAll(",", "");
            int strMoney = Integer.parseInt(money); // 배팅 금액
            int intNowMoney = Integer.parseInt(nowMoney); // 보유 금액
            int rMoney = intNowMoney - strMoney;
            if(rMoney < 0) {
                Toast.makeText(this, "보유한 금액보다 많이 배팅할 수 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                String resultMoney = String.valueOf(rMoney);

                sp_e.putString("money", resultMoney);
                sp_e.commit();

                Intent intent = new Intent();
                intent.putExtra("insuranceMoney", money);
                intent.putExtra("resultMoney", resultMoney);
                setResult(RESULT_OK, intent);
                finish();
            }
        } else if(cnd == 1) {
            // 전액 가져옴
            String strAllInMoney = strNowMoney.replaceAll(",", "");
            int intAllInMoney = Integer.parseInt(strAllInMoney); // 배팅 금액

            if(intAllInMoney == 0) {
                Toast.makeText(this, "보유한 금액이 없어 배팅할 수 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                sp_e.putString("money", "0");
                sp_e.commit();

                Intent intent = new Intent();
                intent.putExtra("insuranceMoney", strAllInMoney); // 배팅금액
                intent.putExtra("resultMoney", "0"); // 보유금액
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }
}