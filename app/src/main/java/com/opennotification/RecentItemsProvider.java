package com.opennotification;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.lang.reflect.Method;

public class RecentItemsProvider extends AppWidgetProvider {

    private static final String ACTION_CLICK = "ACTION_CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                RecentItemsProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            // create some random data
       //     int number = (new Random().nextInt(100));

            try {

                Class serviceManagerClass = Class.forName("android.os.ServiceManager");
                Method getService = serviceManagerClass.getMethod("getService", String.class);
                IBinder retbinder = (IBinder) getService.invoke(serviceManagerClass, "statusbar");
                Class statusBarClass = Class.forName(retbinder.getInterfaceDescriptor());
                Object statusBarObject = statusBarClass.getClasses()[0].getMethod("asInterface", IBinder.class).invoke(null, new Object[] { retbinder });
                Method clearAll = statusBarClass.getMethod("toggleRecentApps");
                clearAll.setAccessible(true);
                clearAll.invoke(statusBarObject);
            }catch (Exception e){
                e.printStackTrace();
            }

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.recent_layout);
         //   Log.w("WidgetExample", String.valueOf(number));
            // Set the text
        //    remoteViews.setTextViewText(R.id.update, String.valueOf(number));


            // Register an onClickListener
            Intent intent = new Intent(context, RecentItemsProvider.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.img, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}