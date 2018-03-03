package com.example.wra1th.recyclerviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.animation.AlphaAnimation;

import com.example.wra1th.recyclerviewdemo.adapters.UserRecyclerAdapter;
import com.example.wra1th.recyclerviewdemo.models.User;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

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

        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(userRecyclerAdapter);

        recyclerView.setAdapter(animationAdapter);
    }

    private void prepareRecyclerView() {
        //For vertical linear layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //For horizontal linear layout
        //linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        //For grid view in linear layout
        //linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        //For staggered layout
        //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        //recyclerView.setLayoutManager(staggeredGridLayoutManager);

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
