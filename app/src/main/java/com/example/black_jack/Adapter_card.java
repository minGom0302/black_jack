package com.example.black_jack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_card extends RecyclerView.Adapter<Adapter_card.MyViewHolder> {
    List<Card> cardList;
    boolean dealer;
    boolean first = true;
    boolean show = false;

    public Adapter_card(List<Card> cardList) {
        this.cardList = cardList;
    }

    public void setFirst(int cnd) {
        this.first = true;
        if(cnd == 1) {
            show = true;
        }
    }

    public void setCardList(List<Card> cardList, int cnd) {
        this.cardList = cardList;
        if(cnd == 1) {
            this.dealer = true;
        } else {
            this.dealer = false;
        }
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder {
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.listview_card_img);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listview_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Card card = cardList.get(position);
        if(dealer) {
            if(first) {
                card.setMyImage(card, holder.imageView);
                first = false;
            } else if(show) {
                card.setMyImage(card, holder.imageView);
            } else {
                holder.imageView.setImageResource(R.drawable.back);
            }
        } else {
            card.setMyImage(card, holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }


}
