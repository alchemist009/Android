package com.example.wra1th.drawableandanimationdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Intent intent;
    Button button_main;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        button_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), AnimationActivity.class);
                startActivity(intent);
                count++;
                String button_count = "Main button was pressed " + count + " times";
                //shared preferences to store count;
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.button_count_sharedPrefs), MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.click_count), button_count);
                editor.commit();
            }
        });


    }

    private void bindViews() {

        button_main = findViewById(R.id.btn_main);
    }




}
