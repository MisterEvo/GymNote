package com.sommerfeld.gymnote.async;

import android.os.AsyncTask;

import com.sommerfeld.gymnote.models.Workout;
import com.sommerfeld.gymnote.persistence.WorkoutDAO;

public class UpdateAsyncTask extends AsyncTask<Workout, Void, Void> {

    private final WorkoutDAO mWorkoutDAO;

    public UpdateAsyncTask(WorkoutDAO dao) {
        mWorkoutDAO = dao;
    }

    @Override
    protected Void doInBackground(Workout... workouts) {
        mWorkoutDAO.update(workouts);
        return null;
    }
}
