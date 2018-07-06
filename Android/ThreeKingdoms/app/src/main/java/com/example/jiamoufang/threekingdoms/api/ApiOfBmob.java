package com.example.jiamoufang.threekingdoms.api;

import android.content.Context;
import android.widget.Toast;

import com.example.jiamoufang.threekingdoms.entities.Hero;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by jiamoufang on 2017/11/18.
 */

public class ApiOfBmob {
    private String resId;
    private Hero heroResult;

    public ApiOfBmob() {
         resId = "";
        heroResult = null;
    }
    /*
    * 上传英雄
    * */
    public String UploadHero(Hero hero, final Context context) {
        hero.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null) {
                    Toast.makeText(context, "云端上传成功，id: " + s, Toast.LENGTH_SHORT).show();
                    resId = s;
                } else {
                    Toast.makeText(context, "云端上传失败 " + e.getMessage() , Toast.LENGTH_SHORT).show();
                }
            }
        });
        return resId;
    }

    /*
    * 查询英雄
    * */
    public Hero getHeroFormBmob(String id, final Context context){
        BmobQuery<Hero> heroBmobQuery = new BmobQuery<>();
        heroBmobQuery.getObject(id, new QueryListener<Hero>() {
            @Override
            public void done(Hero hero, BmobException e) {
                if (e == null) {
                    Toast.makeText(context, "查询成功 " + hero.getName(), Toast.LENGTH_SHORT).show();
                    heroResult = hero;
                } else {
                    Toast.makeText(context, "查询失败 " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return heroResult;
    }


}
