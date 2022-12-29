package com.example.black_jack;

import android.util.Log;
import android.widget.ImageView;

public class Card {
    public String shape;
    public String number;

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setMyImage(Card card, ImageView imageView) {
        String shape = card.getShape();
        String num = card.getNumber();
        String shapeNum = shape + num;
        Log.i("Card", shapeNum);

        switch (shapeNum) {
            case "c1":
                imageView.setImageResource(R.drawable.c1);
                break;
            case "c2" :
                imageView.setImageResource(R.drawable.c2);
                break;
            case "c3" :
                imageView.setImageResource(R.drawable.c3);
                break;
            case "c4" :
                imageView.setImageResource(R.drawable.c4);
                break;
            case "c5" :
                imageView.setImageResource(R.drawable.c5);
                break;
            case "c6" :
                imageView.setImageResource(R.drawable.c6);
                break;
            case "c7" :
                imageView.setImageResource(R.drawable.c7);
                break;
            case "c8" :
                imageView.setImageResource(R.drawable.c8);
                break;
            case "c9" :
                imageView.setImageResource(R.drawable.c9);
                break;
            case "c10" :
                imageView.setImageResource(R.drawable.c10);
                break;
            case "c11" :
                imageView.setImageResource(R.drawable.c11);
                break;
            case "c12" :
                imageView.setImageResource(R.drawable.c12);
                break;
            case "c13" :
                imageView.setImageResource(R.drawable.c13);
                break;
            case "d1":
                imageView.setImageResource(R.drawable.d1);
                break;
            case "d2" :
                imageView.setImageResource(R.drawable.d2);
                break;
            case "d3" :
                imageView.setImageResource(R.drawable.d3);
                break;
            case "d4" :
                imageView.setImageResource(R.drawable.d4);
                break;
            case "d5" :
                imageView.setImageResource(R.drawable.d5);
                break;
            case "d6" :
                imageView.setImageResource(R.drawable.d6);
                break;
            case "d7" :
                imageView.setImageResource(R.drawable.d7);
                break;
            case "d8" :
                imageView.setImageResource(R.drawable.d8);
                break;
            case "d9" :
                imageView.setImageResource(R.drawable.d9);
                break;
            case "d10" :
                imageView.setImageResource(R.drawable.d10);
                break;
            case "d11" :
                imageView.setImageResource(R.drawable.d11);
                break;
            case "d12" :
                imageView.setImageResource(R.drawable.d12);
                break;
            case "d13" :
                imageView.setImageResource(R.drawable.d13);
                break;
            case "h1":
                imageView.setImageResource(R.drawable.h1);
                break;
            case "h2" :
                imageView.setImageResource(R.drawable.h2);
                break;
            case "h3" :
                imageView.setImageResource(R.drawable.h3);
                break;
            case "h4" :
                imageView.setImageResource(R.drawable.h4);
                break;
            case "h5" :
                imageView.setImageResource(R.drawable.h5);
                break;
            case "h6" :
                imageView.setImageResource(R.drawable.h6);
                break;
            case "h7" :
                imageView.setImageResource(R.drawable.h7);
                break;
            case "h8" :
                imageView.setImageResource(R.drawable.h8);
                break;
            case "h9" :
                imageView.setImageResource(R.drawable.h9);
                break;
            case "h10" :
                imageView.setImageResource(R.drawable.h10);
                break;
            case "h11" :
                imageView.setImageResource(R.drawable.h11);
                break;
            case "h12" :
                imageView.setImageResource(R.drawable.h12);
                break;
            case "h13" :
                imageView.setImageResource(R.drawable.h13);
                break;
            case "s1":
                imageView.setImageResource(R.drawable.s1);
                break;
            case "s2" :
                imageView.setImageResource(R.drawable.s2);
                break;
            case "s3" :
                imageView.setImageResource(R.drawable.s3);
                break;
            case "s4" :
                imageView.setImageResource(R.drawable.s4);
                break;
            case "s5" :
                imageView.setImageResource(R.drawable.s5);
                break;
            case "s6" :
                imageView.setImageResource(R.drawable.s6);
                break;
            case "s7" :
                imageView.setImageResource(R.drawable.s7);
                break;
            case "s8" :
                imageView.setImageResource(R.drawable.s8);
                break;
            case "s9" :
                imageView.setImageResource(R.drawable.s9);
                break;
            case "s10" :
                imageView.setImageResource(R.drawable.s10);
                break;
            case "s11" :
                imageView.setImageResource(R.drawable.s11);
                break;
            case "s12" :
                imageView.setImageResource(R.drawable.s12);
                break;
            case "s13" :
                imageView.setImageResource(R.drawable.s13);
                break;
        }
    }
}
