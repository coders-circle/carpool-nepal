package com.notifica.carpoolnepal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CarpoolDetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_carpool_detail, container, false);
    }

    public void setCarpool(Carpool carpool) {
        // User carpool to set view details here

        // E.g.:
        // TextView tv = (TextView)getActivity().findViewById(R.id.my_text_view);
        // tv.setText(carpool.poster.firstName);
    }
}
