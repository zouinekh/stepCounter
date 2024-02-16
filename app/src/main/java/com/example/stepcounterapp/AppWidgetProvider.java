package com.example.stepcounterapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
         for(int appWidgetId:appWidgetIds){
             Intent intent=new Intent(context,MainActivity.class);
             PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 ,intent,PendingIntent.FLAG_IMMUTABLE);
             RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.widget);
             views.setOnClickPendingIntent(R.id.buttonWidget,pendingIntent);
             appWidgetManager.updateAppWidget(appWidgetId,views);
         }
    }
}
