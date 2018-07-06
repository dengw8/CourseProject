package com.example.jiamoufang.threekingdoms;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiamoufang.threekingdoms.activities.AddHero;
import com.example.jiamoufang.threekingdoms.activities.LovedHero;
import com.example.jiamoufang.threekingdoms.activities.SearchHero;
import com.example.jiamoufang.threekingdoms.api.ApiOfDatabase;
import com.example.jiamoufang.threekingdoms.entities.MyLovedHero;
import com.example.jiamoufang.threekingdoms.entities.NonEditedHero;
import com.example.jiamoufang.threekingdoms.entities.PkRecords;
import com.example.jiamoufang.threekingdoms.fragment.HerosListFragment;
import com.example.jiamoufang.threekingdoms.fragment.HerosPKFragment;
import com.example.jiamoufang.threekingdoms.fragment.HitHeroFragment;
import com.example.jiamoufang.threekingdoms.heros.LocalHero;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends FragmentActivity implements View.OnClickListener{
    /*
    * 装所有Hero示例的list
    * 有几个activity都需要访问这个变量，通过设置为static的方式方便访问
    * 唯一标识，设置变量的时候注意一下应该不会出错
    * */
    public static List<LocalHero> Herolist = new ArrayList<>();

    /*尚未编辑的英雄，在添加界面可供选择编辑添加*/
    public static List<NonEditedHero> NonEditedHeroList = new ArrayList<>();

    /*英雄对决记录，在群英会界面使用，每次对决添加*/
    public static List<PkRecords> pkRecordsList = new ArrayList<>();

    /*我喜欢的英雄*/
    public static List<MyLovedHero> MylovedHeros = new ArrayList<>();

    private DrawerLayout mDrawerLayout;

    private LinearLayout mTabHitHero;
    private LinearLayout mTabHeroPk;
    private LinearLayout mTabHeroList;

    /*
    * 底部 Tabs
    * */
    private ImageButton mImgHeroHit;
    private ImageButton mImgHeroPk;
    private ImageButton mImgHeroList;

    /*
    * Fragments
    * */
    private Fragment mTab01;
    private Fragment mTab02;
    private Fragment mTab03;

    /*
    * 滑动抽屉菜单的点击NavigationView
    * */
    private NavigationView navigationView;
    private View nav_headerView;
    private CircleImageView nav_headerImg;
    private TextView username;


    /*
    * 播放音樂MediaPlayer
    * */
    private List<MediaPlayer> mediaPlayerList = new ArrayList<>();
    private int currentFragmentIndex = -1;
    public static boolean isMute = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*创建本地数据库*/
        LitePal.getDatabase();


        /*初始化控件和事件监听*/
        initView();
        initEvent();

        /*
        * 实例化几个Localhero类
        * */
        InitAddHeros();

        /*
        * 实例化尚未编辑的英雄
        * */
        InitNonEditedHeros();

        /*
        * 注册EventBus订阅
        * */
        EventBus.getDefault().register(this);

        /*
        * navigationView的选择事件
        * */
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.my_hero:
                        Intent it = new Intent(MainActivity.this, LovedHero.class);
                        startActivity(it);
                        break;
                    case R.id.add_hero:
                        Intent toAddHero = new Intent(MainActivity.this, AddHero.class);
                        startActivity(toAddHero);
                        //Toast.makeText(MainActivity.this, "you select 添加英雄", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.find_hero:
                        Intent toFindHero = new Intent(MainActivity.this, SearchHero.class);
                        startActivity(toFindHero);
                        break;
                    case R.id.invite_hero:
                        Toast.makeText(MainActivity.this, "未开放邀请好友功能", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("背景音樂")
                                .setMessage("是否調為靜音")
                                .setPositiveButton("一刻寧靜", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (isMute != true) {
                                            isMute = true;
                                            findTargetMusic();
                                        }
                                    }
                                })
                                .setNegativeButton("給我噪起來", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (isMute != false) {
                                            isMute = false;
                                            findTargetMusic();
                                        }
                                    }
                                }).show();

                        break;
                    default:
                        break;
                }
                return true;
            }
        });

       /*       *//*数据库一键清*//*
        ApiOfDatabase apiOfDatabase = new ApiOfDatabase();
        apiOfDatabase.deleteAllMyLovedHero();
        apiOfDatabase.deleteAllLocalHero();
        apiOfDatabase.deleteAllPKRecords();
        apiOfDatabase.deleteAllNonEditedHeros();*/
    }

    /*处理订阅事件*
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void modifyLovedHero(LocalHero hero) {
        nav_headerImg.setImageResource(hero.getHeroImageId());
        username.setText(hero.getName());

    }

    /*
    * 添加英雄回调函数。写入HeroList和数据库
    * */
 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 3:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String name = bundle.getString("name");
                    String sex = bundle.getString("sex");
                    String birth = bundle.getString("birth");
                    String address = bundle.getString("address");
                    String belong = bundle.getString("belong");
                    int attack = Integer.parseInt(bundle.getString("attack"));
                    int intelligence = Integer.parseInt(bundle.getString("intelligence"));
                    int leadership = Integer.parseInt(bundle.getString("leadership"));
                    int food = Integer.parseInt(bundle.getString("food"));
                    String introduction = bundle.getString("introduction");
                    int imageId = bundle.getInt("imageId");
                    LocalHero localHero = new LocalHero(name, imageId, sex, birth, address,
                            belong, introduction, attack, intelligence, leadership, food);
                    *//*写入HeroList*//*
                    Herolist.add(localHero);
                    *//*写入数据库*//*
                    ApiOfDatabase apiOfDatabase = new ApiOfDatabase();
                    apiOfDatabase.addToLocalHeros(localHero);
                }
        }
    }*/

    private void setSelect(final int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);

        switch(i) {
            case 0:
                if (mTab01 == null) {
                    mTab01 = new HitHeroFragment();
                    transaction.add(R.id.id_content, mTab01, "main");
                } else {
                     /*更新对决记录*/
                    pkRecordsList.clear();
                    pkRecordsList = new ApiOfDatabase().queryAllPkRecords();
                    transaction.show(mTab01);
                }
                break;
            case 1:
                if (mTab02 == null) {
                    mTab02 = new HerosPKFragment();
                   transaction.add(R.id.id_content, mTab02, "battle");
                } else {
                    //transaction.add(R.id.id_content, mTab02);
                    transaction.show(mTab02);
                }
                break;
            case 2:
                if (mTab03 == null) {
                    mTab03 = new HerosListFragment();
                    transaction.add(R.id.id_content, mTab03, "list");
                } else {
                    //transaction.add(R.id.id_content, mTab03);
                    transaction.show(mTab03);
                }
                break;
            default:
                break;
        }
        transaction.commit();

        changAndStartMusic(i);
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mTab01 != null) {
            transaction.hide(mTab01);
        }
        if (mTab02 != null) {
            transaction.hide(mTab02);
        }
        if (mTab03 != null) {
            transaction.hide(mTab03);
        }
    }

    private void initEvent() {
        mTabHitHero.setOnClickListener(this);
        mTabHeroPk.setOnClickListener(this);
        mTabHeroList.setOnClickListener(this);
        nav_headerImg.setOnClickListener(this);
    }

    private void initView() {
        mTabHitHero = (LinearLayout)findViewById(R.id.id_tab_hit_hero);
        mTabHeroPk = (LinearLayout)findViewById(R.id.id_tab_heros_pk);
        mTabHeroList = (LinearLayout)findViewById(R.id.id_tab_herolist);

        mImgHeroHit = (ImageButton) findViewById(R.id.id_tab_hit_hero_img);
        mImgHeroPk = (ImageButton) findViewById(R.id.id_tab_heros_pk_img);
        mImgHeroList = (ImageButton) findViewById(R.id.id_tab_herolist_img);

        mImgHeroHit.setImageResource(R.mipmap.tab_weixin_pressed);
        mImgHeroPk.setImageResource(R.mipmap.tab_find_frd_normal);
        mImgHeroList.setImageResource(R.mipmap.tab_settings_normal);
        //mTab01 = new HitHeroFragment();
       // mTab02 = new HerosPKFragment();
        //mTab03 = new HerosListFragment();

        navigationView = (NavigationView)findViewById(R.id.nav_view);
        nav_headerView = navigationView.getHeaderView(0);
        nav_headerImg = nav_headerView.findViewById(R.id.nav_icon_image);
        username = (TextView) nav_headerView.findViewById(R.id.username);

        /*从数据库加载抽屉导航栏头部的信息*/
        loadNavHearInfo();


    }
    /*
    * 从数据库加载抽屉导航栏头部的信息
    * */
    private void loadNavHearInfo() {
        MyLovedHero my = new ApiOfDatabase().queryMyLovedHero();
        if (my != null) {
            nav_headerImg.setImageResource(my.getImageId());
            username.setText(my.getHeroName());
            MylovedHeros.clear();
            MylovedHeros.add(my);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.id_tab_hit_hero:
                mImgHeroHit.setImageResource(R.mipmap.tab_weixin_pressed);
                mImgHeroPk.setImageResource(R.mipmap.tab_find_frd_normal);
                mImgHeroList.setImageResource(R.mipmap.tab_settings_normal);
                setSelect(0);
                break;
            case R.id.id_tab_heros_pk:
                mImgHeroHit.setImageResource(R.mipmap.tab_weixin_normal);
                mImgHeroPk.setImageResource(R.mipmap.tab_find_frd_pressed);
                mImgHeroList.setImageResource(R.mipmap.tab_settings_normal);
                setSelect(1);
                break;
            case R.id.id_tab_herolist:
                mImgHeroHit.setImageResource(R.mipmap.tab_weixin_normal);
                mImgHeroPk.setImageResource(R.mipmap.tab_find_frd_normal);
                mImgHeroList.setImageResource(R.mipmap.tab_settings_pressed);
                setSelect(2);
                break;
            case R.id.nav_icon_image:
                Toast.makeText(this, "你点击了导航的头像", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }

    public void initMediaPlayer() {
        mediaPlayerList.add(MediaPlayer.create(this, R.raw.main_music));
        mediaPlayerList.add(MediaPlayer.create(this, R.raw.battle_music));
        mediaPlayerList.add(MediaPlayer.create(this, R.raw.herolist_music));

        for (int i = 0; i < mediaPlayerList.size(); i++) {
            final MediaPlayer mediaPlayer = mediaPlayerList.get(i);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        }
    }

    public void changAndStartMusic(int i) {
        for (int t = 0; t < mediaPlayerList.size(); t++) {
            if (mediaPlayerList.get(t).isPlaying()) {
                mediaPlayerList.get(t).pause();
            }
        }
        mediaPlayerList.get(i).seekTo(0);
        if (!isMute) {
            mediaPlayerList.get(i).start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        findTargetMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getSupportFragmentManager().findFragmentByTag("main") != null && getSupportFragmentManager().findFragmentByTag("main").isVisible()) {
            currentFragmentIndex = 0;
        } else if (getSupportFragmentManager().findFragmentByTag("battle") != null && getSupportFragmentManager().findFragmentByTag("battle").isVisible()) {
            currentFragmentIndex = 1;
        } else {
            currentFragmentIndex = 2;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (int i = 0; i < mediaPlayerList.size(); i++) {
            if (mediaPlayerList.get(i) != null) {
                mediaPlayerList.get(i).release();
            }
        }
        mediaPlayerList.clear();
    }

    public void findTargetMusic() {
        initMediaPlayer();
        switch (currentFragmentIndex) {
            case -1:
                setSelect(0);
                break;
            case 0:
                setSelect(0);
                break;
            case 1:
                setSelect(1);
                break;
            default:
                setSelect(2);
                break;
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    public void InitAddHeros() {

        List<LocalHero> tmp = DataSupport.findAll(LocalHero.class);
        if (tmp.size() == 0) {
            Herolist.add(new LocalHero("姜维", R.drawable.jiangwei,"男", "（202 - 264）", "凉州汉阳郡冀","蜀","历史简介：\n 大将军。文武双全、侍母至孝。初为魏中郎将，诸葛亮欲取天水后姜维识破设伏败赵云，其后更劫了诸葛亮的寨。诸葛亮思其为接班人，并用计令夏侯楙和马遵误以姜维降蜀，姜维走投无路降蜀。姜维深得亮器重，亮授之平生所学。其后历次北伐，姜维出力甚多，比方献计斩费耀胜曹真。及亮卒，姜维继诸葛之略图复汉室，又招纳羌人以断陇西为蜀汉所有。斩魏将徐质取南安。困司马昭于铁笼山，后虽为陈泰用计降服羌兵，开门迎已被魏兵混入的羌援而终败，但败逃中仍然空手接箭反射杀都督郭淮。出狄道，背水大破王经，但其后中邓艾骚扰计而退兵，迁大将军。后为邓艾设计迫入段谷中伏战败，自贬后将军。再出祁山虽扎营于早为邓艾开地道之地，但被劫寨后仍然冷静命令全军迎战，后斗阵胜邓艾。将计就计诛诈降将王瓘，败邓艾丢盔弃甲，越山舍马。被邓艾看出实出洮阳而设伏兵败，得力助手夏侯霸战死。黄浩弄权欲黜姜维，请刘禅杀专权的他不果，以屯田之名避禍沓中。司马昭大举伐蜀姜维上表请朝廷增援，但黄皓并不理会。姜维为邓艾军所缠，后用计令诸葛绪误以他将袭雍州而得脫，于剑阁拒守钟会二十余万大军。奈先有江油守将马邈投降，再有二千破六万，诸葛瞻父子为邓艾斩于绵竹，蜀汉震恐后从投降派谯周之议，后主投降，并敕令姜维也降，将士得知后奋怒斩石。姜维乃佯降于钟会，看出他阴有异志策应他造反，图谋杀会后重扶汉室，乃事败，姜维及妻子皆伏诛，时年五十九。"
                    ,85, 95, 95, 800));
            Herolist.add(new LocalHero("关羽",R.drawable.guanyu,"男","(？-219)", "司隶河东郡解","蜀","历史简介：\n 前将军。本字长生，亡命奔涿郡。与张飞追随刘备征战，当刘备为平原相时，他们俩为别部司马。二人与刘备寝则同床，恩若兄弟。稠人广坐则侍立终日。随同曹操和刘备讨吕布于下邳，事后为朝廷封为中郎将。当刘备袭杀曹操的徐州刺史车冑后，以关羽镇下邳太守。曹操东征破刘备，关羽被俘，被拜为偏将军，对他礼遇很优厚。白马之战时关羽万军中刺敌主帅颜良，被封为汉寿亭侯，报了曹操之恩后便告辞，寻找刘备。长阪之战刘备败北，抄近路赴汉津与关羽的数百只船汇合至江夏。赤壁之战孙刘联军胜利后，关羽在其后的江陵之战中绝北道，阻隔曹操的援军，为周瑜能攻下江陵创造有利的条件。事后遙领襄阳太守、拜为荡寇将军。诸葛亮等人入蜀增援刘备，关羽便镇荊州。刘备自立为汉中王，被拜为前将军，假节钺。关羽乘汉水暴涨之机出兵襄樊，更在于禁七军为水所淹乘船进攻，梁、郏、陆浑各县的盗贼有些远远地领受了关羽的官印封号，威震华夏；曹操一度想迁都避其锋芒。后因孙权背盟投曹，后方为吕蒙所破，关羽便退兵，但终为孙权军所擒杀。"
                    ,99, 80, 85, 900));
            Herolist.add(new LocalHero("刘备", R.drawable.liubei,"男", "(161-223)", "幽州涿郡涿)","蜀","刘备 历史简介 \n 蜀汉的开国皇帝，相传是汉景帝之子中山靖王刘胜的后代。刘备少年丧父，与母亲贩鞋织草席为生。黄巾起义时，刘备组织义兵，随政府军剿除黄巾，有功，任安喜县尉，不久因鞭打督邮弃官。后诸侯割据，刘备势力弱小，经常寄人篱下，先后投靠过公孙瓒、曹操、袁绍、刘表等人，几经波折，却仍无自己的地盘。赤壁之战之际，刘备联吴抗曹，取得胜利，从东吴处“借”到荆州，迅速发展起来，吞并益州，占领汉中，建立蜀汉政权。后关羽战死，荆州被孙权夺取，刘备于称帝后伐吴，在夷陵之战中被陆逊击败，病逝于白帝城，临终托孤于诸葛亮。"
                    ,80, 85, 90, 1000));
            Herolist.add(new LocalHero("吕布", R.drawable.lvbu,"男","(？-198)", "并州五原郡九原"," 群雄","历史简介：\n 吕布是骁勇善战的汉末诸侯，先后跟随丁原、董卓作战，并最终杀死了丁原和董卓。成为独立势力后，吕布与曹操为敌，和刘备、袁术等诸侯时敌时友，最终不敌曹操和刘备的联军，兵败人亡。吕布虽然勇猛，但是少有计策，为人反复无常，唯利是图。在演义中，吕布是天下无双的超一流武将，曾在虎牢关大战刘备、关羽、张飞三人联手，曾一人独斗曹操军六员大将，武艺可谓公认的演义第一。著名的美女貂蝉上演连环计后，成为吕布的妻室。"
                    ,100, 60, 70, 900));
            Herolist.add(new LocalHero("许褚", R.drawable.xuchu,"男","(？-？)", "豫州沛国谯","魏","历史简介：\n 太祖讨伐黄劭、何仪，褚生擒仪。典韦要人，褚欲与之争斗未果，为太祖赏识，收之用为护卫。官渡破袁绍后，因谋士许攸辱骂，怒而杀之。随同征战多年。褚素呆滞，征战英勇，人称“虎痴”，曾裸衣恶斗马超。后汉中之战护粮，因醉为张飞所败。"
                    ,95, 70, 75, 900));
            Herolist.add(new LocalHero("颜良",  R.drawable.yanliang,"男","(？-200)", "不详","群雄","历史简介：\n 颜良未参加关东联军，使袁绍颇为华雄的猖狂而叹息。绍计逼韩馥，谋夺冀州，耿武欲行刺，颜良斩之。随后与文丑为先锋，击公孙瓒，平定河北。曹袁交兵，颜良统十万精兵为前部，进攻白马，连斩宋宪、魏续，二十合败徐晃，曹军诸将栗然。关羽出战，颜良方欲问时，因赤兔马快，措手不及，被关羽一刀刺于马下而死。"
                    ,97, 80, 80, 900));
            Herolist.add(new LocalHero("张飞", R.drawable.zhangfei,"男","(？-221)", "幽州涿郡(河北保定市涿州)","蜀","历史简介：\n 与刘备和关羽桃园结义，张飞居第三。随刘备征讨黄巾，刘备终因功被朝廷受予平原相，后张飞鞭挞欲受赂的督邮。十八路诸侯讨董时，三英战吕布，其勇为世人所知。曹操以二虎竞食之计迫刘备讨袁术，刘备以张飞守徐州，诫禁酒，但还是因此而鞭打曹豹招致吕布东袭。刘备反曹后，反用劫寨计擒曹将刘岱，为刘备所赞。徐州终为曹操所破，张飞与刘备失散，占据古城。误以为降汉的关羽投敌，差点一矛将其杀掉。曹操降荊州后引骑追击，刘备败逃，张飞引二十余骑，立马于长阪桥，吓退曹军数十里。庞统死后刘备召其入蜀，张飞率军沿江而上，智擒巴郡太守严颜并生获之，张飞壮而释放。于葭萌关和马超战至夜间，双方点灯，终大战数百回合。瓦口关之战时扮作醉酒，智破张郃。后封为蜀汉五虎大将。及关羽卒，张飞悲痛万分，每日饮酒鞭打部下，导致为帐下将张达、范强所杀，他们持其首顺流而奔孙权。"
                    ,97, 75, 85, 900));
            Herolist.add(new LocalHero("曹操",R.drawable.caocao,"男","(155-220)", "豫州沛国谯","魏","历史简介：\n 曹操是西园八校尉之一，曾只身行刺董卓，失败后和袁绍共同联合天下诸侯讨伐董卓，后独自发展自身势力，一生中先后战胜了袁术、吕布、张绣、袁绍、刘表、张鲁、马超等割据势力，统一了北方。但是在南下讨伐江东的战役中，曹操在赤壁惨败。后来在和蜀汉的汉中争夺战中，曹操再次无功而返。曹操一生未称帝，他病死后，曹丕继位后不久称帝，追封曹操为魏武皇帝。"
                    ,85, 92, 92, 950));
            Herolist.add(new LocalHero("周瑜",R.drawable.zhouyu,"男","(175-210)", "扬州庐江郡舒","吴","历史简介：\n 偏将军、南郡太守。自幼与孙策交好，策离袁术讨江东，瑜引兵从之。为中郎将，孙策相待甚厚，又同娶二乔。策临终，嘱弟权曰：“外事不决，可问周瑜”。瑜奔丧还吴，与张昭共佐权，并荐鲁肃等，掌军政大事。赤壁战前，瑜自鄱阳归。力主战曹，后于群英会戏蒋干、怒打黄盖行诈降计、后火烧曹军，大败之。后下南郡与曹仁相持，中箭负伤，与诸葛亮较智斗，定假涂灭虢等计，皆为亮破，后气死于巴陵，年三十六岁。临终，上书荐鲁肃代其位，权为其素服吊丧。"
                    ,80, 90, 89, 900));
            Herolist.add(new LocalHero("孙权",R.drawable.sunquan,"男","（182 - 252）", "扬州吴郡富","吴","历史简介：\n 孙权19岁就继承了其兄孙策之位，力据江东，击败了黄祖。后东吴联合刘备，在赤壁大战击溃了曹操军。东吴后来又和曹操军在合肥附近鏖战，并从刘备手中夺回荆州、杀死关羽、大破刘备的讨伐军。曹丕称帝后孙权先向北方称臣，后自己建吴称帝，迁都建业。"
                    ,75, 87, 90, 950));
            Herolist.add(new LocalHero("吕蒙",R.drawable.lvmeng,"男","（178 - 219）", "豫州汝南郡富波","吴","历史简介：\n 于孙权广纳人才时投到东吴麾下，从破黄祖，吕蒙奋勇先登，亲斩敌将陈就。后于濡须口从拒曹操，吕蒙独具战略眼光，力排众论，卒破取皖城太守朱光。后来吕蒙接任鲁肃之职，镇守陆口，其名声之响，连向来骄矜的关羽也不敢有所轻忽。为了成功袭取荆州，吕蒙连同孙权、陆逊一起筹略，设伏用奇，击败关羽，擒其父子，立下大功。但在君臣庆功之时，吕蒙却因被关羽阴魂索命而死。"
                    ,80, 89, 88, 900));
        } else {
            Herolist.clear();
            for (int i = 0; i < tmp.size(); i++) {
                /*只显示10条
                * 其它用于搜索
                * */
                if (i < 20 ) {
                    Herolist.add(tmp.get(i));
                }
            }
        }

        /*
        * 写入数据库
        * 注意第二次开启APP不重写，以后只会使用增删查改单步操作
        * */
        if (tmp.size() == 0) {
            ApiOfDatabase apiOfDatabase = new ApiOfDatabase();
            apiOfDatabase.WriteLocalHeroToDatabase(Herolist);
        }
    }

    private void InitNonEditedHeros() {

        List<NonEditedHero> tmp = DataSupport.findAll(NonEditedHero.class);

        if (tmp.size() == 0) {
            NonEditedHeroList.add(new NonEditedHero("左慈",R.drawable.zuoci));
            NonEditedHeroList.add(new NonEditedHero("祖茂",R.drawable.zumao));
            NonEditedHeroList.add(new NonEditedHero("诸葛瑾",R.drawable.zhugejin));
            NonEditedHeroList.add(new NonEditedHero("诸葛均",R.drawable.zhugejun));
            NonEditedHeroList.add(new NonEditedHero("朱灵",R.drawable.zhuling));
            NonEditedHeroList.add(new NonEditedHero("周仓",R.drawable.zhoucang));
            NonEditedHeroList.add(new NonEditedHero("钟繇",R.drawable.zhongyao));
            NonEditedHeroList.add(new NonEditedHero("曹植",R.drawable.caozhi));
            NonEditedHeroList.add(new NonEditedHero("曹丕",R.drawable.caopi));
            NonEditedHeroList.add(new NonEditedHero("鲁肃",R.drawable.lusu));
            NonEditedHeroList.add(new NonEditedHero("陆逊",R.drawable.luxun));
            NonEditedHeroList.add(new NonEditedHero("吕蒙",R.drawable.lvmeng));
            NonEditedHeroList.add(new NonEditedHero("张辽",R.drawable.zhangliao));
            NonEditedHeroList.add(new NonEditedHero("华雄",R.drawable.huaxiong));
            NonEditedHeroList.add(new NonEditedHero("孙坚",R.drawable.sunjian));
            NonEditedHeroList.add(new NonEditedHero("孙权",R.drawable.sunquan));
        } else {
            NonEditedHeroList.clear();
            NonEditedHeroList = tmp;
        }

        /*
        * 写入数据库,注意第二次开启APP不重写
        * 以后只会使用增删查改单步操作
        * */

        if (tmp.size() == 0) {
            ApiOfDatabase apiOfDatabase = new ApiOfDatabase();
            apiOfDatabase.WriteNonEditedHeroToDatabase(NonEditedHeroList);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}