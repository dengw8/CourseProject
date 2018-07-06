package com.example.duang1996.mvp_puzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import config.Config;

/**
 * Created by duang1996 on 2018/4/16.
 */

public class SelectImage extends Activity implements View.OnClickListener {
    private ImageView[] imgView = new ImageView[6];
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_image);

        imgView[0] = (ImageView) findViewById(R.id.iv1);
        imgView[1] = (ImageView) findViewById(R.id.iv2);
        imgView[2] = (ImageView) findViewById(R.id.iv3);
        imgView[3] = (ImageView) findViewById(R.id.iv4);
        imgView[4] = (ImageView) findViewById(R.id.iv5);
        imgView[5] = (ImageView) findViewById(R.id.iv6);

        for(int i = 0; i < 6 ; i++){
            imgView[i].setOnClickListener(this);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio0:
                        Config.nandu = 3;
                        break;
                    case R.id.radio1:
                        Config.nandu = 4;
                        break;
                    case R.id.radio2:
                        Config.nandu = 5;
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Config.imageId = v.getId();
        Intent intent = new Intent(this, NewGame.class);
        startActivity(intent);
    }
}