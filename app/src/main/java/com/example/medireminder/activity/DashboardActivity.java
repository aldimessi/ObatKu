package com.example.medireminder.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.ExistingPeriodicWorkPolicy;

import com.example.medireminder.R;
import com.example.medireminder.database.DatabaseHelper;
import com.example.medireminder.helper.NotificationHelper;
import com.example.medireminder.helper.SessionManager;
import com.example.medireminder.model.User;
import com.example.medireminder.worker.LateReminderWorker;

import java.util.concurrent.TimeUnit;

public class DashboardActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 101;
    private TextView txtNamaUser;

    private CardView cardTambah;
    private CardView cardJadwal;
    private CardView cardRiwayat;
    private CardView cardTentang;

    private Button btnLogout;

    private SessionManager sessionManager;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtNamaUser = findViewById(R.id.txtNamaUser);

        cardTambah = findViewById(R.id.cardTambah);
        cardJadwal = findViewById(R.id.cardJadwal);
        cardRiwayat = findViewById(R.id.cardRiwayat);
        cardTentang = findViewById(R.id.cardTentang);

        btnLogout = findViewById(R.id.btnLogout);

        sessionManager = new SessionManager(this);
        databaseHelper = new DatabaseHelper(this);

        tampilkanDataUser();
        checkPermissions();

        cardTambah.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, TambahObatActivity.class));
        });

        cardJadwal.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, JadwalActivity.class));
        });

        cardRiwayat.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, RiwayatActivity.class));
        });

        cardTentang.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, TentangActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        txtNamaUser.setOnClickListener(v -> {
            NotificationHelper.showNotification(this, "Obat Percobaan (Test)");
            Toast.makeText(this, "Mengetes notifikasi...", Toast.LENGTH_SHORT).show();
        });

        setupLateReminderWorker();
    }

    private void setupLateReminderWorker() {
        PeriodicWorkRequest lateWorkRequest = new PeriodicWorkRequest.Builder(
                LateReminderWorker.class,
                15, TimeUnit.MINUTES)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "LateReminderWork",
                ExistingPeriodicWorkPolicy.KEEP,
                lateWorkRequest
        );
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Izin notifikasi diberikan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Izin notifikasi ditolak, pengingat mungkin tidak muncul", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void tampilkanDataUser() {
        int idUser = sessionManager.getUserId();
        User user = databaseHelper.getUserById(idUser);
        if (user != null) {
            txtNamaUser.setText(user.getUsername());
        }
    }
}