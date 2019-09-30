package com.example.bookkeeping.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyUtil {
    public static final String FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

    public static String formatTime(Long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_1);
        String sd = sdf.format(new Date(Long.parseLong(String.valueOf(timeStamp))));
        return sd;
    }
    public static Date convertToDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_1);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String formatN(int n) {
        String sZero = "0";
        String sn = String.valueOf(n);
        StringBuffer sbf = new StringBuffer();
        if (sn.length() < 2) {
            sbf.append(sZero);
        }
        sbf.append(sn);
        return sbf.toString();
    }
    public static int monthPlus(int month) {
        int _monthPlus = month + 1;
        if (_monthPlus > 12) {
            _monthPlus = 1;
        }
        return _monthPlus;
    }
}
