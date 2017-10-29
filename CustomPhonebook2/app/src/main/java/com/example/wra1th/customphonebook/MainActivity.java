/**
 * Custom Contact Manager app for  CS6326.001, Assignment 4
 *
 * @authors: Koulick Sankar Paul, Gunjan Tomer
 *
 * NetIDs: ksp160330, gxt160930
 *
 * version: 1.3: 10/28/2017
 *
 * This app serves as a rudimentary replacement for the regular contact manager app found on android phones.
 * The app uses two activities. The first activity is the main screen displaying the list of contacts read from
 * a text file stored in internal memory. On the top of the screen, in the action bar an icon is present to allow
 * addition of a new contact. If no contacts have previously been added a new file is created when the
 * activity is launched. On this activity screen the user has two options: either tap the add icon in the action bar
 * or select one of the existing contacts and modify or delete it.
 *
 * Tapping the add icon takes the user to the next activity, ContactDetailsActivity which shows four fields for the
 * contact details like FirstName, LastName, PhoneNumber and EmailID along with the Save button. The user must enter
 * at least the FirstName before being allowed to save. Once Save is clicked, the Contact object is passed to
 * writeContact, sorted in alphabetical order along with the existing contacts and then written to the text file.
 *
 * Tapping an item in the listView takes the user to the same activity as the Save action but with the fields populated
 * with the details from the clicked list item and the Modify and Delete buttons. The underlying functionality to add,
 * delete and modify contacts is defined in FileHandler.java.
 *
 * The user can exit the activity and therefore the app anytime by pressing the back key in the android navigation bar.
 *
 */




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

    /**
     * @author: Koulick Sankar Paul
     *
     * onCreate method displays list of contacts on main activity screen
     *
     * @param savedInstanceState
     */

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


    /**
     * @author: Koulick Sankar Paul
     *
     * Method to handle a click on one of the contacts in the list
     */
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

    /**
     * @author: Gunjan Tomer
     *
     * Method to create the operations available in the action bar
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @author: Gunjan Tomer
     *
     * Method to perform add contact operation when add icon clicked in the action bar
     * @param item
     * @return
     */
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