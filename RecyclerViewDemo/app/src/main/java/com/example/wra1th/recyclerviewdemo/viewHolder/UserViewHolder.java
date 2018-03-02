package com.example.wra1th.recyclerviewdemo.viewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wra1th.recyclerviewdemo.R;
import com.example.wra1th.recyclerviewdemo.models.User;
import com.squareup.picasso.Picasso;

/**
 * Created by wra1th on 3/2/2018.
 */

public class UserViewHolder extends RecyclerView.ViewHolder {

    /** Android Views **/
    ImageView usrImg;
    TextView usrName;
    TextView usrPhone;
    TextView usrEmail;
    /** Android Views **/

    /**
     * Binds XML views
     * Call this function after setContentView() in onCreate().
     **/
    private void bindViews(View rootView){
        usrImg = (ImageView) rootView.findViewById(R.id.usr_img);
        usrName = (TextView) rootView.findViewById(R.id.usr_name);
        usrPhone = (TextView) rootView.findViewById(R.id.usr_phone);
        usrEmail = (TextView) rootView.findViewById(R.id.usr_email);
    }

    public UserViewHolder(View itemView) {
        super(itemView);
        bindViews(itemView);
    }

    public void bindUser(Context context, User currentUser) {
        //this method will put user's data in appropriate views

        usrName.setText(currentUser.getName());
        usrEmail.setText(currentUser.getEmail());
        usrPhone.setText(currentUser.getPhone());
        Picasso.with(context).load(currentUser.getPhotoUrl()).into(usrImg);
    }
}
