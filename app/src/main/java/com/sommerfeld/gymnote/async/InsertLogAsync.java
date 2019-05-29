package com.sommerfeld.gymnote.async;

import android.os.AsyncTask;

import com.sommerfeld.gymnote.models.Completed;
import com.sommerfeld.gymnote.persistence.CompletedDAO;

public class InsertLogAsync extends AsyncTask<Completed, Void, Void> {

    private final CompletedDAO mCompletedDAO;

    public InsertLogAsync(CompletedDAO dao) {
        mCompletedDAO = dao;
    }

    @Override
    protected Void doInBackground(Completed... completeds) {
        mCompletedDAO.insertWorkout(completeds);
        return null;
    }
}
