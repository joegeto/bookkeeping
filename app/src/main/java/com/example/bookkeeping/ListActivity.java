package com.example.bookkeeping;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.litepal.tablemanager.Connector;

import java.util.Calendar;
import java.util.Date;

public class ListActivity extends AppCompatActivity {
    private static final String TAG = "ListActivity";
    private ListFragment listFragment;
    private TextView tvYear;
    private TextView tvMonth;
    private ImageView addButton;
    private LinearLayout queryTime;
    private int type;
    private String typeDesc;
    private int year;
    private int month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initTime();
        type = getIntent().getIntExtra("type", -1); // 接收intent数据
        typeDesc = getIntent().getStringExtra("typeDesc");
        createDatabase();
        // 获取句柄
        listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.frag_list);
        tvYear = (TextView) findViewById(R.id.tv_year);
        tvMonth = (TextView) findViewById(R.id.tv_month);
        addButton = (ImageView) findViewById(R.id.add_button);
        queryTime = (LinearLayout) findViewById(R.id.tap_query_time);
        // 默认查询当前的年月清单
        tvYear.setText(String.valueOf(year));
        tvMonth.setText(String.valueOf(month));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, AddDialogActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("typeDesc", typeDesc);
                startActivityForResult(intent, 1);
            }
        });
        // todo
        // 查询时间功能
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (resultCode) {
            case 1:
                // 来自添加弹窗
                listFragment.refreshAdapter(ListActivity.this);
                break;
            case 2:
                // 来自修改弹窗
                listFragment.refreshAdapter(ListActivity.this);
                break;
            default:
                break;
        }
    }

    public void initTime() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
    }

    public void createDatabase() {
        Connector.getDatabase();
    }
}
