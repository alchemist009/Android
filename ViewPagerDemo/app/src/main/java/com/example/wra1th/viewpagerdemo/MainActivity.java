package com.example.wra1th.viewpagerdemo;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        attachViewPagerAdapter();
        attachTabLayoutAndViewPager();
        setupTabIcons();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setText("Tab 1");
        tabLayout.getTabAt(1).setText("Tab 2");
        tabLayout.getTabAt(2).setText("Tab 3");
    }

    /**
     *
     * This method will attach tab layout and our working view pager
     */

    private void attachTabLayoutAndViewPager() {
        tabLayout.setupWithViewPager(viewPager);
    }


    /**
     * create a new object of myPagerAdapter class and attach it with the view pager
     */
    private void attachViewPagerAdapter() {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(myPagerAdapter);
    }

    private void bindViews() {
        viewPager = findViewById(R.id.vpager);
        tabLayout = findViewById(R.id.tab_layout);
    }

    //custom adapter code

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0:
                    FirstFragment firstFragment  = new FirstFragment();
                    return firstFragment;

                case 1:
                    SecondFragment secondFragment = new SecondFragment();
                    return secondFragment;

                case 2:
                    ThirdFragment thirdFragment = new ThirdFragment();
                    return thirdFragment;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Page 1";

                case 1:
                    return "Page 2";

                case 2:
                    return "Page 3";

                default:
                    return null;

            }
        }
    }
}
