package com.example.bookkeeping.entity;

import com.example.bookkeeping.util.MyUtil;

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
