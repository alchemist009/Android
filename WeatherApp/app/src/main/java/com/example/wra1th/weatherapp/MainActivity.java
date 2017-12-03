package com.example.wra1th.weatherapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import Classes.CitySelected;
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
    String scaleUsed;
    LocationManager locationManager;
    WeatherInfo weatherInfo = new WeatherInfo();


    public static String LATITUDE = "LATITUDE";
    public static String LONGITUDE = "LONGITUDE";
    public static String TEMPERATURE_SCALE = "TEMPERATURE_SCALE";
    public static final String USE_GPS = "USE_GPS";
    public static String SCALE_USED = "SCALE_USED";
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static Double longitude;
    public static Double latitude;
    int scaleIndex;

    public MainActivity() {
        handler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if (getIntent().getSerializableExtra(TEMPERATURE_SCALE) != null) {
            scaleIndex = (int) getIntent().getSerializableExtra(TEMPERATURE_SCALE);
        }

        if (scaleIndex == 2)
            scaleUsed = "metric";
        else
            scaleUsed = "imperial";

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                weatherInfo.updateWeatherData(new CitySelected(MainActivity.this).getCity(), scaleUsed);
                weatherInfo.showWeather();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        if (getIntent().getSerializableExtra(LONGITUDE) != null && getIntent().getSerializableExtra(LATITUDE) != null) {
            latitude =  (Double) getIntent().getSerializableExtra(LATITUDE);
            longitude = (Double) getIntent().getSerializableExtra(LONGITUDE);
        }

        weatherFont = Typeface.createFromAsset(this.getAssets(), "fonts/weather.ttf");
        weatherInfo.updateWeatherData(new CitySelected(MainActivity.this).getCity(), scaleUsed);
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
                weatherInfo.updateWeatherData(new CitySelected(MainActivity.this).getCity(), "imperial");
                weatherInfo.showWeather();
                completeRefresh(item);
                break;

            case R.id.change_city:
                showInputDialog();
                break;

            case R.id.settings:
                Intent i = new Intent(this, PreferencesActivity.class);
                startActivity(i);
                break;

            case R.id.about:
                return false;

            case R.id.location:
                Intent locIntent = new Intent(this, LocationActivity.class);
                startActivity(locIntent);
                break;
        }

        return false;
    }

    public void refresh(MenuItem item) {
        LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = (ImageView) inflater.inflate(R.layout.layout_refresh, null);

        Animation rotation = AnimationUtils.loadAnimation(getApplication(), R.anim.refresh_animation);
        rotation.setRepeatCount(Animation.INFINITE);
        iv.startAnimation(rotation);
        item.setActionView(iv);
    }

    public void completeRefresh(MenuItem item) {
        item.getActionView().clearAnimation();
        item.setActionView(null);
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change city");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                changeCity(input.getText().toString());
            }
        });
        builder.show();
    }

    public void changeCity(String city) {


        changeSettings(city);
        new CitySelected(this).setCity(city);
    }

    public void changeSettings(String city) {
        weatherInfo.updateWeatherData(city, "imperial");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getIntent().getSerializableExtra(LONGITUDE) != null && getIntent().getSerializableExtra(LATITUDE) != null) {
            latitude = (Double) getIntent().getSerializableExtra(LATITUDE);
            longitude = (Double) getIntent().getSerializableExtra(LONGITUDE);
        }

        weatherInfo.updateWeatherData(latitude, longitude, scaleUsed);
    }

    public void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }

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

                currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp")) + " °F");

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
