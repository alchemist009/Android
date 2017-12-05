package com.example.wra1th.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class PreferencesActivity extends AppCompatActivity implements View.OnClickListener{


    private RadioGroup radioTemperatureScale;
    private RadioButton radioFahrenheit;
    private RadioButton radioCelsius;
    private String scaleSelected;
    public static String CITY = "CITY";
    String city;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        intent = new Intent(this, MainActivity.class);
        radioTemperatureScale = findViewById(R.id.radioTempScale);
        radioFahrenheit = findViewById(R.id.FScale);
        radioCelsius = findViewById(R.id.CScale);
        city = (String) getIntent().getSerializableExtra(CITY);
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
                if(radioFahrenheit.isChecked())
                    scaleSelected = "imperial";
        }
        intent.putExtra(MainActivity.TEMPERATURE_SCALE, scaleSelected);
        intent.putExtra(MainActivity.CITY_SELECTED, city);
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
