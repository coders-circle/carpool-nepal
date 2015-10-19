package com.notifica.carpoolnepal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class CarpoolsFragment extends Fragment {
    private List<Carpool> mCarpoolList =  new ArrayList<>(); // Initialize by empty list
    private RecyclerView.Adapter mAdapter = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private int mType; // requests or offers

    private void refreshData() {

        // Now get all carpools as list
        List<Carpool> list = Carpool.find(Carpool.class, "type=" + mType); // 1 == request, 0 == offer
        // To filter list by location, use carpool.source and carpool.destination

        // We can't just create new list since adapter already has a reference to the original list
        // Instead replace all items in the mCarpoolList by new list
        mCarpoolList.clear();
        for (Carpool c: list)
            mCarpoolList.add(c);

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
    }

    private void getData(){
        // First download all carpools from server
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");
        CarpoolHandler handler = new CarpoolHandler(username, password);

        // Pass in location instead of null to download carpools by location
        handler.GetCarpools(null, new Callback() {
            @Override
            public void onComplete(String response) {
                refreshData();
            }
        });
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

        // specify an adapter (see also next example)
        mAdapter = new CarpoolAdapter(mCarpoolList);
        recyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        Bundle args = getArguments();
        mType = args.getInt("type");

        refreshData();
        getData();

        return rootView;
    }
}
