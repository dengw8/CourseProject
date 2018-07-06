package com.example.duang1996.mplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;


public class MyService extends Service {

    private MediaPlayer mediaPlayer;

    private MyBinder myBinder = new MyBinder();;
    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    public MyService() {
        mediaPlayer = new MediaPlayer();
        InitPlayer();
    }
    //初始化播放器
    private void InitPlayer() {
        try {
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/melt.mp3";
            mediaPlayer.setDataSource(file_path);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);  // 设置循环播放
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //播放状态后台的转换函数
    public void playOrPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Log.d("状态","暂停");
        } else {
            mediaPlayer.start();
            Log.d("状态","开始");
        }
    }
    //停止播放的后台业务逻辑函数
    public void stop() {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            Log.d("状态","停止");
            try {
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // 退出播放器的后台业务逻辑函数
    public void quit() {
        mediaPlayer.release();
    }

    //获取一个MediaPlayer的对象
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    //创建服务时调用
    @Override
    public void onCreate() {
        super.onCreate();
    }

    //每次启动服务时调用
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    //销毁服务时调用
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

