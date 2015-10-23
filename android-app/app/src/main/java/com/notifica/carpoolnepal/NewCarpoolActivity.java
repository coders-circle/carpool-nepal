package com.notifica.carpoolnepal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class NewCarpoolActivity extends AppCompatActivity {
    public static HomeFragment homeFragment;
    public static int carpoolType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_carpool);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.hideOverflowMenu();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        if (carpoolType == 0)
            setTitle("#offer");
        else
            setTitle("#ask");


        final EditText source = (EditText) findViewById(R.id.edit_source);
        final EditText destination = (EditText) findViewById(R.id.edit_destination);
        final EditText seats = (EditText) findViewById(R.id.edit_number_of_people);
        final EditText date = (EditText) findViewById(R.id.edit_date);
        final EditText time = (EditText) findViewById(R.id.edit_time);
        final EditText description = (EditText) findViewById(R.id.edit_description);

        date.setText(CarpoolHandler.LongToDate(new Date().getTime()));

        Button postButton = (Button)findViewById(R.id.button_post_carpool);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Carpool carpool = new Carpool();

                carpool.source = source.getText().toString();
                if (carpool.source.equals("")) {
                    source.setError("You must enter this");
                    source.requestFocus();
                    return;
                }

                carpool.destination = destination.getText().toString();
                if (carpool.destination.equals("")) {
                    destination.setError("You must enter this");
                    destination.requestFocus();
                    return;
                }

                try {
                    carpool.seats = Integer.parseInt(seats.getText().toString());
                    if (carpool.seats == 0)
                        throw new Exception("Invalid number of seats");
                }
                catch (Exception e) {
                    seats.setError("Invalid number of seats");
                    seats.requestFocus();
                    return;
                }

                carpool.description = description.getText().toString();
                if (carpool.description.equals("")) {
                    description.setError("You must enter this");
                    description.requestFocus();
                    return;
                }

                try {
                    if (date.getText().toString().equals(""))
                        throw new Exception();
                    carpool.date = CarpoolHandler.DateToLong(date.getText().toString());
                }
                catch (Exception e) {
                    date.setError("Invalid date");
                    date.requestFocus();
                    return;
                }

                try {
                    if (time.getText().toString().equals(""))
                        throw new Exception();
                    carpool.time = CarpoolHandler.TimeToLong(time.getText().toString());
                }
                catch (Exception e) {
                    time.setError("Invalid time");
                    time.requestFocus();
                    return;
                }

                carpool.type = carpoolType;

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(NewCarpoolActivity.this);
                String username = preferences.getString("username", "");
                String password = preferences.getString("password", "");
                CarpoolHandler handler = new CarpoolHandler(username, password);

                handler.postCarpool(carpool, new Callback() {
                    @Override
                    public void onComplete(String response) {
                        if (response.equals("Success")) {
                            homeFragment.refreshData();
                            Toast.makeText(NewCarpoolActivity.this, "Posted", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            if (response.equals(""))
                                response = "Error while trying to post carpool";
                            Toast.makeText(NewCarpoolActivity.this, response, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
