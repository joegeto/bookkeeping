package com.example.bookkeeping.baseActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class customDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    protected void closeActivity() {
        if (!customDialogActivity.this.isFinishing()) {
            customDialogActivity.this.finish();
        }
    }
}
