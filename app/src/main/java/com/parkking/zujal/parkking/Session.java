package com.parkking.zujal.parkking;

/**
 * Created by zujal on 25/03/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;
    boolean logggedin=false;
    public String KEY_NAME ="key name";
    String KEY_PASS ="key pass";

    public Session(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedin(boolean logggedin){
        editor.putBoolean("loggedInmode",logggedin);

        editor.commit();
    }

    public void createLoginSession(String name, String password) {
        // Storing login value as TRUE
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PASS, password);
        // Storing email in pref
        // commit changes
        editor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, prefs.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_PASS, prefs.getString(KEY_PASS, null));

        // return user
        return user;
    }

    public boolean loggedin(){
        return prefs.getBoolean("loggedInmode", false);
    }
    public void logout()
    {
        editor.clear();
        editor.commit();
    }
}