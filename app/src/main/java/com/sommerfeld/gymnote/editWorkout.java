package com.sommerfeld.gymnote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class editWorkout extends AppCompatActivity {
    private static final String TAG = "editWorkout";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_view);
        Log.d(TAG, "onCreate: ");
    }
}
