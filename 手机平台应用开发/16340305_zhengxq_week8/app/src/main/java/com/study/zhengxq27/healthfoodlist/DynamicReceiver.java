package com.study.zhengxq27.healthfoodlist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class DynamicReceiver extends BroadcastReceiver {
    private static final String DYNAMIC = "dynamic_notification";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(DYNAMIC))
        {
            Bundle bundle = intent.getExtras();
            Bundle Recommand_bundle = intent.getExtras();
            NotificationManager manager1 = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

           Notification.Builder builder1 = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.empty_star)
                    .setContentTitle("已收藏")
                    //.setContentText("牛奶")
                    .setContentText(Recommand_bundle.getString("food_name"))
                    .setWhen(System.currentTimeMillis());

           Intent mIntent = new Intent(context,MainActivity.class);
            mIntent.putExtras(Recommand_bundle);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent mPendingIntent = PendingIntent.getActivity(context,0,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder1.setContentIntent(mPendingIntent);

            Notification notify = builder1.build();
            manager1.notify(0,notify);
        }

    }
}
