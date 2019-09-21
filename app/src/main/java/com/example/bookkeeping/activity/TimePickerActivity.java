package com.example.bookkeeping.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.bookkeeping.R;
import com.example.bookkeeping.baseActivity.customDialogActivity;
import com.example.bookkeeping.util.MyUtil;

public class TimePickerActivity extends customDialogActivity {
    private static final String TAG = "TimePickerActivity";
    private Context mContext;
    private int itemYear;
    private int itemMonth;
    private int itemDay;
    private int itemHour;
    private int itemMinute;
    private int updateYear;
    private int updateMonth;
    private int updateDay;
    private int updateHour;
    private int updateMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        itemYear = getIntent().getIntExtra("year", -1);
        itemMonth = getIntent().getIntExtra("month", -1);
        itemDay = getIntent().getIntExtra("day", -1);
        itemHour = getIntent().getIntExtra("hour", -1);
        itemMinute = getIntent().getIntExtra("minute", -1);

        mContext = this;
        final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                // 不回调
            }
        }, itemYear, itemMonth - 1, itemDay);
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                closeActivity();
            }
        });
        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateYear = datePickerDialog.getDatePicker().getYear();
                updateMonth = MyUtil.monthPlus(datePickerDialog.getDatePicker().getMonth()) ;
                updateDay = datePickerDialog.getDatePicker().getDayOfMonth();
                openTimePicker();
            }
        });
        datePickerDialog.show();
    }

    private void openTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                updateHour = i;
                updateMinute = i1;

                String sYear = String.valueOf(updateYear);
                String sMonth = MyUtil.formatN(updateMonth);
                String sDay = MyUtil.formatN(updateDay);
                String sHour = MyUtil.formatN(updateHour);
                String sMinute = MyUtil.formatN(updateMinute);
                String sSecond = "00";
                final String updateTime = sYear + "-" + sMonth + "-" + sDay + " " + sHour + ":" + sMinute + ":" + sSecond;

                Intent intent = new Intent();
                intent.putExtra("updateTime", updateTime);
                setResult(RESULT_OK, intent);
                closeActivity();
            }
        }, itemHour, itemMinute, true);
        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                closeActivity();
            }
        });
        timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 不回调
            }
        });
        timePickerDialog.show();
    }
}
