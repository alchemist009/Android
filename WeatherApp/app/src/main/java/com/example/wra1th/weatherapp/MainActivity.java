package com.example.wra1th.weatherapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.container, new WeatherFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){

            case R.id.change_city : showInputDialog();

            case R.id.settings : return false;

            case R.id.about : return false;
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
        WeatherFragment wf = (WeatherFragment) getSupportFragmentManager().findFragmentById(R.id.container);

        wf.changeCity(city);
        new CitySelected(this).setCity(city);
    }
}
