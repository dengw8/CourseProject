package com.example.jiamoufang.threekingdoms.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jiamoufang.threekingdoms.R;
import com.example.jiamoufang.threekingdoms.api.ApiOfDatabase;
import com.example.jiamoufang.threekingdoms.heros.LocalHero;

import static com.example.jiamoufang.threekingdoms.MainActivity.MylovedHeros;

public class LovedHero extends AppCompatActivity {

    private TextView name, sex,birth,address, belong, attack, intelligence,leadership,food, introduction;
    private ImageView heroImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_loved_hero);
        initViews();
        setHeroInfos();
    }

    private void setHeroInfos() {
        LocalHero hero = getLocalHeroFromDataBase();
        if(hero != null) {
            heroImage.setImageResource(hero.getHeroImageId());
            name.setText(hero.getName());
            sex.setText(hero.getSex());
            birth.setText(hero.getDate());
            address.setText(hero.getPlace());
            belong.setText(hero.getState());
            attack.setText(String.valueOf(hero.getForce()));
            intelligence.setText(String.valueOf(hero.getIntelligence()));
            leadership.setText(String.valueOf(hero.getLeadership()));
            food.setText(String.valueOf(hero.getForage()));
            introduction.setText(hero.getIntroduction());
        }
    }

    private LocalHero getLocalHeroFromDataBase() {
        if (MylovedHeros.size() > 0)     {
            String heroName = MylovedHeros.get(0).getHeroName();
            return new ApiOfDatabase().queryLocalHero(heroName);
        }
        return null;
    }

    private void initViews() {
        heroImage = (ImageView)findViewById(R.id.imageview_myhero);
        name = (TextView)findViewById(R.id.tv_myhero_name);
        sex = (TextView)findViewById(R.id.tv_myhero_sex);
        birth = (TextView) findViewById(R.id.tv_myhero_birth);
        address = (TextView) findViewById(R.id.tv_myhero_address);
        belong = (TextView)findViewById(R.id.tv_myhero_belong);
        attack = (TextView) findViewById(R.id.tv_myhero_attack);
        intelligence = (TextView)findViewById(R.id.tv_myhero_intelligence);
        leadership = (TextView) findViewById(R.id.tv_myhero_leadership);
        food = (TextView)findViewById(R.id.tv_myhero_food);
        introduction = (TextView)findViewById(R.id.tv_myhero_introduction);

    }
}
