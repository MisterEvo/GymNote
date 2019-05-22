package com.sommerfeld.gymnote.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.sommerfeld.gymnote.async.DeleteAsyncTask;
import com.sommerfeld.gymnote.async.InsertAsyncTask;
import com.sommerfeld.gymnote.async.UpdateAsyncTask;
import com.sommerfeld.gymnote.models.Workout;

import java.util.List;

public class WorkoutRepo {

    private WorkoutDatabase mWorkoutDatabase;

    public WorkoutRepo(Context context) {
        mWorkoutDatabase = WorkoutDatabase.getInstance(context);
    }

    public void insertWorkoutTask(Workout workout) {
        new InsertAsyncTask(mWorkoutDatabase.getWorkoutDAO()).execute(workout);
    }

    public void updateWorkout(Workout workout) {
        new UpdateAsyncTask(mWorkoutDatabase.getWorkoutDAO()).execute(workout);
    }


    public LiveData<List<Workout>> retrieveWorkoutsTask() {

        return mWorkoutDatabase.getWorkoutDAO().getWorkouts();
    }


    public void deleteWorkouts(Workout workout) {
        new DeleteAsyncTask(mWorkoutDatabase.getWorkoutDAO()).execute(workout);
    }
}
