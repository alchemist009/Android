package com.example.wra1th.weatherapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import android.location.LocationListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.nio.BufferOverflowException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import Classes.CitySelected;
import Classes.FetchInfo;

public class MainActivity extends AppCompatActivity {

    public static String SCALE_USED = "SCALE_USED";
    Typeface weatherFont;

    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;
    Handler handler;
    Context context = this;
    int scaleIndex;
    String scaleUsed;
    LocationManager locationManager;

    public static String TEMPERATURE_SCALE = "TEMPERATURE_SCALE";
    public static final String USE_GPS = "USE_GPS";
    private SwipeRefreshLayout  swipeRefreshLayout;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    public MainActivity() { handler = new Handler();}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateWeatherData(new CitySelected(MainActivity.this).getCity(), scaleUsed);
                showWeather();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        if(getIntent().getSerializableExtra(TEMPERATURE_SCALE) != null){
            scaleIndex = (int) getIntent().getSerializableExtra(TEMPERATURE_SCALE);
        }

        if(scaleIndex == 1)
            scaleUsed = "metric";
        else
            scaleUsed = "imperial";

        weatherFont = Typeface.createFromAsset(this.getAssets(), "fonts/weather.ttf");
        updateWeatherData(new CitySelected(MainActivity.this).getCity(), scaleUsed);
        showWeather();
        checkPermission();

    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ){//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }


    private void updateWeatherData(final String city, final String scale){
        new Thread(){
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

                    String jsonString = json.toString();
                    Writer output = null;
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    try{
                        File f = new File(path +  "/weatherInfo/", city + ".json");
                        path.mkdirs();
                        output = new BufferedWriter(new FileWriter(f));
                        output.write(jsonString);
                       // Toast.makeText(getApplicationContext(), "JSON saved", Toast.LENGTH_LONG).show();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    finally{
                        if(output != null) try{ output.close(); } catch (IOException ignore){}
                    }
                    //finish();
                }

            }

        }.start();

    }


    private void renderWeather(JSONObject json){
        try{
            cityField.setText(json.getString("name").toUpperCase(Locale.US) + "," +
                    json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            detailsField.setText(details.getString("description").toUpperCase(Locale.US) + "\n" +
                    "Humidity: " + main.getString("humidity").toUpperCase(Locale.US) + "\n" +
                    "Pressure: " + main.getString("pressure") + "hPa");

            currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp")) + " °F");

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            updatedField.setText("Last update: " + updatedOn);

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise")*1000,
                    json.getJSONObject("sys").getLong("sunset")*1000);
        }
        catch (Exception e){
            Log.e("WeatherApp", "One or more details not found in the JSON data");

        }

    }



    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = this.getString(R.string.weather_sunny);
            } else {
                icon = this.getString(R.string.weather_clear_night);
            }
        }
        else{
            switch (id){
                case 2 : icon = this.getString(R.string.weather_thunder);
                    break;
                case 3 : icon = this.getString(R.string.weather_drizzle);
                    break;
                case 5 : icon = this.getString(R.string.weather_rainy);
                    break;
                case 6 : icon = this.getString(R.string.weather_snowy);
                    break;
                case 7 : icon = this.getString(R.string.weather_foggy);
                    break;
                case 8 : icon = this.getString(R.string.weather_cloudy);
                    break;
            }
        }
        weatherIcon.setText(icon);
        showWeather();
    }


    public void showWeather() {
        //View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        cityField = findViewById(R.id.city_field);
        updatedField = findViewById(R.id.updated_field);
        detailsField = findViewById(R.id.details_field);
        currentTemperatureField = findViewById(R.id.current_temperature_field);
        weatherIcon = findViewById(R.id.weather_icon);

        weatherIcon.setTypeface(weatherFont);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.refresh :
                updateWeatherData(new CitySelected(MainActivity.this).getCity(), "imperial");
                showWeather();
                break;

            case R.id.change_city : showInputDialog();
                                    break;

            case R.id.settings : Intent i = new Intent(this, PreferencesActivity.class);
                                 startActivity(i);
                                 break;

            case R.id.about : return false;

            case R.id.location :
                Intent locIntent = new Intent(this, LocationActivity.class);
                startActivity(locIntent);
                break;
        }

        return false;
    }

    private void showInputDialog(){
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
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

    public void changeCity(String city){


        changeSettings(city);
        new CitySelected(this).setCity(city);
    }

    public void changeSettings(String city) { updateWeatherData(city, "imperial");}

    @Override
    public void onResume(){
        super.onResume();
        int unit = -1;
        String scale;
        Intent intent = getIntent();
        scaleIndex = intent.getIntExtra(TEMPERATURE_SCALE, 0);
        if(intent.hasExtra(TEMPERATURE_SCALE)) {
            unit = scaleIndex;
        }
        else
            unit = 0;
        if(unit == 0){
            scale = "imperial";
        }
        else
            scale = "metric";
        Toast.makeText(context, "unit = " + Integer.toString(unit), Toast.LENGTH_LONG).show();
        updateWeatherData(new CitySelected(MainActivity.this).getCity(), scale);
        showWeather();
    }

    public void verifyStoragePermissions(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        String provider = locationManager.getBestProvider(new Criteria(), false);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }*/


}
