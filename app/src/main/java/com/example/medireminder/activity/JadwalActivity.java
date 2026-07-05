package com.example.medireminder.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medireminder.R;
import com.example.medireminder.adapter.ObatAdapter;
import com.example.medireminder.database.DatabaseHelper;
import com.example.medireminder.helper.SessionManager;
import com.example.medireminder.model.Obat;

import java.util.List;

public class JadwalActivity extends AppCompatActivity {

    private RecyclerView recyclerJadwal;

    private DatabaseHelper databaseHelper;

    private SessionManager sessionManager;

    private ObatAdapter adapter;

    private List<Obat> listObat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);

        recyclerJadwal = findViewById(R.id.recyclerJadwal);

        databaseHelper = new DatabaseHelper(this);

        sessionManager = new SessionManager(this);

        recyclerJadwal.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadData();

    }

    private void loadData() {

        int idUser = sessionManager.getUserId();

        listObat = databaseHelper.getJadwalObat(idUser);

        adapter = new ObatAdapter(this, listObat);

        recyclerJadwal.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

}