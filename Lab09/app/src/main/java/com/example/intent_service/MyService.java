package com.example.intent_service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyService extends Service {

    MediaPlayer mymedia;
    private boolean isPlaying = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mymedia = MediaPlayer.create(MyService.this, R.raw.tinhme);
        if (mymedia != null) {
            mymedia.setLooping(true);
        } else {
            Toast.makeText(this, "Không thể tạo MediaPlayer", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mymedia != null) {
            if (mymedia.isPlaying()) {
                mymedia.pause();
                isPlaying = false;

            } else {
                mymedia.start();
                isPlaying = true;

            }
        }

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mymedia != null) {
            if (mymedia.isPlaying()) {
                mymedia.stop();
            }
            mymedia.release();
            mymedia = null;
            isPlaying = false;

        }
    }


    public boolean isPlaying() {
        return isPlaying;
    }
}