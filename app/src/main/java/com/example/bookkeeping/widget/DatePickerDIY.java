package com.example.bookkeeping.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.example.bookkeeping.R;
import com.example.bookkeeping.util.MyUtil;

import java.util.Calendar;
import java.util.Date;

public class DatePickerDIY extends AlertDialog implements View.OnClickListener {
    private static final String TAG = "DatePickerDIY";
    private DatePicker mDatePicker;
    private LinearLayout includeDatePickerDialog;
    private Button cancelBtn, confirmBtn;

    private IOnDateSetListener onDateSetListener;

    private Boolean isShowYear = true;
    private Boolean isShowMonth = true;
    private Boolean isShowDay = true;

    public DatePickerDIY(Context context, IOnDateSetListener callback, Boolean isShowYear, Boolean isShowMonth, Boolean isShowDay) {
        super(context);
        this.isShowYear = isShowYear;
        this.isShowMonth = isShowMonth;
        this.isShowDay = isShowDay;
        this.onDateSetListener = callback;
        init();
    }
    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.date_picker_dialog, null);
        setView(view);
        mDatePicker = (DatePicker) view.findViewById(R.id.date_picker);
        includeDatePickerDialog = (LinearLayout) view.findViewById(R.id.include_date_picker_dialog);
        cancelBtn = (Button) includeDatePickerDialog.findViewById(R.id.cancel);
        confirmBtn = (Button) includeDatePickerDialog.findViewById(R.id.confirm);

        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

        Calendar c = Calendar.getInstance();
//        int curYear = c.get(Calendar.YEAR);
//        String curMonth = MyUtil.formatN(c.get(Calendar.MONTH) + 1);
//        String curDay = MyUtil.formatN(c.get(Calendar.DAY_OF_MONTH));
        mDatePicker.setMaxDate(c.getTimeInMillis());

        // 是否显示年
        if (!this.isShowYear) {
            ((ViewGroup) ((ViewGroup) mDatePicker.getChildAt(0)).getChildAt(0))
                    .getChildAt(0)
                    .setVisibility(View.GONE);
        }
        // 是否显示月
        if (!this.isShowMonth) {
            ((ViewGroup) ((ViewGroup) mDatePicker.getChildAt(0)).getChildAt(0))
                    .getChildAt(1)
                    .setVisibility(View.GONE);
        }
        // 是否显示日
        if (!this.isShowDay) {
            ((ViewGroup) ((ViewGroup) mDatePicker.getChildAt(0)).getChildAt(0))
                    .getChildAt(2)
                    .setVisibility(View.GONE);
        }
    }
    public void toggleVisible() {
        if (this == null) {
            return;
        }
        if (!this.isShowing()) {
            this.show();
        } else {
            this.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.confirm:
                // 年选择器
                if (isShowYear == true && isShowMonth == false && isShowDay == false) {
                    onConfirmBtnClick(1);
                }
                // 年月选择器
                else if (isShowYear == true && isShowMonth == true && isShowDay == false) {
                    onConfirmBtnClick(2);
                }
                // 年月日选择器
                else if (isShowYear == true && isShowMonth == true && isShowDay == true) {
                    onConfirmBtnClick(3);
                }
                dismiss();
                break;
            default:
                break;
        }
    }
    public void onConfirmBtnClick(int type) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, mDatePicker.getYear());
        c.set(Calendar.MONTH, mDatePicker.getMonth());
        c.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
        c.getTime();
        if (onDateSetListener != null) {
            onDateSetListener.onDateSet(c.getTime(), type);
        }
    }
    public interface IOnDateSetListener {
        void onDateSet(Date date, int type);
    }
}
