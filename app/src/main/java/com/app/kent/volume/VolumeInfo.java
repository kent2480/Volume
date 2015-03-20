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

    public int getVolume(VolumeType type) {
        switch (type) {
            case MUSIC:
                return am.getStreamVolume(AudioManager.STREAM_MUSIC);
            case ALARM:
                return am.getStreamVolume(AudioManager.STREAM_ALARM);
            case NOTI:
                return am.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
            case RING:
                return am.getStreamVolume(AudioManager.STREAM_RING);
            case SYSTEM:
                return am.getStreamVolume(AudioManager.STREAM_SYSTEM);
            case VOICE:
                return am.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
            default:
                return 0;
        }
    }
}

