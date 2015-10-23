package com.notifica.carpoolnepal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class CarpoolsFragment extends Fragment implements Listeners.CarpoolSelectionListener {
    private RecyclerView.Adapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private int mType; // requests or offers

    public int mLastSelected = 0;

    private void refreshData() {

        // We can't just create new list since adapter already has a reference to the original list
        // Instead replace all items in the mCarpoolList by new list
        getHome().refreshData();
        changeList();
    }

    public void changeList() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);

        if (((mType == 0 && getHome().mOffersList.size() > 0) || (mType == 1 && getHome().mRequestsList.size() > 0))
                && ((HomeFragment)getParentFragment()).multipane)
            onSelect(mLastSelected);
    }



    private void getData(){
        // First download all carpools from server
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");
        CarpoolHandler handler = new CarpoolHandler(username, password);

        // Pass in location instead of null to download carpools by location
        handler.getCarpools(null, new Callback() {
            @Override
            public void onComplete(String response) {
                refreshData();
            }
        });
    }

    public HomeFragment getHome() {
        return (HomeFragment)getParentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_carpools, container, false);
        RecyclerView recyclerView;
        RecyclerView.LayoutManager layoutManager;

        recyclerView = (RecyclerView)rootView.findViewById(R.id.carpool_requests_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);

        Bundle args = getArguments();
        mType = args.getInt("type");

        if (mType == 0)
            mAdapter = new CarpoolAdapter(getHome().mOffersList, this);
        else
            mAdapter = new CarpoolAdapter(getHome().mRequestsList, this);

        recyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        refreshData();
        getData();

        return rootView;
    }

    @Override
    public void onSelect(int position) {
        if (mType == 0)
            getHome().showDetails(getHome().mOffersList.get(position));
        else
            getHome().showDetails(getHome().mRequestsList.get(position));

        mLastSelected = position;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            refreshData();
            Log.d("Carpool list visible", mType+"");
        }
    }
}
