package com.example.jiamoufang.threekingdoms.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jiamoufang.threekingdoms.MainActivity;
import com.example.jiamoufang.threekingdoms.MyMusic;
import com.example.jiamoufang.threekingdoms.R;
import com.example.jiamoufang.threekingdoms.api.ApiOfBmob;
import com.example.jiamoufang.threekingdoms.api.ApiOfDatabase;
import com.example.jiamoufang.threekingdoms.entities.MyLovedHero;
import com.example.jiamoufang.threekingdoms.entities.NonEditedHero;
import com.example.jiamoufang.threekingdoms.heros.LocalHero;

import org.greenrobot.eventbus.EventBus;

import static com.example.jiamoufang.threekingdoms.MainActivity.Herolist;
import static com.example.jiamoufang.threekingdoms.MainActivity.MylovedHeros;
import static com.example.jiamoufang.threekingdoms.MainActivity.NonEditedHeroList;

/**
 * Created by jiamoufang on 2017/11/5.
 */

public class HeroDetailsActivity extends AppCompatActivity {
    public static final String HERO_NAME = "heroName";
    public static final String HERO_IMAGE_ID = "heroImageId";
    //public static final String HERO_IMAGE_BYTES = "heroImageBytes";
    private MyMusic myMusic;
    private Toolbar toolbar;
    private String heroName;
    private  CollapsingToolbarLayout collapsingToolbarLayout;
    private  CoordinatorLayout coordinatorlayout;
    private  ImageView heroImage;
    private TextView heroTextContent;
    private TextView info_heroDetails;
    private FloatingActionButton fab_heroDetails;
    private int sig = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_details);

        Intent intent = getIntent();
        heroName = intent.getStringExtra(HERO_NAME);
        int heroImageId = intent.getIntExtra(HERO_IMAGE_ID,0);
        String introduction = intent.getStringExtra("introduction");
        String birth = intent.getStringExtra("birth");
        String address = intent.getStringExtra("address");
        String sex = intent.getStringExtra("sex");
        String belong = intent.getStringExtra("belong");

        initViews();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        collapsingToolbarLayout.setTitle(heroName);
        Glide.with(this).load(heroImageId).into(heroImage);
        coordinatorlayout.setBackgroundResource(heroImageId);
        heroTextContent.setText(introduction);
        info_heroDetails.setText(sex + ", 生卒：" + birth +", 籍贯：" +  address + ", 主校势力：" + belong);

        myMusic = new MyMusic(this);

        fab_heroDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HeroDetailsActivity.this);
                builder.setMessage("选择" + heroName + "做为您喜爱的英雄？");
                builder.setTitle("收藏");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*将该英雄设置为我的英雄*/
                        lovedThisHero(heroName);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
    }
    /*
    * 设置我的英雄
    * 更新抽屉菜单头像为英雄头像
    * 与MainActvity通信，告诉它更新相关头像
    * 先从数据库获取该英雄，通过EventBus传输给主活动
    * */
    private void lovedThisHero(String heroName) {
        /*先从数据库获取该英雄*/
        LocalHero hero = new ApiOfDatabase().queryLocalHero(heroName);
        /*通过EventBus发送给主活动*/
        EventBus.getDefault().post(hero);
        /*将该英雄放入全局链表*/
        MylovedHeros.clear();
        MylovedHeros.add(new MyLovedHero(hero.getName(),hero.getHeroImageId()));
        /*写入数据库*/
        ApiOfDatabase api = new ApiOfDatabase();
        api.deleteAllMyLovedHero();
        api.WriteMyLovedHerosToDatabase(MylovedHeros);
    }

    private void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar_heroDetails);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collaspsing_heroDetails);
        coordinatorlayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        heroImage = (ImageView)findViewById(R.id.iamge_hero_details);
        heroTextContent = (TextView)findViewById(R.id.textView_heroDetails);
        info_heroDetails = (TextView) findViewById(R.id.info_heroDetails);
        fab_heroDetails = (FloatingActionButton) findViewById(R.id.fab_heroDetails);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!MainActivity.isMute) {
            myMusic.initMusic(R.raw.herodetails_music);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!MainActivity.isMute)
            myMusic.releaseMusic();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单文件
        getMenuInflater().inflate(R.menu.pop_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.detail_edit:
                Intent intent = new Intent(HeroDetailsActivity.this, AddHero.class);
                intent.putExtra("editHero", heroName);
                startActivityForResult(intent, 3);
                break;
            case R.id.detail_delete:
                AlertDialog.Builder message = new AlertDialog.Builder(this);
                message.setTitle("");
                message.setMessage("确定删除英雄" + heroName + "?");
                message.setNegativeButton("取消",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                message.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sig = 1;
                        int index = -1;
                        for(int j = 0; j < Herolist.size(); j++) {
                            if(heroName.equals(Herolist.get(j).getName())) {
                                index = j;
                                break;
                            }
                        }
                        /*从链表和数据库中都删除*/
                        if (index >= 0) {
                            LocalHero hero = Herolist.get(index);
                            ApiOfDatabase apiOfDatabase = new ApiOfDatabase();
                            if(apiOfDatabase.deleteFromLocalHeros(hero.getName())) {
                                Toast.makeText(HeroDetailsActivity.this, hero.getName() + "已删除", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HeroDetailsActivity.this, hero.getName() + "删除失败", Toast.LENGTH_SHORT).show();
                            }
                        /*将被删除的英雄添加到未编辑的英雄中*/
                            NonEditedHero nonHero = new NonEditedHero(hero.getName(), hero.getHeroImageId());
                            apiOfDatabase.addNonEditedHero(nonHero);

                        /*再从Herolist全局链表删除*/
                            Herolist.remove(index);
                        /*需要添加到NonEditedHeroList全局链表*/

                            int index1 = -1;
                            for (int k = 0; i < NonEditedHeroList.size(); i++) {
                                if (NonEditedHeroList.get(k).getHeroName().equals(nonHero.getHeroName())) {
                                    index1 = k;
                                    break;
                                }
                            }
                        /*避免重复添加*/
                            if (index1 == -1) {
                                NonEditedHeroList.add(nonHero);
                            }
                        }

                        /*
                        * 只有finish是不行的，自己再想想
                        * */
                        finish();
                    }
                });
                message.create().show();
                break;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 3:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String name = bundle.getString("name");
                    int index = -1;
                    for (int i = 0; i < Herolist.size(); i++) {
                        if (Herolist.get(i).getName().equals(name)) {
                            index = i;
                            break;
                        }
                    }
                    /*修改数据*/
                    LocalHero localHero = Herolist.get(index);
                    localHero.setName(name);
                    localHero.setSex(bundle.getString("sex"));
                    localHero.setDate(bundle.getString("birth"));
                    localHero.setPlace(bundle.getString("address"));
                    localHero.setState(bundle.getString("belong"));
                    localHero.setForce(Integer.parseInt(bundle.getString("attack")));
                    localHero.setIntelligence(Integer.parseInt(bundle.getString("intelligence")));
                    localHero.setLeadership(Integer.parseInt(bundle.getString("leadership")));
                    localHero.setForage(Integer.parseInt(bundle.getString("food")));
                    localHero.setArmy();
                    localHero.setIntroduction(bundle.getString("introduction"));
                    localHero.setHeroImageId(bundle.getInt("imageId"));
                    Herolist.remove(index);
                    Herolist.add(index,localHero);
                    /*重新渲染*/
                    heroName = name;
                    collapsingToolbarLayout.setTitle(heroName);
                    Glide.with(this).load(bundle.getInt("imageId")).into(heroImage);
                    coordinatorlayout.setBackgroundResource(bundle.getInt("imageId"));
                    heroTextContent.setText(bundle.getString("introduction"));
                    info_heroDetails.setText(bundle.getString("sex")+ ", 生卒：" + bundle.getString("birth") + ", 籍贯：" + bundle.getString("address")
                            + ", 主校势力：" + bundle.getString("belong"));

                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
