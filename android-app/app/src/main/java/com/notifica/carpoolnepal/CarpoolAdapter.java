package com.notifica.carpoolnepal;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        carpoolViewHolder.vTime.setText(CarpoolHandler.LongToTime12(cp.time)
                + ", "
                + CarpoolHandler.LongDateToDayOfWeek(cp.date));
        carpoolViewHolder.vSourceDestination.setText(cp.source + " to " + cp.destination);
        carpoolViewHolder.vDescription.setText(cp.description);
        carpoolViewHolder.vOriginalPoster.setText(cp.poster.firstName + " " + cp.poster.lastName);

    }
    public static class CarpoolViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView vTime;
        protected TextView vSourceDestination;
        protected TextView vDescription;
        protected TextView vOriginalPoster;
        public CarpoolViewHolder(View v) {
            super(v);
            vTime = (TextView)v.findViewById(R.id.carpool_time);
            vSourceDestination = (TextView)v.findViewById(R.id.carpool_source_destination);
            vDescription = (TextView)v.findViewById(R.id.carpool_description);
            vOriginalPoster = (TextView)v.findViewById(R.id.carpool_original_poster);
            CardView cardView = (CardView)v.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.v("yay item was clicked - ", String.valueOf(position));
        }
    }
}
