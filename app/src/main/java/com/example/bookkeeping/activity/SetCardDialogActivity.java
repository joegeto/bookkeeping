package com.example.bookkeeping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.bookkeeping.MainActivity;
import com.example.bookkeeping.R;
import com.example.bookkeeping.baseActivity.customDialogActivity;
import com.example.bookkeeping.model.CardTable;
import com.example.bookkeeping.util.ToastUtil;

public class SetCardDialogActivity extends customDialogActivity {
    private LinearLayout includeAddCardDialog;
    private Button cancelButton, confirmButton;
    private EditText cardName;

    private int cardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card_dialog);

        cardId = getIntent().getIntExtra(MainActivity.CARD_ID, -1);

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
                    CardTable cardTable = new CardTable();
                    cardTable.setTypeDesc(cardNameText);
                    cardTable.updateAll("id = ?", String.valueOf(cardId));

                    ToastUtil.makeText(SetCardDialogActivity.this, "修改成功");
                    Intent intent = new Intent();
                    setResult(2, intent);
                    closeActivity();
                } else {
                    ToastUtil.makeText(SetCardDialogActivity.this, "请填写清单名");
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
}
