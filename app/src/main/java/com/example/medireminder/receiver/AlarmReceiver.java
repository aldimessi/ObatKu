package com.example.medireminder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.example.medireminder.helper.NotificationHelper;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String namaObat =
                intent.getStringExtra("nama_obat");

        NotificationHelper.showNotification(
                context,
                namaObat
        );

        Vibrator vibrator =
                (Vibrator) context.getSystemService(
                        Context.VIBRATOR_SERVICE);

        if (vibrator != null) {

            long[] pattern = {0, 1000, 500, 1000, 500, 1000};

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                vibrator.vibrate(

                        VibrationEffect.createWaveform(pattern, -1)

                );

            } else {

                vibrator.vibrate(pattern, -1);

            }

        }

    }

}