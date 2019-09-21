package com.example.bookkeeping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bookkeeping.R;
import com.example.bookkeeping.baseActivity.customDialogActivity;
import com.example.bookkeeping.model.CardTable;

import org.litepal.LitePal;

import java.util.Random;

public class AddCardDialogActivity extends customDialogActivity {
    private static final String TAG = "AddCardDialogActivity";
    private final float defaultMoney = 0;   // 默认的待写入金额
    private LinearLayout includeAddCardDialog;
    private Button cancelButton, confirmButton;
    private EditText cardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card_dialog);

        initView();
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeActivity();
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cardNameText = cardName.getText().toString();
                if (!TextUtils.isEmpty(cardNameText)) {
                    int rColor = randomColor();
                    int maxType = getMaxType();

                    // 写入数据
                    CardTable cardTable = new CardTable();
                    cardTable.setBgcId(rColor);
                    cardTable.setMoney(defaultMoney);
                    cardTable.setType(++maxType);
                    cardTable.setTypeDesc(cardNameText);
                    cardTable.save();

                    Toast.makeText(AddCardDialogActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(1, intent);
                    closeActivity();
                } else {
                    Toast.makeText(AddCardDialogActivity.this, "请填写清单名", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void initView() {
        cardName = (EditText) findViewById(R.id.card_name);
        includeAddCardDialog = (LinearLayout) findViewById(R.id.include_add_card_dialog);
        cancelButton = (Button) includeAddCardDialog.findViewById(R.id.cancel);
        confirmButton = (Button) includeAddCardDialog.findViewById(R.id.confirm);
    }
    private int randomColor() {
        Random ra = new Random();
        int res = ra.nextInt(4) + 1;
        return res;
    }
    // 查询CardTable表中type最大的值，最低为1
    private int getMaxType() {
        int maxType = LitePal.max(CardTable.class, "type", int.class);
        return Math.max(maxType, 1);
    }
}
