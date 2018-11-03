package com.study.zhengxq27.healthfoodlist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //Toast.makeText(context, "尝试广播的用法", Toast.LENGTH_LONG).show();
        if (intent.getAction().equals("OpenAppNotification"))
        {
            Bundle Recommand_bundle = intent.getExtras();
            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.empty_star)
                    .setContentTitle("今日推荐")
                    .setContentText(Recommand_bundle.getString("food_name"))
                    .setWhen(System.currentTimeMillis());

            Intent mIntent = new Intent(context,foodDetail.class);
            mIntent.putExtras(Recommand_bundle);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent mPendingIntent = PendingIntent.getActivity(context,0,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(mPendingIntent);

            Notification notify = builder.build();
            manager.notify(0,notify);
        }


    }

}



