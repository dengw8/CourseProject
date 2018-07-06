package com.example.duang1996.birthdaymemo;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button addBtn;
    private ListView list;

    private MyDatabase dbHelper;

    private List<Info> items = new ArrayList<Info>();
    private List<Map<String, Object>> headerlist = new ArrayList<>();
    private SimpleAdapter simpleAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * 初始化控件元素
         */
        initElement();
        /*
         *从数据库读取元素
         */
        readFromDatabase();
        /*
         * 初始化界面
         */
        resetView();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Info item = items.get(i);

                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                View newView = factory.inflate(R.layout.dialog_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                final TextView name = (TextView)newView.findViewById(R.id.nameDialog);
                name.setText(item.getName());
                final EditText birth= (EditText) newView.findViewById(R.id.birthDialog);
                birth.setText(item.getBirth());
                final EditText gift = (EditText) newView.findViewById(R.id.giftDialog);
                gift.setText(item.getGift());
                TextView tel = newView.findViewById(R.id.number);
                String phoneNumber = "";

                // 通过询问，动态申请读取通信录的权限
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS},0);
                }

                Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);
                if(cursor.moveToFirst()) {
                    do {
                        if (cursor.getString(cursor.getColumnIndex("display_name")).equals(item.getName())) {

                            String str = cursor.getString(cursor.getColumnIndex("_id"));
                            // 判断某条联系人的信息中，是否有电话号码
                            if (Integer.parseInt(cursor.getString(cursor.getColumnIndex("has_phone_number"))) > 0) {
                                Log.d("mydebug", "执行到这儿了");
                                Cursor phone = getContentResolver().query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id = " + str,
                                        null, null);
                                while (phone.moveToNext()) {
                                    phoneNumber += phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + " ";
                                }
                                phone.close();
                            }
                            break;
                        }
                    }
                    while(cursor.moveToNext());
                }
                cursor.close();

                if (phoneNumber.equals("")) phoneNumber = "无";
                tel.setText(phoneNumber);


                builder.setTitle("ヽ(*￣∀￣)ﾉ～★恭喜发财★～");
                builder.setView(newView);
                builder.setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(int j = 0; j < items.size(); j++) {
                            if(item.getName().equals(items.get(j).getName())) {
                                String birthText = birth.getText().toString();
                                String giftText = gift.getText().toString();
                                items.get(j).setBirth(birthText);
                                items.get(j).setGift(giftText);
                                databaseUpdate(items.get(j));
                                resetView();
                                break;
                            }
                        }
                    }
                });
                builder.setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                builder.show();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder message = new AlertDialog.Builder(MainActivity.this);
                message.setTitle("是否删除？");
                message.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseDelete(items.get(position));
                        items.remove(position);
                        headerlist.remove(position);
                        simpleAdapter.notifyDataSetChanged();
                    }
                });
                message.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                message.show();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String name = data.getExtras().getString("name");
        String birth = data.getExtras().getString("birth") ;
        String gift = data.getExtras().getString("gift");

        Info info = new Info(name, birth, gift);
        items.add(info);
        /*
         *网数据库中添加数据
         */
        databaseInsert(info);
        resetView();
    }

    private void initElement() {
        addBtn = findViewById(R.id.addBtn);
        list = findViewById(R.id.list);
    }

    private void resetView() {
        headerlist.clear();
        for(int i = 0; i < items.size(); i++) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("name", items.get(i).getName());
            temp.put("birth", items.get(i).getBirth());
            temp.put("gift", items.get(i).getGift());
            headerlist.add(temp);
        }
        simpleAdapter = new SimpleAdapter(MainActivity.this, headerlist, R.layout.item,
                new String[]{"name", "birth", "gift"}, new int[]{R.id.name, R.id.birth, R.id.gift});
        list.setAdapter(simpleAdapter);
    }

    private void databaseInsert(Info info) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", info.getName());
        values.put("birth", info.getBirth());
        values.put("gift", info.getGift());
        db.insert("Contacts", null, values);
        Log.d("MyDatabase","insert " + info.getName() + " successfully" );
        values.clear();
        db.close();
    }

    private void databaseUpdate(Info info) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", info.getName());
        values.put("birth", info.getBirth());
        values.put("gift", info.getGift());
        db.update("Contacts", values, "name = ?", new String[] {info.getName()});
        Log.d("MyDatabase","update " + info.getName() + " successfully" );
        values.clear();
        db.close();
    }

    private void databaseDelete(Info info) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Contacts", "name = ?", new String[] {info.getName()});
        Log.d("MyDatabase","delete " + info.getName() + " successfully" );
        db.close();
    }

    private void readFromDatabase() {
        dbHelper = new MyDatabase(this, "Contract.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Contacts",null, null, null,
                null, null, null);
        if(cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String birth = cursor.getString(cursor.getColumnIndex("birth"));
                String gift = cursor.getString(cursor.getColumnIndex("gift"));
                items.add(new Info(name, birth, gift));
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }
}

