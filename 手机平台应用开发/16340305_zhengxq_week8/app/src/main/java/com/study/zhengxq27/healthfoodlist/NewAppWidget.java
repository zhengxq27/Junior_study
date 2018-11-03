package com.study.zhengxq27.healthfoodlist;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    public static boolean has_collect = false;
    public static String food_name;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
       for (int appWidgetId : appWidgetIds)
        {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            Intent pi = new Intent("com.com.study.zhengxq27.healthfoodlist.Widget.BroadCast");
            pi.setClass(context,MainActivity.class);
            PendingIntent BIntent = PendingIntent.getActivity(context,0,pi,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_linearlayout,BIntent);
            appWidgetManager.updateAppWidget(appWidgetId,views);

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context,intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Bundle bundle = intent.getExtras();
        if(intent.getAction().equals("com.com.study.zhengxq27.healthfoodlist.Widget.BroadCast"))
        {
            if( has_collect == false)
            {
                Random random = new Random();
                int I = random.nextInt(MainActivity.data.size());
                item chosen_item = MainActivity.data.get(I);
                RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
                remoteView.setTextViewText(R.id.appwidget_text,"今日推荐 " + chosen_item.textview2_content);
                food_name = chosen_item.textview2_content;
                ComponentName thisName = new ComponentName(context,NewAppWidget.class);

                Intent pI = new Intent("com.com.study.zhengxq27.healthfoodlist.Widget.BroadCast");
                pI.setClass(context,foodDetail.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("circle_name",chosen_item.textview1_content);
                bundle1.putString("food_name",chosen_item.textview2_content);
                bundle1.putString("food_kind",chosen_item.kind);
                bundle1.putString("element",chosen_item.element);
                bundle1.putString("title_color",chosen_item.title_color);
                pI.putExtras(bundle1);

                PendingIntent AIntent = PendingIntent.getActivity(context,0,pI,PendingIntent.FLAG_UPDATE_CURRENT);
                remoteView.setOnClickPendingIntent(R.id.appwidget_text,AIntent);

                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                manager.updateAppWidget(thisName,remoteView);

                has_collect = true;
            }
            else if( has_collect == true )
            {
                RemoteViews remoteView = new RemoteViews(context.getPackageName(),R.layout.new_app_widget);
                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                remoteView.setTextViewText(R.id.appwidget_text,"已收藏 " + food_name);
                ComponentName thisName = new ComponentName(context,NewAppWidget.class);

                Intent MainIntent = new Intent("OPen the MainActivity");
                MainIntent.setClass(context,MainActivity.class);
                PendingIntent MIntent = PendingIntent.getActivity(context,0,MainIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                remoteView.setOnClickPendingIntent(R.id.appwidget_text,MIntent);
                manager.updateAppWidget(thisName,remoteView);
            }

        }

    }
}

