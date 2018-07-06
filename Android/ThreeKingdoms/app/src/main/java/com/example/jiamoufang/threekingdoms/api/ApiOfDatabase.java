package com.example.jiamoufang.threekingdoms.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ArrayAdapter;

import com.example.jiamoufang.threekingdoms.entities.MyLovedHero;
import com.example.jiamoufang.threekingdoms.entities.NonEditedHero;
import com.example.jiamoufang.threekingdoms.entities.PkRecords;
import com.example.jiamoufang.threekingdoms.heros.LocalHero;
import com.example.jiamoufang.threekingdoms.MainActivity.*;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.example.jiamoufang.threekingdoms.MainActivity.Herolist;

/**
 * Created by jiamoufang on 2017/11/17.
 */

public class ApiOfDatabase {

    /*
     * 将所有的PKRecords写回数据库
     * */
    public boolean WritoRecordsToDatabase(List<PkRecords> PKList) {
        for (int i = 0; i < PKList.size(); i++) {
            PKList.get(i).save();
        }
        return true;
    }
    /*
     * 将所有的LocalHero写回数据库
     * */
    public boolean WriteLocalHeroToDatabase(List<LocalHero> localHerosLists) {
        for (int i = 0; i < Herolist.size(); i++) {
            /*检查是否重复写入*/
            List<LocalHero> tmp = DataSupport.where("name = ?",Herolist.get(i).getName()).find(LocalHero.class);
            if (tmp.size() == 0) {
                Herolist.get(i).save();
            }
        }
        return true;
    }
    /*
    * 将所有的NonEditedHero写回数据库
    * */
    public boolean WriteNonEditedHeroToDatabase(List<NonEditedHero> nonEditedHerosList) {
        for (int i = 0; i < nonEditedHerosList.size(); i++) {

            nonEditedHerosList.get(i).save();
        }
        return true;
    }
    /*
    * 将所有的MyLovedHero写回数据库
    * */
    public boolean WriteMyLovedHerosToDatabase(List<MyLovedHero> myLovedHerosList) {
        for (int i = 0; i < myLovedHerosList.size(); i++) {
            myLovedHerosList.get(i).save();
        }
        return true;
    }
    /*
    * 向LocalHeros表添加一条数据
    * */
    public boolean addToLocalHeros(LocalHero hero) {
        List<LocalHero> tmp = DataSupport.where("name = ?", hero.getName()).find(LocalHero.class);
        if (tmp.size() == 0) {   //检查重复
            return hero.save();
        }
       return false;
    }
    /*
    * 向NonEditedHero表添加一条数据
    * */
    public boolean addNonEditedHero(NonEditedHero hero) {
        List<NonEditedHero> tmp = DataSupport.where("heroName = ?", hero.getHeroName()).find(NonEditedHero.class);
        if (tmp.size() == 0) {
            return hero.save();
        }
        return false;
    }
    /*
    * 按英雄名字删除一条本地英雄记录
    * false : 没有这个英雄
    * true : 删除成功
    * */
    public boolean deleteFromLocalHeros(String heroName) {
        return DataSupport.deleteAll(LocalHero.class,"name = ?", heroName) > 0;
    }
    /*
    * 向PKRecords表添加一条数据
    * */
    public boolean addToPKRecords(PkRecords pkRecords) {
        return pkRecords.save();
    }
    /*
    * 当未编辑的英雄被添加后，从数据库里删除
    * 按英雄名字删除
    * false : 没有这个英雄
    * true : 删除成功
    * */
    public boolean deleteFromNonEditedHeros(String heroName) {
       return DataSupport.deleteAll(NonEditedHero.class,"heroName = ?",heroName) > 0;
    }
    /*
    * 按名字查询本地已经已经添加的英雄
    */
    public LocalHero queryLocalHero(String heroName) {
        List<LocalHero> tmp =  DataSupport.where("name = ?",heroName).find(LocalHero.class);
        if (tmp.size() > 0 ) {
            return tmp.get(0);
        }
        return null;
    }
    /*
    * 查询全部本地英雄
    * */
    public List<LocalHero> queryAllLocalHeros() {
        return DataSupport.findAll(LocalHero.class);
    }
    /*
    * 查找所有的对决记录
    * */
    public List<PkRecords> queryAllPkRecords() {
        return DataSupport.findAll(PkRecords.class);
    }

    /*
    * 删除所有的MyLovedHero
    * 其实一直都只保存了一条数据
    * */
    public boolean deleteAllMyLovedHero() {
       return DataSupport.deleteAll(MyLovedHero.class) > 0;
    }

    /*
    * 查询获取MyLovedHero
    * */
    public MyLovedHero queryMyLovedHero() {
        List<MyLovedHero> res = DataSupport.findAll(MyLovedHero.class);
        if (res.size() > 0) {
            return res.get(0);
        }
        return null;
    }
    public boolean deleteAllLocalHero() {
        return DataSupport.deleteAll(LocalHero.class) > 0;
    }
    public boolean deleteAllPKRecords() {
        return DataSupport.deleteAll(PkRecords.class) > 0;
    }
    public boolean deleteAllNonEditedHeros() {
        return DataSupport.deleteAll(NonEditedHero.class) > 0;
    }


}
