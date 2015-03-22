package com.app.kent.volume;

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
}
