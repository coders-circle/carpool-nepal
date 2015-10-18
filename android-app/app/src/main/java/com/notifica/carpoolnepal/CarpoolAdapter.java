package com.notifica.carpoolnepal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ankit on 10/18/2015.
 */
public class CarpoolAdapter extends RecyclerView.Adapter<CarpoolAdapter.CarpoolViewHolder>{
    List<Carpool> mCarpoolList;
    public CarpoolAdapter(List<Carpool> carpools){
        mCarpoolList = carpools;
    }
    @Override
    public int getItemCount() {
        return mCarpoolList.size();
    }
    @Override
    public CarpoolViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new CarpoolViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(CarpoolViewHolder carpoolViewHolder, int i) {
        Carpool cp = mCarpoolList.get(i);
        carpoolViewHolder.vTime.setText(CarpoolHandler.LongToDate(cp.date));
        carpoolViewHolder.vSource.setText(cp.source);
        carpoolViewHolder.vDestination.setText(cp.destination);

    }
    public static class CarpoolViewHolder extends RecyclerView.ViewHolder{
        protected TextView vTime;
        protected TextView vSource;
        protected TextView vDestination;
        public CarpoolViewHolder(View v) {
            super(v);
            vTime = (TextView)v.findViewById(R.id.carpool_time);
            vSource = (TextView)v.findViewById(R.id.carpool_source);
            vDestination = (TextView)v.findViewById(R.id.carpool_destination);
        }
    }
}
