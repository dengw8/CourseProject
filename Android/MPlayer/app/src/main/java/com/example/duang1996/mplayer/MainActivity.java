package com.example.duang1996.mplayer;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    private ImageView image;
    private TextView state;
    private SeekBar seekBar;
    private TextView playingTime;
    private TextView totalTime;
    private Button stopBtn;
    private Button playOrPauseBtn;
    private Button quitBtn;

    private MyService myService;
    //定义时间格式
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");

    private ObjectAnimator animator;

    private  ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = new MyService();
            myService = ((MyService.MyBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    // 通过 Handler 更新 UI
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            playingTime.setText(time.format(myService.getMediaPlayer().getCurrentPosition()));
            totalTime.setText(time.format(myService.getMediaPlayer().getDuration()));
            seekBar.setProgress(myService.getMediaPlayer().getCurrentPosition());
            seekBar.setMax(myService.getMediaPlayer().getDuration());
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser == true) {
                        myService.getMediaPlayer().seekTo(progress);
                    }
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
            handler.post(runnable);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitElement();

        bindServiceConnection();
        InitElement();
        setAnimator();

        verifyStoragePermissions(this);
    }

    private void InitElement() {
        image = (ImageView)findViewById(R.id.image);
        state = (TextView)findViewById(R.id.state);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        playingTime = (TextView)findViewById(R.id.playingTime);
        totalTime = (TextView) findViewById(R.id.totalTime);
        stopBtn = (Button) findViewById(R.id.stopBtn);
        playOrPauseBtn = (Button) findViewById(R.id.PlayOrPauseBtn);
        quitBtn = (Button) findViewById(R.id.quitBtn);

        /*
         *为三个按钮添加事件监听器
         */
        stopBtn.setOnClickListener(new myOnClickListener());
        playOrPauseBtn.setOnClickListener(new myOnClickListener());
        quitBtn.setOnClickListener(new myOnClickListener());
        
    }

    //因为存在较多的交互，使用IBinder的方式开启服务
    private void bindServiceConnection() {
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, serviceConnection, this.BIND_AUTO_CREATE);
    }

    private class myOnClickListener implements View.OnClickListener {
        @Override
        public void onClick (View v) {
            switch(v.getId()) {
                case R.id.PlayOrPauseBtn:
                    startOrPause();
                    break;
                case R.id.stopBtn:
                    stopPlayer();
                    break;
                case R.id.quitBtn:
                    quitPlayer();
                    break;
                default:
                    break;
            }
        }
    }
    //播放状态转换的前台逻辑函数
    private void startOrPause() {
        if(state.getText().equals("Stopped") || state.getText().equals("Paused")) {
            if(state.getText().equals("Stopped")) {
                animator.start();
                handler.post(runnable);
            }
            else {
                animator.resume();
            }
            state.setText("Playing");
            playOrPauseBtn.setText("PAUSED");
        }
        else if(state.getText().equals("Playing") ){
            state.setText("Paused");
            playOrPauseBtn.setText("PLAY");
            animator.pause();
        }
        myService.playOrPause();
    }

    //停止播放的前台业务逻辑函数
    private void stopPlayer() {
        state.setText("Stopped");
        playOrPauseBtn.setText("PLAY");
        myService.stop();
        animator.end();
    }

    //退出播放器的前台业务逻辑函数
    private void quitPlayer() {
        myService.quit();
        handler.removeCallbacks(runnable);
        this.finish();
    }

    /*
     *定义动画
     */
    private void setAnimator() {
        animator = ObjectAnimator.ofFloat(image, "rotation", 0f, 360.0f);
        animator.setDuration(5000);
        animator.setInterpolator(new LinearInterpolator()); // 均速旋转
        animator.setRepeatCount(ValueAnimator.INFINITE); // 无限循环
        animator.setRepeatMode(ValueAnimator.RESTART);
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        try {
            // Check if we have write permission
            int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        //退出活动前停止绑定服务
        unbindService(serviceConnection);
        super.onDestroy();
    }

}
