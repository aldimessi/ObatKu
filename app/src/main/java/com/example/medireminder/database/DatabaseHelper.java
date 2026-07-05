package com.example.medireminder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.medireminder.model.Obat;
import com.example.medireminder.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "obatku.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USER = "user";
    public static final String USER_ID = "id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static final String TABLE_OBAT = "obat";
    public static final String OBAT_ID = "id";
    public static final String ID_USER = "id_user";
    public static final String NAMA_OBAT = "nama_obat";
    public static final String DOSIS = "dosis";
    public static final String JAM_MINUM = "jam_minum";
    public static final String STATUS = "status";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable =
                "CREATE TABLE " + TABLE_USER + " (" +
                        USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        USERNAME + " TEXT UNIQUE, " +
                        PASSWORD + " TEXT" +
                        ")";

        String createObatTable =
                "CREATE TABLE " + TABLE_OBAT + " (" +
                        OBAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ID_USER + " INTEGER, " +
                        NAMA_OBAT + " TEXT, " +
                        DOSIS + " TEXT, " +
                        JAM_MINUM + " TEXT, " +
                        STATUS + " TEXT, " +
                        "FOREIGN KEY(" + ID_USER + ") REFERENCES " +
                        TABLE_USER + "(" + USER_ID + ")" +
                        ")";

        db.execSQL(createUserTable);
        db.execSQL(createObatTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public boolean registerUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME, user.getUsername());
        values.put(PASSWORD, user.getPassword());
        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, null, USERNAME + "=?", new String[]{username}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public int loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]{USER_ID}, USERNAME + "=? AND " + PASSWORD + "=?", new String[]{username, password}, null, null, null);
        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID));
        }
        cursor.close();
        db.close();
        return id;
    }

    public User getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, null, USER_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(USERNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(PASSWORD)));
        }
        cursor.close();
        db.close();
        return user;
    }

    public boolean insertObat(Obat obat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_USER, obat.getIdUser());
        values.put(NAMA_OBAT, obat.getNamaObat());
        values.put(DOSIS, obat.getDosis());
        values.put(JAM_MINUM, obat.getJamMinum());
        values.put(STATUS, obat.getStatus());
        long result = db.insert(TABLE_OBAT, null, values);
        db.close();
        return result != -1;
    }

    public List<Obat> getJadwalObat(int idUser) {
        List<Obat> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_OBAT, null, ID_USER + "=? AND " + STATUS + "=?", new String[]{String.valueOf(idUser), "Belum diminum"}, null, null, JAM_MINUM + " ASC");
        while (cursor.moveToNext()) {
            Obat obat = new Obat();
            obat.setId(cursor.getInt(cursor.getColumnIndexOrThrow(OBAT_ID)));
            obat.setIdUser(cursor.getInt(cursor.getColumnIndexOrThrow(ID_USER)));
            obat.setNamaObat(cursor.getString(cursor.getColumnIndexOrThrow(NAMA_OBAT)));
            obat.setDosis(cursor.getString(cursor.getColumnIndexOrThrow(DOSIS)));
            obat.setJamMinum(cursor.getString(cursor.getColumnIndexOrThrow(JAM_MINUM)));
            obat.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(STATUS)));
            list.add(obat);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Obat> getRiwayatObat(int idUser) {
        List<Obat> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_OBAT, null, ID_USER + "=? AND " + STATUS + "=?", new String[]{String.valueOf(idUser), "Sudah diminum"}, null, null, JAM_MINUM + " DESC");
        while (cursor.moveToNext()) {
            Obat obat = new Obat();
            obat.setId(cursor.getInt(cursor.getColumnIndexOrThrow(OBAT_ID)));
            obat.setIdUser(cursor.getInt(cursor.getColumnIndexOrThrow(ID_USER)));
            obat.setNamaObat(cursor.getString(cursor.getColumnIndexOrThrow(NAMA_OBAT)));
            obat.setDosis(cursor.getString(cursor.getColumnIndexOrThrow(DOSIS)));
            obat.setJamMinum(cursor.getString(cursor.getColumnIndexOrThrow(JAM_MINUM)));
            obat.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(STATUS)));
            list.add(obat);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Obat> getAllObat(int idUser) {
        List<Obat> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_OBAT, null, ID_USER + "=?", new String[]{String.valueOf(idUser)}, null, null, JAM_MINUM + " ASC");
        while (cursor.moveToNext()) {
            Obat obat = new Obat();
            obat.setId(cursor.getInt(cursor.getColumnIndexOrThrow(OBAT_ID)));
            obat.setIdUser(cursor.getInt(cursor.getColumnIndexOrThrow(ID_USER)));
            obat.setNamaObat(cursor.getString(cursor.getColumnIndexOrThrow(NAMA_OBAT)));
            obat.setDosis(cursor.getString(cursor.getColumnIndexOrThrow(DOSIS)));
            obat.setJamMinum(cursor.getString(cursor.getColumnIndexOrThrow(JAM_MINUM)));
            obat.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(STATUS)));
            list.add(obat);
        }
        cursor.close();
        db.close();
        return list;
    }

    public boolean updateStatusObat(int idObat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS, "Sudah diminum");
        int result = db.update(TABLE_OBAT, values, OBAT_ID + "=?", new String[]{String.valueOf(idObat)});
        db.close();
        return result > 0;
    }

    public boolean deleteObat(int idObat) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_OBAT, OBAT_ID + "=?", new String[]{String.valueOf(idObat)});
        db.close();
        return result > 0;
    }
}