package com.example.bookkeeping.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static void makeText(Context context, String text) {
        Toast toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.show();
    }
}
