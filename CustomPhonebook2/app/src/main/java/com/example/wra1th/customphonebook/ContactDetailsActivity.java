package com.example.wra1th.customphonebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;

import classes.Contact;
import classes.FileHandler;

public class ContactDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int ADD_OPERATION = 1;
    public static final int MODIFY_OPERATION = 2;
    public static final String OPERATION = "OPERATION";
    public static final String CONTACT = "CONTACT";

    EditText firstName, lastName, emailID, phoneNumber;
    Button saveContact, modifyContact, deleteContact;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        firstName = (EditText) findViewById(R.id.et_first_name);
        lastName = (EditText) findViewById(R.id.et_last_name);
        emailID = (EditText) findViewById(R.id.et_email_id);
        phoneNumber = (EditText) findViewById(R.id.et_phone_number);
        saveContact = (Button) findViewById(R.id.b_save);
        modifyContact = (Button) findViewById(R.id.b_modify);
        deleteContact = (Button) findViewById(R.id.b_delete);

        int operation = getIntent().getIntExtra(OPERATION, 0);
        if(operation == ADD_OPERATION) {
            saveContact.setVisibility(View.VISIBLE);
            modifyContact.setVisibility(View.GONE);
            deleteContact.setVisibility(View.GONE);
        } else if(operation == MODIFY_OPERATION) {
            Contact contact;
            saveContact.setVisibility(View.GONE);
            modifyContact.setVisibility(View.VISIBLE);
            deleteContact.setVisibility(View.VISIBLE);
            contact = (Contact) getIntent().getSerializableExtra(CONTACT);
            firstName.setText(contact.getFirstName());
            lastName.setText(contact.getLastName());
            phoneNumber.setText(contact.getPhoneNumber());
            emailID.setText(contact.getEmailID());
        }
    }

    @Override
    public void onBackPressed() {
        ContactDetailsActivity.this.finish();
    }

    @Override
    public void onClick(View view) {
        String message = "";
        switch (view.getId()) {
            case R.id.b_save:
            case R.id.b_modify:
                Contact c = new Contact(firstName.getText().toString(),
                        lastName.getText().toString(),
                        phoneNumber.getText().toString(),
                        emailID.getText().toString());
                try {
                    FileHandler.addContact(ContactDetailsActivity.this, c);
                    message = "Contact saved";
                } catch (IOException ex) {
                    ex.printStackTrace();
                    message = "Error saving contact";
                } finally {
                    Toast.makeText(ContactDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                }
                ContactDetailsActivity.this.finish();
                break;

            case R.id.b_delete:
                c = new Contact(firstName.getText().toString(),
                        lastName.getText().toString(),
                        phoneNumber.getText().toString(),
                        emailID.getText().toString());
                try {
                    FileHandler.deleteContact(ContactDetailsActivity.this, c);
                    message = "Contact deleted";
                } catch (IOException e) {
                    e.printStackTrace();
                    message = "Error deleting contact";
                } finally {
                    Toast.makeText(ContactDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                }
                ContactDetailsActivity.this.finish();
                break;
        }
    }
}
