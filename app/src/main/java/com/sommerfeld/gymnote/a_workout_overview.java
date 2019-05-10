package com.sommerfeld.gymnote;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sommerfeld.gymnote.adapters.WorkoutRecyclerAdapter;
import com.sommerfeld.gymnote.models.Workout;
import com.sommerfeld.gymnote.util.VerticalSpacingItemDecorator;

import java.util.ArrayList;

public class a_workout_overview extends AppCompatActivity {

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





    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerview.addItemDecoration(itemDecorator);
        mWorkoutRecyclerAdapter = new WorkoutRecyclerAdapter(mWorkout);
        mRecyclerview.setAdapter(mWorkoutRecyclerAdapter);
    }

    private void loadTestObj() {
        // Create static test obj
        //Create test vars to create the object from
        Workout workout;
        String title;
        String color;
        ArrayList<String> exercise = new ArrayList<>();
        ArrayList<Integer> repsS1 = new ArrayList<>();
        ArrayList<Integer> repsS2 = new ArrayList<>();
        ArrayList<Integer> repsS3 = new ArrayList<>();

        //Initialize the vars with content
        for (int i = 1; i < 100; i++) {
            workout = new Workout();
            title = "ChestBiceps #" + i;
            color = "orange #" + i;
            exercise.add("Benchpress");
            exercise.add("Curls");
            repsS1.add(10);
            repsS1.add(9);
            repsS2.add(8);
            repsS2.add(7);
            repsS3.add(6);
            repsS3.add(5);


            //Pass the vars to the obj to generate the obj
            workout.setTitle(title);
            workout.setColor(color);
            workout.setExercise(exercise);
            workout.setRepsS1(repsS1);
            workout.setRepsS2(repsS2);
            workout.setRepsS3(repsS3);

            //Print obj to log using .toString

            // Log.d(TAG, "onCreate: myWorkout" + workout.toString() );

            //Pass the generated object in the corresponding outter Array
            mWorkout.add(workout);
        }
        mWorkoutRecyclerAdapter.notifyDataSetChanged();
    }
}

