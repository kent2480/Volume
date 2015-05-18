package com.app.kent.volume;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by kent on 2015/5/16.
 */
public class NotificationService extends Service{
    private static final String TAG = "NotificationService";
    private AudioManager mAudioManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        Bundle bundle = intent.getExtras();
        String mode = bundle.getString("mode");
        if (mode.equals("up")) {
            Log.d(TAG, "up");
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + 1, 0);
            Log.d(TAG, "Up: " + mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        } else if (mode.equals("down")) {
            Log.d(TAG, "down");
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) - 1, 0);
            Log.d(TAG, "Down: " + mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }

        return super.onStartCommand(intent,flags,startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
