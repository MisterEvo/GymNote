package com.sommerfeld.gymnote.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sommerfeld.gymnote.models.Workout;

import java.util.List;

@Dao
public interface WorkoutDAO {

    @Insert
    long[] insertWorkout(Workout... workouts);


    @Query("SELECT * FROM workouts")
    LiveData<List<Workout>> getWorkouts();


    @Delete
    int delete(Workout... workouts);

    @Update
    int update(Workout... workouts);


}
