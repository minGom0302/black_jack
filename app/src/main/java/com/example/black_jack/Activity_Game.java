package com.example.black_jack;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class Activity_Game extends AppCompatActivity {
    TextView countTv;
    ImageView cardBack, dealer_back01, dealer_back02, dealer_back03, dealer_back04, dealer_back05, myCard01, myCard02, myCard03, myCard04, myCard05;
    ImageView cardBack01, cardBack02, cardBack03, cardBack04;
    Button goBtn, stopBtn;

    Card cardActivity;
    ArrayList<Card> cards;
    ArrayList<Card> dealerCards;
    ArrayList<Card> myCards;
    Dealer dealer;

    int mySum = 0;
    int dealerSum = 0;
    int count = 0;
    int myACount = 0;
    int dealerACount = 0;
    boolean myA = false;
    boolean dealerA = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Objects.requireNonNull(getSupportActionBar()).hide();

        cardBack01 = findViewById(R.id.game_cardBack01);
        cardBack02 = findViewById(R.id.game_cardBack02);
        cardBack03 = findViewById(R.id.game_cardBack03);
        cardBack04 = findViewById(R.id.game_cardBack04);
        countTv = findViewById(R.id.game_countTv);
        cardBack = findViewById(R.id.game_cardBack);
        dealer_back01 = findViewById(R.id.game_img_back01);
        dealer_back02 = findViewById(R.id.game_img_back02);
        dealer_back03 = findViewById(R.id.game_img_back03);
        dealer_back04 = findViewById(R.id.game_img_back04);
        dealer_back05 = findViewById(R.id.game_img_back05);
        myCard01 = findViewById(R.id.game_myCard01);
        myCard02 = findViewById(R.id.game_myCard02);
        myCard03 = findViewById(R.id.game_myCard03);
        myCard04 = findViewById(R.id.game_myCard04);
        myCard05 = findViewById(R.id.game_myCard05);
        goBtn = findViewById(R.id.game_goBtn);
        stopBtn = findViewById(R.id.game_stopBtn);

        goBtn.setOnClickListener(v -> {
            moveCard();
        });
        stopBtn.setOnClickListener(v -> {
            whoWin();
        });

        setting();
    }

    private void moveCard() {
        count ++;
        ImageView img = null;
        switch (count) {
            case 1 :
                img = cardBack01;
                break;
            case 2 :
                img = cardBack02;
                break;
            case 3 :
                img = cardBack03;
                break;
            case 4 :
                img = cardBack04;
                break;
        }

        if(img != null) {
            ImageView finalImg = img;
            img.animate().translationY(500).rotation(180).setDuration(1000).withLayer().withEndAction(new Runnable() {
                @Override
                public void run() {
                    finalImg.setVisibility(View.GONE);
                    go();
                }
            });
        }
    }

    private void go() {
        myCards.add(dealer.getCard(cards));
        sum(1);
        switch (count) {
            case 1 :
                cardActivity.setMyImage(myCards.get(count), myCard02);
                myCard02.setVisibility(View.VISIBLE);
                break;
            case 2 :
                cardActivity.setMyImage(myCards.get(count), myCard03);
                myCard03.setVisibility(View.VISIBLE);
                break;
            case 3 :
                cardActivity.setMyImage(myCards.get(count), myCard04);
                myCard04.setVisibility(View.VISIBLE);
                break;
            case 4 :
                cardActivity.setMyImage(myCards.get(count), myCard05);
                myCard05.setVisibility(View.VISIBLE);
                break;
        }
        myCard01.setVisibility(View.VISIBLE);
        countTv.setText(String.valueOf(mySum));

        if(mySum > 21) {
            isBust(1);
        }
    }

    private void setting() {
        // 딜러 먼저 카드 셋팅하고 나 go/stop
        cardActivity = new Card();
        dealerCards = new ArrayList<>();
        myCards = new ArrayList<>();
        dealer = new Dealer();
        // 나눠가질 카드 뭉치 만들기 (54장 가득)
        cards = dealer.setCards();

        myCards.add(dealer.getCard(cards));

        sum(1);

        cardActivity.setMyImage(myCards.get(0), myCard01);
        myCard01.setVisibility(View.VISIBLE);

        countTv.setText(String.valueOf(mySum));

        // 딜러와 카드 하나씩 나눠가지면서 2장씩 가져야함
        for(int i=0; i<5; i++) {
            if(dealerSum < 17) {
                Card card = dealer.getCard(cards);
                dealerCards.add(card);
                Log.i("dealer card", card.getShape() + card.getNumber());
                //dealerCards.add(dealer.getCard(cards));
                sum(0);
                Log.i("dealer sum",  i + "번째 합 : " + dealerSum);
                if(i == 0) {
                    cardActivity.setMyImage(card, dealer_back01);
                    dealer_back01.setVisibility(View.VISIBLE);
                } else if(i == 1) {
                    dealer_back02.setVisibility(View.VISIBLE);
                } else if(i == 2) {
                    dealer_back03.setVisibility(View.VISIBLE);
                } else if(i == 3) {
                    dealer_back04.setVisibility(View.VISIBLE);
                } else {
                    dealer_back05.setVisibility(View.VISIBLE);
                }
            } else if(dealerSum > 21) {
                isBust(0);
            }
        }
    }

    private void sum(int cnd) {
        if(cnd == 0) {
            dealerSum = 0;
            dealerACount = 0;
            for(int i=0; i<dealerCards.size(); i++) {
                Card card = dealerCards.get(i);
                int num = Integer.parseInt(card.getNumber());

                if(num == 1) {
                    dealerA = true;
                    dealerSum += 10;
                    dealerACount ++;
                } else {
                    // num 과 10을 비교하여 작은 걸 가져옴
                    dealerSum += Math.min(num, 10);
                }
            }

            if(dealerSum >= 21 && dealerA) {
                for(int i=0; i<dealerACount; i++) {
                    dealerSum -= 9;
                    if(dealerSum <= 21) {
                        return;
                    }
                }
            }
        }

        if (cnd == 1) {
            mySum = 0;
            myACount = 0;
            for(int i=-0; i<myCards.size(); i++) {
                Card card = myCards.get(i);
                int num = Integer.parseInt(card.getNumber());

                if(num == 1) {
                    Log.i("my card num", "카드 1 일치하여 들어옴");
                    myA = true;
                    mySum += 10;
                    myACount ++;
                } else {
                    Log.i("my Card Num", "카드 1 이외의 숫자들임");
                    // num 과 10을 비교하여 작은 걸 가져옴
                    mySum += Math.min(num, 10);
                }
            }

            if(mySum >= 21 && myA) {
                for(int i=0; i<myACount; i++) {
                    mySum -= 9;
                    Log.i("mySum", "현재 내 합계 : " + mySum);
                    if(mySum <= 21) {
                        return;
                    }
                }
            }
            Log.i("mySum", "현재 내 합계 : " + mySum);
        }
    }

    private void isBust(int cnd) {
        showCard();
        if(cnd == 0) {
            showDialog("Bust!", "딜러의 합은 " + dealerSum + "이므로 당신의 승리입니다.");
        } else if(cnd == 1) {
            showDialog("Bust!", "당신의 패가 21을 넘어 패배했습니다.");
        }
    }

    private void whoWin() {
        showCard();
        if(dealerSum > mySum) {
            showDialog("딜러의 승리!", "딜러의 합은 " + dealerSum + "이므로 당신의 패배입니다.");
        } else if(dealerSum == mySum) {
            showDialog("무승부!", "딜러의 합과 당신의 합이 같아 무승부입니다.");
        } else {
            showDialog("당신의 승리!", "딜러의 합은 " + dealerSum + "이므로 당신의 승리입니다.");
        }
    }

    private void showCard() {
        // 카드 앞면으로 바꾸기
        for(int i=1; i<dealerCards.size(); i++) {
            Card card = dealerCards.get(i);

            if(i == 1) {
                cardActivity.setMyImage(card, dealer_back02);
            } else if(i == 2) {
                cardActivity.setMyImage(card, dealer_back03);
            } else if(i == 3) {
                cardActivity.setMyImage(card, dealer_back04);
            } else if(i == 4) {
                cardActivity.setMyImage(card, dealer_back05);
            }
        }
    }

    private void showDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Game.this);
        builder.setTitle(title).setMessage(msg);
        // 버튼의 위치때문에 말과 실행문을 반대로 함
        builder.setPositiveButton("게임 종료", (dialogInterface, i) -> finish());

        builder.setNeutralButton("다시 시작", (dialogInterface, i) -> {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}