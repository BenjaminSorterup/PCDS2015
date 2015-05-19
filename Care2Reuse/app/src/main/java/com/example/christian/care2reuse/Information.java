package com.example.christian.care2reuse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Information class is the container of the different info pages.
 * Depending on the user input, it creates an instance of the info page fragments.
 */
public class Information extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        //Initialises the viewpager
        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        //Initialises the pageadapter
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {
                //Creates instance of fragment depending on position in UI
                case 0: return care2reuse.newInstance();
                case 1: return innoaid.newInstance();
                case 2: return disclaimer.newInstance();
                default: return disclaimer.newInstance();
            }
        }

        @Override
        public int getCount() {
            //number of info pages/fragments
            return 3;
        }
    }
}