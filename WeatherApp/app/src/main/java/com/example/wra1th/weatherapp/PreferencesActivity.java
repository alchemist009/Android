package com.example.wra1th.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class PreferencesActivity extends AppCompatActivity implements View.OnClickListener{


    private RadioGroup radioTemperatureScale;
    private RadioButton radioFahrenheit;
    private RadioButton radioCelsius;
    private Switch switchGPS;
    private String scaleSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        radioTemperatureScale = findViewById(R.id.radioTempScale);
        switchGPS = findViewById(R.id.toggleGPS);
    }

    @Override
    public void onClick(View v){

        if(radioTemperatureScale.getCheckedRadioButtonId() == -1)
        {}
        else
        {
            if(radioCelsius.isChecked())
                scaleSelected = "metric";
            else
                scaleSelected = "imperial";
        }

    }

    @Override
    public void onBackPressed(){
        passSettingsIntent();
        PreferencesActivity.this.finish();
    }

    public void passSettingsIntent() {

        boolean useGPS = switchGPS.isChecked();
        Intent intent = new Intent(this, MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(MainActivity.TEMPERATURE_SCALE, scaleSelected);
        intent.putExtra(MainActivity.USE_GPS, useGPS);

        startActivity(intent);

    }

}
