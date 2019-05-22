package com.sommerfeld.gymnote.listener;

import com.sommerfeld.gymnote.models.Workout;

import java.util.List;

public interface OnWorkoutListChangedListener {
    void onWorkoutListChanged(List<Workout> workouts);
}
