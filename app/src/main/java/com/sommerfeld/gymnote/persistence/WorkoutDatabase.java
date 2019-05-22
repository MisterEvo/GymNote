package com.sommerfeld.gymnote.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sommerfeld.gymnote.models.Workout;

@Database(entities = {Workout.class}, version = 1)
public abstract class WorkoutDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "workouts.db";

    private static WorkoutDatabase instance;

    static WorkoutDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    WorkoutDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract WorkoutDAO getWorkoutDAO();
}
