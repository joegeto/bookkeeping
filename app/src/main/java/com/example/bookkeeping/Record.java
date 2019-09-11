package com.example.bookkeeping;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Record {
    private int id;
    private float money;
    private Long time;

    public int getId() {
        return id;
    }

    public String getMoney() {
        return "￥" + money;
    }

    public String getTime() {
        String _time = MyUtil.formatTime2(time);
        return _time;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setId(int id) {
        this.id = id;
    }
}
