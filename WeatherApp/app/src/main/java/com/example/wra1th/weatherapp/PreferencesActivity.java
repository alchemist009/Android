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
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        intent = new Intent(this, MainActivity.class);
        radioTemperatureScale = findViewById(R.id.radioTempScale);
        radioFahrenheit = findViewById(R.id.FScale);
        radioCelsius = findViewById(R.id.CScale);
        switchGPS = findViewById(R.id.toggleGPS);
    }

    @Override
    public void onClick(View v){

        /*radioTemperatureScale.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.FScale:
                        scaleSelected = "imperial";
                        break;
                    case R.id.CScale:
                        scaleSelected = "metric";
                        break;
                }
            }
        });*/


        if(radioTemperatureScale.getCheckedRadioButtonId() == -1)
        {}
        else
        {
            if(radioCelsius.isChecked())
                scaleSelected = "metric";
            else
                if(radioFahrenheit.isChecked())
                    scaleSelected = "imperial";
        }

        boolean useGPS = switchGPS.isChecked();
        intent.putExtra(MainActivity.TEMPERATURE_SCALE, scaleSelected);
        intent.putExtra(MainActivity.USE_GPS, useGPS);

    }

    @Override
    public void onBackPressed(){
        passSettingsIntent();
        this.finish();
    }

    public void passSettingsIntent() {
        startActivity(intent);
    }

}
