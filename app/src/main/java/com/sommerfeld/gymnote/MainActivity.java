package com.sommerfeld.gymnote;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sommerfeld.gymnote.models.Completed;
import com.sommerfeld.gymnote.persistence.CompletedRepo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainAct";
    TextView total_workout;
    TextView last_workout;
    private ArrayList<Completed> mCompleteds;
    private CompletedRepo mCompletedRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");

        mCompleteds = new ArrayList<Completed>();
        mCompletedRepo = new CompletedRepo(this);

        //Set up variables
        BottomNavigationView bnw = findViewById(R.id.bottomNavViewBar);
        total_workout = findViewById(R.id.tv_main_workout_total);
        last_workout = findViewById(R.id.tv_main_last_workout);

        retrieveLog();

        //Set OnClickListener to bottom toolbar
        //This will switch the clicked menu item and then will intent the respective activity
        bnw.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()) {
                    case R.id.ic_new_plan:
                        Intent intent_newPlan = new Intent(MainActivity.this, a_workout_overview.class);
                        startActivity(intent_newPlan);
                        break;
                    case R.id.ic_workout:
                        Intent intent_log = new Intent(MainActivity.this, CompletedLog.class);
                        startActivity(intent_log);

                        break;
                    case R.id.ic_analysis:
                        Intent intent_analysis = new Intent(MainActivity.this, graphics.class);
                        startActivity(intent_analysis);
                        break;

                }
                return false;
            }
        });
    }

    private void retrieveLog() {
        mCompletedRepo.retrieveCompletedTask().observe(this, new Observer<List<Completed>>() {
            @Override
            public void onChanged(List<Completed> completeds) {

                if (mCompleteds.size() > 0) {
                    mCompleteds.clear();
                }
                if (completeds != null) {
                    mCompleteds.addAll(completeds);
                    String size = String.valueOf(mCompleteds.size());
                    total_workout.setText(size);
                    String lastWorkout = mCompleteds.get((mCompleteds.size() - 1)).getTitle();
                    last_workout.setText(lastWorkout);
                }
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_Order:

        }
        return super.onOptionsItemSelected(item);
    }
}
