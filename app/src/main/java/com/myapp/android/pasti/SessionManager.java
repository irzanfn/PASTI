package com.myapp.android.pasti;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences("PASTI", 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void setLogin(boolean login){
        editor.putBoolean("KEY_LOGIN", login);
        editor.commit();
    }

    public boolean getLogin(){
        return sharedPreferences.getBoolean("KEY_LOGIN", false);
    }

    public void setLevel(String level){
        editor.putString("KEY_LEVEL", level).commit();
    }

    public String getLevel(){
        return sharedPreferences.getString("KEY_LEVEL", "");
    }

    public void setId(String id){
        editor.putString("KEY_NIM", id).commit();
    }

    public String getId(){
        return sharedPreferences.getString("KEY_NIM", "");
    }
}
