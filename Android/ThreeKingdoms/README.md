# ThreeKingdoms
![](https://img.shields.io/badge/download-1K-brightgreen.svg)   ![](https://img.shields.io/badge/android-app-brightgreent.svg)


Android手机平台应用开发课程期中项目作业

# Project Design 

### 应用的主界面由三个Fragment组成，同时含有抽屉导航。

三个fragment分别是 三国志， 群英会，风云录

## 维护三个全局列表（项目根本）

```
   /*已经编辑的英雄*/
    public static List<LocalHero> Herolist = new ArrayList<>();

    /*尚未编辑的英雄，在添加界面可供选择编辑添加*/
    public static List<NonEditedHero> NonEditedHeroList = new ArrayList<>();

    /*英雄对决记录，在群英会界面使用，每次对决添加*/
    public static List<PkRecords> pkRecordsList = new ArrayList<>();
    
     /*我喜欢的英雄*/
    public static List<LocalHero> MylovedHeros = new ArrayList<>();
    
```
### 对决记录存取操作已完成，考虑优化：同步更新（多线程、或广播、或服务）

### LocalHero 存取操作已完成，考虑优化：同步更新

### 删除了图片上传功能，图片只能从系统内置资源中选择，添加方法与之前一样！！！

## 本地数据库存取

采用``Litepal``库，而不用系统自带的``SQLite``，后者的操作语句有点繁琐，前者是面向对象的，是一种对象关系映射模型，直接对应数据库的表的行与列。
详细使用可以看《第一行代码（第二版）》或者自行搜索资料解决。

对象模型

```
    LocalHero.java  //用于储存英雄资料信息
    PKRecords.java  //用于英雄对决记录
    MyHero.java  //这个还未添加，用于‘我的英雄’
    NonEditedHeros.java // 表示那些还没被编辑的英雄
```

## 注意查看 api 目录下的 apiOfDatabase，提供了数据库操作方法，数据库操作方法尽可能都写在这个文件里！！！

# Get Started

项目目录

ThreeKingdoms/app/src/main/java/com/example/jiamoufang/threekingdoms/

```
  -- activities	//所有活动
    -- AddHero.java
    -- HeroDetailsActivity.java
  -- adapter  //所有适配器
    -- HeroAdapter.java
  -- entities  //所有的与云端数据库和本地数据库之间的关系映射的对象模型，即实体（entity）
    -- Hero.java
    -- PKRecords
  -- api
    -- database  // 数据库存取api
  -- fragment	 //三个Fragment
    -- HerosListFragment.java	
    -- HerosPKFragment.java
    -- HitHeroFragment.java
  -- heros	 //人物英雄类
   -- Hero.java
  -- MainActivity.java
```





