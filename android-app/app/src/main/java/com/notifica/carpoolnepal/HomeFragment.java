package com.notifica.carpoolnepal;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ankit on 10/18/2015.
 */
public class HomeFragment extends Fragment {

    static final int NUM_ITEMS = 2;
    private String[] tabs = { "Offers", "Requests"};
    private TabsPagerAdapter mAdapter;
    private ViewPager mPager;
    private ActionBar mActionBar;

    private static boolean tabsAdded = false;

    private FragmentActivity mContext;

    @Override
    public void onAttach(Activity activity) {
        mContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mAdapter = new TabsPagerAdapter(mContext.getSupportFragmentManager());

        mPager = (ViewPager)rootView.findViewById(R.id.pager_home);
        mPager.setAdapter(mAdapter);

        mActionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        mActionBar.setHomeButtonEnabled(false);
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        mPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        mActionBar.setSelectedNavigationItem(position);
                    }
                });


        if(!tabsAdded) {
            for (String tab_name : tabs) {
                mActionBar.addTab(mActionBar.newTab().setText(tab_name)
                        .setTabListener(tabListener));
            }
            tabsAdded = true;
        }
        return rootView;
    }

    public static class TabsPagerAdapter extends FragmentPagerAdapter {
        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new RequestsFragment();
                case 1:
                    return new OffersFragment();
            }

            return null;
        }
    }
}
