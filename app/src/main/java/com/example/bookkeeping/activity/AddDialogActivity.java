package com.example.bookkeeping.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookkeeping.baseActivity.customDialogActivity;
import com.example.bookkeeping.model.ListTable;
import com.example.bookkeeping.R;
import com.example.bookkeeping.util.MyUtil;

import java.util.Date;

public class AddDialogActivity extends customDialogActivity {
    private static final String TAG = "AddDialogActivity";
    private int type;
    private String typeDesc;
    private LinearLayout includeAddDialog;
    private Button cancelButton;
    private Button confirmButton;
    private TextView moneyText;
    private TextView typeText;
    private Long currentTimeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dialog);
        // 接收intent数据
        type = getIntent().getIntExtra("type", -1);
        typeDesc = getIntent().getStringExtra("typeDesc");

        initTime();
        typeText = (TextView) findViewById(R.id.type_text);
        typeText.setText(typeDesc);
        moneyText = (TextView) findViewById(R.id.money_text);
        includeAddDialog = (LinearLayout) findViewById(R.id.include_add_dialog);
        cancelButton = (Button) includeAddDialog.findViewById(R.id.cancel);
        confirmButton = (Button) includeAddDialog.findViewById(R.id.confirm);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeActivity();
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String money = moneyText.getText().toString();
                float moneyReal = 0;
                if (!TextUtils.isEmpty(money)) {
                    moneyReal = Float.parseFloat(money);
                }
                if (moneyReal != 0) {
                    // 写入数据
                    ListTable list = new ListTable();
                    list.setType(type);
                    list.setTypeDesc(typeDesc);
                    list.setMoney(moneyReal);
                    list.setTime(currentTimeStamp);
                    list.save();

                    Toast.makeText(AddDialogActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(1, intent);
                    closeActivity();
                } else {
                    Toast.makeText(AddDialogActivity.this, "请填写金额", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void initTime() {
        currentTimeStamp = new Date().getTime() + MyUtil.EIGHT_HOUR;
    }
}
