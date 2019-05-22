package com.sommerfeld.gymnote;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sommerfeld.gymnote.models.Completed;
import com.sommerfeld.gymnote.persistence.CompletedRepo;

import java.util.ArrayList;
import java.util.List;

public class graphics extends AppCompatActivity {

    private static final String TAG = "graphics";
    //UI
    LineChart chart;

    //Vars
    private ArrayList<Completed> mCompleteds;
    private CompletedRepo mCompletedRepo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphics);

        BottomNavigationView bnw = findViewById(R.id.bottomNavViewBar);
        //Set OnClickListener to bottom toolbar
        //This will switch the clicked menu item and then will intent the respective activity
        bnw.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.ic_new_plan:
                        Intent intent_newPlan = new Intent(graphics.this, a_workout_overview.class);
                        startActivity(intent_newPlan);
                        break;
                    case R.id.ic_workout:
                        Intent intent_log = new Intent(graphics.this, CompletedLog.class);
                        startActivity(intent_log);

                        break;
                    case R.id.ic_analysis:
                        //   Intent intent_analysis = new Intent(MainActivity.this, aAnalysis.class);
                        //   startActivity(intent_analysis);
                        break;

                }
                return false;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chart = findViewById(R.id.datachart);
        mCompleteds = new ArrayList<Completed>();
        mCompletedRepo = new CompletedRepo(this);

        retrieveLog();

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
                    Log.d(TAG, "onChanged: " + mCompleteds);
                    buildGraph();
                }
            }
        });
    }

    private void buildGraph() {
        List<Entry> entries = new ArrayList<>();
        int i = 1;
        for (Completed completed : mCompleteds) {

            entries.add(new Entry(i, completed.getWeight()));
            i++;
        }

        LineDataSet dataSet = new LineDataSet(entries, "DatenVol");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
    }
}
