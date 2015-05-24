package com.app.kent.volume;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by kent on 2015/5/16.
 */
public class Middle extends Service{
    private static final String TAG = "Middle";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        RemoteViews contentViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
        contentViews.setImageViewResource(R.id.imageNo, R.drawable.ic_launcher); //big icon
        contentViews.setTextViewText(R.id.titleNo, getString(R.string.noti_title));

        contentViews.setTextViewText(R.id.textNo, getString(R.string.noti_content));

        Intent intentDown = new Intent(getApplicationContext(), NotificationService.class);
        intentDown.putExtra("mode", "down");
        PendingIntent pendingDownIntent = PendingIntent.getService(getApplicationContext(), 0, intentDown,
                PendingIntent.FLAG_UPDATE_CURRENT);
        contentViews.setOnClickPendingIntent(R.id.noti_down, pendingDownIntent);

        Intent intentUp = new Intent(getApplicationContext(), NotificationService.class);
        intentUp.putExtra("mode", "up");
        PendingIntent pendingUpIntent = PendingIntent.getService(getApplicationContext(), 1, intentUp,
                PendingIntent.FLAG_UPDATE_CURRENT);
        contentViews.setOnClickPendingIntent(R.id.noti_up, pendingUpIntent);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_action_volume) // notification icon
                .setTicker(getString(R.string.noti_ticker)); // notification message
        mBuilder.setAutoCancel(true);
        mBuilder.setContent(contentViews);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(10, mBuilder.build());
        Log.d(TAG, "actionNotification");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
