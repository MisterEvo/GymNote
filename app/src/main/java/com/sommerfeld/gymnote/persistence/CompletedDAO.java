package com.sommerfeld.gymnote.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sommerfeld.gymnote.models.Completed;

import java.util.List;

@Dao
public interface CompletedDAO {

    @Insert
    long[] insertWorkout(Completed... completeds);


    @Query("SELECT * FROM completed")
    LiveData<List<Completed>> getCompleteds();

    @Delete
    int delete(Completed... completeds);

    @Update
    int update(Completed... completeds);
}
