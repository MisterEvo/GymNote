package com.sommerfeld.gymnote.async;

import android.os.AsyncTask;

import com.sommerfeld.gymnote.models.Completed;
import com.sommerfeld.gymnote.persistence.CompletedDAO;

public class DeleteLogAsync extends AsyncTask<Completed, Void, Void> {

    private CompletedDAO mCompletedDAO;

    public DeleteLogAsync(CompletedDAO dao) {
        mCompletedDAO = dao;
    }

    @Override
    protected Void doInBackground(Completed... completeds) {
        mCompletedDAO.delete(completeds);
        return null;
    }
}
