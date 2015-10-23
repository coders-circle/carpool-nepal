package com.notifica.carpoolnepal;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Ankit on 10/19/2015.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.NavigationItemViewHolder> {
    private Context context;
    private ArrayList<NavigationDrawerItem> navDrawerItems;

    private static final int HEADER = 0;
    private static final int ROW = 1;

    private int selectedItem = 0;

    public void setSelectedItem(int item) {
        if (item>=0)
            selectedItem = item;
    }
    public int getSelectedItem() {
        return selectedItem;
    }


    public NavigationDrawerAdapter(Context context, ArrayList<NavigationDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getItemCount() {
        // +1 for header
        return navDrawerItems.size()+1;
    }

    @Override
    public int getItemViewType(int position){
        if(position == 0)
            return HEADER;
        return ROW;
    }

    @Override
    public NavigationItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(viewType==HEADER?R.layout.drawer_header:R.layout.drawer_list_item, viewGroup, false);
        return new NavigationDrawerAdapter.NavigationItemViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(NavigationItemViewHolder viewHolder, int i) {
        if (viewHolder.viewType == HEADER) {
            Log.v("Drawer Adapter", "header");
        }
        else{
            Log.v("Drawer Adapter", "header");
            NavigationDrawerItem drawerItem = navDrawerItems.get(i-1);
            viewHolder.vTitle.setText(drawerItem.getTitle());
            viewHolder.vIcon.setImageResource(drawerItem.getIcon());
            if(drawerItem.getCounterVisibility()){
                viewHolder.vCounter.setVisibility(View.GONE);
            }
            if(selectedItem == i-1) {
                viewHolder.vCard.setCardBackgroundColor(context.getResources().getColor(R.color.primary_light));
            }
            else{
                viewHolder.vCard.setCardBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));
            }
        }

    }



    public static class NavigationItemViewHolder extends RecyclerView.ViewHolder{
        protected ImageView vIcon;
        protected TextView vTitle;
        protected TextView vCounter;
        protected CardView vCard;
        protected int viewType;
        public NavigationItemViewHolder(View v, int viewType) {
            super(v);
            this.viewType = viewType;
            if(this.viewType == 0) {
                // header
            }
            else {
                // list item
                vIcon = (ImageView) v.findViewById(R.id.drawer_list_icon);
                vTitle = (TextView) v.findViewById(R.id.drawer_list_title);
                vCounter = (TextView) v.findViewById(R.id.drawer_list_counter);
                vCard = (CardView)v.findViewById(R.id.drawer_list_card);
                vCard.setCardElevation(0.0f);
            }
        }
    }
}
