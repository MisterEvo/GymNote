package com.sommerfeld.gymnote;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sommerfeld.gymnote.models.Workout;

import java.util.ArrayList;

public class a_workout_overview extends AppCompatActivity {

    private static final String TAG = "a_workout_overview";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_overview);


        // Create static test obj
        //Create test vars to create the object from
        Workout workout = new Workout();
        String titel;
        ArrayList<String> exercise = new ArrayList<>();
        ArrayList<Integer> repsS1 = new ArrayList<>();
        ArrayList<Integer> repsS2 = new ArrayList<>();
        ArrayList<Integer> repsS3 = new ArrayList<>();

        //Initialize the vars with content
        titel = "ChestBiceps";
        exercise.add("Benchpress");
        exercise.add("Curls");
        repsS1.add(10);
        repsS1.add(9);
        repsS2.add(8);
        repsS2.add(7);
        repsS3.add(6);
        repsS3.add(5);


        //Pass the vars to the obj to generate the obj
        workout.setTitle(titel);
        workout.setExercise(exercise);
        workout.setRepsS1(repsS1);
        workout.setRepsS2(repsS2);
        workout.setRepsS3(repsS3);

        //Print obj to log using .toString

        Log.d(TAG, "onCreate: myWorkout" + workout.toString() );
    }
}

