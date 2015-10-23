package com.notifica.carpoolnepal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    static final int NUM_ITEMS = 2;
    private final static String[] tabs = { "Offers", "Requests"};
    public boolean multipane = false;

    public List<Carpool> mOffersList =  new ArrayList<>(); // Initialize by empty list
    public List<Carpool> mRequestsList =  new ArrayList<>(); // Initialize by empty list

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TabsPagerAdapter adapter = new TabsPagerAdapter(getChildFragmentManager());
        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.pager_home);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        CarpoolDetailFragment displayFrag = (CarpoolDetailFragment) getChildFragmentManager().findFragmentById(R.id.fragment_details);
        multipane = !(displayFrag == null || displayFrag.getActivity() == null);

    }

    public void showDetails(Carpool carpool) {
        CarpoolDetailFragment displayFrag = (CarpoolDetailFragment) getChildFragmentManager().findFragmentById(R.id.fragment_details);
        if (displayFrag == null || displayFrag.getActivity() == null) {
            CarpoolDetailActivity.carpool = carpool;
            Intent intent = new Intent(getActivity(), CarpoolDetailActivity.class);
            startActivity(intent);
        } else {
            displayFrag.setCarpool(carpool);
        }
    }

    private CarpoolsFragment mCurrentFragment;

    public class TabsPagerAdapter extends FragmentStatePagerAdapter {
        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public Fragment getItem(int position) {
            if (position != 0 && position != 1)
                return null;

            // type: 0 == offers, 1 == requests
            Fragment fragment = new CarpoolsFragment();
            Bundle args = new Bundle();
            args.putInt("type", position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            //do nothing here! no call to super.restoreState(arg0, arg1);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (mCurrentFragment != object) {
                mCurrentFragment = (CarpoolsFragment) object;
            }
            super.setPrimaryItem(container, position, object);
        }
    }


    public void refreshData() {
        if (mCurrentFragment != null)
            mCurrentFragment.changeList();

        // List<Comment> list = Comment.find(Comment.class,"carpool=?",  new String[]{""+mCarpool.getId()}, null, "posted_on DESC", null);

        List<Carpool> olist = Carpool.find(Carpool.class, "type=0", new String[]{}, null, "posted_on DESC", null); // 0 == offer
        List<Carpool> rlist = Carpool.find(Carpool.class, "type=1", new String[]{}, null, "posted_on DESC", null); // 1 == request

        // We can't just create new list since adapter already has a reference to the original list
        // Instead replace all items in the mCarpoolList by new list
        mOffersList.clear();
        for (Carpool c: olist)
            mOffersList.add(c);

        mRequestsList.clear();
        for (Carpool c: rlist)
            mRequestsList.add(c);
    }
}
