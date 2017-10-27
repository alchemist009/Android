package com.example.wra1th.customphonebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import classes.Contact;
import classes.FileHandler;

public class MainActivity extends AppCompatActivity {

    ListView contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = (ListView) findViewById(R.id.contact_list);
        try {
            FileHandler.createFileIfNotPresent(MainActivity.this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        List<Contact> list;
        try {
            list = FileHandler.readContacts(MainActivity.this);
        } catch (IOException e) {
            e.printStackTrace();
            list = new ArrayList<>();
        }
        ArrayAdapter<Contact> contactArrayAdapter = new ArrayAdapter<>(
                MainActivity.this, android.R.layout.simple_list_item_1, list);
        contactList.setAdapter(contactArrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_contact) {
            Intent intent = new Intent(MainActivity.this, ContactDetailsActivity.class);
            intent.putExtra(ContactDetailsActivity.OPERATION, ContactDetailsActivity.ADD_OPERATION);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
