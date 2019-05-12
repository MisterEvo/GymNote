package com.sommerfeld.gymnote;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.sommerfeld.gymnote.adapters.WorkoutRecyclerAdapter;
import com.sommerfeld.gymnote.models.Workout;
import com.sommerfeld.gymnote.util.VerticalSpacingItemDecorator;

import java.util.ArrayList;

public class a_workout_overview extends AppCompatActivity implements WorkoutRecyclerAdapter.OnWorkoutListener {

    private static final String TAG = "a_workout_overview";

    //UI Components
    private RecyclerView mRecyclerview;

    //Vars
    private ArrayList<Workout> mWorkout = new ArrayList<>();
    private WorkoutRecyclerAdapter mWorkoutRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_overview);

        mRecyclerview = findViewById(R.id.recyclerView);

        //Load the test obj
        initRecyclerView();
        loadTestObj();

        setSupportActionBar((Toolbar) findViewById(R.id.workout_toolbar));
        setTitle("GymNote");


    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerview.addItemDecoration(itemDecorator);
        mWorkoutRecyclerAdapter = new WorkoutRecyclerAdapter(mWorkout, this);
        mRecyclerview.setAdapter(mWorkoutRecyclerAdapter);
    }

    private void loadTestObj() {
        // Create static test obj
        //Create test vars to create the object from
        Workout workout;
        String title;
        String color;
        ArrayList<String> exercise = new ArrayList<>();
        ArrayList<String> weight = new ArrayList<>();
        ArrayList<String> repsS1 = new ArrayList<>();
        ArrayList<String> repsS2 = new ArrayList<>();
        ArrayList<String> repsS3 = new ArrayList<>();

        //Initialize the vars with content
        workout = new Workout();
        title = "ChestBiceps";
        color = "orange";
        exercise.add("Benchpress");
        exercise.add("Curls");
        weight.add("50");
        weight.add("0");
        repsS1.add("10");
        repsS1.add("9");
        repsS2.add("8");
        repsS2.add("7");
        repsS3.add("6");
        repsS3.add("5");


        //Pass the vars to the obj to generate the obj
        workout.setTitle(title);
        workout.setColor(color);
        workout.setExercise(exercise);
        workout.setWeight(weight);
        workout.setRepsS1(repsS1);
        workout.setRepsS2(repsS2);
        workout.setRepsS3(repsS3);

        //Print obj to log using .toString

        //Log.d(TAG, "onCreate: myWorkout" + workout.toString() );

        //Pass the generated object in the corresponding outter Array
        mWorkout.add(workout);

        //Log.d(TAG, "Added workout to mWorkout: " + mWorkout);

        mWorkoutRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onWorkoutClick(int position) {
        Log.d(TAG, "onWorkoutClick: clicked" + position);

        Intent intent = new Intent(this, detailed_workout_plan.class);
        Log.d(TAG, "onWorkoutClick: Setting Extra: " + mWorkout.get(position));
        intent.putExtra("selected_workout", mWorkout.get(position));
        startActivity(intent);

    }
}

