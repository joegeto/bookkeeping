package com.example.bookkeeping.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookkeeping.adapter.ListAdapter;
import com.example.bookkeeping.R;
import com.example.bookkeeping.entity.Record;
import com.example.bookkeeping.model.ListTable;
import com.example.bookkeeping.util.MyUtil;
import com.example.bookkeeping.widget.DatePickerDIY;
import com.example.bookkeeping.widget.SwipeLayoutManager;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListFragment extends Fragment {
    private static final String TAG = "ListFragment";
    private static TextView tvNoData;
    private static RecyclerView recyclerView;
    private static CardView stickyWrapper;
    private static TextView tvYear;
    private static TextView tvMonth;
    private static TextView tvTotalMoneyOfMonth;
    private static LinearLayoutManager layoutManager;

    private static ListAdapter adapter;
    private static List<Record> recordList = new ArrayList<>();
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_list, container, false);
        tvNoData = (TextView) view.findViewById(R.id.tv_no_data);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        stickyWrapper = (CardView) view.findViewById(R.id.sticky_wrapper);
        tvYear = (TextView) stickyWrapper.findViewById(R.id.tv_year);
        tvMonth = (TextView) stickyWrapper.findViewById(R.id.tv_month);
        tvTotalMoneyOfMonth = (TextView) stickyWrapper.findViewById(R.id.tv_total_money_of_month);
        layoutManager = new LinearLayoutManager(getActivity()); // 通信activity，采用getActivity()
        return view;
    }
    // 查询年月
    public static List<Record> getRecordsByDate(int type, int year, int month) {
        int nextMonth = MyUtil.monthPlus(month);
        if (nextMonth == 1) {
            year = year + 1;
        }
        Date d = MyUtil.convertToDate(year + "-" + MyUtil.formatN(nextMonth) + "-" + "01 00:00:00");

        recordList.clear();
        List<ListTable> list = LitePal
                .where("type = ? and time < ?", String.valueOf(type), String.valueOf(d.getTime()))
                .order("time desc")
                .find(ListTable.class);
        if (list.size() > 0) {
            for (ListTable l : list) {
                Record record = new Record();
                record.setId(l.getId());
                record.setMoney(l.getMoney());
                record.setDescription(l.getDescription());
                record.setTime(l.getTime());
                recordList.add(record);
            }
            tvNoData.setVisibility(View.GONE);
        } else {
            tvNoData.setVisibility(View.VISIBLE);
        }
        return recordList;
    }
    // 查询全部数据，按时间降序排列
    public static List<Record> getRecordsAll(int type) {
        recordList.clear();
        List<ListTable> list = LitePal.where("type = ?", String.valueOf(type)).order("time desc").find(ListTable.class);
        if (list.size() > 0) {
            for (ListTable l : list) {
                Record record = new Record();
                record.setId(l.getId());
                record.setMoney(l.getMoney());
                record.setDescription(l.getDescription());
                record.setTime(l.getTime());
                recordList.add(record);
            }
            tvNoData.setVisibility(View.GONE);
        } else {
            tvNoData.setVisibility(View.VISIBLE);
        }
        return recordList;
    }
    public static void initAdapter(Context context, ListAdapter.IAdapterListener listener, DatePickerDIY.IOnDateSetListener dateSetListener, final int type) {
        List<Record> tempList = getRecordsAll(type);
        if (tempList.size() == 0) {
            setDefaultTopTitleBarText();
        }
        adapter = new ListAdapter(context, type, tempList, listener, dateSetListener);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                // 垂直滚动关闭滑动视图
                SwipeLayoutManager.getInstance().closeCurrentLayout();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                View stickyInfoView = recyclerView.findChildViewUnder(stickyWrapper.getMeasuredWidth() / 2, 5);
                if (stickyInfoView != null) {
                    String[] dateArgs = String.valueOf(stickyInfoView.getContentDescription()).split(",");

                    tvYear.setText(dateArgs[0]);
                    tvMonth.setText(dateArgs[1]);

                    float totalMoneyOfMonth = ListAdapter.queryTotalMoneyOfMonth(type, Integer.parseInt(dateArgs[0]), Integer.parseInt(dateArgs[1]));
                    if (totalMoneyOfMonth > 0) {
                        stickyWrapper.findViewById(R.id.rl_total_text).setVisibility(View.VISIBLE);
                    } else {
                        stickyWrapper.findViewById(R.id.rl_total_text).setVisibility(View.GONE);
                    }
                    tvTotalMoneyOfMonth.setText(totalMoneyOfMonth + "元");
                }
            }
        });
    }
    public static void refreshAdapter(int type, int year, int month) {
        List<Record> tempList = getRecordsByDate(type, year, month);
        adapter.notifyDataSetChanged();

        if (tempList.size() > 0) {
            Record record = tempList.get(0);
            Date recordDate = MyUtil.convertToDate(record.getTime());
            Calendar c = Calendar.getInstance();
            c.setTime(recordDate);

            int recordYear = c.get(Calendar.YEAR);
            int recordMonth = c.get(Calendar.MONTH) + 1;

            tvYear.setText(String.valueOf(recordYear));
            tvMonth.setText(String.valueOf(recordMonth));

            float totalMoneyOfMonth = ListAdapter.queryTotalMoneyOfMonth(type, recordYear, recordMonth);
            if (totalMoneyOfMonth > 0) {
                stickyWrapper.findViewById(R.id.rl_total_text).setVisibility(View.VISIBLE);
            } else {
                stickyWrapper.findViewById(R.id.rl_total_text).setVisibility(View.GONE);
            }
            tvTotalMoneyOfMonth.setText(totalMoneyOfMonth + "元");
        } else {
            stickyWrapper.findViewById(R.id.rl_total_text).setVisibility(View.GONE);
        }
    }
    // 默认设置当前时间给头部标题栏文本
    private static void setDefaultTopTitleBarText() {
        Calendar c = Calendar.getInstance();
        int defaultYear = c.get(Calendar.YEAR);
        int defaultMonth = c.get(Calendar.MONTH) + 1;

        tvYear.setText(String.valueOf(defaultYear));
        tvMonth.setText(String.valueOf(defaultMonth));
    }
}
