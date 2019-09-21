package com.example.bookkeeping.entity;

public class Card {
    private int type;
    private int bgcId;
    private String name;
    private float totalMoney;

    public int getType() {
        return type;
    }

    public int getBgcId() {
        return bgcId;
    }

    public String getName() {
        return name;
    }

    public String getTotalMoney() {
        return totalMoney + "元";
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setBgcId(int bgcId) {
        this.bgcId = bgcId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }
}
