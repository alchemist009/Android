package com.example.wra1th.funwork1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    Intent intent;
    TextView fname, lname, email, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        bindViews();
        intent = getIntent();
        fname.setText("First name: " + intent.getSerializableExtra("FIRSTNAME").toString());
        lname.setText("Last name: " + intent.getSerializableExtra("LASTNAME").toString());
        email.setText("Email: " + intent.getSerializableExtra("EMAIL").toString());
        if(intent.getBooleanExtra("GENDER", true)){
            gender.setText("Gender: Male");
        }
        else
            gender.setText("Gender: Female");
    }

    public void bindViews() {

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        email = findViewById(R.id.email);
        gender = findViewById(R.id.gender);
    }
}
