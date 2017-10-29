/**
 *
 * Details activity screeen for Custom Contacts app
 *
 * @authors: Gunjan Tomer, Kouluck Sankar Paul
 *
 * NetIDs: gxt160930, ksp160330
 *
 * This activity screen is launched when the user tries to add a new contact or modify an existing contact
 * The activity shows the four fields for user details i.e. FirstName, LastName, PhoneNumber and EmailID.
 * Based on whether the activity is launched using the add contact icon on the MainActivity screen or by
 * clicking on one of the contacts in the list either the Save button is displayed or the Modify-Delete buttons
 * are displayed.
 *
 * If the activity is launched by clicking an item in the list of contacts on the main screen, the contact's
 * details are used to populate the text boxes which can then be modified and saved.
 * Clicking on any of the buttons calls the corresponding method in FileHandler.java
 *
 * The user can exit the activity and go back to the contacts list activity without saving or modifying anything
 * at any time by pressing the back key in the android navigation bar.
 */

package com.example.wra1th.customphonebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import classes.Contact;
import classes.FileHandler;

public class ContactDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int ADD_OPERATION = 1;
    public static final int MODIFY_OPERATION = 2;
    public static final String OPERATION = "OPERATION";
    public static final String CONTACT = "CONTACT";

    EditText firstName, lastName, emailID, phoneNumber;
    Button saveContact, modifyContact, deleteContact;

    /**
     * @author: Gunjan Tomer
     *
     * Method to display the text boxes for contact details and buttons based on which operation is to be performed.
     *
     * @param savedInstanceState
     */
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
        }
        else if(operation == MODIFY_OPERATION) {
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

    /**
     * @authors: Koulick Sankar Paul, Gunjan Tomer
     *
     * Method to perform an operation based on the button clicked. Each operation calls the corresponding method in FileHandler.java.
     * Based on the success or failure of the requested operation a toast message is displayed.
     * To prevent addition of contacts with all fields empty, a toast message is displayed when the user tries to save or modifies a
     * contact without a FirstName.
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        String message = "";
        switch (view.getId()) {
            case R.id.b_save:
                if (firstName.getText().toString().isEmpty()) {
                    message = "Please add first name";
                    Toast.makeText(ContactDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                }
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

            case R.id.b_modify:
                if (firstName.getText().toString().isEmpty()) {
                    message = "Please add first name";
                    Toast.makeText(ContactDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                }
                c = new Contact(firstName.getText().toString(),
                        lastName.getText().toString(),
                        phoneNumber.getText().toString(),
                        emailID.getText().toString());
                try {
                    FileHandler.modifyContact(ContactDetailsActivity.this, c);
                    message = "Contact modified";
                } catch (IOException ex) {
                    ex.printStackTrace();
                    message = "Error modifying contact";
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