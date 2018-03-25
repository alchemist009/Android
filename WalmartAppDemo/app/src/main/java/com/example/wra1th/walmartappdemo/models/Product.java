package com.example.wra1th.walmartappdemo.models;

/**
 * Created by wra1th on 3/23/2018.
 */

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Product {
    // TODO: 12/20/17 add (implementation 'com.google.code.gson:gson:2.2.4') in your build.gradle file
    //this url will give list of products
    public static String WALMART_PRODUCTS_URL = "https://demo1738991.mockable.io/walmart-products";
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description ")
    @Expose
    private String description;
    @SerializedName("price")
    @Expose
    private float price;
    @SerializedName("imageURL")
    @Expose
    private String imageURL;

    public static List<Product> getProductListFromJsonArray(String jsonProductArray) {
        List<Product> products = new ArrayList<>();

        //create gson object
        Gson gson = new Gson();

        //create list type
        Type productListType = new TypeToken<List<Product>>() {
        }.getType();

        //convert json to list
        try {
            products = gson.fromJson(jsonProductArray, productListType);
        } catch (Exception e) {
            Log.d("Product", "getProductListFromJsonArray: could not convert:" + jsonProductArray);
        }

        return products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
