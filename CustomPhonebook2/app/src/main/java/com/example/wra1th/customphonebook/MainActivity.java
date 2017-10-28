package com.example.wra1th.customphonebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import classes.Contact;
import classes.FileHandler;

public class MainActivity extends AppCompatActivity implements Serializable {

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

            contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Contact contact = (Contact) contactList.getItemAtPosition(position);
                    Intent intent = new Intent(MainActivity.this, ContactDetailsActivity.class);
                    intent.putExtra(ContactDetailsActivity.OPERATION, ContactDetailsActivity.MODIFY_OPERATION);
                    intent.putExtra(ContactDetailsActivity.CONTACT, contact);
                    startActivity(intent);
                }
            });

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