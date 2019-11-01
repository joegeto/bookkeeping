package com.example.bookkeeping.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bookkeeping.MainActivity;
import com.example.bookkeeping.adapter.ListAdapter;
import com.example.bookkeeping.entity.Record;
import com.example.bookkeeping.fragment.ListFragment;
import com.example.bookkeeping.R;
import com.example.bookkeeping.model.ListTable;
import com.example.bookkeeping.util.MyUtil;
import com.example.bookkeeping.widget.DatePickerDIY;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ListActivity extends AppCompatActivity implements DatePickerDIY.IOnDateSetListener, ListAdapter.IAdapterListener {
    public static final String RESOURCE_ID = "recordId";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";
    public static final String HOUR = "hour";
    public static final String MINUTE = "minute";

    private static final String TAG = "ListActivity";
    private ListFragment listFragment;
    private ImageView addButton;
    private LinearLayout queryTime;
    private static SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvYear;
    private TextView tvMonth;
    private TextView tvTotalMoneyOfMonth;

    private DatePickerDIY dpDIY;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private int type;
    private String typeDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initView();
        // 添加返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        listFragment.initAdapter(ListActivity.this,ListActivity.this, ListActivity.this, type);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorRed);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int tvYearValue = Integer.parseInt(tvYear.getText().toString());
                int tvMonthValue = Integer.parseInt(tvMonth.getText().toString());
                int nextMonth = MyUtil.monthPlus(tvMonthValue);
                if (nextMonth == 1) {
                    tvYearValue = tvYearValue + 1;
                }
                listFragment.refreshAdapter(type, tvYearValue, nextMonth);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, AddDialogActivity.class);
                intent.putExtra(MainActivity.TYPE, type);
                intent.putExtra(MainActivity.TYPE_DESC, typeDesc);
                startActivityForResult(intent, 1);
            }
        });
        queryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpDIY.toggleVisible();
            }
        });
    }

    private void initView() {
        type = getIntent().getIntExtra(MainActivity.TYPE, -1); // 接收intent数据
        typeDesc = getIntent().getStringExtra(MainActivity.TYPE_DESC);
        dpDIY = new DatePickerDIY(ListActivity.this, ListActivity.this, true, true, false);

        listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.frag_list);
        addButton = (ImageView) findViewById(R.id.add_button);
        queryTime = (LinearLayout) findViewById(R.id.tap_query_time);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        tvYear = (TextView) findViewById(R.id.tv_year);
        tvMonth = (TextView) findViewById(R.id.tv_month);
        tvTotalMoneyOfMonth = (TextView) findViewById(R.id.tv_total_money_of_month);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (resultCode) {
            case 1:
                // 来自添加弹窗
                Calendar c = Calendar.getInstance();
                int lastYear = c.get(Calendar.YEAR);
                int lastMonth = c.get(Calendar.MONTH) + 1;
                listFragment.refreshAdapter(type, lastYear, lastMonth);
                break;
            case 2:
                // 来自修改弹窗
                listFragment.refreshAdapter(type, Integer.parseInt(tvYear.getText().toString()), Integer.parseInt(tvMonth.getText().toString()));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(Date date, int dType) {
        String str = mFormatter.format(date);
        String[] s = str.split("-");
        if (dType == 2) {
            listFragment.refreshAdapter(type, Integer.parseInt(s[0]), Integer.parseInt(s[1]));
        }
    }

    @Override
    public void onSetBtnClick(View view, Record record) {
        // 当前点击item的时间参数
        Date date = MyUtil.convertToDate(record.getTime());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        Intent intent = new Intent(ListActivity.this, SetDialogActivity.class);
        intent.putExtra(RESOURCE_ID, record.getId());
        intent.putExtra(YEAR, year);
        intent.putExtra(MONTH, month);
        intent.putExtra(DAY, day);
        intent.putExtra(HOUR, hour);
        intent.putExtra(MINUTE, minute);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onDelBtnClick(View view, int id) {
        LitePal.deleteAll(ListTable.class, "id = ?", String.valueOf(id));
        listFragment.refreshAdapter(type, Integer.parseInt(tvYear.getText().toString()), Integer.parseInt(tvMonth.getText().toString()));
    }
}
