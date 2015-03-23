package com.app.kent.volume;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * STREAM_MUSIC 15
 * STREAM_ALARM 7
 * STREAM_DTMF
 * STREAM_NOTIFICATION 7
 * STREAM_RING 7
 * STREAM_SYSTEM 7
 * STREAM_VOICE_CALL 7
 * */
public class Main extends ActionBarActivity {
    private final static String TAG = "VolumeMain";
    private final static boolean Debug = false;
    private Context mContext;
    private DisplayMetrics dm;
    private AudioManager am;

    //private SeekBar sbMusic, sbAlarm, sbNotification, sbRing, sbSystem, sbVoice;
    //private int musicMax, alarmMax, notificationMax, ringMax, systemMax, voiceMax;
    //private int musicVolume, alarmVolume, notificationVolumn,
    //            ringVolume, systemVolume, voiceVolume;
    //private TextView tvMusic, tvAlarm, tvNotification, tvRing, tvSystem, tvVoice;

    private Button outdoor, mute, custom;
    private VolumeInfo mVolumeInfo;
    private int startModeItemPref, stopModeItemPref, startTimeItemPref, stopTimeItemPref;
    private LinearLayout mLinearCustom;
    private int customBtnSumPref = 0;
    private String customBtnPref1, customBtnPref2, customBtnPref3;
    private SharedPreferences settings;
    private Button mButton;
    private VolumeMember music, alarm, noti, ring, system, voice;
    private ArrayList<VolumeMember> mVolMember;

    // https://www.iconfinder.com/iconsets/slim-square-icons-basics
    //private ImageView musicUp, musicDown; // using imageView onClick properties

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        createMember();
        initView();

        getVolumeInfo();
        initSeekBar();
        setListener();

        dm = mContext.getResources().getDisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        // FE375CG: 1216*800 scale = 1.332501
        // Nexus2:  1824*1200 scale = 2.0
        // Zenfone: 1280*720 scale = 2.0
        Log.d(TAG, "Height = " + dm.heightPixels + ", width = " + dm.widthPixels);
        Log.d(TAG, "Density = " + dm.density);
        Log.d(TAG, "DensityDpi = " + dm.densityDpi);
        Log.d(TAG, "X dpi = " + dm.xdpi);
        Log.d(TAG, "Y dpi = " + dm.ydpi);
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        Log.d(TAG, "Scale = " + scale);


        Log.d(TAG, "start activity1");
        Intent mIntent = new Intent();
        mIntent.setClass(Main.this, Welcome.class);
        startActivity(mIntent);

        reloadData();

        scanDevice();
    }

    public void scanDevice() {
        int[] temp = new int[6]; //0 ~ 5
        for(int i = 0; i < mVolMember.size(); i++) {
            temp[i] = am.getStreamVolume(mVolMember.get(i).getAudioType(i));
            Log.d(TAG, "temp[i] = " + temp[i]);
        }

        for(int i = 0; i < mVolMember.size(); i++) {
            am.setStreamVolume(mVolMember.get(i).getAudioType(i), 5, 0);
        }
        Log.d(TAG, "System volume0 = " + am.getStreamVolume(AudioManager.STREAM_SYSTEM));
//        am.setStreamVolume(AudioManager.STREAM_SYSTEM, 5, 0);
        Log.d(TAG, "System volume1 = " + am.getStreamVolume(AudioManager.STREAM_SYSTEM));

        //am.setStreamVolume(AudioManager.STREAM_SYSTEM, 4, 0);
        //Log.d(TAG, "System volume2 = " + am.getStreamVolume(AudioManager.STREAM_SYSTEM));

//        am.setStreamVolume(mVolMember.get(2).getAudioType(2), 0, 0);
//        Log.d(TAG, "a = " + am.getStreamVolume(AudioManager.STREAM_NOTIFICATION));
//        Log.d(TAG, "b = " + am.getStreamVolume(AudioManager.STREAM_SYSTEM));
//
//        am.setStreamVolume(mVolMember.get(2).getAudioType(2), 1, 0);
//        Log.d(TAG, "c = " + am.getStreamVolume(AudioManager.STREAM_NOTIFICATION));
//        Log.d(TAG, "d = " + am.getStreamVolume(AudioManager.STREAM_SYSTEM));
//
//        am.setStreamVolume(mVolMember.get(2).getAudioType(2), 2, 0);
//        Log.d(TAG, "e = " + am.getStreamVolume(AudioManager.STREAM_NOTIFICATION));
//        Log.d(TAG, "f = " + am.getStreamVolume(AudioManager.STREAM_SYSTEM));


//        if(am.getStreamVolume(mVolMember.get(3).getAudioType(3)) == 1 &&
//                am.getStreamVolume(mVolMember.get(4).getAudioType(4)) == 1) {
//            Log.d(TAG, "Device audio mode: noti = ring = system");
//        } else if (am.getStreamVolume(mVolMember.get(3).getAudioType(3)) == 1) {
//            Log.d(TAG, "Device audio mode: noti = ring");
//        } else if (am.getStreamVolume(mVolMember.get(4).getAudioType(4)) == 1) {
//            Log.d(TAG, "Device audio mode: noti = system");
//        }

        for(int i = 0; i < mVolMember.size(); i++) {
            Log.d(TAG, "before: i = " + am.getStreamVolume(mVolMember.get(i).getAudioType(i)));
            Log.d(TAG, "System volume3 = " + am.getStreamVolume(AudioManager.STREAM_SYSTEM));
            am.setStreamVolume(mVolMember.get(i).getAudioType(i), temp[i], 0);
        }
    }

    public void createMember() {
        music = new VolumeMember();
        alarm = new VolumeMember();
        noti = new VolumeMember();
        ring = new VolumeMember();
        system = new VolumeMember();
        voice = new VolumeMember();

        mVolMember = new ArrayList<>();
        mVolMember.add(music);
        mVolMember.add(alarm);
        mVolMember.add(noti);
        mVolMember.add(ring);
        mVolMember.add(system);
        mVolMember.add(voice);
    }

    public void reloadData() {
        customBtnSumPref = settings.getInt("customBtuSum", 0);
        Log.d(TAG, "reloadData Pref: customBtnSumPref = " + customBtnSumPref);

        customBtnPref1 = settings.getString("customBtn1", "null");
        Log.d(TAG, "reloadData Pref: customBtnPref1 = " + customBtnPref1);
        if(!customBtnPref1.equals("null")) {
            addDynamicButton(customBtnPref1, 0);
        }

        customBtnPref2 = settings.getString("customBtn2", "null");
        Log.d(TAG, "reloadData Pref: customBtnPref2 = " + customBtnPref2);
        if(!customBtnPref2.equals("null")) {
            addDynamicButton(customBtnPref2, 1);
        }

        customBtnPref3 = settings.getString("customBtn3", "null");
        Log.d(TAG, "reloadData Pref: customBtnPref3 = " + customBtnPref3);
        if(!customBtnPref3.equals("null")) {
            addDynamicButton(customBtnPref3, 2);
        }
    }

    public void addDynamicButton(String buttonName, int count) {
        Log.d(TAG, "addDynamicButton(): " + buttonName);
        mLinearCustom = (LinearLayout) findViewById(R.id.linear_custom);
        mButton = new Button(getApplicationContext());
        mButton.setText(buttonName);
        mButton.setId(count);
        mLinearCustom.addView(mButton);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "addDynamicButton onClick(): " + v.getId());
            }

        });

        mButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "addDynamicButton onLongClick(): " + v.getId());
                actionLongClick(v);

//                ViewGroup layout = (ViewGroup) v.getParent();
//                if(null != layout) {
//                    //for safety only  as you are doing onClick
//                    layout.removeView(v);
//                }
                return true;
            }
        });
    }

    public void actionLongClick(final View buttonView) {
        final AboutDialog dialog = new AboutDialog(this, getWindow().getDecorView().getRootView());
        dialog.setTitle("Remove button?");
        dialog.setMessage("Click YES to Remove this custom button");
        dialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup layout = (ViewGroup) buttonView.getParent();
                if(null != layout) {
                    //for safety only  as you are doing onClick
                    layout.removeView(buttonView);
                    customBtnSumPref--;
                    Log.d(TAG, "customBtnSumPref = " + customBtnSumPref);
                    //apply() ? commit() ?
                    settings.edit().putString("customBtn" + buttonView.getId(), "null").apply();
                    settings.edit().putInt("customBtuSum", customBtnSumPref).apply();
                }
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    public void addVolume(int type) {
        mVolMember.get(type).current = mVolumeInfo.getVolume(type);

        if(mVolMember.get(type).current < mVolMember.get(type).max) {
            mVolMember.get(type).current++;
        }
        am.setStreamVolume(mVolMember.get(type).getAudioType(type),
                mVolMember.get(type).current, 0);
        mVolMember.get(type).seekBar.setProgress(mVolMember.get(type).current);
        mVolMember.get(type).textView.setText(mVolMember.get(type).current + "/" +
                                              mVolMember.get(type).max);
    }

    public void reduceVolume(int type) {
        mVolMember.get(type).current = mVolumeInfo.getVolume(type);

        if(mVolMember.get(type).current > 0) {
            mVolMember.get(type).current--;
        }
        am.setStreamVolume(mVolMember.get(type).getAudioType(type),
                mVolMember.get(type).current, 0);
        mVolMember.get(type).seekBar.setProgress(mVolMember.get(type).current);
        mVolMember.get(type).textView.setText(mVolMember.get(type).current + "/" +
                mVolMember.get(type).max);
    }



    public void adjustVolume(View v) {
        switch (v.getId()) {
            case R.id.iv_music_up:
                addVolume(0);
                break;

            case R.id.iv_music_down:
                reduceVolume(0);
                break;

            case R.id.iv_alarm_up:
                addVolume(1);
                break;

            case R.id.iv_alarm_down:
                reduceVolume(1);
                break;

            case R.id.iv_noti_up:
                addVolume(2);
                break;

            case R.id.iv_noti_down:
                reduceVolume(2);
                break;

            case R.id.iv_ring_up:
                addVolume(3);
                break;

            case R.id.iv_ring_down:
                reduceVolume(3);
                break;

            case R.id.iv_sys_up:
                addVolume(4);
                break;

            case R.id.iv_sys_down:
                reduceVolume(4);
                break;

            case R.id.iv_voice_up:
                addVolume(5);
                break;

            case R.id.iv_voice_down:
                reduceVolume(5);
                break;
        }
    }

    public void setListener() {
        outdoor.setOnClickListener(new volumeMode());
        mute.setOnClickListener(new volumeMode());
        custom.setOnClickListener(new volumeMode());

        for (int i = 0; i < mVolMember.size(); i++) {
            mVolMember.get(i).seekBar.setOnSeekBarChangeListener(new seekBarChange());
        }
    }

    public void initSeekBar() {
        for(int i = 0; i < mVolMember.size(); i++) {
            mVolMember.get(i).seekBar.setMax(mVolMember.get(i).max);
            mVolMember.get(i).seekBar.setProgress(mVolMember.get(i).current);
        }
    }

    public void initView() {
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        music.seekBar = (SeekBar) findViewById(R.id.sb_music);
        alarm.seekBar = (SeekBar) findViewById(R.id.sb_alarm);
        noti.seekBar = (SeekBar) findViewById(R.id.sb_noti);
        ring.seekBar = (SeekBar) findViewById(R.id.sb_ring);
        system.seekBar = (SeekBar) findViewById(R.id.sb_system);
        voice.seekBar = (SeekBar) findViewById(R.id.sb_voice);

        music.textView = (TextView) findViewById(R.id.tv_Music);
        alarm.textView = (TextView) findViewById(R.id.tv_Alarm);
        noti.textView = (TextView) findViewById(R.id.tv_Notification);
        ring.textView = (TextView) findViewById(R.id.tv_Ring);
        system.textView = (TextView) findViewById(R.id.tv_System);
        voice.textView = (TextView) findViewById(R.id.tv_Voice);

        outdoor = (Button) findViewById(R.id.btn_outdoor);
        mute = (Button) findViewById(R.id.btn_mute);
        custom = (Button) findViewById(R.id.btn_custom);

        mContext = this.getApplicationContext();
        mVolumeInfo = new VolumeInfo(mContext);

        settings = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void getVolumeInfo() {
        //musicMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        for (int i = 0; i < mVolMember.size(); i++) {
            mVolMember.get(i).max =  am.getStreamMaxVolume(mVolMember.get(i).getAudioType(i));
            mVolMember.get(i).current =  am.getStreamVolume(mVolMember.get(i).getAudioType(i));
            mVolMember.get(i).textView
                             .setText(mVolMember.get(i).current + "/" + mVolMember.get(i).max);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                actionSettings();
                return true;

            case R.id.action_schedule:
                actionSchedule();
                return true;

            case R.id.action_about:
                actionAbout();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void actionSettings() {

    }

    public void actionAbout() {
        final AboutDialog dialog = new AboutDialog(this, getWindow().getDecorView().getRootView());
        dialog.setTitle("About:");
        dialog.setMessage("This is test!");
        dialog.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void actionSchedule() {
//                LayoutInflater mLayoutinflater = (LayoutInflater)
//                                                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View contentview = mLayoutinflater.inflate(R.layout.popup_schedule, null);
//                PopupWindow popupWindow = new PopupWindow(contentview,
//                                                          ViewGroup.LayoutParams.WRAP_CONTENT,
//                                                          ViewGroup.LayoutParams.WRAP_CONTENT);
//                popupWindow.setFocusable(true);
//                popupWindow.showAtLocation(findViewById(R.id.textView), Gravity.BOTTOM, 0, 0);
//                popupWindow.setBackgroundDrawable(new PaintDrawable());
//                popupWindow.update();

        readSelectedData();
        final ScheduleDialog mSD = new ScheduleDialog(this,
                getWindow().getDecorView().getRootView());
        mSD.setTitle("Schedule:");
        mSD.setSpinnerInit();

        mSD.setSpinnerItem(startModeItemPref, stopModeItemPref,
                startTimeItemPref, stopTimeItemPref);
        mSD.setSpinnerListener();

        mSD.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSD.dismiss();
            }
        });
        mSD.setNegativeButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSD.dismiss();
            }
        });
        mSD.show();
    }

    public void readSelectedData() {
        Log.d(TAG, "readSelectedData");

        startModeItemPref = Integer.parseInt(settings.getString("startModeItem", "0"));
        Log.d(TAG, "Pref1: startModeItemPref = " + startModeItemPref);

        stopModeItemPref = Integer.parseInt(settings.getString("stopModeItem", "0"));
        Log.d(TAG, "Pef2: stopModeItemPref = " + stopModeItemPref);

        startTimeItemPref = Integer.parseInt(settings.getString("startTimeItem", "0"));
        Log.d(TAG, "Pref3: startTimeItemPref = " + startTimeItemPref);

        stopTimeItemPref = Integer.parseInt(settings.getString("stopTimeItem", "0"));
        Log.d(TAG, "Pref4: stopTimeModePref = " + stopTimeItemPref);
    }

    public void saveVolumeMode(String modeName) {
        for(int i = 0; i < mVolMember.size(); i++) {

            int getVolume = am.getStreamVolume(mVolMember.get(i).getAudioType(i));
            Log.d(TAG, "saveVolumeMode - i = " + getVolume);
            //record all type volume with modeName
            settings.edit()
                    .putInt("[" + modeName + "]" + i, getVolume) //avoid the same mode name
                    .apply();

        }
    }


    private class volumeMode implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn_outdoor:
                    Log.d(TAG, "volumeMode onclick - btn_outdoor");
                    for(int i = 2; i < 5; i++) {
                        am.setStreamVolume(mVolMember.get(i).getAudioType(i),
                                           mVolMember.get(i).max, 0);
                        mVolMember.get(i).seekBar.setProgress(mVolMember.get(i).max);
                        mVolMember.get(i).textView.setText(mVolumeInfo.getVolume(i) + "/" +
                                                           mVolMember.get(i).max);
                    }

                    break;

                case R.id.btn_mute:
                    Log.d(TAG, "volumeMode onclick - btn_mute");
                    for(int i = 2; i < 5; i++) {
                        am.setStreamVolume(mVolMember.get(i).getAudioType(i), 0, 0);
                        mVolMember.get(i).seekBar.setProgress(0);
                        mVolMember.get(i).textView.setText(0 + "/" + mVolMember.get(i).max);
                    }
                    break;

                case R.id.btn_custom:
                    Log.d(TAG, "volumeMode onclick - btn_custom");
                    final CustomDialog dialog = new CustomDialog(getApplicationContext(),
                            getWindow().getDecorView().getRootView());
                    dialog.setTitle("Customer:");
                    //dialog.setMessage("This is test!");
                    dialog.setPositiveButton("Yes", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String customEditName = dialog.getEditTextName();
                            Log.d(TAG, "customEditName = " + customEditName);
                            Log.d(TAG, "customEditName = " + (customEditName.length()));
                            if(customEditName.equals("")) {
                                Log.d(TAG, "invalid name");
                                Toast.makeText(getApplicationContext(),
                                        "Please input valid name!", Toast.LENGTH_LONG).show();

                            } else if(customEditName.length() > 10) {
                                Toast.makeText(getApplicationContext(),
                                        "Please shorten your name!", Toast.LENGTH_LONG).show();

                            } else if(customBtnSumPref >= 3) {
                                Toast.makeText(getApplicationContext(),
                                        "Only three custom button.", Toast.LENGTH_LONG).show();

                            // smae name check!

                            } else {
                                addDynamicButton(customEditName, customBtnSumPref);

                                customBtnSumPref++;
                                Log.d(TAG, "customBtnSumPref = " + customBtnSumPref);
                                //apply() ? commit() ?
                                settings.edit()
                                        .putString("customBtn" + customBtnSumPref, customEditName)
                                        .apply();

                                settings.edit()
                                        .putInt("customBtuSum", customBtnSumPref)
                                        .apply();

                                saveVolumeMode(customEditName);
                            }

                            dialog.dismiss();

                        }
                    });

                    dialog.setNegativeButton("No", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    break;
            }
        }
    }

    private class seekBarChange implements SeekBar.OnSeekBarChangeListener {
        int progressValue = 0;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            progressValue = progress;
            Log.d(TAG, "onProgressChanged: " + progress);
            switch(seekBar.getId()) {
                case R.id.sb_music:
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    break;
                case R.id.sb_alarm:
                    am.setStreamVolume(AudioManager.STREAM_ALARM, progress, 0);
                    break;
                case R.id.sb_noti:
                    am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, progress, 0);
                    break;
                case R.id.sb_ring:
                    am.setStreamVolume(AudioManager.STREAM_RING, progress, 0);
                    break;
                case R.id.sb_system:
                    am.setStreamVolume(AudioManager.STREAM_SYSTEM, progress, 0);
                    break;
                case R.id.sb_voice:
                    am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, progress, 0);
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            switch(seekBar.getId()) {
                case R.id.sb_music:
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, progressValue, 0);
                    mVolMember.get(0).textView.setText(progressValue + "/" + mVolMember.get(0).max);
                    break;
                case R.id.sb_alarm:
                    am.setStreamVolume(AudioManager.STREAM_ALARM, progressValue, 0);
                    mVolMember.get(1).textView.setText(progressValue + "/" + mVolMember.get(1).max);
                    break;
                case R.id.sb_noti:
                    am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, progressValue, 0);
                    mVolMember.get(2).textView.setText(progressValue + "/" + mVolMember.get(2).max);
                    break;
                case R.id.sb_ring:
                    am.setStreamVolume(AudioManager.STREAM_RING, progressValue, 0);
                    mVolMember.get(3).textView.setText(progressValue + "/" + mVolMember.get(3).max);
                    break;
                case R.id.sb_system:
                    am.setStreamVolume(AudioManager.STREAM_SYSTEM, progressValue, 0);
                    mVolMember.get(4).textView.setText(progressValue + "/" + mVolMember.get(4).max);
                    break;
                case R.id.sb_voice:
                    am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, progressValue, 0);
                    mVolMember.get(5).textView.setText(progressValue + "/" + mVolMember.get(5).max);
                    break;

                default:
                    break;
            }
        }
    }
}
