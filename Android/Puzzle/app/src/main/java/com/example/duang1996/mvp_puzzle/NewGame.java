package com.example.duang1996.mvp_puzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.duang1996.mvp_puzzle.eventBus.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import config.Config;

/**
 * Created by duang1996 on 2018/4/16.
 */

public class NewGame extends Activity {
    private TextView timeValue;
    private TextView stepValue;
    private Button button;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    // 移除所有的msg.what为0等消息，保证只有一个循环消息队列再跑
                    handler.removeMessages(0);

                    Config.time = (int)((System.currentTimeMillis() - Config.startTime) / 1000);
                    timeValue.setText("" + Config.time);
                    handler.sendEmptyMessageDelayed(0, 1000);
                    break;
                case 1:
                    // 直接移除，定时器停止
                    handler.removeMessages(0);
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game);

        timeValue = findViewById(R.id.timeValue);
        stepValue = findViewById(R.id.stepValue);
        button = findViewById(R.id.reset);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( NewGame.this, SelectImage.class);
                startActivity(intent);
            }
        });

        Config.startTime = System.currentTimeMillis();

        handler.sendEmptyMessage(0);

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if(messageEvent.getMessage().equals("step")) {
            stepValue.setText("" + Config.bushu);
        }
        if(messageEvent.getMessage().equals("success")) {
            handler.removeMessages(0);

            Intent intent = new Intent(this, ScoreActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}

