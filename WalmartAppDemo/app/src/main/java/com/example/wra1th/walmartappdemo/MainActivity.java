package com.example.wra1th.walmartappdemo;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.wra1th.walmartappdemo.models.Product;
import com.example.wra1th.walmartappdemo.viewHolderAdapters.ProductRVAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        attachPagerAdapter();
        startDataFetch();
    }

    private void startDataFetch() {
        ProductDataFetchTask productDataFetchTask = new ProductDataFetchTask();

        productDataFetchTask.execute();
    }


    private void attachPagerAdapter() {
        //create new adapter object
        WalmartPagerAdapter walmartPagerAdapter = new WalmartPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(walmartPagerAdapter);
    }

    private void bindViews() {

        viewPager = findViewById(R.id.view_pager);
        recyclerView = findViewById(R.id.recycler_view);
    }

    private String getWebPageContent(String pageLink) {
        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;

        try {
            //this is just to see the loading during api fetch. Never include this in real apps.
            //it simply waits for 5 sec before doing anything.
            Thread.sleep(5000);
            URL urlObject = new URL(pageLink);
            connection = (HttpsURLConnection) urlObject.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(3000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(3000);
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();

            if (stream != null) {
                // Converts Stream to String.
                Reader reader = null;
                final int bufferSize = 1024;
                final char[] buffer = new char[bufferSize];
                final StringBuilder out = new StringBuilder();
                reader = new InputStreamReader(stream, "UTF-8");
                for (; ; ) {
                    int rsz = reader.read(buffer, 0, buffer.length);
                    if (rsz < 0)
                        break;
                    out.append(buffer, 0, rsz);
                }
                result = out.toString();
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }


    class ProductDataFetchTask extends AsyncTask<String, String, List<Product>> {

        @Override
        protected List<Product> doInBackground(String... strings) {
            String stringJsonArray = getWebPageContent(Product.WALMART_PRODUCTS_URL);

            List<Product> products = Product.getProductListFromJsonArray(stringJsonArray);

            return products;
        }

        @Override
        protected void onPostExecute(List<Product> products) {
            super.onPostExecute(products);
            if(products != null && products.size() > 0) {
                Log.d("AsyncTask", "OnPostExecute: Total size of products list is:" + products.size());
                loadProductListInRecyclerView(products);
            }
            else{
                Log.d("AsyncTask", "onPostExecute: no data received. Something went wrong." );
            }
        }
    }

    private void loadProductListInRecyclerView(List<Product> products) {
        //create new adapter
        ProductRVAdapter productRVAdapter = new ProductRVAdapter(this, products);

        //set layout manager to recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //finally bind them
        recyclerView.setAdapter(productRVAdapter);

    }

    class WalmartPagerAdapter extends FragmentPagerAdapter {

        public WalmartPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {

                case 0:
                    return new ProductOneFragment();

                case 1:
                    return new ProductTwoFragment();

                case 2:
                    return new ProductThreeFragment();

                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
