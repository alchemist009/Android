package com.example.wra1th.recyclerviewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnUsers;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        assignClicks();
    }

    private void assignClicks() {

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, UserRecyclerActivity.class);
                startActivity(intent);
            }
        };
        btnUsers.setOnClickListener(listener);
    }

    private void bindView() {

        btnUsers = (Button) findViewById(R.id.btn_users);
    }
}
