package com.example.bookkeeping;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.bookkeeping.activity.AddCardDialogActivity;
import com.example.bookkeeping.activity.ListActivity;
import com.example.bookkeeping.adapter.CardAdapter;
import com.example.bookkeeping.entity.Card;
import com.example.bookkeeping.model.CardTable;
import com.example.bookkeeping.model.ListTable;
import com.example.bookkeeping.util.Colors;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CardAdapter.ICardListener {
    private static final String TAG = "MainActivity";
    private RecyclerView rcView;
    private CardAdapter adapter;
    private LinearLayoutManager layoutManager;
    private RelativeLayout addCardBtn;

    private List<Card> cardList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDatabase();

        rcView = (RecyclerView) findViewById(R.id.recycler_card_view);
        layoutManager = new LinearLayoutManager(this);
        adapter = new CardAdapter(getCards(), this);
        rcView.setLayoutManager(layoutManager);
        rcView.setAdapter(adapter);
        addCardBtn = (RelativeLayout) findViewById(R.id.add_card_btn);

        addCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 打开添加清单弹窗
                Intent intent = new Intent(MainActivity.this, AddCardDialogActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    public void actionStart(Class cls, int type, String typeDesc) {
        Intent intent = new Intent(MainActivity.this, cls);
        intent.putExtra("type", type);
        intent.putExtra("typeDesc", typeDesc);
        startActivity(intent);
    }
    // 更新首页列表数据
    public void updateData() {
        getCards();
        adapter.notifyDataSetChanged();
    }
    private float queryTotalMoney(int type) {
        float res = LitePal
                .where("type = ?", String.valueOf(type))
                .sum(ListTable.class, "money", float.class);
        return res;
    }
    public List<Card> getCards() {
        cardList.clear();
        List<CardTable> cardTables = LitePal.findAll(CardTable.class);

        // 初始化两个主要实体
        Card card1 = new Card();
        card1.setType(0);
        card1.setName("收入");
        card1.setBgcId(R.drawable.round_primary);
        card1.setTotalMoney(queryTotalMoney(0));
        cardList.add(card1);

        Card card2 = new Card();
        card2.setType(1);
        card2.setName("支出");
        card2.setBgcId(R.drawable.round_red);
        card2.setTotalMoney(queryTotalMoney(1));
        cardList.add(card2);

        if (cardTables.size() > 0) {
            for (CardTable cardTable : cardTables) {
                Card card = new Card();
                card.setType(cardTable.getType());
                card.setName(cardTable.getTypeDesc());
                card.setBgcId(mapBgcId(cardTable.getBgcId()));
                card.setTotalMoney(queryTotalMoney(cardTable.getType()));
                cardList.add(card);
            }
        }
        return cardList;
    }
    private int mapBgcId(int bgcId) {
        int res;
        switch (bgcId) {
            case Colors.COLOR_ORANGE:
                res = Colors.DRAWABLE_ORANGE;
                break;
            case Colors.COLOR_GREEN:
                res = Colors.DRAWABLE_GREEN;
                break;
            case Colors.COLOR_BLUE:
                res = Colors.DRAWABLE_BLUE;
                break;
            case Colors.COLOR_CYAN:
                res = Colors.DRAWABLE_CYAN;
                break;
            default:
                res = Colors.DRAWABLE_ORANGE;
                break;
        }
        return res;
    }
    public void createDatabase() {
        Connector.getDatabase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (resultCode) {
            case 1:
                updateData();
                break;
            default:
        }
    }

    @Override
    public void onCardClick(View view, int type, String typeDesc) {
        actionStart(ListActivity.class, type, typeDesc);
    }
}
