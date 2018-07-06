
package com.example.jiamoufang.threekingdoms.entities;

import android.graphics.Bitmap;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by jiamoufang on 2017/11/5.
 */

public class Hero extends BmobObject{
    private String name;   //姓名
    private BmobFile heroImage;
    private String sex;     //性别
    private String date;    //出生日期
    private String place;   //籍贯
    private String state;   //主校势力
    private String introduction;    //简介

    private int force;  //武力值(0~100)
    private int intelligence;  //智力值(0~100)
    private int leadership;   //统率值(0~100)
    private int army;   //军队数量:有统率值决定
    private int forage; // 粮草数量(0~1000)


    public Hero(String name, BmobFile heroImage, String sex, String date, String place, String state, String introduction,
                int force, int intelligence, int leadership, int forage) {
        this.name = name;
        this.heroImage = heroImage;
        this.sex = sex;
        this.date = date;
        this.place = place;
        this.state = state;
        this.introduction = introduction;
        this.force = force;
        this.intelligence = intelligence;
        this.leadership = leadership;
        this.army = leadership * 1000;
        this.forage = forage;
    }

    public BmobFile getImageFile() {
        return heroImage;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public String getState() {
        return state;
    }

    public String getIntroduction() {
        return introduction;
    }

    public int  getForce() {
        return force;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getLeadership() {
        return leadership;
    }

    public int getArmy() {
        return army;
    }

    public int getForage() {
        return forage;
    }

    public void setImageFile(BmobFile imageFile) {
        this.heroImage = imageFile;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPlace(String place) {
        this.place  = place;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setForce(Integer force) {
        this.force = force;
    }

    public void setIntelligence(Integer intel) {
        this.intelligence = intel;
    }

    public void setLeadership(Integer leadership) {
        this.leadership = leadership;
        this.army = leadership * 1000;
    }

    public void setArmy(Integer army) {
        this.army = army;
    }

    public void setForage() {
        this.forage = forage;
    }

}
