package com.example.bookkeeping;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

public class SetDialogActivity extends AppCompatActivity {
    private static final String TAG = "SetDialogActivity";
    private Button cancelButton;
    private Button confirmButton;
    private TextView tapTime;
    private TextView moneyText;
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

        recordId = getIntent().getIntExtra("recordId", -1);
        itemYear = getIntent().getIntExtra("year", -1);
        itemMonth = getIntent().getIntExtra("month", -1);
        itemDay = getIntent().getIntExtra("day", -1);
        itemHour = getIntent().getIntExtra("hour", -1);
        itemMinute = getIntent().getIntExtra("minute", -1);

        moneyText = (TextView) findViewById(R.id.money_text);
        tapTime = (TextView) findViewById(R.id.tap_time);
        cancelButton = (Button) findViewById(R.id.cancel_add);
        confirmButton = (Button) findViewById(R.id.confirm_add);

        tapTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetDialogActivity.this, TimePickerActivity.class);
                intent.putExtra("year", itemYear);
                intent.putExtra("month", itemMonth);
                intent.putExtra("day", itemDay);
                intent.putExtra("hour", itemHour);
                intent.putExtra("minute", itemMinute);
                startActivityForResult(intent, 1);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSetDialogActivity();
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
                    // 修改数据
                    ListTable list = new ListTable();
                    list.setMoney(moneyReal);
                    if (updateTime != null) {
                        Long updateTimeMillSec = MyUtil.convertToDate(updateTime).getTime();
                        list.setTime(updateTimeMillSec);
                    }
                    list.updateAll("id = ?", String.valueOf(recordId));

                    Toast.makeText(SetDialogActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(2, intent);
                    closeSetDialogActivity();
                } else {
                    Toast.makeText(SetDialogActivity.this, "请填写金额", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    updateTime = data.getStringExtra("updateTime");
                    // 更新时间文本
                    tapTime.setText(updateTime);
                }
                break;
            default:
        }
    }

    public void closeSetDialogActivity() {
        if (!SetDialogActivity.this.isFinishing()) {
            SetDialogActivity.this.finish();
        }
    }
}
