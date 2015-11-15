package zhou.allen.bruinmenu;

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
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            Intent i = new Intent(context, UpdateDBService.class);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            long updateStartHour = prefs.getLong("update_start_hour", 6);
            long updateStartMinute = prefs.getLong("update_start_minute", 0);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            cal.set(Calendar.HOUR_OF_DAY, (int) updateStartHour);
            cal.set(Calendar.MINUTE, (int) updateStartMinute);

            PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pi);
            alarmManager.setInexactRepeating(AlarmManager.RTC, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);

        }
    }
}