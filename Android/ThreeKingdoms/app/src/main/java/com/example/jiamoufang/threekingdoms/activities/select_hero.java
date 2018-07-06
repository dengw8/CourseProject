package com.example.jiamoufang.threekingdoms.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.jiamoufang.threekingdoms.MainActivity;
import com.example.jiamoufang.threekingdoms.R;
import com.example.jiamoufang.threekingdoms.fragment.HerosPKFragment;
import com.example.jiamoufang.threekingdoms.heros.LocalHero;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.jiamoufang.threekingdoms.MainActivity.Herolist;

public class select_hero extends AppCompatActivity {
    private ListView selectList ;
    private List<Map<String, Object>> headerlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_hero);

        InitListView();

        SimpleAdapter simpleAdapter = new SimpleAdapter(select_hero.this, headerlist, R.layout.select_item,
                new String[]{"image","name", "detail"}, new int[]{R.id.image, R.id.name, R.id.detail});
        selectList = (ListView) findViewById(R.id.select_items);
        selectList.setAdapter(simpleAdapter);

        selectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LocalHero tem = Herolist.get(i);
                Intent intent = new Intent();
                intent.putExtra("selectName", tem.getName());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    //更新视图列表
    private void InitListView() {
        for(int i = 0; i < Herolist.size(); i++) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("image", Herolist.get(i).getHeroImageId());
            temp.put("name", Herolist.get(i).getName());
            temp.put("detail", "武力:" + Herolist.get(i).getForce() + " 智力" + Herolist.get(i).getIntelligence() + " 统率" + Herolist.get(i).getLeadership()
                    + "\n军队" +  Herolist.get(i).getArmy() + " 粮草" + Herolist.get(i).getForage());
            headerlist.add(temp);
        }

    }
}
