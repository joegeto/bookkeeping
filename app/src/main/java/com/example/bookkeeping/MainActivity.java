package com.example.bookkeeping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button addList;
    private LinearLayout incomeButton;
    private LinearLayout expandButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addList = (Button) findViewById(R.id.add_list);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, R.string.waiting, Toast.LENGTH_SHORT).show();
            }
        });
        incomeButton = (LinearLayout) findViewById(R.id.left_layout);
        incomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionStart(ListActivity.class, 0, "收入");
            }
        });
    }
    public void actionStart(Class cls, int type, String typeDesc) {
        Intent intent = new Intent(MainActivity.this, cls);
        intent.putExtra("type", type);
        intent.putExtra("typeDesc", typeDesc);
        startActivity(intent);
    }
}
