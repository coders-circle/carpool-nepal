package com.notifica.carpoolnepal;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class CarpoolAdapter extends RecyclerView.Adapter<CarpoolAdapter.CarpoolViewHolder> {
    List<Carpool> mCarpoolList;
    Listeners.CarpoolSelectionListener mListener;
    CarpoolsFragment mFragment;

    public CarpoolAdapter(List<Carpool> carpools, CarpoolsFragment fragment){
        mCarpoolList = carpools;
        mListener = fragment;
        mFragment = fragment;
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

        String time = CarpoolHandler.LongToTime12(cp.time)
                + ", "
                + CarpoolHandler.LongDateToDayOfWeek(cp.date);
        carpoolViewHolder.vTime.setText(time);

        String sd = cp.source + " to " + cp.destination;
        carpoolViewHolder.vSourceDestination.setText(sd);
        carpoolViewHolder.vDescription.setText(cp.description);

        String poster = cp.poster.firstName + " " + cp.poster.lastName;
        carpoolViewHolder.vOriginalPoster.setText(poster);

        if (mFragment.getHome().multipane) {
            carpoolViewHolder.cardView.setSelected(lastSelected == i);
            if (lastSelected == i)
                lastContainer = carpoolViewHolder.container;
        }
    }

    private int lastSelected = 0;
    private RelativeLayout lastContainer;
    public class CarpoolViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView vTime;
        protected TextView vSourceDestination;
        protected TextView vDescription;
        protected TextView vOriginalPoster;
        protected CardView cardView;
        protected RelativeLayout container;

        public CarpoolViewHolder(View v) {
            super(v);
            vTime = (TextView)v.findViewById(R.id.carpool_time);
            vSourceDestination = (TextView)v.findViewById(R.id.carpool_source_destination);
            vDescription = (TextView)v.findViewById(R.id.carpool_description);
            vOriginalPoster = (TextView)v.findViewById(R.id.carpool_original_poster);
            cardView = (CardView)v.findViewById(R.id.card_view);
            container = (RelativeLayout)v.findViewById(R.id.container);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (mFragment.getHome().multipane) {
                container.setSelected(true);
                lastSelected = position;
                if (lastContainer != null && lastContainer != container)
                    lastContainer.setSelected(false);
                lastContainer = container;
            }
            mListener.onSelect(position);
        }
    }
}
