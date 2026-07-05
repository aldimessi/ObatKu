package com.example.medireminder.activity;

import com.example.medireminder.helper.AlarmHelper;
import com.example.medireminder.helper.NotificationHelper;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medireminder.R;
import com.example.medireminder.database.DatabaseHelper;
import com.example.medireminder.helper.SessionManager;
import com.example.medireminder.model.Obat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

public class TambahObatActivity extends AppCompatActivity {

    private TextInputEditText edtNamaObat;
    private TextInputEditText edtDosis;
    private TextInputEditText edtJam;

    private MaterialButton btnSimpan;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_obat);

        edtNamaObat = findViewById(R.id.edtNamaObat);
        edtDosis = findViewById(R.id.edtDosis);
        edtJam = findViewById(R.id.edtJam);

        btnSimpan = findViewById(R.id.btnSimpan);

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        edtJam.setOnClickListener(v -> tampilTimePicker());

        btnSimpan.setOnClickListener(v -> simpanObat());

    }

    private void tampilTimePicker() {

        Calendar calendar = Calendar.getInstance();

        int jam = calendar.get(Calendar.HOUR_OF_DAY);
        int menit = calendar.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {

                    String waktu = String.format(
                            Locale.getDefault(),
                            "%02d:%02d",
                            hourOfDay,
                            minute
                    );

                    edtJam.setText(waktu);

                },
                jam,
                menit,
                true
        );

        dialog.show();

    }

    private void simpanObat() {

        String nama = edtNamaObat.getText().toString().trim();
        String dosis = edtDosis.getText().toString().trim();
        String jam = edtJam.getText().toString().trim();

        if (TextUtils.isEmpty(nama)) {

            edtNamaObat.setError("Nama obat tidak boleh kosong");
            edtNamaObat.requestFocus();
            return;

        }

        if (TextUtils.isEmpty(dosis)) {

            edtDosis.setError("Dosis tidak boleh kosong");
            edtDosis.requestFocus();
            return;

        }

        if (TextUtils.isEmpty(jam)) {

            edtJam.setError("Pilih jam minum");
            edtJam.requestFocus();
            return;

        }

        Obat obat = new Obat();

        obat.setIdUser(sessionManager.getUserId());
        obat.setNamaObat(nama);
        obat.setDosis(dosis);
        obat.setJamMinum(jam);
        obat.setStatus("Belum diminum");

        boolean berhasil = databaseHelper.insertObat(obat);

        if (berhasil) {

            String[] waktu = jam.split(":");

            int hour = Integer.parseInt(waktu[0]);

            int minute = Integer.parseInt(waktu[1]);

            Calendar calendarCheck = Calendar.getInstance();
            int currentHour = calendarCheck.get(Calendar.HOUR_OF_DAY);
            int currentMinute = calendarCheck.get(Calendar.MINUTE);

            if (currentHour > hour || (currentHour == hour && currentMinute > minute)) {
                NotificationHelper.showLateNotification(this, nama);
            }

            AlarmHelper.setAlarm(

                    this,

                    (int) System.currentTimeMillis(),

                    hour,

                    minute,

                    nama

            );

            Toast.makeText(
                    this,
                    "Jadwal berhasil disimpan",
                    Toast.LENGTH_SHORT
            ).show();

            finish();


        } else {

            Toast.makeText(
                    this,
                    "Gagal menyimpan data",
                    Toast.LENGTH_SHORT
            ).show();

        }

    }

}