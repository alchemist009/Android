package com.example.wra1th.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import Classes.FetchInfo;

public class MainActivity extends AppCompatActivity {

    Typeface weatherFont;
    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;
    Handler handler;
    Context context = this;
    WeatherInfo weatherInfo = new WeatherInfo();


    public static String LATITUDE = "LATITUDE";
    public static String LONGITUDE = "LONGITUDE";
    public static String TEMPERATURE_SCALE = "TEMPERATURE_SCALE";
    public static final String USE_GPS = "USE_GPS";
    //public static String CITY_FLAG = "CITY_FLAG";
    public static String CITY_SELECTED = "CITY_SELECTED";
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static Double longitude;
    public static Double latitude;
    public String scaleSelected = "imperial";

    public MainActivity() {
        handler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(getIntent().getSerializableExtra(CITY_SELECTED) != null){
                    weatherInfo.updateWeatherData((String) getIntent().getSerializableExtra(CITY_SELECTED), scaleSelected);
                }
                else
                    if (scaleSelected.equals("metric")){
                        weatherInfo.updateWeatherData("Dallas", scaleSelected);
                    }
                    else
                    {
                    weatherInfo.updateWeatherData("Dallas", "imperial");
                }
                weatherInfo.showWeather();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        if (getIntent().getSerializableExtra(LONGITUDE) != null && getIntent().getSerializableExtra(LATITUDE) != null) {
            latitude =  (Double) getIntent().getSerializableExtra(LATITUDE);
            longitude = (Double) getIntent().getSerializableExtra(LONGITUDE);
        }

        weatherFont = Typeface.createFromAsset(this.getAssets(), "fonts/weather.ttf");
        weatherInfo.updateWeatherData("Dallas", scaleSelected);
        weatherInfo.showWeather();
        checkPermission();
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                refresh(item);
                if(getIntent().getSerializableExtra(CITY_SELECTED) != null){
                    if(getIntent().getSerializableExtra(TEMPERATURE_SCALE) != null)
                        weatherInfo.updateWeatherData((String) getIntent().getSerializableExtra(CITY_SELECTED), (String) getIntent().getSerializableExtra(TEMPERATURE_SCALE));
                    else
                        weatherInfo.updateWeatherData((String) getIntent().getSerializableExtra(CITY_SELECTED), scaleSelected);
                }
                else {
                    weatherInfo.updateWeatherData("Dallas", "imperial");
                }
                weatherInfo.showWeather();
                completeRefresh(item);
                Toast.makeText(this, "Refreshing weather...", Toast.LENGTH_LONG).show();
                break;

            case R.id.change_city:
                Intent locIntent = new Intent(this, LocationActivity.class);
                locIntent.putExtra(LocationActivity.SCALE, scaleSelected);
                startActivity(locIntent);
                refreshWeather();
                break;

            case R.id.settings:
                Intent i = new Intent(this, PreferencesActivity.class);
                i.putExtra(PreferencesActivity.CITY, (String) getIntent().getSerializableExtra(CITY_SELECTED));
                startActivity(i);
                break;

            case R.id.about:
                return false;

        }

        return false;
    }

    public void refreshWeather(){
        weatherInfo.updateWeatherData("Dallas", scaleSelected );
    }

    public void refresh(MenuItem item) {
        LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = (ImageView) inflater.inflate(R.layout.layout_refresh, null);

        Animation rotation = AnimationUtils.loadAnimation(getApplication(), R.anim.refresh_animation);
        rotation.setRepeatCount(Animation.INFINITE);
        iv.startAnimation(rotation);
        item.setActionView(iv);
    }

    public void completeRefresh(MenuItem item)
    {
        item.getActionView().clearAnimation();
        item.setActionView(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getIntent().getSerializableExtra(LONGITUDE) != null && getIntent().getSerializableExtra(LATITUDE) != null) {
            latitude = (Double) getIntent().getSerializableExtra(LATITUDE);
            longitude = (Double) getIntent().getSerializableExtra(LONGITUDE);
        }

        if (getIntent().getSerializableExtra(TEMPERATURE_SCALE) != null){
            scaleSelected = (String) getIntent().getSerializableExtra(TEMPERATURE_SCALE);
            if(getIntent().getSerializableExtra(CITY_SELECTED) != null)
                weatherInfo.updateWeatherData((String) getIntent().getSerializableExtra(CITY_SELECTED), scaleSelected);
            else
                weatherInfo.updateWeatherData("Dallas", scaleSelected);
            Toast.makeText(this, scaleSelected, Toast.LENGTH_SHORT).show();
        }

        if(getIntent().getSerializableExtra(USE_GPS) != null) {
            if((boolean) getIntent().getSerializableExtra(USE_GPS))
                weatherInfo.updateWeatherData(latitude, longitude, scaleSelected);
        }

        if (getIntent().getSerializableExtra(CITY_SELECTED) != null) {
                weatherInfo.updateWeatherData((String) getIntent().getSerializableExtra(CITY_SELECTED), scaleSelected);
            }

        weatherInfo.showWeather();
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        //MainActivity.super.onBackPressed();
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    }
                }).create().show();
    }

    /**
     *
     *
     *
     *
     *
     */


    public class WeatherInfo {

        private void updateWeatherData(final String city, final String scale) {
            new Thread() {
                public void run() {
                    final JSONObject json = FetchInfo.getJSON(context, city, scale);
                    if (json == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, getString(R.string.place_not_found), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                renderWeather(json);
                            }
                        });
                    }

                }

            }.start();
        }

        private void updateWeatherData(final Double latitude, final Double longitude, final String scale) {
            new Thread() {
                public void run() {
                    final JSONObject json = FetchInfo.getJSON(context, latitude, longitude, scale);
                    if (json == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, getString(R.string.place_not_found), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                renderWeather(json);
                            }
                        });
                    }

                }

            }.start();

        }

        private void renderWeather(JSONObject json) {
            try {
                cityField.setText(json.getString("name").toUpperCase(Locale.US) + "," +
                        json.getJSONObject("sys").getString("country"));

                JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                JSONObject main = json.getJSONObject("main");
                detailsField.setText(details.getString("description").toUpperCase(Locale.US) + "\n" +
                        "Humidity: " + main.getString("humidity").toUpperCase(Locale.US) + "\n" +
                        "Pressure: " + main.getString("pressure") + "hPa");

                if(Objects.equals(scaleSelected, "imperial")) {
                    currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp")) + " °F");
                }
                else
                    currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp")) + " °C");

                DateFormat df = DateFormat.getDateTimeInstance();
                String updatedOn = df.format(new Date(json.getLong("dt") * 1000));
                updatedField.setText("Last update: " + updatedOn);

                setWeatherIcon(details.getInt("id"),
                        json.getJSONObject("sys").getLong("sunrise") * 1000,
                        json.getJSONObject("sys").getLong("sunset") * 1000);
            } catch (Exception e) {
                Log.e("WeatherApp", "One or more details not found in the JSON data");

            }

        }

        public void showWeather() {
            cityField = findViewById(R.id.city_field);
            updatedField = findViewById(R.id.updated_field);
            detailsField = findViewById(R.id.details_field);
            currentTemperatureField = findViewById(R.id.current_temperature_field);
            weatherIcon = findViewById(R.id.weather_icon);

            weatherIcon.setTypeface(weatherFont);
        }

        private void setWeatherIcon(int actualId, long sunrise, long sunset) {
            int id = actualId / 100;
            String icon = "";
            if (actualId == 800) {
                long currentTime = new Date().getTime();
                if (currentTime >= sunrise && currentTime < sunset) {
                    icon = getString(R.string.weather_sunny);
                } else {
                    icon = getString(R.string.weather_clear_night);
                }
            } else {
                switch (id) {
                    case 2:
                        icon = getString(R.string.weather_thunder);
                        break;
                    case 3:
                        icon = getString(R.string.weather_drizzle);
                        break;
                    case 5:
                        icon = getString(R.string.weather_rainy);
                        break;
                    case 6:
                        icon = getString(R.string.weather_snowy);
                        break;
                    case 7:
                        icon = getString(R.string.weather_foggy);
                        break;
                    case 8:
                        icon = getString(R.string.weather_cloudy);
                        break;
                }
            }
            weatherIcon.setText(icon);
            showWeather();
        }

    }

}
