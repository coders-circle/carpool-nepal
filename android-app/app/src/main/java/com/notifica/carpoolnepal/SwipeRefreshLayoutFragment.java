package com.notifica.carpoolnepal;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Ankit on 10/19/2015.
 */
public class SwipeRefreshLayoutFragment extends SwipeRefreshLayout {
    private ListView mListView;

    public SwipeRefreshLayoutFragment(Context context){
        super(context);
    }

    public SwipeRefreshLayoutFragment(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setListView(ListView view) {
        mListView = view;
    }

    @Override
    public boolean canChildScrollUp() {
        return mListView != null &&
                (mListView.getFirstVisiblePosition() > 0
                        || mListView.getChildAt(0) == null
                        || mListView.getChildAt(0).getTop() < 0);
    }

}
