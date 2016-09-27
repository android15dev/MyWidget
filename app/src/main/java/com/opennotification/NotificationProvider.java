package com.opennotification;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import java.lang.reflect.Method;

public class NotificationProvider extends AppWidgetProvider {

    private static final String ACTION_CLICK = "ACTION_CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                NotificationProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            // create some random data
       //     int number = (new Random().nextInt(100));

            try {

                Object sbservice = context.getSystemService("statusbar");
                Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
                Method showsb;
                if (Build.VERSION.SDK_INT >= 17) {
                    showsb = statusbarManager.getMethod("expandNotificationsPanel");
                } else {
                    showsb = statusbarManager.getMethod("expand");
                }
                showsb.invoke(sbservice);
            }catch (Exception e){
                e.printStackTrace();
            }

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
         //   Log.w("WidgetExample", String.valueOf(number));
            // Set the text
        //    remoteViews.setTextViewText(R.id.update, String.valueOf(number));


            // Register an onClickListener
            Intent intent = new Intent(context, NotificationProvider.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.img, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}