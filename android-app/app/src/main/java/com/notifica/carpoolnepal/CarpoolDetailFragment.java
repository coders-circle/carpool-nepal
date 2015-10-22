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

public class CarpoolDetailFragment extends Fragment {

    private List<Comment> mComments = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private Carpool mCarpool;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_carpool_detail, container, false);
    }

    private void refreshData() {

        // Now get all carpools as list
        List<Comment> list = Comment.find(Comment.class, "carpool=?", ""+mCarpool.getId());

        // We can't just create new list since adapter already has a reference to the original list
        // Instead replace all items in the mCarpoolList by new list
        mComments.clear();
        for (Comment c: list)
            mComments.add(c);

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void getData(){
        // First download all carpools from server
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");
        CarpoolHandler handler = new CarpoolHandler(username, password);

        // Pass in location instead of null to download carpools by location
        handler.getComments(mCarpool, new Callback() {
            @Override
            public void onComplete(String response) {
                refreshData();
            }
        });
    }

    public void setCarpool(Carpool carpool) {
        mCarpool = carpool;
        // User carpool to set view details here

        // E.g.:
        // TextView tv = (TextView)getActivity().findViewById(R.id.my_text_view);
        // tv.setText(carpool.poster.firstName);

        RecyclerView recyclerView;
        RecyclerView.LayoutManager layoutManager;

        recyclerView = (RecyclerView)getActivity().findViewById(R.id.comments_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new CommentAdapter(mComments);
        recyclerView.setAdapter(mAdapter);

        refreshData();
        getData();
    }
}
