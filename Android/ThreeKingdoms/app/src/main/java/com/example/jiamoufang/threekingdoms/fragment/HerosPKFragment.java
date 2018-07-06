package com.example.jiamoufang.threekingdoms.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiamoufang.threekingdoms.MainActivity;
import com.example.jiamoufang.threekingdoms.R;
import com.example.jiamoufang.threekingdoms.activities.select_hero;
import com.example.jiamoufang.threekingdoms.api.ApiOfDatabase;
import com.example.jiamoufang.threekingdoms.entities.PkRecords;
import com.example.jiamoufang.threekingdoms.heros.LocalHero;

import static com.example.jiamoufang.threekingdoms.MainActivity.Herolist;

/**
 * Created by jiamoufang on 2017/11/5.
 */

public class HerosPKFragment extends Fragment {
    private ImageView user1_imag;
    private TextView user1_text;
    private ImageView user2_imag;
    private TextView user2_text;
    private Button battle_btn;
    private View view;

    //用于区分是点击了user1还是user2
    private int sig1 = 0;
    private int sig2 = 0;

    private LocalHero hero1;
    private LocalHero hero2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab02, container, false);
        user1_imag = (ImageView)view.findViewById(R.id.user1_imag);
        user1_text = (TextView)view.findViewById(R.id.user1_text);
        user2_imag = (ImageView)view.findViewById(R.id.user2_imag);
        user2_text = (TextView)view.findViewById(R.id.user2_text);
        battle_btn = (Button)view.findViewById(R.id.battle_btn);


        //点击图片跳转到select_hero活动中选择武将
        user1_imag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sig1 = 1;
                sig2 = 0;
               startActivityForResult(new Intent(getActivity(), select_hero.class), Activity.RESULT_FIRST_USER);
            }
        });

        user2_imag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sig1 = 0;
                sig2 = 1;
                startActivityForResult(new Intent(getActivity(), select_hero.class), Activity.RESULT_FIRST_USER);
            }
        });

        battle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int who = 0;
                if(hero1 == null || hero2 == null) {
                    Toast.makeText(getActivity(), "请选择英雄！", Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder message = new AlertDialog.Builder(getActivity());
                    message.setIcon(R.mipmap.vs);
                    message.setTitle("英雄对战结果");
                    if(hero1.getEffectiveness() > hero2.getEffectiveness()) {
                        message.setMessage( "进过激烈的战斗，最终 " + hero1.getName() + " 获得了胜利");
                        who = 1;
                    }
                    else if(hero1.getEffectiveness() < hero2.getEffectiveness()) {
                        message.setMessage("进过激烈的战斗，最终 " +hero2.getName() + " 获得了胜利");
                        who = 2;
                    }
                    else {
                        message.setMessage("平局");
                    }
                    final int who1 = who;
                    message.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String res = "";
                            switch (who1) {
                                case 0:
                                    res = "平分秋色";
                                    break;
                                case 1:
                                    res = "胜了";
                                    break;
                                case 2:
                                    res = "败给了";
                                    break;
                                default:
                                    break;
                            }
                            int a = (int) (Math.random()*100);
                            res = a + "回合下"+ hero1.getName() + res +  hero2.getName();
                            ApiOfDatabase apiOfDatabase = new ApiOfDatabase();
                            PkRecords pkRecords = new PkRecords(hero1.getName(),hero2.getName(),res,"2017-11-14:20:30:34");
                            apiOfDatabase.addToPKRecords(pkRecords);
                        }
                    });
                    message.create().show();
                }
            }
        });

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //当从英雄Pk选择的界面跳转到MainActivity的时候执行下面的操作
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getStringExtra("selectName").length() > 0) {
            String  heroName = data.getStringExtra("selectName");
            int index = 0;
            for(int i = 0; i < Herolist.size(); i++) {
                if(heroName.equals(Herolist.get(i).getName())) {
                    index = i;
                    break;
                }
            }
            LocalHero temp = Herolist.get(index);
            if(sig1 == 1) {
                sig1 = 0;
                hero1 = temp;
                user1_imag.setImageResource(temp.getHeroImageId());
                String text = temp.getName() + "\n武力：" + temp.getForce() + "\n智力：" + temp.getIntelligence() + "\n统率：" + temp.getLeadership() + "\n军队：" + temp.getArmy() + "\n粮草：" + temp.getForage();
                user1_text.setText(text);
            }
            if(sig2 == 1) {
                sig2 = 0;
                hero2 = temp;
                user2_imag.setImageResource(temp.getHeroImageId());
                String text = temp.getName() + "\n武力：" + temp.getForce() + "\n智力：" + temp.getIntelligence() + "\n统率：" + temp.getLeadership() + "\n军队：" + temp.getArmy() + "\n粮草：" + temp.getForage();
                user2_text.setText(text);
            }
        }
    }
}
