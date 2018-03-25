package com.example.wra1th.walmartappdemo.viewHolderAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wra1th.walmartappdemo.R;
import com.example.wra1th.walmartappdemo.models.Product;
import com.example.wra1th.walmartappdemo.viewholders.ProductViewHolder;

import java.util.List;

/**
 * Created by wra1th on 3/23/2018.
 */

public class ProductRVAdapter extends RecyclerView.Adapter {

    Context context;
    List<Product> productList;

    public ProductRVAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.layout_product_row, parent, false);

        ProductViewHolder productViewHolder = new ProductViewHolder(rowView);

        return productViewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProductViewHolder productViewHolder = (ProductViewHolder) holder;

        Product product = productList.get(position);

        productViewHolder.bindProductItems(context, product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
