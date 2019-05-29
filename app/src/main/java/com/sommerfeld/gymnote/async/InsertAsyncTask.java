package com.sommerfeld.gymnote.async;

import android.os.AsyncTask;

import com.sommerfeld.gymnote.models.Workout;
import com.sommerfeld.gymnote.persistence.WorkoutDAO;

public class InsertAsyncTask extends AsyncTask<Workout, Void, Void> {

    private final WorkoutDAO mWorkoutDAO;

    public InsertAsyncTask(WorkoutDAO dao) {
        mWorkoutDAO = dao;
    }

    @Override
    protected Void doInBackground(Workout... workouts) {
        mWorkoutDAO.insertWorkout(workouts);
        return null;
    }
}
