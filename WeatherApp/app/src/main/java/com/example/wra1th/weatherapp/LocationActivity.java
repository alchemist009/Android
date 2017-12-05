/**
 *
 * Location Activity :
 *
 * This activity is launched when the user clicks on the location icon in the main activity's action bar.
 * It consists of three different methods by which the user can change the location for which he want the weather
 * displayed :
 *
 * 1. Selecting a city from a drop down list of major cities in the world and clicking the "Done" button
 * 2. Clicking "Enter a city" and manually entering a city name
 * 3. Clicking on "Use GPS" button and get the weather for the latest GPS location, or else the last known
 *    location if GPS is turned off.
 *
 * Pressing the back key takes the user back to the main activity which now displays weather info for the selected city.
 */
package com.example.wra1th.weatherapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Vector;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener{

    double latitude;
    double longitude;
    Intent cityIntent;
    public static String SCALE = "SCALE";
    private String scale;
    Spinner city_spinner;

    /**
     * Method to assign layout, link buttons to variables and initialize the drop down list of cities
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        getLastLocation();
        cityIntent = new Intent(this, MainActivity.class);
        Button button_city_input = (Button) findViewById(R.id.city_button);
        city_spinner = (Spinner) findViewById(R.id.city_spinner);
        Button button_spinner = (Button) findViewById(R.id.done_button);
        Button button_gps = (Button) findViewById(R.id.gps_button);
        button_gps.setOnClickListener(this);
        button_city_input.setOnClickListener(this);
        button_spinner.setOnClickListener(this);
        try {
            populateSpinner();
        } catch (IOException e) {
            e.printStackTrace();
        }
        scale = (String) getIntent().getSerializableExtra(SCALE);
    }


    @Override
    public void onResume(){
        super.onResume();
    }

    /**
     * Method to handle a location object, display a toast with the most recent location coordinates,
     * and assign those values to latitude, longitude variables. Just to confirm location is updating.
     * @param location
     */

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + ", " +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        // You can now create a LatLng Object for use with maps
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }


    /**
     * Helper method to pass the intent to Main activity
     */

    public void passIntent() {

        cityIntent.putExtra(MainActivity.TEMPERATURE_SCALE, scale);
        startActivity(cityIntent);

    }

    /**
     * Method to get the last known location using the Google FusedLocationProvider API which uses the combination of GPS and
     * network to get the most accurate location data
     */
    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);
                            latitude =location.getLatitude();
                            longitude = location.getLongitude();
                            cityIntent.putExtra(MainActivity.LATITUDE, latitude);
                            cityIntent.putExtra(MainActivity.LONGITUDE, longitude);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("LocationActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

    /**
     * Method to pass the intent and finish activity when back pressed
     */

    @Override
    public void onBackPressed() {
        passIntent();
        this.finish();
    }


    /**
     *
     * Method to show a pop up dialog asking for user input for the city to show weather for.
     * Adds the input to intent.
     */

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change city");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                cityIntent.putExtra(MainActivity.CITY_SELECTED, input.getText().toString());
            }
        });
        builder.show();
    }

    /**
     * Method to handle clicks on the three buttons in the layout and take the respective actions
     * @param view
     */

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.city_button :
                showInputDialog();
                break;
            case R.id.done_button :
                String spinner_city_selected = city_spinner.getSelectedItem().toString();
                cityIntent.putExtra(MainActivity.CITY_SELECTED, spinner_city_selected);
                break;
            case R.id.gps_button :
                cityIntent.putExtra(MainActivity.USE_GPS, true);
                break;
        }
    }

    /**
     *
     * Method to populate the drop down list of cities form a text file after sorting them alphabetically.
     * @throws IOException
     */


    public void populateSpinner() throws IOException {
        Vector<String>str=new Vector<String>();
        BufferedReader in = new BufferedReader(new InputStreamReader(getAssets().open("newCities.txt")));

        String line = in.readLine();
        while (line != null) {

            str.add(line);
            line = in.readLine();
        }
        Collections.sort(str);
        Spinner spinner = (Spinner) findViewById(R.id.city_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,str);
        spinner.setAdapter(adapter);
    }


}