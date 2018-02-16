package com.example.wra1th.firstapp;

import android.content.Intent;
import android.net.sip.SipSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button NextButton;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("FirstScreen:", "I'm  onCreate()");
        bindViews();
    }

    private void bindViews() {
        NextButton = (Button) findViewById(R.id.NextButton);
        tv = findViewById(R.id.TextBox);
        assignClick();
    }

    private void assignClick() {
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "I'll show you now!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, Screen2Activity.class);
                if(tv.getText().length() == 0){
                    i.putExtra("FLAG", false);
                    Toast.makeText(MainActivity.this, "Are you sure you're ready for the next screen?", Toast.LENGTH_LONG).show();
                }
                else {
                    i.putExtra("GREETING", tv.getText().toString());
                    i.putExtra("FLAG", true);
                }
                startActivity(i);
            }
        };
        NextButton.setOnClickListener(l);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("FirstScreen", "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("FirstScreen", "I'm on Resume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("FirstScreen", "I'm on Pause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("FirstScreen", "I'm on Destroy()");
    }


}
