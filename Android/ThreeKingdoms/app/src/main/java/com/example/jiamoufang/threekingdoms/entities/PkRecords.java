package com.example.jiamoufang.threekingdoms.entities;

import org.litepal.crud.DataSupport;

import java.sql.Time;
import java.util.Date;

/**
 * Created by jiamoufang on 2017/11/5.
 */

public class PkRecords extends DataSupport {
    private String heroNameA;
    private String heroNameB;
    private String PKresult;
    private String PKtime;

    public PkRecords(String heroNameA, String heroNameB, String PKresult, String PKtime) {
        this.heroNameA = heroNameA;
        this.heroNameB = heroNameB;
        this.PKresult = PKresult;
        this.PKtime = PKtime;
    }
    public String getPKtime() {
        return PKtime;
    }

    public String getHeroNameA() {
        return heroNameA;
    }

    public String getHeroNameB() {
        return heroNameB;
    }

    public String getPKresult() {
        return PKresult;
    }

}
