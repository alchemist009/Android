package Classes;

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

    public String getCity(){
        return preferences.getString("city", "Dallas, US");
    }

    public void setCity(String city){
        preferences.edit().putString("city", city).commit();
    }

    public void setScale(String scale) {
        preferences.edit().putString("scale", scale).commit();
    }

    public String getScale() {
        return preferences.getString("scale", "imperial");
    }
}
