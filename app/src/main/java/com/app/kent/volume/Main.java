package com.app.kent.volume;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
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

import com.google.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class Main extends ActionBarActivity {
    private final static String TAG = "VolumeMain";
    private final static boolean Debug = false;
    private AudioManager am;
    private Button outdoor, mute, exit;
    private AdView adView;
    private long MY_AD_UNIT_ID = 7642106680902262L; //pub-7642106680902262
//    private int startModeItemPref, stopModeItemPref, startTimeItemPref, stopTimeItemPref;

    private int customBtnSumPref = 0;
    private SharedPreferences settings;
    private VolumeMember music, alarm, noti, ring, system, voice;
    private ArrayList<VolumeMember> mVolMember;
    private int deviceMode = 0;

    // https://www.iconfinder.com/iconsets/slim-square-icons-basics
    //private ImageView musicUp, musicDown; // using imageView onClick properties

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Debug) {
            Log.d(TAG, "onCreate");
        }

        setContentView(R.layout.activity_main);
        createMember();
        initView();

        getVolumeInfo();
        initSeekBar();
        setListener();

//        Log.d(TAG, "start activity1");
//        Intent mIntent = new Intent();
//        mIntent.setClass(Main.this, Welcome.class);
//        startActivity(mIntent);

        reloadData();
        scanDevice();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getVolumeInfo();
        initSeekBar();

        if (Debug) {
            Log.d(TAG, "onResume");
        }

    }

    public void createMember() {
        music = new VolumeMember(this);
        alarm = new VolumeMember(this);
        noti = new VolumeMember(this);
        ring = new VolumeMember(this);
        system = new VolumeMember(this);
        voice = new VolumeMember(this);

        mVolMember = new ArrayList<>();
        mVolMember.add(music);
        mVolMember.add(alarm);
        mVolMember.add(noti);
        mVolMember.add(ring);
        mVolMember.add(system);
        mVolMember.add(voice);

        adView = new AdView(this, AdSize.BANNER, MY_AD_UNIT_ID);

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
        exit = (Button) findViewById(R.id.btn_exit);

        settings = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void getVolumeInfo() {
        for (int i = 0; i < mVolMember.size(); i++) {
            mVolMember.get(i).textView
                    .setText(mVolMember.get(i).getCurrent(i) + "/" + mVolMember.get(i).getMax(i));
        }
    }

    public void initSeekBar() {
        for(int i = 0; i < mVolMember.size(); i++) {
            mVolMember.get(i).seekBar.setMax(mVolMember.get(i).max);
            mVolMember.get(i).seekBar.setProgress(mVolMember.get(i).getCurrent(i));
        }
    }

    public void setListener() {
        outdoor.setOnClickListener(new volumeMode());
        mute.setOnClickListener(new volumeMode());
        exit.setOnClickListener(new volumeMode());

        for (int i = 0; i < mVolMember.size(); i++) {
            mVolMember.get(i).seekBar.setOnSeekBarChangeListener(new seekBarChange());
        }
    }

    public void reloadData() {
        customBtnSumPref = settings.getInt("customBtuSum", 0);

        String customBtnPref1 = settings.getString("customBtn1", "null");
        if(!customBtnPref1.equals("null")) {
            addDynamicButton(customBtnPref1, 1);
        }

        String customBtnPref2 = settings.getString("customBtn2", "null");
        if(!customBtnPref2.equals("null")) {
            addDynamicButton(customBtnPref2, 2);
        }

        String customBtnPref3 = settings.getString("customBtn3", "null");
        if(!customBtnPref3.equals("null")) {
            addDynamicButton(customBtnPref3, 3);
        }

        if(Debug) {
            Log.d(TAG, "reloadData Pref: customBtnSumPref = " + customBtnSumPref +
                    ", customBtnPref1 = " + customBtnPref1 +
                    ", customBtnPref2 = " + customBtnPref2 +
                    ", customBtnPref3 = " + customBtnPref3);
        }
    }

    public void scanDevice() {
        int[] temp = new int[6]; //0 ~ 5
        for(int i = 0; i < mVolMember.size(); i++) {
            temp[i] = am.getStreamVolume(mVolMember.get(i).getAudioType(i));
        }

        for(int i = 0; i < mVolMember.size(); i++) {
            am.setStreamVolume(mVolMember.get(i).getAudioType(i), 5, 0);
        }

        am.setStreamVolume(mVolMember.get(2).getAudioType(2), 1, 0);

        if(am.getStreamVolume(mVolMember.get(3).getAudioType(3)) == 1 &&
                am.getStreamVolume(mVolMember.get(4).getAudioType(4)) == 1) {
            deviceMode = 1;

        } else if (am.getStreamVolume(mVolMember.get(3).getAudioType(3)) == 1) {
            deviceMode = 2;

        } else if (am.getStreamVolume(mVolMember.get(4).getAudioType(4)) == 1) {
            deviceMode = 3;
        }

        if(Debug) {
            Log.d(TAG, "scanDevice: deviceMode = " + deviceMode);
        }

        for(int i = 0; i < mVolMember.size(); i++) {
            am.setStreamVolume(mVolMember.get(i).getAudioType(i), temp[i], 0);
        }
    }

    public void addDynamicButton(final String buttonName, int count) {
        if(Debug) {
            Log.d(TAG, "addDynamicButton(): " + buttonName);
        }

        LinearLayout mLinearCustom = (LinearLayout) findViewById(R.id.linear_custom);

        View v = getLayoutInflater().inflate(R.layout.custom_view, null);
        Button mButton = (Button) v.findViewById(R.id.btn_custom);
        mButton.setText(buttonName);
        mButton.setId(count);

        mLinearCustom.addView(v);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, "addDynamicButton onClick(): " + v.getId() + "name = " + buttonName);
                customSetVolume(buttonName);
            }

        });

        mButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Log.d(TAG, "addDynamicButton onLongClick(): " + v.getId());
                actionLongClick(v, buttonName);

//                ViewGroup layout = (ViewGroup) v.getParent();
//                if(null != layout) {
//                    //for safety only  as you are doing onClick
//                    layout.removeView(v);
//                }
                return true;
            }
        });
    }

    public void customSetVolume(String name) {
        //int name="[a1]0" value="1"
        for(int i = 0; i < 6; i++) {
            mVolMember.get(i).current = settings.getInt("[" + name + "]" + i, 0);
            Log.d(TAG, "customSetVolume: name" + i + " = " + mVolMember.get(i).current);
            mVolMember.get(i).seekBar.setProgress(mVolMember.get(i).current);
            mVolMember.get(i).textView.setText(mVolMember.get(i).current + "/" +
                                               mVolMember.get(i).max);
        }
    }

    public void actionLongClick(final View buttonView, final String name) {
        final LongClickDialog dialog = new LongClickDialog(this, getWindow().getDecorView().getRootView());
        dialog.setTitle(getString(R.string.dlg_long_title));
        dialog.setMessage(getString(R.string.dlg_long_text));

        dialog.setPositiveButton(getString(R.string.dlg_ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup layout = (ViewGroup) buttonView.getParent();
                ViewGroup master = (ViewGroup)  layout.getParent();
                if(null != layout && null != master) {
                    for(int i = 0; i < 6; i++) {
                        settings.edit().remove("[" + name + "]" + i).apply();
//                    for safety only  as you are doing onClick
//                    layout.removeView(buttonView.get);
                        master.removeView(layout);
                    }

                    customBtnSumPref--;
                    if(Debug) {
                        Log.d(TAG, "customBtnSumPref = " + customBtnSumPref);
                        Log.d(TAG, "Setting null id = " + buttonView.getId());
                    }
                    settings.edit().putString("customBtn" + buttonView.getId(), "null").apply();
                    settings.edit().putInt("customBtuSum", customBtnSumPref).apply();


                }
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton(getString(R.string.dlg_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void addVolume(int type) {
        if(mVolMember.get(3).getCurrent(3) == 0 && (type == 2 || type == 4)) {
            Toast.makeText(getApplicationContext(), getString(R.string.vibrste_warnning),
                           Toast.LENGTH_SHORT).show();
            return;
        }

        if(mVolMember.get(type).getCurrent(type) < mVolMember.get(type).max) {
            mVolMember.get(type).current++;
        }
        am.setStreamVolume(mVolMember.get(type).getAudioType(type),
                mVolMember.get(type).current, 0);

        mVolMember.get(type).seekBar.setProgress(mVolMember.get(type).getCurrent(type));
        mVolMember.get(type).textView.setText(mVolMember.get(type).getCurrent(type) + "/" +
                mVolMember.get(type).max);

        checkDeviceMode(type);
    }

    public void reduceVolume(int type) {

        if(mVolMember.get(type).getCurrent(type) > 0) {
            mVolMember.get(type).current--;
        }
        am.setStreamVolume(mVolMember.get(type).getAudioType(type), mVolMember.get(type).current, 0);
        mVolMember.get(type).seekBar.setProgress(mVolMember.get(type).current);
        mVolMember.get(type).textView.setText(mVolMember.get(type).current + "/" +
                mVolMember.get(type).max);

        checkDeviceMode(type);

        // when ring = 0, noti and system will be set to 0 (vibrate mode)
        if(type == 3 && mVolMember.get(type).getCurrent(type) == 0) {
            mVolMember.get(2).seekBar.setProgress(mVolMember.get(2).getCurrent(2));
            mVolMember.get(4).seekBar.setProgress(mVolMember.get(4).getCurrent(4));

            mVolMember.get(2).textView.setText(mVolMember.get(2).getCurrent(2) + "/" +
                    mVolMember.get(2).max);
            mVolMember.get(4).textView.setText(mVolMember.get(4).getCurrent(4) + "/" +
                                               mVolMember.get(4).max);
        }
    }

    public void checkDeviceMode(int type) {
        if(deviceMode == 1) {
            if(type == 2 || type == 3 || type == 4) {
                mVolMember.get(2).textView.setText(mVolMember.get(2).getCurrent(2) + "/" +
                                                   mVolMember.get(2).max);
                mVolMember.get(3).textView.setText(mVolMember.get(2).getCurrent(3) + "/" +
                                                   mVolMember.get(3).max);
                mVolMember.get(4).textView.setText(mVolMember.get(2).getCurrent(4) + "/" +
                                                   mVolMember.get(4).max);

            }
        } else if (deviceMode == 3) {
            if(type == 2 || type == 4) {
                mVolMember.get(2).textView.setText(mVolMember.get(2).getCurrent(2) + "/" +
                                                   mVolMember.get(2).max);
                mVolMember.get(4).textView.setText(mVolMember.get(2).getCurrent(4) + "/" +
                                                   mVolMember.get(4).max);
            }
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_custom:
                actionCustiom();
                return true;

            case R.id.action_about:
                actionAbout();
                return true;

            case R.id.action_feedback:
                actionfeedback();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void actionCustiom() {
        final CustomDialog mCustomDialog = new CustomDialog(this, getWindow().getDecorView().getRootView());

        mCustomDialog.setTitle(getString(R.string.dlg_custom));
        mCustomDialog.setPositiveButton(getString(R.string.dlg_ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customEditName = mCustomDialog.getEditTextName();
                if(Debug) {
                    Log.d(TAG, "customEditName = " + customEditName);
                    Log.d(TAG, "customEditName = " + (customEditName.length()));
                }

                if(customEditName.equals("")) {
                    if(Debug) {
                        Log.d(TAG, "invalid name");
                    }
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.cust_name_error), Toast.LENGTH_LONG).show();

                } else if(customEditName.length() > 10) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.cust_name_length), Toast.LENGTH_LONG).show();

                } else if(customBtnSumPref >= 3) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.cust_amount), Toast.LENGTH_LONG).show();

                    // smae name check!

                } else {
                    customBtnSumPref++;
                    addDynamicButton(customEditName, customBtnSumPref);
                    if(Debug) {
                        Log.d(TAG, "customBtnSumPref = " + customBtnSumPref);
                    }

                    //apply() ? commit() ?
                    settings.edit()
                            .putString("customBtn" + customBtnSumPref, customEditName)
                            .apply();

                    settings.edit()
                            .putInt("customBtuSum", customBtnSumPref)
                            .apply();

                    saveVolumeMode(customEditName);
                }
                mCustomDialog.dismiss();
            }
        });

        mCustomDialog.setNegativeButton(getString(R.string.dlg_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomDialog.dismiss();
            }
        });
        mCustomDialog.show();
    }


    public void actionAbout() {
        final AboutDialog mAboutDialog = new AboutDialog(this, getWindow().getDecorView().getRootView());
        mAboutDialog.setTitle(getString(R.string.dlg_about));
        mAboutDialog.setMessage(Html.fromHtml(getString(R.string.dlg_about_message)));
        mAboutDialog.setPositiveButton(getString(R.string.dlg_ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAboutDialog.dismiss();
            }
        });

        mAboutDialog.setNegativeButton(getString(R.string.dlg_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAboutDialog.dismiss();
            }
        });
        mAboutDialog.show();
    }

    public void actionfeedback() {
        final FeedbackDialog mFeedbackDialog = new FeedbackDialog(this, getWindow().getDecorView().getRootView());
        mFeedbackDialog.setTitle(getString(R.string.dlg_feedback));
        mFeedbackDialog.setMessage(Html.fromHtml(getString(R.string.dlg_feedback_message)));
        mFeedbackDialog.setPositiveButton(getString(R.string.dlg_ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFeedbackDialog.dismiss();
            }
        });

        mFeedbackDialog.setNegativeButton(getString(R.string.dlg_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFeedbackDialog.dismiss();
            }
        });
        mFeedbackDialog.show();
    }

//    public void actionSchedule() {
////                LayoutInflater mLayoutinflater = (LayoutInflater)
////                                                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////                View contentview = mLayoutinflater.inflate(R.layout.popup_schedule, null);
////                PopupWindow popupWindow = new PopupWindow(contentview,
////                                                          ViewGroup.LayoutParams.WRAP_CONTENT,
////                                                          ViewGroup.LayoutParams.WRAP_CONTENT);
////                popupWindow.setFocusable(true);
////                popupWindow.showAtLocation(findViewById(R.id.textView), Gravity.BOTTOM, 0, 0);
////                popupWindow.setBackgroundDrawable(new PaintDrawable());
////                popupWindow.update();
//
//        readSelectedData();
//        final ScheduleDialog mSD = new ScheduleDialog(this,
//                getWindow().getDecorView().getRootView());
//        mSD.setTitle("Schedule:");
//        mSD.setSpinnerInit();
//
//        mSD.setSpinnerItem(startModeItemPref, stopModeItemPref,
//                startTimeItemPref, stopTimeItemPref);
//        mSD.setSpinnerListener();
//
//        mSD.setPositiveButton("OK", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSD.dismiss();
//            }
//        });
//        mSD.setNegativeButton("Cancel", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSD.dismiss();
//            }
//        });
//        mSD.show();
//    }

//    public void readSelectedData() {
//        Log.d(TAG, "readSelectedData");
//
//        startModeItemPref = Integer.parseInt(settings.getString("startModeItem", "0"));
//        Log.d(TAG, "Pref1: startModeItemPref = " + startModeItemPref);
//
//        stopModeItemPref = Integer.parseInt(settings.getString("stopModeItem", "0"));
//        Log.d(TAG, "Pef2: stopModeItemPref = " + stopModeItemPref);
//
//        startTimeItemPref = Integer.parseInt(settings.getString("startTimeItem", "0"));
//        Log.d(TAG, "Pref3: startTimeItemPref = " + startTimeItemPref);
//
//        stopTimeItemPref = Integer.parseInt(settings.getString("stopTimeItem", "0"));
//        Log.d(TAG, "Pref4: stopTimeModePref = " + stopTimeItemPref);
//    }

    public void saveVolumeMode(String modeName) {
        for(int i = 0; i < mVolMember.size(); i++) {
            //record all type volume with modeName,  avoid the same mode name
            settings.edit()
                    .putInt("[" + modeName + "]" + i, mVolMember.get(i).getCurrent(i))
                    .apply();

        }
    }


    private class volumeMode implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn_outdoor:

                    //set ring first i = 3, avoid noti can not up volume.
                    for(int i = 3; i < 5; i++) {

                        am.setStreamVolume(mVolMember.get(i).getAudioType(i),
                                           mVolMember.get(i).max, 0);
                        mVolMember.get(i).seekBar.setProgress(mVolMember.get(i).max);
                        mVolMember.get(i).textView.setText(mVolMember.get(i).getCurrent(i) + "/" +
                                                           mVolMember.get(i).max);
                    }

                    am.setStreamVolume(mVolMember.get(2).getAudioType(2), mVolMember.get(2).max, 0);
                    mVolMember.get(2).seekBar.setProgress(mVolMember.get(2).max);
                    mVolMember.get(2).textView.setText(mVolMember.get(2).getCurrent(2) + "/" +
                            mVolMember.get(2).max);


                    break;

                case R.id.btn_mute:
                    for(int i = 2; i < 5; i++) {
                        am.setStreamVolume(mVolMember.get(i).getAudioType(i), 0, 0);
                        mVolMember.get(i).seekBar.setProgress(0);
                        mVolMember.get(i).textView.setText(mVolMember.get(i).getCurrent(i) + "/" +
                                                           mVolMember.get(i).max);
                    }
                    break;

                case R.id.btn_exit:
                    finish();
                    break;
            }
        }
    }

    private class seekBarChange implements SeekBar.OnSeekBarChangeListener {
        int progressValue = 0;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            progressValue = progress;
            switch(seekBar.getId()) {
                case R.id.sb_music:
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    break;

                case R.id.sb_alarm:
                    am.setStreamVolume(AudioManager.STREAM_ALARM, progress, 0);
                    break;

                case R.id.sb_noti:
                    am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, progress, 0);
                    if(deviceMode == 1) {
                        ring.seekBar.setProgress(progress);
                        system.seekBar.setProgress(progress);
                    } else if (deviceMode == 3) {
                        system.seekBar.setProgress(progress);
                    }
                    break;

                case R.id.sb_ring:
                    am.setStreamVolume(AudioManager.STREAM_RING, progress, 0);
                    if(deviceMode == 1) {
                        noti.seekBar.setProgress(progress);
                        system.seekBar.setProgress(progress);
                    } else if (deviceMode == 2) {
                        system.seekBar.setProgress(progress);
                    }
                    break;

                case R.id.sb_system:
                    am.setStreamVolume(AudioManager.STREAM_SYSTEM, progress, 0);
                    if(deviceMode == 1) {
                        noti.seekBar.setProgress(progress);
                        ring.seekBar.setProgress(progress);
                    } else if (deviceMode == 3) {
                        noti.seekBar.setProgress(progress);
                    }
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

                    if(deviceMode == 1) {
                        mVolMember.get(3).textView.setText(progressValue + "/" + mVolMember.get(3).max);
                        mVolMember.get(4).textView.setText(progressValue + "/" + mVolMember.get(4).max);
                    } else if (deviceMode == 3) {
                        mVolMember.get(4).textView.setText(progressValue + "/" + mVolMember.get(4).max);
                    }
                    break;

                case R.id.sb_ring:
                    am.setStreamVolume(AudioManager.STREAM_RING, progressValue, 0);
                    mVolMember.get(3).textView.setText(progressValue + "/" + mVolMember.get(3).max);
                    if(deviceMode == 1) {
                        mVolMember.get(2).textView.setText(progressValue + "/" + mVolMember.get(2).max);
                        mVolMember.get(4).textView.setText(progressValue + "/" + mVolMember.get(4).max);
                    } else if(deviceMode == 2) {
                        mVolMember.get(2).textView.setText(progressValue + "/" + mVolMember.get(4).max);
                    }
                    break;

                case R.id.sb_system:
                    am.setStreamVolume(AudioManager.STREAM_SYSTEM, progressValue, 0);
                    mVolMember.get(4).textView.setText(progressValue + "/" + mVolMember.get(4).max);

                    if(deviceMode == 1) {
                        mVolMember.get(2).textView.setText(progressValue + "/" + mVolMember.get(2).max);
                        mVolMember.get(3).textView.setText(progressValue + "/" + mVolMember.get(3).max);
                    } else if (deviceMode == 3) {
                        mVolMember.get(2).textView.setText(progressValue + "/" + mVolMember.get(2).max);
                    }
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
