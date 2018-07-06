package com.example.jiamoufang.threekingdoms.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jiamoufang.threekingdoms.R;
import com.example.jiamoufang.threekingdoms.adapter.HeroAdapter;
import com.example.jiamoufang.threekingdoms.heros.LocalHero;

import java.util.ArrayList;
import java.util.List;

import static com.example.jiamoufang.threekingdoms.MainActivity.Herolist;

/*
 * Created by jiamoufang on 2017/11/5.
 */

public class HerosListFragment extends Fragment {

    private List<LocalHero>  mHerosList = new ArrayList<>();
    private HeroAdapter heroAdapter;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.tab03, container, false);
        initHeros();
        //这里有点坑,注意是用 view 来拿到 recyclerView，否则拿不到就成了NullPointer，后面就崩了
        RecyclerView recyclerview = (RecyclerView)view.findViewById(R.id.tab03_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerview.setLayoutManager(gridLayoutManager);
        heroAdapter = new HeroAdapter(mHerosList);
        recyclerview.setAdapter(heroAdapter);

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            initHeros();
            //这里有点坑,注意是用 view 来拿到 recyclerView，否则拿不到就成了NullPointer，后面就崩了
            RecyclerView recyclerview = (RecyclerView)view.findViewById(R.id.tab03_recyclerview);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
            recyclerview.setLayoutManager(gridLayoutManager);
            heroAdapter = new HeroAdapter(mHerosList);
            recyclerview.setAdapter(heroAdapter);
        }else{  //隐藏的时候一般没什么要处理的
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void initHeros() {
        mHerosList.clear();
        for (int i = 0; i < Herolist.size(); i++) {
            mHerosList.add(Herolist.get(i));
        }
    }
}
