package com.example.wra1th.librarydemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.content.Context;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    ImageView mImageView;
    Context mContext;
    ListView listView;
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.iv_main);
        mContext = getApplicationContext();

        Picasso.with(mContext)
                .load("https://i.imgur.com/eWtfMME.png")
                .into(mImageView);

    }
}


