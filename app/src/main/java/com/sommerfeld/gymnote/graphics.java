package com.sommerfeld.gymnote;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sommerfeld.gymnote.models.Completed;
import com.sommerfeld.gymnote.persistence.CompletedRepo;

import java.util.ArrayList;
import java.util.List;

public class graphics extends AppCompatActivity {

    private static final String TAG = "graphics";
    //UI
    private LineChart chart;
    private Spinner mSpinner;

    //Vars
    private ArrayList<Completed> mCompleteds;
    private CompletedRepo mCompletedRepo;
    private ArrayList<String> mExercise;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphics);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Analysis");

        BottomNavigationView bnw = findViewById(R.id.bottomNavViewBar);
        bnw.setSelectedItemId(R.id.ic_analysis);
        //Set OnClickListener to bottom toolbar
        //This will switch the clicked menu item and then will intent the respective activity
        bnw.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.ic_dashboard:
                        Intent intent_Dashboard = new Intent(graphics.this, MainActivity.class);
                        intent_Dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_Dashboard);
                        break;
                    case R.id.ic_new_plan:
                        Intent intent_newPlan = new Intent(graphics.this, a_workout_overview.class);
                        intent_newPlan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_newPlan);
                        break;
                    case R.id.ic_workout:
                        Intent intent_log = new Intent(graphics.this, CompletedLog.class);
                        intent_log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_log);

                        break;
                    case R.id.ic_analysis:
                        //Current view
                        break;

                }
                return false;
            }
        });


        chart = findViewById(R.id.datachart);
        mCompleteds = new ArrayList<Completed>();
        mExercise = new ArrayList<>();
        mCompletedRepo = new CompletedRepo(this);
        mSpinner = findViewById(R.id.spinner_excercise);


        retrieveLog();
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadSpecificArray(mSpinner.getSelectedItem().toString());
                buildGraph();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                    Log.d(TAG, "onChanged: " + mCompleteds);
                    loadExercises();

                }
            }
        });
    }

    // Loads the Exercises Names in the AL to fill the spinner
    private void loadExercises() {
        for (Completed item : mCompleteds) {
            if (!mExercise.contains(item.getExercise())) {
                mExercise.add(item.getExercise());
            }
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mExercise);
        mSpinner.setAdapter(spinnerAdapter);

    }

    private ArrayList<Completed> loadSpecificArray(String exercise) {
        ArrayList<Completed> SpecificArray = new ArrayList<>();
        for (Completed item : mCompleteds) {
            if (item.getExercise().equals(exercise)) {
                SpecificArray.add(item);
            }
        }
        Log.d(TAG, "loadSpecificArray: Array: " + SpecificArray);
        return SpecificArray;
    }

    private void buildGraph() {
        List<Entry> entries = new ArrayList<>();
        for (Completed completed : loadSpecificArray(mSpinner.getSelectedItem().toString())) {
            entries.add(new Entry(completed.getId(), completed.getWeight()));
            Log.d(TAG, "buildGraph: Added X,Y:" + completed.getId() + " " + completed.getWeight());
        }

        LineDataSet dataSet = new LineDataSet(entries, "Weight");
        LineData lineData = new LineData(dataSet);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new MyAxisValueFormatter());
        chart.setData(lineData);
        chart.invalidate();
    }

    private int findIndex(ArrayList<Completed> CompletedArr, float t) {
        int len = CompletedArr.size();
        int i = 0;

        while (i < len) {

            if (CompletedArr.get(i).getId() == t) {
                return i;
            } else {
                i = i + 1;
            }
        }
        return -1;
    }

    private class MyAxisValueFormatter implements IAxisValueFormatter {
        final ArrayList<Completed> specifics = loadSpecificArray(mSpinner.getSelectedItem().toString());

        @Override
        public String getFormattedValue(float value, AxisBase axis) {


            int index = findIndex(specifics, value);
            Log.d(TAG, "getFormattedValue: Passed id: " + value);
            Log.d(TAG, "getFormattedValue: Index of id: " + index);
            String result = "";

            try {
                result = specifics.get(index).getTimestamp();
            } catch (Exception e) {
                result = "";
            }
            Log.d(TAG, "getFormattedValue: Result: " + result);
            return result;
        }
    }


}

