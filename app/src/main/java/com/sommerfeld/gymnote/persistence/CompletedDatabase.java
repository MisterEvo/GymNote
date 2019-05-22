package com.sommerfeld.gymnote.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sommerfeld.gymnote.models.Completed;

@Database(entities = {Completed.class}, version = 1)
public abstract class CompletedDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "completed.db";

    private static CompletedDatabase instance;

    static CompletedDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CompletedDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract CompletedDAO getCompleteDAO();
}
