package com.example.jiamoufang.threekingdoms.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.jiamoufang.threekingdoms.R;
import com.example.jiamoufang.threekingdoms.api.ApiOfDatabase;
import com.example.jiamoufang.threekingdoms.heros.LocalHero;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SearchHero extends AppCompatActivity {

    private ListView listview_search;
    private SearchView searchView;

    private List<LocalHero> searchItems = new ArrayList<LocalHero>();   //用于装搜索结果的list
    private List<String> heroNameList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_hero);

        listview_search = (ListView)findViewById(R.id.listview_searchhero);
        searchView = (SearchView)findViewById(R.id.search_input);

        /*获取已添加的全部英雄的名字列表*/
        heroNameList = getHeroNameLists();

       /*适配器*/
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchHero.this,android.R.layout.simple_list_item_1,heroNameList);
        listview_search.setAdapter(adapter);
        listview_search.setTextFilterEnabled(true);


        /*监听搜索文本变化*/
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(SearchHero.this, "该英雄不存在或尚未添加", Toast.LENGTH_SHORT).show();
                return false;
                /*这里可以提示是否添加，然后跳转到添加界面*/
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    listview_search.setFilterText(newText);
                } else {
                    listview_search.clearTextFilter();
                }
                return false;
            }
        });

        /*监听item点击事件*/
        listview_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = heroNameList.get(position);

                /*到数据库找到该英雄*/
                LocalHero hero = new ApiOfDatabase().queryLocalHero(selectedName);
                if (hero != null) {
                    /*跳转到英雄详情页面*/
                    Intent intent = new Intent(SearchHero.this, HeroDetailsActivity.class);
                    intent.putExtra(HeroDetailsActivity.HERO_NAME, hero.getName());
                    intent.putExtra(HeroDetailsActivity.HERO_IMAGE_ID,hero.getHeroImageId());
                    intent.putExtra("introduction", hero.getIntroduction());
                    intent.putExtra("sex", hero.getSex());
                    intent.putExtra("birth", hero.getDate());
                    intent.putExtra("address", hero.getPlace());
                    intent.putExtra("belong", hero.getState());
                    startActivity(intent);
                    /*注意，不要finish掉当前活动，因为用户可能返回重新查询*/
                }
            }
        });
    }

    /*从数据库获取全部的英雄名字*/
    private List<String> getHeroNameLists() {
        List<LocalHero> queryResult = new ApiOfDatabase().queryAllLocalHeros();
        List<String> ret = new ArrayList<>();
        for (LocalHero hero : queryResult) {
            ret.add(hero.getName());
        }
        return ret;
    }


    @Override
    protected void onResume() {
        super.onResume();
        heroNameList = getHeroNameLists();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        heroNameList = getHeroNameLists();
    }
}
