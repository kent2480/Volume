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

    public int getValue(VolumeType type) {
        switch(type){
            case MUSIC:
                return 0;
            case ALARM:
                return 1;
            case NOTI:
                return 2;
            case RING:
                return 3;
            case SYSTEM:
                return 4;
            case VOICE:
                return 5;
            default:
                return -1;

        }
    }
}

