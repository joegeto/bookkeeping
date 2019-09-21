package com.example.bookkeeping.model;

import org.litepal.crud.LitePalSupport;

public class CardTable extends LitePalSupport {
    private int id;
    private int bgcId;
    private float money;
    private int type;
    private String typeDesc;

    public int getId() {
        return id;
    }

    public int getBgcId() {
        return bgcId;
    }

    public float getMoney() {
        return money;
    }

    public int getType() {
        return type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBgcId(int bgcId) {
        this.bgcId = bgcId;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }
}
