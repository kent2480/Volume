package com.app.kent.volume;

/**
 * Created by Kent_Zheng on 2015/3/20.
 */
public enum VolumeType {
    MUSIC(0), ALARM(1), NOTI(2), RING(3), SYSTEM(4), VOICE(5);

    private final int value;

    private VolumeType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

