package com.app.kent.volume;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.Voice;
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

    private SeekBar sbMusic, sbAlarm, sbNotification, sbRing, sbSystem, sbVoice;
    private int musicMax, alarmMax, notificationMax, ringMax, systemMax, voiceMax;
    private int musicVolume, alarmVolume, notificationVolumn, ringVolume, systemVolume, voiceVolume;
    private TextView tvMusic, tvAlarm, tvNotification, tvRing, tvSystem, tvVoice;

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

        initView();
        createMember();
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

    public void adjustVolume(View v) {
        switch (v.getId()) {
            case R.id.iv_music_up:
                addVolume(VolumeType.MUSIC);

                musicVolume = mVolumeInfo.getVolume(0);
                if(musicVolume < musicMax) {
                    musicVolume++;
                }
                am.setStreamVolume(AudioManager.STREAM_MUSIC, musicVolume, 0);
                sbMusic.setProgress(musicVolume);
                tvMusic.setText(musicVolume + "/" + musicMax);
                break;

            case R.id.iv_music_down:
                musicVolume = mVolumeInfo.getVolume(0);
                if(musicVolume > 0) {
                    musicVolume--;
                }
                am.setStreamVolume(AudioManager.STREAM_MUSIC, musicVolume, 0);
                sbMusic.setProgress(musicVolume);
                tvMusic.setText(musicVolume + "/" + musicMax);
                break;

            case R.id.iv_alarm_up:
                alarmVolume = mVolumeInfo.getVolume(1);
                if(alarmVolume < alarmMax) {
                    alarmVolume++;
                }
                am.setStreamVolume(AudioManager.STREAM_ALARM, alarmVolume, 0);
                sbAlarm.setProgress(alarmVolume);
                tvAlarm.setText(alarmVolume + "/" + alarmMax);
                break;

            case R.id.iv_alarm_down:
                alarmVolume = mVolumeInfo.getVolume(1);
                if(alarmVolume > 0) {
                    alarmVolume--;
                }
                am.setStreamVolume(AudioManager.STREAM_ALARM, alarmVolume, 0);
                sbAlarm.setProgress(alarmVolume);
                tvAlarm.setText(alarmVolume + "/" + alarmMax);
                break;

            case R.id.iv_noti_up:
                notificationVolumn = mVolumeInfo.getVolume(2);
                if(notificationVolumn < notificationMax) {
                    notificationVolumn++;
                }
                am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, notificationVolumn, 0);
                sbNotification.setProgress(notificationVolumn);
                tvNotification.setText(notificationVolumn + "/" + notificationMax);
                break;

            case R.id.iv_noti_down:
                notificationVolumn = mVolumeInfo.getVolume(2);
                if(notificationVolumn > 0) {
                    notificationVolumn--;
                }
                am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, notificationVolumn, 0);
                sbNotification.setProgress(notificationVolumn);
                tvNotification.setText(notificationVolumn + "/" + notificationMax);
                break;

            case R.id.iv_ring_up:
                ringVolume = mVolumeInfo.getVolume(3);
                if(ringVolume < ringMax) {
                    ringVolume++;
                }
                am.setStreamVolume(AudioManager.STREAM_RING, ringVolume, 0);
                sbRing.setProgress(ringVolume);
                tvRing.setText(ringVolume + "/" + ringMax);
                break;

            case R.id.iv_ring_down:
                ringVolume = mVolumeInfo.getVolume(3);
                if(ringVolume > 0) {
                    ringVolume--;
                }
                am.setStreamVolume(AudioManager.STREAM_RING, ringVolume, 0);
                sbRing.setProgress(ringVolume);
                tvRing.setText(ringVolume + "/" + ringMax);
                break;

            case R.id.iv_sys_up:
                systemVolume = mVolumeInfo.getVolume(4);
                if(systemVolume < systemMax) {
                    systemVolume++;
                }
                am.setStreamVolume(AudioManager.STREAM_SYSTEM, systemVolume, 0);
                sbSystem.setProgress(systemVolume);
                tvSystem.setText(systemVolume + "/" + systemMax);
                break;

            case R.id.iv_sys_down:
                systemVolume = mVolumeInfo.getVolume(4);
                if(systemVolume > 0) {
                    systemVolume--;
                }
                am.setStreamVolume(AudioManager.STREAM_SYSTEM, systemVolume, 0);
                sbSystem.setProgress(systemVolume);
                tvSystem.setText(systemVolume + "/" + systemMax);
                break;

            case R.id.iv_voice_up:
                voiceVolume = mVolumeInfo.getVolume(5);
                if(voiceVolume < voiceMax) {
                    voiceVolume++;
                }
                am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, voiceVolume, 0);
                sbVoice.setProgress(voiceVolume);
                tvVoice.setText(voiceVolume + "/" + voiceMax);
                break;

            case R.id.iv_voice_down:
                voiceVolume = mVolumeInfo.getVolume(5);
                if(voiceVolume > 0) {
                    voiceVolume--;
                }
                am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, voiceVolume, 0);
                sbVoice.setProgress(voiceVolume);
                tvVoice.setText(voiceVolume + "/" + voiceMax);
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
//        sbMusic.setOnSeekBarChangeListener(new seekBarChange());
//        sbAlarm.setOnSeekBarChangeListener(new seekBarChange());
//        sbNotification.setOnSeekBarChangeListener(new seekBarChange());
//        sbRing.setOnSeekBarChangeListener(new seekBarChange());
//        sbSystem.setOnSeekBarChangeListener(new seekBarChange());
//        sbVoice.setOnSeekBarChangeListener(new seekBarChange());

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
        sbVoice.setMax(voiceMax);
        sbVoice.setProgress(voiceVolume);
    }

    public void initView() {
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //sbMusic = (SeekBar) findViewById(R.id.sb_music);
        music.seekBar = (SeekBar) findViewById(R.id.sb_music);

        //sbAlarm = (SeekBar) findViewById(R.id.sb_alarm);
        alarm.seekBar = (SeekBar) findViewById(R.id.sb_alarm);

        //sbNotification = (SeekBar) findViewById(R.id.sb_noti);
        noti.seekBar = (SeekBar) findViewById(R.id.sb_noti);

        //sbRing = (SeekBar) findViewById(R.id.sb_ring);
        ring.seekBar = (SeekBar) findViewById(R.id.sb_ring);

        //sbSystem = (SeekBar) findViewById(R.id.sb_system);
        system.seekBar = (SeekBar) findViewById(R.id.sb_system);

        //sbVoice = (SeekBar) findViewById(R.id.sb_voice);
        voice.seekBar = (SeekBar) findViewById(R.id.sb_voice);

        //tvMusic = (TextView) findViewById(R.id.tv_Music);
        music.textView = (TextView) findViewById(R.id.tv_Music);

        //tvAlarm = (TextView) findViewById(R.id.tv_Alarm);
        alarm.textView = (TextView) findViewById(R.id.tv_Alarm);

        //tvNotification = (TextView) findViewById(R.id.tv_Notification);
        noti.textView = (TextView) findViewById(R.id.tv_Notification);

        //tvRing = (TextView) findViewById(R.id.tv_Ring);
        ring.textView = (TextView) findViewById(R.id.tv_Ring);

        //tvSystem = (TextView) findViewById(R.id.tv_System);
        system.textView = (TextView) findViewById(R.id.tv_System);

        //tvVoice = (TextView) findViewById(R.id.tv_Voice);
        voice.textView = (TextView) findViewById(R.id.tv_Voice);

        outdoor = (Button) findViewById(R.id.btn_outdoor);
        mute = (Button) findViewById(R.id.btn_mute);
        custom = (Button) findViewById(R.id.btn_custom);

        mContext = this.getApplicationContext();
        mVolumeInfo = new VolumeInfo(mContext);

        settings = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void getVolumeInfo() {
        musicMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if(Debug) Log.d(TAG, "musicMax = " + musicMax);
        musicVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        if(Debug) Log.d(TAG, "musicVolume = " + musicVolume);
        tvMusic.setText(musicVolume + "/" + musicMax);

        alarmMax = am.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        if(Debug) Log.d(TAG, "alarmMax = " + alarmMax);
        alarmVolume = am.getStreamVolume(AudioManager.STREAM_ALARM);
        if(Debug) Log.d(TAG, "alarmVolume = " + alarmVolume);
        tvAlarm.setText(alarmVolume + "/" + alarmMax);

        notificationMax = am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
        if(Debug) Log.d(TAG, "notificationMax = " + notificationMax);
        notificationVolumn = am.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        if(Debug) Log.d(TAG, "notificationVolumn = " + notificationVolumn);
        tvNotification.setText(notificationVolumn + "/" + notificationMax);

        ringMax = am.getStreamMaxVolume(AudioManager.STREAM_RING);
        if(Debug) Log.d(TAG, "ringMax = " + ringMax);
        ringVolume = am.getStreamVolume(AudioManager.STREAM_RING);
        if(Debug) Log.d(TAG, "ringVolume = " + ringVolume);
        tvRing.setText(ringVolume + "/" + ringMax);

        systemMax = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        if(Debug) Log.d(TAG, "systemMax = " + systemMax);
        systemVolume = am.getStreamVolume(AudioManager.STREAM_SYSTEM);
        if(Debug) Log.d(TAG, "systemVolume = " + systemVolume);
        tvSystem.setText(systemVolume + "/" + systemMax);

        voiceMax = am.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        if(Debug) Log.d(TAG, "voiceMax = " + voiceMax);
        voiceVolume = am.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        if(Debug) Log.d(TAG, "voiceVolume = " + voiceVolume);
        tvVoice.setText(voiceVolume + "/" + voiceMax);
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
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

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
//                LayoutInflater mLayoutinflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View contentview = mLayoutinflater.inflate(R.layout.popup_schedule, null);
//                PopupWindow popupWindow = new PopupWindow(contentview,
//                                                          ViewGroup.LayoutParams.WRAP_CONTENT,
//                                                          ViewGroup.LayoutParams.WRAP_CONTENT);
//                popupWindow.setFocusable(true);
//                popupWindow.showAtLocation(findViewById(R.id.textView), Gravity.BOTTOM, 0, 0);
//                popupWindow.setBackgroundDrawable(new PaintDrawable());
//                popupWindow.update();

        readSelectedData();
        final ScheduleDialog mSD = new ScheduleDialog(this, getWindow().getDecorView().getRootView());
        mSD.setTitle("Schedule:");
        mSD.setSpinnerInit();

        mSD.setSpinnerItem(startModeItemPref, stopModeItemPref, startTimeItemPref, stopTimeItemPref);
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

    public void addVolume(VolumeType type) {
        int currentVolume = mVolumeInfo.getVolume(type);


    }
    private class volumeMode implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn_outdoor:
                    Log.d(TAG, "volumeMode onclick - btn_outdoor");
                    am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, notificationMax, 0);
                    am.setStreamVolume(AudioManager.STREAM_RING, ringMax, 0);
                    am.setStreamVolume(AudioManager.STREAM_SYSTEM, systemMax, 0);

                    //mVolumeInfo.getCurrentVolume();
                    sbNotification.setProgress(notificationMax);
                    tvNotification.setText(mVolumeInfo.getVolume(2) + "/" + notificationMax);
                    sbRing.setProgress(ringMax);
                    tvRing.setText(mVolumeInfo.getVolume(3) + "/" + ringMax);
                    sbSystem.setProgress(systemMax);
                    tvSystem.setText(mVolumeInfo.getVolume(4) + "/" + systemMax);
                    break;

                case R.id.btn_mute:
                    Log.d(TAG, "volumeMode onclick - btn_mute");
                    am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0);
                    am.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
                    am.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0);

                    //mVolumeInfo.getCurrentVolume();
                    sbNotification.setProgress(0);
                    tvNotification.setText(0 + "/" + notificationMax);
                    sbRing.setProgress(0);
                    tvRing.setText(0 + "/" + ringMax);
                    sbSystem.setProgress(0);
                    tvSystem.setText(0 + "/" + systemMax);
                    break;

                case R.id.btn_custom:
                    Log.d(TAG, "volumeMode onclick - btn_custom");
                    final CustomDialog dialog = new CustomDialog(getApplicationContext(), getWindow().getDecorView().getRootView());
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
                                Toast.makeText(getApplicationContext(), "Please input valid name!", Toast.LENGTH_LONG).show();

                            } else if(customBtnSumPref >= 3) {
                                Toast.makeText(getApplicationContext(), "Only three custom button.", Toast.LENGTH_LONG).show();
                            } else {
                                addDynamicButton(customEditName, customBtnSumPref);

                                customBtnSumPref++;
                                Log.d(TAG, "customBtnSumPref = " + customBtnSumPref);
                                //apply() ? commit() ?
                                settings.edit().putString("customBtn" + customBtnSumPref, customEditName).apply();
                                settings.edit().putInt("customBtuSum", customBtnSumPref).apply();
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
//            Log.d(TAG, "seekBar.getId() = " + seekBar.getId());
//            Log.d(TAG, "musdic seekbar id = " + R.id.sb_music);
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
                    tvMusic.setText(progressValue + "/" + musicMax);
                    break;
                case R.id.sb_alarm:
                    tvAlarm.setText(progressValue + "/" + notificationMax);
                    break;
                case R.id.sb_noti:
                    tvNotification.setText(progressValue + "/" + notificationMax);
                    break;
                case R.id.sb_ring:
                    tvRing.setText(progressValue + "/" + ringMax);
                    break;
                case R.id.sb_system:
                    tvSystem.setText(progressValue + "/" + systemMax);
                    break;
                case R.id.sb_voice:
                    tvVoice.setText(progressValue + "/" + voiceMax);
                    break;

                default:
                    break;
            }
        }
    }


}
