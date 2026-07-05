package com.example.medireminder.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.medireminder.receiver.AlarmReceiver;

import java.util.Calendar;

public class AlarmHelper {

    public static void setAlarm(Context context,
                                int requestCode,
                                int hour,
                                int minute,
                                String namaObat){

        AlarmManager alarmManager =
                (AlarmManager)
                        context.getSystemService(
                                Context.ALARM_SERVICE);

        Intent intent =
                new Intent(
                        context,
                        AlarmReceiver.class
                );

        intent.putExtra("nama_obat", namaObat);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(

                        context,

                        requestCode,

                        intent,

                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE

                );

        Calendar calendar =
                Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour);

        calendar.set(Calendar.MINUTE, minute);

        calendar.set(Calendar.SECOND,0);

        if(calendar.before(Calendar.getInstance())){

            calendar.add(Calendar.DAY_OF_MONTH,1);

        }

        if(alarmManager!=null){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            } else {
                alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            }

        }

    }

    public static void cancelAlarm(Context context,
                                   int requestCode){

        AlarmManager alarmManager =
                (AlarmManager)
                        context.getSystemService(
                                Context.ALARM_SERVICE);

        Intent intent =
                new Intent(
                        context,
                        AlarmReceiver.class
                );

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(

                        context,

                        requestCode,

                        intent,

                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE

                );

        if(alarmManager!=null){

            alarmManager.cancel(pendingIntent);

        }

    }

}