package com.example.black_jack;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Dealer {

    public ArrayList<Card> setCards() {
        ArrayList<Card> cards = new ArrayList<>();

        for(int i=0; i<156; i++) {
            Card card = new Card();
            int v1 = i%4;
            if(v1 == 0) {
                card.setShape("c");
            } else if(v1 == 1) {
                card.setShape("d");
            } else if(v1 == 2) {
                card.setShape("h");
            } else if(v1 == 3) {
                card.setShape("s");
            }

            int v2 = i%13;
            if(v2 == 0) {
                card.setNumber("1");
            } else if(v2 == 1) {
                card.setNumber("2");
            } else if(v2 == 2) {
                card.setNumber("3");
            } else if(v2 == 3) {
                card.setNumber("4");
            } else if(v2 == 4) {
                card.setNumber("5");
            } else if(v2 == 5) {
                card.setNumber("6");
            } else if(v2 == 6) {
                card.setNumber("7");
            } else if(v2 == 7) {
                card.setNumber("8");
            } else if(v2 == 8) {
                card.setNumber("9");
            } else if(v2 == 9) {
                card.setNumber("10");
            } else if(v2 == 10) {
                card.setNumber("11");
            } else if(v2 == 11) {
                card.setNumber("12");
            } else if(v2 == 12) {
                card.setNumber("13");
            }
            cards.add(card);
        }
        return cards;
    }

    public Card getCard(ArrayList<Card> cards) {
        int size = cards.size();
        int i = (int)(Math.random() * size);
        Card card = cards.get(i);
        cards.remove(i);
        return card;
    }
}
