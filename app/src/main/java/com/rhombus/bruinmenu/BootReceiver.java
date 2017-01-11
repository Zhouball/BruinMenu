package com.rhombus.bruinmenu;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") || intent.getAction().equals("com.rhombus.action.RESET_ALARM")) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            long updateStartHour = prefs.getLong("update_start_hour", 6);
            long updateStartMinute = prefs.getLong("update_start_minute", 0);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            cal.set(Calendar.HOUR_OF_DAY, (int) updateStartHour);
            cal.set(Calendar.MINUTE, (int) updateStartMinute);

            Intent i = new Intent(context, UpdateDBService.class);
            PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.cancel(pi);
            am.setInexactRepeating(AlarmManager.RTC, cal.getTimeInMillis(), prefs.getLong("update_frequency", AlarmManager.INTERVAL_DAY), pi);
        }
    }
}