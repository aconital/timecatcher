package com.cpsc.timecatcher;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.parse.ParseUser;

/**
 * Created by hroshandel on 3/18/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if((boolean) ParseUser.getCurrentUser().get("notification")){
            String id = intent.getStringExtra("id");
            String msg = intent.getStringExtra("msg");



            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("TimeCatcher")
                            .setContentText("Your task '"+msg+"' is going to start soon!");

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());
        }

    }

}
