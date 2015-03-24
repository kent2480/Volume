package com.app.kent.volume;

import android.content.Context;
import android.media.AudioManager;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Kent_Zheng on 2015/3/20.
 */

public class VolumeMember {
    public SeekBar seekBar;
    public int max;
    public int current;
    public TextView textView;
    public AudioManager am;

    VolumeMember(Context context) {
        am = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
    }

    public int getAudioType(int type) {
        switch(type) {
            case 0:
                return AudioManager.STREAM_MUSIC;
            case 1:
                return AudioManager.STREAM_ALARM;
            case 2:
                return AudioManager.STREAM_NOTIFICATION;
            case 3:
                return AudioManager.STREAM_RING;
            case 4:
                return  AudioManager.STREAM_SYSTEM;
            case 5:
                return AudioManager.STREAM_VOICE_CALL;
            default:
                return -1;
        }
    }

    public int getCurrent(int type) {
        this.current = am.getStreamVolume(getAudioType(type));
        return this.current;
    }

    public int getMax(int type) {
        this.max = am.getStreamMaxVolume(getAudioType(type));
        return this.max;
    }
}
