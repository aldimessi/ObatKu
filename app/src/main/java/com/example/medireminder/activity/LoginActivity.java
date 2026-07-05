package com.example.medireminder.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.medireminder.R;
import com.example.medireminder.database.DatabaseHelper;
import com.example.medireminder.helper.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText edtUsername;
    private TextInputEditText edtPassword;

    private MaterialButton btnLogin;

    private TextView txtRegister;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);

        btnLogin = findViewById(R.id.btnLogin);

        txtRegister = findViewById(R.id.txtRegister);

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        requestNotificationPermission();

        btnLogin.setOnClickListener(v -> login());

        txtRegister.setOnClickListener(v -> {

            startActivity(new Intent(
                    LoginActivity.this,
                    RegisterActivity.class));

        });

    }

    private void requestNotificationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.POST_NOTIFICATIONS
                        },
                        100
                );

            }

        }

    }

    private void login() {

        String username = edtUsername.getText().toString().trim();

        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {

            edtUsername.setError("Username tidak boleh kosong");
            return;

        }

        if (TextUtils.isEmpty(password)) {

            edtPassword.setError("Password tidak boleh kosong");
            return;

        }

        int idUser = databaseHelper.loginUser(username, password);

        if (idUser != -1) {

            sessionManager.createLoginSession(idUser);

            Toast.makeText(
                    this,
                    "Login Berhasil",
                    Toast.LENGTH_SHORT
            ).show();

            startActivity(
                    new Intent(
                            this,
                            DashboardActivity.class
                    )
            );

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Username atau Password Salah",
                    Toast.LENGTH_SHORT
            ).show();

        }

    }

}