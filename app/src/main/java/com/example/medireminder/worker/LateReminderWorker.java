package com.example.medireminder.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.medireminder.database.DatabaseHelper;
import com.example.medireminder.helper.NotificationHelper;
import com.example.medireminder.helper.SessionManager;
import com.example.medireminder.model.Obat;

import java.util.Calendar;
import java.util.List;

public class LateReminderWorker extends Worker {

    public LateReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SessionManager sessionManager = new SessionManager(context);

        if (sessionManager.isLogin()) {
            int idUser = sessionManager.getUserId();
            List<Obat> listObat = databaseHelper.getJadwalObat(idUser);

            for (Obat obat : listObat) {
                if (isTelat(obat.getJamMinum())) {
                    NotificationHelper.showLateNotification(context, obat.getNamaObat());
                }
            }
        }

        return Result.success();
    }

    private boolean isTelat(String jamMinum) {
        try {
            String[] parts = jamMinum.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            Calendar now = Calendar.getInstance();
            int currentHour = now.get(Calendar.HOUR_OF_DAY);
            int currentMinute = now.get(Calendar.MINUTE);

            if (currentHour > hour) return true;
            if (currentHour == hour && currentMinute > minute) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}