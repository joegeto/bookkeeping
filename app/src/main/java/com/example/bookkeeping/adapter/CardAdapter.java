package com.example.bookkeeping.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookkeeping.R;
import com.example.bookkeeping.entity.Card;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private static final String TAG = "CardAdapter";
    private List<Card> mCardList;
    private ICardListener mListener;

    public CardAdapter(List<Card> cardList, ICardListener listener) {
        mCardList = cardList;
        mListener = listener;
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cardWrapper;
        LinearLayout roundView;
        TextView tvName;
        TextView tvTotalMoney;

        public ViewHolder(View view) {
            super(view);
            cardWrapper = (LinearLayout) view.findViewById(R.id.card_wrapper);
            roundView = (LinearLayout) view.findViewById(R.id.round_view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvTotalMoney = (TextView) view.findViewById(R.id.tv_total_money);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Card card = mCardList.get(position);
        holder.roundView.setBackgroundResource(card.getBgcId());
        holder.tvName.setText(card.getName());
        holder.tvTotalMoney.setText(card.getTotalMoney());

        // 盒子点击事件
        holder.cardWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = holder.getLayoutPosition();
                Card card = mCardList.get(n);
                mListener.onCardClick(view, card.getType(), card.getName());
            }
        });
        // 盒子长按事件
        holder.cardWrapper.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int n = holder.getLayoutPosition();
                Card card = mCardList.get(n);
                int cardId = card.getId();
                int cardType = card.getType();
                if (cardId != 0) {
                    mListener.onCardLongClick(view, cardId, cardType);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }
    public interface ICardListener {
        void onCardClick(View view, int type, String typeDesc);
        void onCardLongClick(View view, int id, int type);
    }
}
