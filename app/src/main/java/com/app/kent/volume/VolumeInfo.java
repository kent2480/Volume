package com.app.kent.volume;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by kent on 2015/3/1.
 */

/**
 * music = 0
 * alarm = 1
 * notification = 2
 * ring = 3
 * system = 4
 * */

public class VolumeInfo {

    private AudioManager am;
    private int musicCurrent, alarmCurrent, notificationCurrent, ringCurrent, systemCurrent;

    VolumeInfo (Context context) {
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void getCurrentVolume() {
        musicCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        alarmCurrent = am.getStreamVolume(AudioManager.STREAM_ALARM);
        notificationCurrent = am.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        ringCurrent = am.getStreamVolume(AudioManager.STREAM_RING);
        systemCurrent = am.getStreamVolume(AudioManager.STREAM_SYSTEM);
    }

    public int getVolume(int type) {
        switch (type) {
            case 0:
                return musicCurrent;
            case 1:
                return alarmCurrent;
            case 2:
                return notificationCurrent;
            case 3:
                return ringCurrent;
            case 4:
                return systemCurrent;
            default:
                return 0;
        }
    }
}

