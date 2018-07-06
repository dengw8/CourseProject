package com.example.jiamoufang.threekingdoms.heros;

import org.litepal.crud.DataSupport;

/**
 * Created by jiamoufang on 2017/11/17.
 */

public class testBitmap extends DataSupport{
    private String  heroBitmap;

    public String getHeroBitmap() {
        return heroBitmap;
    }

    public void setHeroBitmap(String heroBitmap) {
        this.heroBitmap = heroBitmap;
    }
}
