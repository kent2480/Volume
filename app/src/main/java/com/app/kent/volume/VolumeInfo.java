package com.app.kent.volume;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

/**
 * Created by kent on 2015/3/1.
 */

/**
 * music = 0
 * alarm = 1
 * notification = 2
 * ring = 3
 * system = 4
 * voice call = 5
 * */

public class VolumeInfo {
    private final static String TAG = "VolumnInfo";

    private AudioManager am;
    private int musicCurrent, alarmCurrent, notificationCurrent, ringCurrent, systemCurrent,
                voiceCurrent;

    VolumeInfo (Context context) {
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void getCurrentVolume() {
        Log.d(TAG, "getCurrentVolume");
        musicCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        alarmCurrent = am.getStreamVolume(AudioManager.STREAM_ALARM);
        notificationCurrent = am.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        ringCurrent = am.getStreamVolume(AudioManager.STREAM_RING);
        systemCurrent = am.getStreamVolume(AudioManager.STREAM_SYSTEM);
        voiceCurrent = am.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
    }

    public int getVolume(int type) {
        switch (type) {
            case 0:
                return musicCurrent;
            case 1:
                return alarmCurrent;
            case 2:
                //Log.d(TAG, "getVolume 2: notification currently =" + notificationCurrent);
                return notificationCurrent;
            case 3:
                return ringCurrent;
            case 4:
                return systemCurrent;
            case 5:
                return voiceCurrent;
            default:
                return 0;
        }
    }
}

