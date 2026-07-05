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

public class RiwayatActivity extends AppCompatActivity {

    private RecyclerView recyclerRiwayat;

    private DatabaseHelper databaseHelper;

    private SessionManager sessionManager;

    private ObatAdapter adapter;

    private List<Obat> listObat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        recyclerRiwayat = findViewById(R.id.recyclerRiwayat);

        databaseHelper = new DatabaseHelper(this);

        sessionManager = new SessionManager(this);

        recyclerRiwayat.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadData();

    }

    private void loadData() {

        int idUser = sessionManager.getUserId();

        listObat = databaseHelper.getRiwayatObat(idUser);

        adapter = new ObatAdapter(this, listObat);

        recyclerRiwayat.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

}