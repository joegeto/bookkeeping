package com.example.bookkeeping.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookkeeping.adapter.ListAdapter;
import com.example.bookkeeping.R;
import com.example.bookkeeping.entity.Record;
import com.example.bookkeeping.model.ListTable;
import com.example.bookkeeping.util.MyUtil;
import com.example.bookkeeping.widget.SwipeLayoutManager;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListFragment extends Fragment {
    private static final String TAG = "ListFragment";
    private static TextView tvNoData;
    private static RecyclerView recyclerView;
    private static LinearLayoutManager layoutManager;

    private static ListAdapter adapter;
    private static List<Record> recordList = new ArrayList<>();
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_list, container, false);
        tvNoData = (TextView) view.findViewById(R.id.tv_no_data);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity()); // 通信activity，采用getActivity()
        return view;
    }
    // 查询指定时间段数据
    public static List<Record> getRecordsByDate(int type, int year, int month) {
        int nextMonth = MyUtil.monthPlus(month);
        Date d1 = MyUtil.convertToDate(year + "-" + MyUtil.formatN(month) + "-" + "01 00:00:00");
        Date d2 = MyUtil.convertToDate(year + "-" + MyUtil.formatN(nextMonth) + "-" + "01 00:00:00");

        recordList.clear();
        List<ListTable> list = LitePal.where("type = ? and time between ? and ?", String.valueOf(type), String.valueOf(d1.getTime()), String.valueOf(d2.getTime())).find(ListTable.class);
        if (list.size() > 0) {
            for (ListTable l : list) {
                Record record = new Record();
                record.setId(l.getId());
                record.setMoney(l.getMoney());
                record.setTime(l.getTime());
                recordList.add(record);
            }
            tvNoData.setVisibility(View.GONE);
        } else {
            tvNoData.setVisibility(View.VISIBLE);
        }
        return recordList;
    }
    public static void initAdapter(Context context, ListAdapter.IAdapterListener listener, int type, int year, int month) {
        adapter = new ListAdapter(context, getRecordsByDate(type, year, month), listener);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    public static void refreshAdapter(int type, int year, int month) {
        getRecordsByDate(type, year, month);
        adapter.notifyDataSetChanged();
    }
}
