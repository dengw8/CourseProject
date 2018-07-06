package com.example.jiamoufang.threekingdoms.entities;

import android.widget.ImageView;

import org.litepal.crud.DataSupport;

/**
 * Created by jiamoufang on 2017/11/18.
 */

public class NonEditedHero extends DataSupport{
    private int imageViewId;
    private String heroName;

   public NonEditedHero(String heroName,int imageViewId ) {
        this.imageViewId = imageViewId;
        this.heroName = heroName;
    }

    public int getImageViewId() {
        return imageViewId;
    }

    public String getHeroName() {
        return heroName;
    }
}
