package com.example.jiamoufang.threekingdoms.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiamoufang.threekingdoms.MainActivity;
import com.example.jiamoufang.threekingdoms.MyMusic;
import com.example.jiamoufang.threekingdoms.R;
import com.example.jiamoufang.threekingdoms.api.ApiOfBmob;
import com.example.jiamoufang.threekingdoms.api.ApiOfDatabase;
import com.example.jiamoufang.threekingdoms.entities.Hero;
import com.example.jiamoufang.threekingdoms.heros.LocalHero;
import com.example.jiamoufang.threekingdoms.heros.testBitmap;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.http.bean.Api;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import static com.example.jiamoufang.threekingdoms.MainActivity.Herolist;

public class AddHero extends AppCompatActivity implements View.OnClickListener{
    private ImageView heroImage;

    private MyMusic myMusic;
    /*
    * 人物属性
    * */
    private TextInputEditText heroName, heroSex, heroBirth, heroAddress, heroBelong;
    private TextInputEditText heroAttack, heroIntelligence,heroLeadership, heroFood;
    private EditText heroIntroduction;

    /*
    * 提交按钮
    * */
    private Button submit;

    /*
    * 区分当前是添加英雄状态还是修改英雄状态
    * */
    private int defaultImageId = R.mipmap.ic_take_photo;

    private  int  resImageId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hero);

        initView();
        initEvent();

        myMusic = new MyMusic(this);

        /*
         * 编辑英雄信息
         */
        if(getIntent().getStringExtra("editHero") != null) {
            String name = getIntent().getStringExtra("editHero");
            EditHero(name);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!MainActivity.isMute) {
            myMusic.initMusic(R.raw.addhero_music);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (!MainActivity.isMute) {
            myMusic.releaseMusic();
        }
    }

    /*
    * 点击事件处理
    * */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_addhero:
                selectFrormNonEditedHeros();
                break;
            case R.id.add_hero_submit:
                onCheckAllTextInput();
            default:
                break;
        }
    }

    /*
    * 从未被编辑的英雄列表中选择想要添加的英雄
    * */
    private void selectFrormNonEditedHeros() {
        Intent it = new Intent(AddHero.this, SelectHeroToEdite.class);
        startActivityForResult(it, 102);
    }

    /*
    * 检查所有的输入是否合法
    * */
    private void onCheckAllTextInput() {
        String name = heroName.getText().toString();
        String sex = heroSex.getText().toString();
        String birth = heroBirth.getText().toString();
        String address = heroAddress.getText().toString();
        String belong = heroBelong.getText().toString();
        String attack = heroAttack.getText().toString();
        String intelligence = heroIntelligence.getText().toString();
        String leadership = heroLeadership.getText().toString();
        String food = heroFood.getText().toString();
        String introduction = heroIntroduction.getText().toString();

        if (resImageId == R.mipmap.ic_take_photo) {
            Toast.makeText(this, "尚未选择英雄", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.length() == 0) {
            Toast.makeText(this, "人物名字不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        /*
        * 根据当前状态检查人物名字是否已被占用
        * */
        if (isHeroNameInvalid(name)) {
            Toast.makeText(this, "人物名字已被占用", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sex.length() == 0) {
            Toast.makeText(this, "人物性别未填写", Toast.LENGTH_SHORT).show();
            return;
        }
        if (birth.length() == 0) {
            Toast.makeText(this, "生卒年不能为空，不知则填写不明", Toast.LENGTH_SHORT).show();
            return;
        }
        /*
        * 籍贯的检查待优化，暂时只检查不为空，待云端数据库做好了再检查籍贯是否存在
        * */
        if (address.length() == 0) {
            Toast.makeText(this, "籍贯不能为空，不知则填写不明", Toast.LENGTH_SHORT).show();
            return;
        }
        /*
        * 主效势力待优化，暂时只检查不为空，待云端数据库做好了再检查主校势力是否存在
        * */
        if (belong.length() == 0) {
            Toast.makeText(this, "主效势力不能为空，若不知则填不明", Toast.LENGTH_SHORT).show();
            return;
        }

        if (attack.length() == 0 ) {
            Toast.makeText(this, "武力值不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (Integer.parseInt(attack) > 100 || Integer.parseInt(attack) < 0) {
            Toast.makeText(this, "武力值不超过100", Toast.LENGTH_SHORT).show();
            return;
        }

        if (intelligence.length() == 0 ) {
            Toast.makeText(this, "智力值不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (Integer.parseInt(intelligence) > 100 || Integer.parseInt(intelligence) < 0) {
            Toast.makeText(this, "智力值不超过100", Toast.LENGTH_SHORT).show();
            return;
        }

        if (leadership.length() == 0 ) {
            Toast.makeText(this, "统帅值不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (Integer.parseInt(leadership) > 100 || Integer.parseInt(leadership) < 0) {
            Toast.makeText(this, "统帅值不超过100", Toast.LENGTH_SHORT).show();
            return;
        }

        if (food.length() == 0 ) {
            Toast.makeText(this, "粮草值不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (Integer.parseInt(food) > 1000 || Integer.parseInt(leadership) < 0) {
            Toast.makeText(this, "粮草值不超过1000", Toast.LENGTH_SHORT).show();
            return;
        }

        if (introduction.length() == 0) {
            Toast.makeText(this, "人物简介不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        /*
        * 检查完毕，没有非法数据
       */

        /*写回数据库*/
        /*
        *  public LocalHero(String name, int heroImageId, String sex, String date, String place, String state, String introduction,
                int force, int intelligence, int leadership, int forage)
        * */
        if (defaultImageId == R.mipmap.ic_take_photo && resImageId != 0) {
            LocalHero localHero = new LocalHero(name,resImageId,sex,birth,address,belong,introduction,Integer.parseInt(attack),Integer.parseInt(intelligence),
                    Integer.parseInt(leadership),Integer.parseInt(food));
            Herolist.add(localHero);
            /*加到数据库*/
            boolean res = new ApiOfDatabase().addToLocalHeros(localHero);
            if (res) {
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        }


        /*返回*/
       if(defaultImageId != R.mipmap.ic_take_photo && resImageId != 0) {
        /*
        * 回传给HeroDetailsActivity
        * */
           Intent retIntent = new Intent();
           Bundle retBundle = new Bundle();
           retBundle.putString("name", name);
           retBundle.putString("sex", sex);
           retBundle.putString("birth",birth);
           retBundle.putString("address",address);
           retBundle.putString("belong",belong);
           retBundle.putString("attack",attack);
           retBundle.putString("intelligence", intelligence);
           retBundle.putString("leadership", leadership);
           retBundle.putString("food", food);
           retBundle.putString("introduction", introduction);
           retBundle.putInt("imageId", resImageId);
           retIntent.putExtras(retBundle);
           setResult(RESULT_OK,retIntent);
           finish();
       }


    }

    private boolean isHeroNameInvalid(String name) {
        if (isAddHeroState()) {
            if (isHeroNameRegistered(name))
                return true;
        }
        return false;
    }

    private boolean isAddHeroState() {
        return defaultImageId == 0;
    }

    private boolean isHeroNameRegistered(String name) {
        for(int i = 0; i < Herolist.size(); i++) {
            if(name.equals(Herolist.get(i).getName())) {
                return true;
            }
        }
        return false;
    }


    /*
    * 初始化控件
    * */
    public void initView() {
        heroImage = (ImageView)findViewById(R.id.imageview_addhero);
        heroName = (TextInputEditText) findViewById(R.id.tv_hero_name);
        heroSex = (TextInputEditText) findViewById(R.id.tv_hero_sex);
        heroBirth = (TextInputEditText) findViewById(R.id.tv_hero_birth);
        heroAddress = (TextInputEditText) findViewById(R.id.tv_hero_address);
        heroBelong = (TextInputEditText) findViewById(R.id.tv_hero_belong);
        heroAttack = (TextInputEditText) findViewById(R.id.tv_hero_attack);
        heroIntelligence = (TextInputEditText) findViewById(R.id.tv_hero_intelligence);
        heroLeadership = (TextInputEditText) findViewById(R.id.tv_hero_leadership);
        heroFood = (TextInputEditText) findViewById(R.id.tv_hero_food);
        heroIntroduction = (EditText) findViewById(R.id.etv_hero_introduction);
        submit = (Button) findViewById(R.id.add_hero_submit);
        /*
        * haveTakenPic 初始化为默认的R.mipmap.ic_take_photo,表示图片为上传
        * */
    }
    /*
    * 事件注册
    * */
    private void initEvent() {
        heroImage.setOnClickListener(this);
        submit.setOnClickListener(this);
    }


    /*
    * 获取照片并处理
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 102:
                        Bundle bundle = data.getExtras();
                        String name = bundle.getString("heroName");
                        int id = bundle.getInt("imageId");
                        heroImage.setImageResource(id);
                        heroName.setText(name);
                        resImageId = id;
                        break;
                default:
                    break;
            }
        }
    }

    private void EditHero(String heroname) {
        LocalHero tem = Herolist.get(0);
        for(int i = 0; i < Herolist.size(); i++) {
            if(heroname.equals(Herolist.get(i).getName())){
                tem = Herolist.get(i);
                break;
            }
        }

        heroName.setText(tem.getName());
        heroSex.setText(tem.getSex());
        heroBirth.setText(tem.getDate());
        heroAddress.setText(tem.getPlace());
        heroBelong.setText(tem.getState());
        heroIntroduction.setText(tem.getIntroduction());
        heroAttack.setText(""+ tem.getForce());
        heroIntelligence.setText(""+ tem.getIntelligence());
        heroLeadership.setText(""+ tem.getLeadership());
        heroFood.setText(""+ tem.getForage());
        heroImage.setImageResource(tem.getHeroImageId());
        defaultImageId = tem.getHeroImageId();
        /*人物头像设置不能点击，强制不让用户替换*/
        heroImage.setClickable(false);

        resImageId = tem.getHeroImageId();
    }
}
