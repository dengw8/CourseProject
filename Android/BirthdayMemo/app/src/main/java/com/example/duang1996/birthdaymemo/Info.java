package com.example.duang1996.birthdaymemo;

/**
 * Created by duang1996 on 2017/12/6.
 */

public class Info {
    private String name;
    private String birth;
    private String gift;

    public Info (String name, String birth, String gift) {
        this.name = name;
        this.birth = birth;
        this.gift = gift;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }
}
