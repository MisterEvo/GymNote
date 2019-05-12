package com.sommerfeld.gymnote;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sommerfeld.gymnote.models.Workout;
import com.sommerfeld.gymnote.adapters.detailed_recycler_adapter;

import java.util.ArrayList;

public class detailed_workout_plan extends AppCompatActivity {
    private static final String TAG = "detailed_workout_plan";

    //UI Components
    RecyclerView mRecyclerView;


    //Vars

    private Workout mWorkout = new Workout();
    private ArrayList<String> mWorkoutList = new ArrayList<>();
    private detailed_recycler_adapter mDetailedRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_workout_plan);
        mRecyclerView = findViewById(R.id.recyclerViewDetail);


        mWorkout = getIntent().getParcelableExtra("selected_workout");

        mWorkoutList = mWorkout.getExercise();
        initRecyclerView();
        Log.d(TAG, "onCreate: Neues String item " + mWorkoutList);
        mDetailedRecyclerAdapter.notifyDataSetChanged();
    }

    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mDetailedRecyclerAdapter = new detailed_recycler_adapter(mWorkoutList);
        mRecyclerView.setAdapter(mDetailedRecyclerAdapter);

    }
}
