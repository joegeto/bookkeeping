package com.example.bookkeeping.activity;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bookkeeping.baseActivity.customDialogActivity;
import com.example.bookkeeping.model.ListTable;
import com.example.bookkeeping.R;
import com.example.bookkeeping.util.MyUtil;
import com.example.bookkeeping.util.ToastUtil;

public class SetDialogActivity extends customDialogActivity {
    private static final String TAG = "SetDialogActivity";
    private LinearLayout includeSetDialog;
    private Button cancelButton;
    private Button confirmButton;
    private TextView tapTime;
    private TextView moneyText;
    private TextView descText;
    private int itemYear;
    private int itemMonth;
    private int itemDay;
    private int itemHour;
    private int itemMinute;
    private int recordId;
    private String updateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_dialog);

        recordId = getIntent().getIntExtra(ListActivity.RESOURCE_ID, -1);
        itemYear = getIntent().getIntExtra(ListActivity.YEAR, -1);
        itemMonth = getIntent().getIntExtra(ListActivity.MONTH, -1);
        itemDay = getIntent().getIntExtra(ListActivity.DAY, -1);
        itemHour = getIntent().getIntExtra(ListActivity.HOUR, -1);
        itemMinute = getIntent().getIntExtra(ListActivity.MINUTE, -1);

        moneyText = (TextView) findViewById(R.id.money_text);
        descText = (TextView) findViewById(R.id.desc_text);
        tapTime = (TextView) findViewById(R.id.tap_time);
        includeSetDialog = (LinearLayout) findViewById(R.id.include_set_dialog);
        cancelButton = (Button) includeSetDialog.findViewById(R.id.cancel);
        confirmButton = (Button) includeSetDialog.findViewById(R.id.confirm);

        tapTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetDialogActivity.this, TimePickerActivity.class);
                intent.putExtra(ListActivity.YEAR, itemYear);
                intent.putExtra(ListActivity.MONTH, itemMonth);
                intent.putExtra(ListActivity.DAY, itemDay);
                intent.putExtra(ListActivity.HOUR, itemHour);
                intent.putExtra(ListActivity.MINUTE, itemMinute);
                startActivityForResult(intent, 1);
            }
        });
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
                String desc = descText.getText().toString();
                float moneyReal = 0;
                if (!TextUtils.isEmpty(money)) {
                    moneyReal = Float.parseFloat(money);
                }

                if (updateTime == null && moneyReal == 0 && TextUtils.isEmpty(desc)) {
                    ToastUtil.makeText(SetDialogActivity.this, "至少要填写一项");
                    return;
                }

                ListTable list = new ListTable();
                if (updateTime != null) {
                    Long updateTimeMillSec = MyUtil.convertToDate(updateTime).getTime();
                    list.setTime(updateTimeMillSec);
                }
                if (moneyReal != 0) {
                    list.setMoney(moneyReal);
                }
                if (!TextUtils.isEmpty(desc)) {
                    list.setDescription(desc);
                }
                list.updateAll("id = ?", String.valueOf(recordId));

                ToastUtil.makeText(SetDialogActivity.this, "修改成功");
                Intent intent = new Intent();
                setResult(2, intent);
                closeActivity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    updateTime = data.getStringExtra(TimePickerActivity.UPDATE_TIME);
                    // 更新时间文本
                    tapTime.setText(updateTime);
                }
                break;
            default:
        }
    }
}
