package com.notifica.carpoolnepal;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CarpoolAdapter extends RecyclerView.Adapter<CarpoolAdapter.CarpoolViewHolder> implements Listeners.CardSelectionListener {
    List<Carpool> mCarpoolList;
    Listeners.CarpoolSelectionListener mListener;

    public CarpoolAdapter(List<Carpool> carpools, Listeners.CarpoolSelectionListener listener){
        mCarpoolList = carpools;
        mListener = listener;
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

        return new CarpoolViewHolder(itemView, this);
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

    @Override
    public void onSelect(int position) {
        mListener.onSelect(mCarpoolList.get(position));
    }

    public static class CarpoolViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView vTime;
        protected TextView vSourceDestination;
        protected TextView vDescription;
        protected TextView vOriginalPoster;
        private Listeners.CardSelectionListener mListener;

        public CarpoolViewHolder(View v, Listeners.CardSelectionListener listener) {
            super(v);
            vTime = (TextView)v.findViewById(R.id.carpool_time);
            vSourceDestination = (TextView)v.findViewById(R.id.carpool_source_destination);
            vDescription = (TextView)v.findViewById(R.id.carpool_description);
            vOriginalPoster = (TextView)v.findViewById(R.id.carpool_original_poster);
            CardView cardView = (CardView)v.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);

            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.v("yay item was clicked - ", String.valueOf(position));
            mListener.onSelect(position);
        }
    }
}
