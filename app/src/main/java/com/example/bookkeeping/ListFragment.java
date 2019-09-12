package com.example.bookkeeping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListFragment extends Fragment implements IAdapterListener {
    private static final String TAG = "ListFragment";
    private static RecyclerView recyclerView;
    private static LinearLayoutManager layoutManager;
    private static IAdapterListener mListener;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mListener = this;
        View view = inflater.inflate(R.layout.frag_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        ListAdapter adapter = new ListAdapter(getActivity(), getRecords(), this);
        layoutManager = new LinearLayoutManager(getActivity()); // 通信activity，采用getActivity()
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }
    public static List<Record> getRecords() {
        List<Record> records = new ArrayList<>();
        List<ListTable> list = LitePal.findAll(ListTable.class);
        for (ListTable l : list) {
            Record record = new Record();
            record.setId(l.getId());
            record.setMoney(l.getMoney());
            record.setTime(l.getTime());
            records.add(record);
        }
        return records;
    }
    public static void refreshAdapter(Context context) {
        ListAdapter adapter = new ListAdapter(context, getRecords(), mListener);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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

        Intent intent = new Intent(getActivity(), SetDialogActivity.class);
        intent.putExtra("recordId", record.getId());
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("day", day);
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onDelBtnClick(View view, int id) {
        LitePal.deleteAll(ListTable.class, "id = ?", String.valueOf(id));
    }
}
