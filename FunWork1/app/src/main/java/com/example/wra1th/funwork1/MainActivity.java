package com.example.wra1th.funwork1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public Button button;
    EditText fname;
    EditText lname;
    EditText email;
    CheckBox accept;
    RadioButton mRadio;
    RadioButton fradio;
//    public static String FIRSTNAME = "FIRSTNAME";
//    public static String LASTNAME = "LASTNAME";
//    public static String EMAIL = "EMAIL";
//    public static String GENDER = "GENDER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
    }

    private void bindViews(){

        button = (Button) findViewById(R.id.submit);
        fname = findViewById(R.id.firstname);
        lname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        accept = findViewById(R.id.accept);
        mRadio = findViewById(R.id.radiomale);
        fradio = findViewById(R.id.radiofemale);
        submitInfo();
    }

    private void submitInfo() {

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                if(fname.getText().length() == 0)
                    Toast.makeText(MainActivity.this, "First name field cannot be left blank.", Toast.LENGTH_SHORT).show();
                else
                    if(!fname.getText().toString().matches("[a-zA-Z]+"))
                        Toast.makeText(MainActivity.this, "Only letters are allowed in first name.", Toast.LENGTH_SHORT).show();
                    else
                        if(lname.getText().length() == 0)
                            Toast.makeText(MainActivity.this, "Unless you are Bono, Pink or Drake a last name is required.", Toast.LENGTH_SHORT ).show();
                        else
                            if(!lname.getText().toString().matches("[a-zA-Z]+"))
                                Toast.makeText(MainActivity.this, "Only letters are allowed in last name.", Toast.LENGTH_SHORT).show();
                            else
                                if(email.getText().length() == 0)
                                    Toast.makeText(MainActivity.this, "Since you are on the Internet, it is very likely you have an email ID.", Toast.LENGTH_LONG).show();
                                else
                                    if(!email.getText().toString().matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$"))
                                        Toast.makeText(MainActivity.this, "Incorrect email format.", Toast.LENGTH_SHORT).show();
                                    else
                                        if(!mRadio.isChecked() && !fradio.isChecked())
                                            Toast.makeText(MainActivity.this, "Male or Female, please select one.", Toast.LENGTH_LONG).show();
                                        else
                                            if(!accept.isChecked())
                                                Toast.makeText(MainActivity.this, "You must accept the T & C.", Toast.LENGTH_SHORT).show();
                                            else
                                                if(fname.getText().length() > 0 && lname.getText().length() > 0 && email.getText().length() > 0 && (mRadio.isChecked() || fradio.isChecked()) && accept.isChecked()) {
                                                    intent.putExtra("FIRSTNAME", fname.getText().toString());
                                                    intent.putExtra("LASTNAME", lname.getText().toString());
                                                    intent.putExtra("EMAIL", email.getText().toString());
                                                    intent.putExtra("GENDER", mRadio.isChecked());
                                                    startActivity(intent);
                                                }
            }
        };
        button.setOnClickListener(listener);
    }


}
