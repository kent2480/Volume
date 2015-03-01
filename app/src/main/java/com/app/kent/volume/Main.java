package com.app.kent.volume;

import android.content.Context;
import android.media.AudioManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * STREAM_MUSIC 15
 * STREAM_ALARM 7
 * STREAM_DTMF
 * STREAM_NOTIFICATION 7
 * STREAM_RING 7
 * STREAM_SYSTEM 7
 * STREAM_VOICE_CALL*/
public class Main extends ActionBarActivity {
    private final static String TAG = "VolumnMain";
    private Context mContext;
    private AudioManager am;
    private SeekBar sbMusic, sbAlarm, sbNotification, sbRing, sbSystem;
    private int musicMax, alarmMax, notificationMax, ringMax, systemMax;
    private int musicVolume, alarmVolume, notificationVolumn, ringVolume, systemVolume;
    private TextView tvMusic, tvAlarm, tvNotification, tvRing, tvSystem;
    private Button outdoor, mute, custom;
    private VolumeInfo mVolumeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        getVolumeInfo();
        initSeekBar();
        setListener();
    }

    public void setListener() {

        outdoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVolumeInfo.getCurrentVolume();
                am.setStreamVolume(AudioManager.STREAM_ALARM, alarmMax, 0);
                am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, notificationMax, 0);
                am.setStreamVolume(AudioManager.STREAM_RING, ringMax, 0);
                am.setStreamVolume(AudioManager.STREAM_SYSTEM, systemMax, 0);

                sbAlarm.setProgress(alarmMax);
                tvAlarm.setText(alarmVolume + "/" + alarmMax);
                sbNotification.setProgress(notificationMax);
                tvNotification.setText(notificationVolumn + "/" + notificationMax);
                sbRing.setProgress(ringMax);
                tvRing.setText(ringVolume + "/" + ringMax);
                sbSystem.setProgress(systemMax);
                tvSystem.setText(systemVolume + "/" + systemMax);
            }
        });

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVolumeInfo.getCurrentVolume();
                am.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
                am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0);
                am.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
                am.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0);

                sbAlarm.setProgress(0);
                tvAlarm.setText(0 + "/" + alarmMax);
                sbNotification.setProgress(0);
                tvNotification.setText(0 + "/" + notificationMax);
                sbRing.setProgress(0);
                tvRing.setText(0 + "/" + ringMax);
                sbSystem.setProgress(0);
                tvSystem.setText(0 + "/" + systemMax);
            }
        });

        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // need file to store value
                mVolumeInfo.getCurrentVolume();

                am.setStreamVolume(AudioManager.STREAM_MUSIC, mVolumeInfo.getVolume(0), 0);
                am.setStreamVolume(AudioManager.STREAM_ALARM, mVolumeInfo.getVolume(1), 0);
                am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, mVolumeInfo.getVolume(2), 0);
                am.setStreamVolume(AudioManager.STREAM_RING, mVolumeInfo.getVolume(3), 0);
                am.setStreamVolume(AudioManager.STREAM_SYSTEM, mVolumeInfo.getVolume(4), 0);

                sbMusic.setProgress(mVolumeInfo.getVolume(0));
                tvMusic.setText(mVolumeInfo.getVolume(0) + "/" + musicMax);
                sbAlarm.setProgress(mVolumeInfo.getVolume(1));
                tvAlarm.setText(mVolumeInfo.getVolume(1) + "/" + alarmMax);
                sbNotification.setProgress(mVolumeInfo.getVolume(2));
                tvNotification.setText(mVolumeInfo.getVolume(2) + "/" + notificationMax);
                sbRing.setProgress(mVolumeInfo.getVolume(3));
                tvRing.setText(mVolumeInfo.getVolume(3) + "/" + ringMax);
                sbSystem.setProgress(mVolumeInfo.getVolume(4));
                tvSystem.setText(mVolumeInfo.getVolume(4) + "/" + systemMax);
            }
        });

        sbMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "progress = " + progress);
                progressValue = progress;
                am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvMusic.setText(progressValue + "/" + musicMax);
            }
        });

        sbAlarm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "progress = " + progress);
                progressValue = progress;
                am.setStreamVolume(AudioManager.STREAM_ALARM, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvAlarm.setText(progressValue + "/" + notificationMax);
            }
        });

        sbNotification.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "progress = " + progress);
                progressValue = progress;
                am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvNotification.setText(progressValue + "/" + notificationMax);
            }
        });

        sbRing.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "progress = " + progress);
                progressValue = progress;
                am.setStreamVolume(AudioManager.STREAM_RING, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvRing.setText(progressValue + "/" + ringMax);
            }
        });

        sbSystem.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "progress = " + progress);
                progressValue = progress;
                am.setStreamVolume(AudioManager.STREAM_SYSTEM, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvSystem.setText(progressValue + "/" + systemMax);
            }
        });

    }

    public void initSeekBar() {
        sbMusic.setMax(musicMax);
        sbMusic.setProgress(musicVolume);
        sbAlarm.setMax(alarmMax);
        sbAlarm.setProgress(alarmVolume);
        sbNotification.setMax(notificationMax);
        sbNotification.setProgress(notificationVolumn);
        sbRing.setMax(ringMax);
        sbRing.setProgress(ringVolume);
        sbSystem.setMax(systemMax);
        sbSystem.setProgress(systemVolume);
    }

    public void initView() {
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        sbMusic = (SeekBar) findViewById(R.id.seekBar);
        sbAlarm = (SeekBar) findViewById(R.id.seekBar2);
        sbNotification = (SeekBar) findViewById(R.id.seekBar3);
        sbRing = (SeekBar) findViewById(R.id.seekBar4);
        sbSystem = (SeekBar) findViewById(R.id.seekBar5);

        tvMusic = (TextView) findViewById(R.id.tv_Music);
        tvAlarm = (TextView) findViewById(R.id.tv_Alarm);
        tvNotification = (TextView) findViewById(R.id.tv_Notification);
        tvRing = (TextView) findViewById(R.id.tv_Ring);
        tvSystem = (TextView) findViewById(R.id.tv_System);

        outdoor = (Button) findViewById(R.id.button);
        mute = (Button) findViewById(R.id.button2);
        custom = (Button) findViewById(R.id.button3);

        mContext = this.getApplicationContext();
        mVolumeInfo = new VolumeInfo(mContext);
    }

    public void getVolumeInfo() {
        musicMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        Log.d(TAG, "musicMax = " + musicMax);
        musicVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.d(TAG, "musicVolume = " + musicVolume);
        tvMusic.setText(musicVolume + "/" + musicMax);

        alarmMax = am.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        Log.d(TAG, "alarmMax = " + alarmMax);
        alarmVolume = am.getStreamVolume(AudioManager.STREAM_ALARM);
        Log.d(TAG, "alarmVolume = " + alarmVolume);
        tvAlarm.setText(alarmVolume + "/" + alarmMax);


        notificationMax = am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
        Log.d(TAG, "notificationMax = " + notificationMax);
        notificationVolumn = am.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        Log.d(TAG, "notificationVolumn = " + notificationVolumn);
        tvNotification.setText(notificationVolumn + "/" + notificationMax);


        ringMax = am.getStreamMaxVolume(AudioManager.STREAM_RING);
        Log.d(TAG, "ringMax = " + ringMax);
        ringVolume = am.getStreamVolume(AudioManager.STREAM_RING);
        Log.d(TAG, "ringVolume = " + ringVolume);
        tvRing.setText(ringVolume + "/" + ringMax);


        systemMax = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        Log.d(TAG, "systemMax = " + systemMax);
        systemVolume = am.getStreamVolume(AudioManager.STREAM_SYSTEM);
        Log.d(TAG, "systemVolume = " + systemVolume);
        tvSystem.setText(systemVolume + "/" + systemMax);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
