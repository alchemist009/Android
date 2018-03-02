package com.example.wra1th.recyclerviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.wra1th.recyclerviewdemo.adapters.UserRecyclerAdapter;
import com.example.wra1th.recyclerviewdemo.models.User;

import java.util.List;

public class UserRecyclerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    UserRecyclerAdapter userRecyclerAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        bindViews();
        prepareAdapter();
        prepareRecyclerView();
        attachRVandAdapter();
    }

    private void attachRVandAdapter() {

        recyclerView.setAdapter(userRecyclerAdapter);
    }

    private void prepareRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void prepareAdapter() {
        //this method will get the data and create new adapter using that data

        List<User> users = User.getDummyUserList();

        userRecyclerAdapter = new UserRecyclerAdapter(users, this);
    }

    private void bindViews() {

        recyclerView = findViewById(R.id.recycler_view);
    }
}
