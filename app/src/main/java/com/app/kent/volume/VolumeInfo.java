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
 * voice call = 5
 * */

public class VolumeInfo {
    private final static String TAG = "VolumeInfo";

    private AudioManager am;

    VolumeInfo (Context context) {
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public int getVolume(int type) {
        switch (type) {
            case 0:
                return am.getStreamVolume(AudioManager.STREAM_MUSIC);
            case 1:
                return am.getStreamVolume(AudioManager.STREAM_ALARM);
            case 2:
                return am.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
            case 3:
                return am.getStreamVolume(AudioManager.STREAM_RING);
            case 4:
                return am.getStreamVolume(AudioManager.STREAM_SYSTEM);
            case 5:
                return am.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
            default:
                return 0;
        }
    }
}

