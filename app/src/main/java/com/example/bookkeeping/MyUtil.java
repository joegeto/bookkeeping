package com.example.bookkeeping;

import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyUtil {
    public static final int EIGHT_HOUR =  8 * 60 * 60 * 1000;

    public static String formatTime(Long timeStamp) {
        timeStamp = timeStamp + EIGHT_HOUR;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(Long.parseLong(String.valueOf(timeStamp))));
        return sd;
    }
    public static String formatTime2(Long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(Long.parseLong(String.valueOf(timeStamp))));
        return sd;
    }
    public static Date convertToDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
}
