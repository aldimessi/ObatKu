package com.example.medireminder.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.medireminder.R;

public class NotificationHelper {

    public static String getChannelId(Context context) {
        SessionManager sessionManager = new SessionManager(context);
        String ringtone = sessionManager.getRingtoneUri();
        if (ringtone != null) {
            return "OBATKU_CHANNEL_" + Math.abs(ringtone.hashCode());
        }
        return "OBATKU_NOTIF_CHANNEL_V1";
    }

    public static void showNotification(Context context, String namaObat){
        createChannel(context);
        String channelId = getChannelId(context);
        SessionManager sessionManager = new SessionManager(context);
        String savedRingtone = sessionManager.getRingtoneUri();
        Uri sound;

        if (savedRingtone != null) {
            sound = Uri.parse(savedRingtone);
        } else {
            sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (sound == null) {
                sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Pengingat Obat")
                .setContentText("Saatnya minum " + namaObat)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setSound(sound)
                .setVibrate(new long[]{0, 1000, 500, 1000});

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify((int) System.currentTimeMillis(), builder.build());
    }

    public static void showLateNotification(Context context, String namaObat) {
        createChannel(context);
        String channelId = getChannelId(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Jadwal Terlewat!")
                .setContentText("Anda belum minum obat: " + namaObat + ". Segera minum!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500, 200, 500});

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private static void createChannel(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = getChannelId(context);
            NotificationChannel channel = new NotificationChannel(channelId, "Reminder Obat", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Channel untuk pengingat minum obat");
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});

            SessionManager sessionManager = new SessionManager(context);
            String savedRingtone = sessionManager.getRingtoneUri();
            Uri sound;

            if (savedRingtone != null) {
                sound = Uri.parse(savedRingtone);
            } else {
                sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                if (sound == null) {
                    sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                }
            }

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            channel.setSound(sound, audioAttributes);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if(manager != null){
                manager.createNotificationChannel(channel);
            }
        }
    }
}