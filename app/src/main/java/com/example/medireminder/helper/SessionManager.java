package com.example.medireminder.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "ObatKuSession";
    private static final String KEY_LOGIN = "isLogin";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_RINGTONE_URI = "ringtoneUri";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(int userId){
        editor.putBoolean(KEY_LOGIN,true);
        editor.putInt(KEY_USER_ID,userId);
        editor.apply();
    }

    public boolean isLogin(){
        return sharedPreferences.getBoolean(KEY_LOGIN,false);
    }

    public int getUserId(){
        return sharedPreferences.getInt(KEY_USER_ID,-1);
    }

    public void setRingtoneUri(String uri) {
        editor.putString(KEY_RINGTONE_URI, uri);
        editor.apply();
    }

    public String getRingtoneUri() {
        return sharedPreferences.getString(KEY_RINGTONE_URI, null);
    }

    public void logout(){
        editor.clear();
        editor.apply();
    }
}