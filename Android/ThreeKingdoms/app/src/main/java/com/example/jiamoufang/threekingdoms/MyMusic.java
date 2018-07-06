package com.example.jiamoufang.threekingdoms;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by a0924 on 2017/11/7.
 */
public class MyMusic {

    private MediaPlayer mediaPlayer;
    private Context context;

    public MyMusic(Context context) {
        this.context = context;
    }

    public void initMusic(int id) {
            mediaPlayer = MediaPlayer.create(context, id);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.start();
    }

    public void releaseMusic() {
            if (mediaPlayer != null ) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
    }
}
