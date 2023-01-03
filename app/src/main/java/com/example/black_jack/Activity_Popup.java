package com.example.black_jack;

import androidx.appcompat.app.AlertDialog;

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

public class Activity_Popup extends Activity {
    EditText moneyEt;
    TextView nowMoneyTv;

    backspaceHandler bsHandler = new backspaceHandler(Activity_Popup.this);
    DecimalFormat format = new DecimalFormat("#,###");
    String result = "";
    String strNowMoney = "";
    String nowMoney = "";

    SharedPreferences sp;
    SharedPreferences.Editor sp_e;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sp_e = sp.edit();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        Button okBtn = findViewById(R.id.popup_okBtn);
        Button allInBtn = findViewById(R.id.popup_AllInBtn);
        moneyEt = findViewById(R.id.popup_moneyEt);
        nowMoneyTv = findViewById(R.id.popup_nowMoneyTv);

        nowMoney = sp.getString("money", "100");
        strNowMoney = format.format(Integer.parseInt(nowMoney));
        nowMoneyTv.setText("보유금액 : " + strNowMoney + "만원");

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

        allInBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Popup.this);
            builder.setTitle("안내").setMessage("보유한 금액을 전부 배팅하시겠습니까?");
            builder.setPositiveButton("예", (dialogInterface, i) -> battingEnd(1));
            builder.setNeutralButton("아니오", (dialogInterface, i) -> { });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    private void battingEnd(int cnd) {
        if(cnd == 0) {
            String money = String.valueOf(moneyEt.getText());
            money = money.replaceAll(",", "");
            int strMoney = Integer.parseInt(money); // 배팅 금액
            int intNowMoney = Integer.parseInt(nowMoney); // 보유 금액
            int rMoney = intNowMoney - strMoney;
            if(rMoney < 0) {
                Toast.makeText(this, "보유한 금액보다 많이 배팅할 수 없습니다.", Toast.LENGTH_SHORT).show();
            } else if(strMoney == 0) {
                Toast.makeText(this, "배팅 금액을 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                String resultMoney = String.valueOf(rMoney);

                sp_e.putString("money", resultMoney);
                sp_e.commit();

                Intent intent = new Intent();
                intent.putExtra("money", money);
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
                intent.putExtra("money", strAllInMoney); // 배팅금액
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

    @Override
    public void onBackPressed() {
        bsHandler.onBackPressed("뒤로가기 버튼을 한번 더 누르면 종료됩니다.", 1);
    }
}