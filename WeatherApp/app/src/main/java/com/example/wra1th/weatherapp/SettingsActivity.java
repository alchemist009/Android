package com.example.wra1th.weatherapp;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by wra1th on 11/18/2017.
 */

public class SettingsActivity {

    SharedPreferences preferences;

    public SettingsActivity(Activity activity){

        preferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    String getScale(){
        return preferences.getString("scale", "imperial");
    }

    void setScale(String scale){
        preferences.edit().putString("scale", scale).commit();
    }

}
