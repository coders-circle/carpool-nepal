package com.notifica.carpoolnepal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 10/18/2015.
 */
public class RequestsFragment extends Fragment {
    private List<Carpool> testList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        initData();
    }

    private void initData(){
        testList = new ArrayList<Carpool>();
        Carpool cp = new Carpool();
        cp.source = "Imadol";
        cp.destination = "Pulchowk";
        testList.add(cp);
        testList.add(cp);
        testList.add(cp);
        testList.add(cp);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_requests, container, false);
        RecyclerView recyclerView;
        RecyclerView.Adapter adapter;
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
