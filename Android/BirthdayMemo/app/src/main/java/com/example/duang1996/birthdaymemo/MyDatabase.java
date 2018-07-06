package com.example.duang1996.birthdaymemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by duang1996 on 2017/12/6.
 */

public class MyDatabase extends SQLiteOpenHelper {
    private Context context;
    private String tableName = "Contacts";

    public MyDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + tableName
                + "(id integer primary key autoincrement, "
                + "name text, "
                + "birth text, "
                + "gift text)";

        db.execSQL(CREATE_TABLE);
        Log.d("MyDatabase", "创建成功！");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oidVersion, int newVersion) {
        Log.d("MyDatabase", "更新成功！");
    }
}
