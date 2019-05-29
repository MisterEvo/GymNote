package com.sommerfeld.gymnote.async;

import android.os.AsyncTask;

import com.sommerfeld.gymnote.models.Completed;
import com.sommerfeld.gymnote.persistence.CompletedDAO;

public class UpdateLogAsync extends AsyncTask<Completed, Void, Void> {

    private final CompletedDAO mCompletedDAO;

    public UpdateLogAsync(CompletedDAO dao) {
        mCompletedDAO = dao;
    }

    @Override
    protected Void doInBackground(Completed... completeds) {
        mCompletedDAO.update(completeds);
        return null;
    }
}
