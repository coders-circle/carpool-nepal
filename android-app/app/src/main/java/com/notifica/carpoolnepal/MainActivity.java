package com.notifica.carpoolnepal;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter mDrawerAdapter;
    private RecyclerView mDrawerRecyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    private String navMenuTitles[] = {"Home", "My Requests", "My Offers"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        mDrawerRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_slider);

        ArrayList<NavigationDrawerItem> navDrawerItems = new ArrayList<>();

        // adding nav drawer items to array
        navDrawerItems.add(new NavigationDrawerItem("Home", android.R.drawable.ic_menu_today));
        navDrawerItems.add(new NavigationDrawerItem("My Requests", android.R.drawable.ic_menu_today));
        navDrawerItems.add(new NavigationDrawerItem("My Offers", android.R.drawable.ic_menu_today));


        // setting the nav drawer list adapter
        mDrawerAdapter = new NavigationDrawerAdapter(getApplicationContext(), navDrawerItems);
        mDrawerRecyclerView.setAdapter(mDrawerAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mDrawerRecyclerView.setLayoutManager(layoutManager);
        mDrawerRecyclerView.setHasFixedSize(true);

        // enabling action bar app icon and behaving it as toggle button
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.hideOverflowMenu();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        );

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }

        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        mDrawerRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    int position = recyclerView.getChildAdapterPosition(child) - 1;
                    if (position >= 0)
                        displayView(position);
                    return true;
                }
                return false;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }
    }

    public String searchLocation = "";
    public void search(String location) {
        Log.d("Search string", location);
        searchLocation = location;
        if (mHomeFragment != null)
            mHomeFragment.refreshData();
        displayView(0);
    }

    public void newCarpool(int type) {
        NewCarpoolActivity.carpoolType = type;
        NewCarpoolActivity.homeFragment = mHomeFragment;
        Intent intent = new Intent(this, NewCarpoolActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        if (title.equals(navMenuTitles[0]) && searchLocation != "")
            title = "Search: " + searchLocation;

        mTitle = title;
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private HomeFragment mHomeFragment = new HomeFragment();
    private MyOffersFragment mMyOffersFragment = new MyOffersFragment();
    private MyRequestsFragment mMyRequestFragment = new MyRequestsFragment();

    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = mHomeFragment;
                break;
            case 1:
                fragment = mMyRequestFragment;
                break;
            case 2:
                fragment = mMyOffersFragment;
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_content, fragment).commitAllowingStateLoss();

            // update selected item and title, then close the drawer
            //mDrawerRecyclerView.setItemChecked(position, true);
            mDrawerAdapter.setSelectedItem(position);
            mDrawerAdapter.notifyDataSetChanged();
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerRecyclerView);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }
}
