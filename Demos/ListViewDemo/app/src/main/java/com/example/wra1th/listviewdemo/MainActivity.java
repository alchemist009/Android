package com.example.wra1th.listviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.lv_main);

        String[] places = {"Dublin", "Oslo", "Amsterdam", "Paris", "Berlin", "Prague", "Madrid", "Rome", "Brussels", "Vladivostok", "Zurich", "Weimar", "Venice", "Vienna", "Hague"};

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, places );

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String item = adapter.getItem(i);
                Toast.makeText(getApplicationContext(), item + " was clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
