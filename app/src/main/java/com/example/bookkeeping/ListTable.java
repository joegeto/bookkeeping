package com.example.bookkeeping;

import org.litepal.crud.LitePalSupport;

public class ListTable extends LitePalSupport {
    private int id;
    private float money;
    private Long time;
    private int type;
    private String typeDesc;

    public String getTypeDesc() {
        return typeDesc;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public float getMoney() {
        return money;
    }

    public Long getTime() {
        return time;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }
}
