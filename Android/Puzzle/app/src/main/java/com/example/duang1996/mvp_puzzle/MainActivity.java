package com.example.duang1996.mvp_puzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import config.Config;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button[] button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config.metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Config.metrics);

        setContentView(R.layout.activity_main);

        button = new Button[2];
        button[0] = (Button) findViewById(R.id.newGame);
        button[1] = (Button) findViewById(R.id.exit);
        for(int i = 0; i < 2; i++){
            button[i].setOnClickListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.newGame:
                Intent intent = new Intent(this, SelectImage.class);
                startActivity(intent);
                break;
            case R.id.exit:
                finish();
                break;
        }
    }
}