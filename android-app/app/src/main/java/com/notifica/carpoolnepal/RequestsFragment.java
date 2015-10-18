package com.notifica.carpoolnepal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class RequestsFragment extends Fragment {
    private List<Carpool> testList =  new ArrayList<>(); // Initialize by empty list
    private RecyclerView.Adapter adapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize data set, this data would usually come from a local content provider or
        // remote server.
        initData();
    }

    private void initData(){
        // First download all carpools from server
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");
        CarpoolHandler handler = new CarpoolHandler(username, password);

        // Pass in location instead of null to download carpools by location
        handler.GetCarpools(null, new Callback() {
            @Override
            public void onComplete(String response) {

                // Now get all carpools as list
                List<Carpool> list = Carpool.find(Carpool.class, "type=1"); // 1 == request, 0 == offer
                // To filter list by location, use carpool.source and carpool.destination

                // We can't just create new testList since adapter already has a reference to the original testlist
                // Instead replace all items in the testList by new list
                testList.clear();
                for (Carpool c: list)
                    testList.add(c);

                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_requests, container, false);
        RecyclerView recyclerView;
        RecyclerView.LayoutManager layoutManager;

        recyclerView = (RecyclerView)rootView.findViewById(R.id.carpool_requests_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        adapter = new CarpoolAdapter(testList);
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}
