package com.example.wra1th.recyclerviewdemo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wra1th.recyclerviewdemo.R;
import com.example.wra1th.recyclerviewdemo.models.User;
import com.example.wra1th.recyclerviewdemo.viewHolder.UserViewHolder;

import java.util.List;

/**
 * Created by wra1th on 3/2/2018.
 */

public class UserRecyclerAdapter extends RecyclerView.Adapter {

    List<User> users;
    Context context;
    String TAG = "UserRecycleAdapter";

    public UserRecyclerAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: I am creating a new Viewholder. Heavy operation.");

        View rowView = LayoutInflater.from(context).inflate(R.layout.user_details_file, parent, false);

        UserViewHolder userViewHolder = new UserViewHolder(rowView);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //This will bind user's data in ViewHolder
        //Now fetch the current user
        User currentUser = users.get(position);
        Log.d(TAG, "onBindViewHolder: I'm here to bind data of " + currentUser.getName());

        //now typecast holder to viewholder
        UserViewHolder userViewHolder = (UserViewHolder) holder;

        //now hand over the current user to the userViewHolder so that
        //it can set details of user in image view and text views (for name, phone and email)

        userViewHolder.bindUser(context,currentUser);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
