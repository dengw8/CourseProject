package com.example.jiamoufang.threekingdoms.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.jiamoufang.threekingdoms.R;
import com.example.jiamoufang.threekingdoms.adapter.HeroAdapter;
import com.example.jiamoufang.threekingdoms.adapter.PKRecordAdapter;
import com.example.jiamoufang.threekingdoms.entities.PkRecords;
import com.oragee.banners.BannerView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jiamoufang on 2017/11/5.
 */

public class HitHeroFragment extends Fragment {

    private BannerView bannerView;
    private int[] imgIds = {
            R.drawable.ic_simayi,
            R.drawable.ic_zhouyu,
            R.drawable.ic_zhugeliang,
            R.drawable.ic_caocao,
            R.drawable.ic_diaochan,
            R.drawable.ic_guanyu
    };
    private List<View> viewsList;
    private List<PkRecords> pkRecordsList = new ArrayList<>();
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.tab01, container, false);

        /*
        * 设置 ‘三国志’页面顶部的动画轮播
        * */
        bannerView = view.findViewById(R.id.banner_tab01);
        viewsList = new ArrayList<>();
        for (int i = 0; i < imgIds.length; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imgIds[i]);
            viewsList.add(imageView);
        }
        bannerView.startLoop(true);
        bannerView.setViewList(viewsList);

        /*
        * 设置‘三国志’底部的对决记录列表
        * */
        initRecords();

        PKRecordAdapter adapter = new PKRecordAdapter(getContext(),R.layout.pk_record_item,pkRecordsList);
        ListView pkrecordslistview = (ListView) view.findViewById(R.id.listview_records);
        pkrecordslistview.setAdapter(adapter);

        return view;
    }
    /*
    * 初始化记录列表的items
    * PKRecords的构造函数参数：String usernameA, String usernameB, String heroNameA, String heroNameB, String PKresult, String PKtime
    * */
    private void initRecords() {
        List<PkRecords> tmp = DataSupport.findAll(PkRecords.class);
        pkRecordsList.clear();
       // PkRecords pkrcs = new PkRecords("关羽","张飞","张飞胜了关羽","今日:15:30:44");
        for (int i = 0;i < tmp.size(); i++) {
            if (i < 10)
                pkRecordsList.add(tmp.get(i));
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            initRecords();
            //这里有点坑,注意是用 view 来拿到 recyclerView，否则拿不到就成了NullPointer，后面就崩了
            PKRecordAdapter adapter = new PKRecordAdapter(getContext(),R.layout.pk_record_item,pkRecordsList);
            ListView pkrecordslistview = (ListView) view.findViewById(R.id.listview_records);
            pkrecordslistview.setAdapter(adapter);
        }else{  //隐藏的时候一般没什么要处理的
        }
    }
}
