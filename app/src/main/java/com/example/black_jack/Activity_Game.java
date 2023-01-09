package com.example.black_jack;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Activity_Game extends AppCompatActivity {
    TextView countTv, moneyTv, totalMoneyTv;
    ImageView cardBack;
    Button goBtn, stopBtn;

    Card cardActivity;
    ArrayList<Card> cards;
    ArrayList<Card> dealerCards;
    ArrayList<Card> myCards;
    Dealer dealer;
    RecyclerView myRecyclerView, dealerRecyclerView;
    Adapter_card myAdapterCard, dealerAdapterCard;
    Animation animation, animationReverse;
    SharedPreferences sp;
    SharedPreferences.Editor sp_e;
    DecimalFormat format = new DecimalFormat("###,###");
    ItemDecoration decoration = new ItemDecoration(-85);
    backspaceHandler bsHandler = new backspaceHandler(this);

    // i 는 setting 에서 사용함
    int i = 0;
    int mySum = 0;
    int dealerSum = 0;
    int myACount = 0;
    int dealerACount = 0;
    int iMoney = 0;
    int iTotalMoney = 0;
    boolean myA = false;
    boolean dealerA = false;
    boolean mySet = true;
    boolean dealerSet = true;
    boolean insuranceT = false;
    String money;
    String nowMoney;
    String insuranceMoney;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sp_e = sp.edit();
        Objects.requireNonNull(getSupportActionBar()).hide();

        animation = AnimationUtils.loadAnimation(Activity_Game.this, R.anim.anim_translate);
        animation.setAnimationListener(animationListener);
        animationReverse = AnimationUtils.loadAnimation(Activity_Game.this, R.anim.anim_translate_reverse);
        animationReverse.setAnimationListener(animationReverseListener);

        myRecyclerView = findViewById(R.id.game_myRecyclerView);
        dealerRecyclerView = findViewById(R.id.game_dealerRecyclerView);

        cardBack = findViewById(R.id.game_cardBack);
        countTv = findViewById(R.id.game_countTv);
        totalMoneyTv = findViewById(R.id.game_TotalMoneyTv);
        moneyTv = findViewById(R.id.game_moneyTv);
        goBtn = findViewById(R.id.game_goBtn);
        stopBtn = findViewById(R.id.game_stopBtn);

        goBtn.setEnabled(false);
        stopBtn.setEnabled(false);

        money = sp.getString("money", "100");
        setMoneyText(0, Integer.parseInt(money), 0, -1);

        goBtn.setOnClickListener(v -> moveCard(0));
        stopBtn.setOnClickListener(v -> whoWin());

        cardActivity = new Card();
        dealerCards = new ArrayList<>();
        myCards = new ArrayList<>();
        dealer = new Dealer();
        // 나눠가질 카드 뭉치 만들기 (52장 가득) > 3묶음 함
        cards = dealer.setCards();

        goIntent(100);
    }

    private void goIntent( int requestCode) {
        Intent intent = null;
        if(requestCode == 100) {
            intent = new Intent(Activity_Game.this, Activity_Popup.class);
        } else if(requestCode == 200) {
            intent = new Intent(Activity_Game.this, Activity_Popup_Insurance.class);
        }
        startActivityForResult(intent, requestCode);
    }

    private void setting() {
        // 나 2장, 딜러 2장으로 카드 셋팅 후 21확인
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if(i < 2) {
                        moveCard(0);
                    } else if(i == 2) {
                        moveCard(1);
                    } else if(i == 3) {
                        moveCard(1);
                        if(!insuranceT) {
                            blackJackCheck(0);
                        }
                    }
                    i++;
                });
            }
        };
        timer.schedule(timerTask, 0, 1300);
    }

    private void moveCard(int cnd) {
        Animation a = null;
        if(cnd == 0) {
             a = animation;
        } else if(cnd == 1) {
             a = animationReverse;
        }
        if(!insuranceT) {
            cardBack.startAnimation(a);
        }
    }

    private void go(int cnd) {
        Card card = dealer.getCard(cards);
        if(cnd == 0) {
            // 내 카드 뽑아서 화면에 보여주고 셋팅
            myCards.add(card);
            sum(1);
            myAdapterCard.setCardList(myCards, 0);
            myRecyclerView.removeAllViews();
            myRecyclerView.setAdapter(myAdapterCard);

            countTv.setText(String.valueOf(mySum));

        } else if(cnd == 1) {
            // 딜러 카드 뽑아서 화면에 셋팅
            dealerCards.add(card);
            sum(0);
            dealerAdapterCard.setCardList(dealerCards, 1);
            dealerAdapterCard.setFirst(0);
            dealerRecyclerView.setAdapter(dealerAdapterCard);
        }

        if(mySum > 21) {
            isBust(1);
        } else if(dealerSum > 21) {
            isBust(0);
        }
    }

    private void sum(int cnd) {
        if(cnd == 0) {
            // 딜러 총합 계산
            dealerSum = 0;
            dealerACount = 0;
            for(int i=0; i<dealerCards.size(); i++) {
                Card card = dealerCards.get(i);
                int num = Integer.parseInt(card.getNumber());

                if(num == 1) {
                    dealerA = true;
                    dealerSum += 11;
                    dealerACount ++;
                } else {
                    // num 과 10을 비교하여 작은 걸 가져옴
                    dealerSum += Math.min(num, 10);
                }
            }

            if(dealerSum > 21 && dealerA) {
                for(int i=0; i<dealerACount; i++) {
                    dealerSum -= 9;
                    if(dealerSum <= 21) {
                        return;
                    }
                }
            }
        }

        if (cnd == 1) {
            // 나 총합 계산
            mySum = 0;
            myACount = 0;
            for(int i=-0; i<myCards.size(); i++) {
                Card card = myCards.get(i);
                int num = Integer.parseInt(card.getNumber());

                if(num == 1) {
                    myA = true;
                    mySum += 11;
                    myACount ++;
                } else {
                    // num 과 10을 비교하여 작은 걸 가져옴
                    mySum += Math.min(num, 10);
                }
            }

            if(mySum > 21 && myA) {
                for(int i=0; i<myACount; i++) {
                    mySum -= 9;
                    if(mySum <= 21) {
                        return;
                    }
                }
            }
        }
    }

    private void blackJackCheck(int cnd) {
        // 블랙잭 (A와 10) 확인
        new Handler().postDelayed(() -> {
            goBtn.setEnabled(true);
            stopBtn.setEnabled(true);
            if(mySum == 21) {
                showDialog("Black Jack!!", "당신이 Black Jack을 달성했습니다.\n당신의 승리입니다.");
                moneyCal(3);
            } else if(dealerSum == 21) {
                if(cnd == 0) {
                    showDialog("Black Jack!!", "딜러가 Black Jack을 달성했습니다.\n당신의 패배입니다.");
                    moneyCal(0);
                } else if(cnd == 1) {
                    showDialog("Black Jack!!", "딜러가 Black Jack을 달성했지만 Insurance를 통해 배팅에 성공했습니다.");
                    money = insuranceMoney;
                    moneyCal(3);
                } else if(cnd == 1 && insuranceMoney.equals("0")) {
                    showDialog("Black Jack!!", "딜러가 Black Jack을 달성했습니다.\n추가 배팅을 하지 않아 획든한 금액은 없습니다.");
                }
            }
        }, 1300);
    }

    private void isBust(int cnd) {
        // 누군가 21을 초과했을 때
        dealerAdapterCard.setFirst(1);
        dealerRecyclerView.setAdapter(dealerAdapterCard);

        if(cnd == 0) {
            showDialog("Bust!", "딜러의 합은 " + dealerSum + "이므로 당신의 승리입니다.");
            moneyCal(2);
        } else if(cnd == 1) {
            showDialog("Bust!", "당신의 패가 21을 넘어 패배했습니다.");
            moneyCal(0);
        }
    }

    private void whoWin() {
        // 승부
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if(dealerSum < 17) {
                        moveCard(1);
                    } else if(dealerSum > 21) {
                        isBust(0);
                        timer.cancel();
                    } else {
                        dealerAdapterCard.setFirst(1);
                        dealerRecyclerView.setAdapter(dealerAdapterCard);

                        if(dealerSum > mySum) {
                            showDialog("딜러의 승리!", "딜러의 합은 " + dealerSum + "이므로 당신의 패배입니다.");
                            moneyCal(0);
                        } else if(dealerSum == mySum) {
                            showDialog("무승부!", "딜러의 합과 당신의 합이 같아 무승부입니다.");
                            moneyCal(1);
                        } else {
                            showDialog("당신의 승리!", "딜러의 합은 " + dealerSum + "이므로 당신의 승리입니다.");
                            moneyCal(2);
                        }
                        timer.cancel();
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1300);

    }

    private void moneyCal(int num) {
        iMoney = Integer.parseInt(nowMoney);
        iTotalMoney = Integer.parseInt(money);

        iTotalMoney = iTotalMoney * num;
        iMoney = iMoney + iTotalMoney;
        sp_e.putString("money", String.valueOf(iMoney));
        sp_e.commit();
    }

    private void finishGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Game.this);
        builder.setTitle("안내").setMessage("보유 금액이 없습니다.\n첫 화면에서 금액 초기화를 해주세요.");
        // 버튼의 위치때문에 말과 실행문을 반대로 함
        builder.setPositiveButton("첫 화면으로", (dialogInterface, i) -> finish());

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void showDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Game.this);
        builder.setTitle(title).setMessage(msg);
        // 버튼의 위치때문에 말과 실행문을 반대로 함
        builder.setPositiveButton("게임 종료", (dialogInterface, i) -> finish());

        builder.setNeutralButton("다시 시작", (dialogInterface, i) -> {
            if(iMoney == 0) {
                finishGame();
            } else {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) { }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(mySet) {
                // 내가 카드 받고 합을 확인하고 adpater를 통해 recyclerview 에 표현
                Card card = dealer.getCard(cards);
                myCards.add(card);
                sum(1);
                myAdapterCard = new Adapter_card(myCards);
                myRecyclerView.setLayoutManager(new LinearLayoutManager(Activity_Game.this, RecyclerView.HORIZONTAL, false));
                myRecyclerView.setAdapter(myAdapterCard);
                myRecyclerView.addItemDecoration(decoration);

                countTv.setText(String.valueOf(mySum));

                mySet = false;
            } else {
                go(0);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) { }
    };

    Animation.AnimationListener animationReverseListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) { }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(dealerSet) {
                // 딜러 카드 셋팅 > 첫 장은 보이게
                Card card = dealer.getCard(cards);
                dealerCards.add(card);
                sum(0);
                dealerAdapterCard = new Adapter_card(dealerCards);
                dealerRecyclerView.setLayoutManager(new LinearLayoutManager(Activity_Game.this, RecyclerView.HORIZONTAL, false));
                dealerRecyclerView.setAdapter(dealerAdapterCard);
                dealerRecyclerView.addItemDecoration(decoration);

                dealerSet = false;

                if(card.getNumber().equals("1") || card.getNumber().equals("10") || card.getNumber().equals("11") || card.getNumber().equals("12") || card.getNumber().equals("13")) {
                    insurance();
                }
            } else {
                go(1);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) { }
    };

    private void insurance() {
        if (nowMoney.equals("0")) {
            Toast.makeText(this, "보유 금액이 없어 추가 배팅은 넘어갑니다.", Toast.LENGTH_SHORT).show();
        } else {
            goIntent(200);
            insuranceT = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100) {
            if(resultCode == RESULT_OK) {
                if(data != null) {
                    bettingAfter(data, 0);
                }
            }
        } else if(requestCode == 200) {
            if(resultCode == RESULT_OK) {
                if(data != null) {
                    bettingAfter(data, 1);

                    insuranceT = false;
                    moveCard(1);
                    blackJackCheck(1);
                }
            }
        }
    }

    private void bettingAfter(Intent data, int cnd) {
        nowMoney = data.getStringExtra("resultMoney");
        insuranceMoney = data.getStringExtra("insuranceMoney");

        int i1 = 0;
        int i2 = Integer.parseInt(nowMoney);
        int i3 = 0;

        if(cnd == 0) {
            money = data.getStringExtra("money");
            i1 = Integer.parseInt(money);
        } else if(cnd == 1) {
            i1 = Integer.parseInt(money);
            i3 = Integer.parseInt(insuranceMoney);
            i1 = i1 + i3;
        }

        setMoneyText(i1, i2, i3, cnd);
    }

    @SuppressLint("SetTextI18n")
    private void setMoneyText(int battingMoney, int myMoney, int insuranceMoney, int cnd) {
        String battingMsg = "현재 배팅액 : ";

        if(myMoney < 10000) {
            String s1 = format.format(myMoney);
            moneyTv.setText("보유금액 : " + s1 + "만원");
        } else if(myMoney%10000 == 0) {
            int i1 = myMoney / 10000;
            moneyTv.setText("보유금액 : " + i1 + "억원");
        } else {
            int i1 = myMoney / 10000;
            int i2 = myMoney % 10000;
            String s1 = format.format(i1);
            String s2 = format.format(i2);
            moneyTv.setText("보유금액 : " + s1 + "억 " + s2 + "만원");
        }

        if(battingMoney < 10000) {
            String s1 = format.format(battingMoney);
            battingMsg += s1 + "만원";
        } else if(battingMoney%10000 == 0) {
            int i1 = battingMoney / 10000;
            battingMsg += i1 + "억원";
        } else {
            int i1 = battingMoney / 10000;
            int i2 = battingMoney % 10000;
            String s1 = format.format(i1);
            String s2 = format.format(i2);
            battingMsg += s1 + "억 " + s2 + "만원";
        }

        if(insuranceMoney < 10000) {
            String s1 = format.format(insuranceMoney);
            totalMoneyTv.setText("(Insurance Money : " + s1 + "만원)\n" + battingMsg);
        } else if(insuranceMoney%10000 == 0) {
            int i1 = insuranceMoney / 10000;
            totalMoneyTv.setText("(Insurance Money : " + i1 + "억원)\n" + battingMsg);
        } else {
            int i1 = insuranceMoney / 10000;
            int i2 = insuranceMoney % 10000;
            String s1 = format.format(i1);
            String s2 = format.format(i2);
            totalMoneyTv.setText("(Insurance Money : " + s1 + "억 " + s2 + "만원)\n" + battingMsg);
        }

        if(cnd == 0) {
            setting();
        }
    }

    @Override
    public void onBackPressed() {
        bsHandler.onBackPressed("'뒤로가기' 버튼을 한번 더 누르면 종료됩니다.\n또한 배팅한 금액은 돌려주지 않습니다.", 0);
    }
}