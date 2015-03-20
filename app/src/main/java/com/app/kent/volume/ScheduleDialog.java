package com.app.kent.volume;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Kent_Zheng on 2015/3/11.
 */
public class ScheduleDialog {
    private static final String TAG = "ScheduleDialog";
    //public static final String TAG = "ScheduleDialog";
    private View mParent;
    private PopupWindow mPopupWindow;
    private LinearLayout mRootLayout;
    private ViewGroup.LayoutParams mLayoutParams;
    private Spinner mStartMode, mStopMode, mStartTime, mStopTime;

    private String[] Mode = {"Outdoor", "Mute", "A", "B", "C"};
    private ArrayAdapter adapterMode;
    private ArrayAdapter adapterTime;
    private int startModeItem, stopModeItem, startTimeItem, stopTimeItem;
    private SharedPreferences settings;

    ScheduleDialog(Context context, View parent) {
        mParent = parent;
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootLayout = (LinearLayout) mInflater.inflate(R.layout.schedule_dialog, null);

        mLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                         ViewGroup.LayoutParams.WRAP_CONTENT);

        adapterMode = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, Mode);
        adapterTime = ArrayAdapter.createFromResource(context, R.array.time,
                android.R.layout.simple_spinner_dropdown_item);

        settings = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public void setTitle(String title) {
        TextView mTitle = (TextView) mRootLayout.findViewById(R.id.ScheduleDlgTitle);
        mTitle.setText(title);
    }

    public void setMessage(String message) {
        TextView mMessage = (TextView) mRootLayout.findViewById(R.id.CustomDlgContentText);
        mMessage.setText(message);
    }

    public void setSpinnerInit() {
        mStartMode = (Spinner) mRootLayout.findViewById(R.id.spinner_start_mode);
        mStartMode.setAdapter(adapterMode);

        mStopMode = (Spinner) mRootLayout.findViewById(R.id.spinner_stop_mode);
        mStopMode.setAdapter(adapterMode);

        mStartTime = (Spinner) mRootLayout.findViewById(R.id.spinner_start_time);
        mStartTime.setAdapter(adapterTime);

        mStopTime = (Spinner) mRootLayout.findViewById(R.id.spinner_stop_time);
        mStopTime.setAdapter(adapterTime);
    }

    public void setSpinnerItem(int item1, int item2, int item3, int item4) {
        mStartMode.setSelection(item1);
        mStopMode.setSelection(item2);
        mStartTime.setSelection(item3);
        mStopTime.setSelection(item4);
    }

    public void setSpinnerListener() {

//        Log.d(TAG, "startModeItem = " + startModeItem);
//        if(startModeItem != 0) {
//            mStartMode.setSelection(startModeItem);
//        }

        mStartMode.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "startModeItem: OnItemSelected = " + position);
                startModeItem = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mStopMode.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "stopModeItem: OnItemSelected = " + position);
                stopModeItem = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mStartTime.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "startTimeItem: OnItemSelected = " + position);
                startTimeItem = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mStopTime.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "stopTimeItem: OnItemSelected = " + position);
                stopTimeItem = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void setPositiveButton(String text, View.OnClickListener listener ) {
        final Button buttonOK = (Button) mRootLayout.findViewById(R.id.CustomDlgButtonOK);
        buttonOK.setText(text);
        buttonOK.setOnClickListener(listener);
        buttonOK.setVisibility(View.VISIBLE);
    }

    public void setNegativeButton(String text, View.OnClickListener listener ) {
        final Button buttonCancel = (Button) mRootLayout.findViewById(R.id.CustomDlgButtonCancel);
        buttonCancel.setText(text);
        buttonCancel.setOnClickListener(listener);
        buttonCancel.setVisibility(View.VISIBLE);
    }

    public void setContentLayout(View layout) {
        TextView mMessage = (TextView) mRootLayout.findViewById(R.id.CustomDlgContentText);
        mMessage.setVisibility(View.GONE);
        LinearLayout contentLayout = (LinearLayout) mRootLayout.findViewById(R.id.CustomDlgContentView);
        contentLayout.addView(layout);
    }

    public void setLayoutParams(int width, int height) {
        mLayoutParams.width  = width;
        mLayoutParams.height = height;
    }

    public void show() {
        if(mPopupWindow == null) {
            mPopupWindow = new PopupWindow(mRootLayout, mLayoutParams.width, mLayoutParams.height);
            mPopupWindow.setFocusable(true);
        }
        mPopupWindow.showAtLocation(mParent, Gravity.CENTER, Gravity.CENTER, Gravity.CENTER);
    }

    public void dismiss() {
        Log.d(TAG,"dismiss()");
        saveSelectedData();

        if(mPopupWindow == null) {
            return;
        }

        mPopupWindow.dismiss();
    }

    public void saveSelectedData() {
        Log.d(TAG, "saveSelectedDate");
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("startModeItem", String.valueOf(startModeItem));
        editor.putString("stopModeItem", String.valueOf(stopModeItem));
        editor.putString("startTimeItem", String.valueOf(startTimeItem));
        editor.putString("stopTimeItem", String.valueOf(stopTimeItem));

//        editor.putInt("stopModeItem", stopModeItem);
//        editor.putInt("startTimeItem", startTimeItem);
//        editor.putInt("stopTimeItem", stopTimeItem);
        editor.commit();
    }
}