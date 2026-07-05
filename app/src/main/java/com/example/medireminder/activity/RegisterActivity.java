package com.example.medireminder.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medireminder.R;
import com.example.medireminder.database.DatabaseHelper;
import com.example.medireminder.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText edtUsername;
    private TextInputEditText edtPassword;
    private TextInputEditText edtKonfirmasiPassword;

    private MaterialButton btnRegister;

    private TextView txtLogin;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtKonfirmasiPassword = findViewById(R.id.edtKonfirmasiPassword);

        btnRegister = findViewById(R.id.btnRegister);

        txtLogin = findViewById(R.id.txtLogin);

        databaseHelper = new DatabaseHelper(this);

        btnRegister.setOnClickListener(v -> registerUser());

        txtLogin.setOnClickListener(v -> finish());

    }

    private void registerUser() {

        String username = edtUsername.getText().toString().trim();

        String password = edtPassword.getText().toString().trim();

        String konfirmasi = edtKonfirmasiPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {

            edtUsername.setError("Username tidak boleh kosong");
            edtUsername.requestFocus();
            return;

        }

        if (TextUtils.isEmpty(password)) {

            edtPassword.setError("Password tidak boleh kosong");
            edtPassword.requestFocus();
            return;

        }

        if (TextUtils.isEmpty(konfirmasi)) {

            edtKonfirmasiPassword.setError("Konfirmasi Password tidak boleh kosong");
            edtKonfirmasiPassword.requestFocus();
            return;

        }

        if (!password.equals(konfirmasi)) {

            edtKonfirmasiPassword.setError("Password tidak sama");
            edtKonfirmasiPassword.requestFocus();
            return;

        }

        if (databaseHelper.checkUsername(username)) {

            edtUsername.setError("Username sudah digunakan");
            edtUsername.requestFocus();
            return;

        }

        User user = new User();

        user.setUsername(username);
        user.setPassword(password);

        boolean berhasil = databaseHelper.registerUser(user);

        if (berhasil) {

            Toast.makeText(
                    this,
                    "Registrasi Berhasil",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Registrasi Gagal",
                    Toast.LENGTH_SHORT
            ).show();

        }

    }

}