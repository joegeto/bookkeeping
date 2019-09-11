package com.example.bookkeeping;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SetDialogActivity extends AppCompatActivity {
    private static final String TAG = "SetDialogActivity";
    private Button cancelButton;
    private Button confirmButton;
    private TextView tapTime;
    private TextView moneyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_dialog);

        moneyText = (TextView) findViewById(R.id.money_text);
        tapTime = (TextView) findViewById(R.id.tap_time);
        cancelButton = (Button) findViewById(R.id.cancel_add);
        confirmButton = (Button) findViewById(R.id.confirm_add);

        tapTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetDialogActivity.this, TimePickerActivity.class);
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

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {

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
