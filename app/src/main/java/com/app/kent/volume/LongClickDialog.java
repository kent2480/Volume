package com.app.kent.volume;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by Kent_Zheng on 2015/3/11.
 */
public class LongClickDialog {
    private View mParent;
    private PopupWindow mPopupWindow;
    private LinearLayout mRootLayout;
    private ViewGroup.LayoutParams mLayoutParams;

    LongClickDialog(Context context, View parent) {
        mParent = parent;
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootLayout = (LinearLayout)mInflater.inflate(R.layout.longclick_dialog, null);
        mLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setTitle(String title) {
        TextView mTitle = (TextView)mRootLayout.findViewById(R.id.LongClickDlgTitle);
        mTitle.setText(title);
    }

    public void setMessage(String message) {
        TextView mMessage = (TextView)mRootLayout.findViewById(R.id.LongClickDlgContentText);
        mMessage.setText(message);
    }

    public void setPositiveButton(String text,View.OnClickListener listener ) {
        final Button buttonOK = (Button)mRootLayout.findViewById(R.id.LongClickDlgButtonOK);
        buttonOK.setText(text);
        buttonOK.setOnClickListener(listener);
        buttonOK.setVisibility(View.VISIBLE);
    }

    public void setNegativeButton(String text,View.OnClickListener listener ) {
        final Button buttonCancel = (Button)mRootLayout.findViewById(R.id.LongClickDlgButtonCancel);
        buttonCancel.setText(text);
        buttonCancel.setOnClickListener(listener);
        buttonCancel.setVisibility(View.VISIBLE);
    }

    public void setContentLayout(View layout) {
        TextView mMessage = (TextView)mRootLayout.findViewById(R.id.LongClickDlgContentText);
        mMessage.setVisibility(View.GONE);
        LinearLayout contentLayout = (LinearLayout)mRootLayout.findViewById(R.id.LongClickDlgContentView);
        contentLayout.addView(layout);
    }

    public void setLayoutParams(int width, int height) {
        mLayoutParams.width  = width;
        mLayoutParams.height = height;
    }

    public void show() {
        if(mPopupWindow == null) {
            mPopupWindow = new PopupWindow(mRootLayout, mLayoutParams.width,mLayoutParams.height);
            mPopupWindow.setFocusable(true);
        }
        mPopupWindow.showAtLocation(mParent, Gravity.CENTER, Gravity.CENTER, Gravity.CENTER);
    }

    public void dismiss() {
        if(mPopupWindow == null) {
            return;
        }
        mPopupWindow.dismiss();
    }
}