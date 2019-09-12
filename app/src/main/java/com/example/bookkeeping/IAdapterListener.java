package com.example.bookkeeping;

import android.view.View;

public interface IAdapterListener {
//    void onItemClick(View view, int position);
    void onSetBtnClick(View view, Record record);
    void onDelBtnClick(View view, int id);
}
