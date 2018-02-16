package com.example.wra1th.firstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Screen2Activity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);
        TextView TV = findViewById(R.id.TV);
        intent = getIntent();
        boolean greet_flag = intent.getBooleanExtra("FLAG", false);

        if(greet_flag){
            TV.setText("Well, you're not a total buffoon." + "You said: " + intent.getSerializableExtra("GREETING").toString());
            //Toast.makeText(Screen2Activity.this, intent.getSerializableExtra("GREETING").toString(), Toast.LENGTH_LONG).show();
        }
    }

}
