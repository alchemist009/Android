package com.example.wra1th.weatherapp;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by wra1th on 11/16/2017.
 */

public class CitySelected {

    SharedPreferences preferences;

    public CitySelected(Activity activity){
        preferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    String getCity(){
        return preferences.getString("city", "Dallas, US");
    }

    void setCity(String city){
        preferences.edit().putString("city", city).commit();
    }
}
