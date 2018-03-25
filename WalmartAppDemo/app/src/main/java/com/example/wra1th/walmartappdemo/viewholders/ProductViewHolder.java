package com.example.wra1th.walmartappdemo.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.wra1th.walmartappdemo.R;
import com.example.wra1th.walmartappdemo.models.Product;

/**
 * Created by wra1th on 3/23/2018.
 */

public class ProductViewHolder extends RecyclerView.ViewHolder {

    TextView textViewTitle;
    TextView textViewDesc;

    public ProductViewHolder(View itemView) {
        super(itemView);
        bindViews(itemView);
    }

    private void bindViews(View itemView) {
        textViewTitle = itemView.findViewById(R.id.tv_product_title);
        textViewDesc  = itemView.findViewById(R.id.tv_product_desc);
    }

    public void bindProductItems(Context context, Product product) {
        textViewTitle.setText(product.getName());
        textViewDesc.setText(product.getDescription());
    }
}
