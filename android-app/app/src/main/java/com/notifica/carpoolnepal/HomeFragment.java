package com.notifica.carpoolnepal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {

    static final int NUM_ITEMS = 2;
    private final static String[] tabs = { "Offers", "Requests"};
    public boolean multipane = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CarpoolDetailFragment displayFrag = (CarpoolDetailFragment) getChildFragmentManager().findFragmentById(R.id.fragment_details);
        multipane = !(displayFrag == null || displayFrag.getActivity() == null);
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

    public static class TabsPagerAdapter extends FragmentStatePagerAdapter {
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
    }
}
