package com.sommerfeld.gymnote.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.sommerfeld.gymnote.async.DeleteLogAsync;
import com.sommerfeld.gymnote.async.InsertLogAsync;
import com.sommerfeld.gymnote.models.Completed;

import java.util.List;

public class CompletedRepo {

    private CompletedDatabase mCompletedDatabase;

    public CompletedRepo(Context context) {
        mCompletedDatabase = CompletedDatabase.getInstance(context);
    }

    public void insertCompleteTask(Completed completed) {
        new InsertLogAsync(mCompletedDatabase.getCompleteDAO()).execute(completed);
    }

    public void updateComplete(Completed completed) {

    }

    public LiveData<List<Completed>> retrieveCompletedTask() {

        return mCompletedDatabase.getCompleteDAO().getCompleteds();
    }

    public void deleteNote(Completed completed) {
        new DeleteLogAsync(mCompletedDatabase.getCompleteDAO()).execute(completed);
    }
}