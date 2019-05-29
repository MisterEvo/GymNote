package com.sommerfeld.gymnote;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sommerfeld.gymnote.adapters.LogListAdapter;
import com.sommerfeld.gymnote.models.Completed;
import com.sommerfeld.gymnote.persistence.CompletedRepo;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class CompletedLog extends AppCompatActivity implements LogListAdapter.OnLogListener {
    private static final String TAG = "CompletedLog";

    //UI
    private RecyclerView mRecyclerView;
    private TextView mTVCount;
    private TextView mTVTotal;

    //vars
    private ArrayList<Completed> mCompleteds;
    private LogListAdapter mLogListAdapter;
    private CompletedRepo mCompletedRepo;
    private final ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteEntry(mCompleteds.get(viewHolder.getAdapterPosition()));
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completeds);
        mCompleteds = new ArrayList<Completed>();
        mRecyclerView = findViewById(R.id.rv_log);
        mCompletedRepo = new CompletedRepo(this);

        mTVCount = findViewById(R.id.tv_count);
        mTVTotal = findViewById(R.id.tv_total);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Log of Workouts");

        retrieveLog();


        BottomNavigationView bnw = findViewById(R.id.bottomNavViewBar);
        bnw.setSelectedItemId(R.id.ic_workout);


        //Set OnClickListener to bottom toolbar
        //This will switch the clicked menu item and then will intent the respective activity
        bnw.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.ic_dashboard:
                        Intent intent_Dashboard = new Intent(CompletedLog.this, MainActivity.class);
                        intent_Dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_Dashboard);
                        break;
                    case R.id.ic_new_plan:
                        Intent intent_newPlan = new Intent(CompletedLog.this, a_workout_overview.class);
                        intent_newPlan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_newPlan);
                        break;
                    case R.id.ic_workout:
                        Intent intent_log = new Intent(CompletedLog.this, CompletedLog.class);
                        intent_log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_log);

                        break;
                    case R.id.ic_analysis:
                        Intent intent_analysis = new Intent(CompletedLog.this, graphics.class);
                        intent_analysis.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_analysis);
                        break;

                }
                return false;
            }
        });
    }

    private void retrieveLog() {
        mCompletedRepo.retrieveCompletedTask().observe(this, new Observer<List<Completed>>() {
            @Override
            public void onChanged(List<Completed> completeds) {

                if (mCompleteds.size() > 0) {
                    mCompleteds.clear();
                }
                if (completeds != null) {
                    Log.d(TAG, "onChanged: Single Completed: " + completeds);
                    mCompleteds.addAll(completeds);
                    Log.d(TAG, "onChanged: Array of Completeds: " + mCompleteds);
                    String size = String.valueOf(mCompleteds.size());
                    mTVTotal.setText(size);
                    initRecyclerView();
                }

                mLogListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        Log.d(TAG, "initRecyclerView: Pass mCompleteds: " + mCompleteds);
        mLogListAdapter = new LogListAdapter(mCompleteds, this);
        mRecyclerView.setAdapter(mLogListAdapter);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.colorPrimaryDark).size(2).build());
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);


    }

    private void deleteEntry(Completed completed) {
        mCompleteds.remove(completed);
        mLogListAdapter.notifyDataSetChanged();
        mCompletedRepo.deleteNote(completed);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.log_menu, menu);


        MenuItem searchItem = menu.findItem(R.id.tool_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mTVCount.setText(String.valueOf(mRecyclerView.getChildCount()));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mLogListAdapter.getFilter().filter(newText);
                return false;
            }

        });
        try {
            Log.d(TAG, "onQueryTextChange: Size of Array: " + mLogListAdapter.getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onLogLongClick(int id) {
        // Launch edit view
        int index = findIndex(mCompleteds, id);
        Log.d(TAG, "onLogLongClick: Click id: " + id + mCompleteds.get(index));
        buildDialog(mCompleteds.get(index));
    }

    private void buildDialog(final Completed completed) {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(CompletedLog.this);
        final View mView = getLayoutInflater().inflate(R.layout.d_edit_log, null);

        //Dialog UI

        TextView exercise = mView.findViewById(R.id.tv_d_exercise);
        final EditText timestamp = mView.findViewById(R.id.et_d_timestamp);
        final EditText weight = mView.findViewById(R.id.et_d_weight);
        final EditText repS1 = mView.findViewById(R.id.et_d_repsS1);
        final EditText repS2 = mView.findViewById(R.id.et_d_repsS2);
        final EditText repS3 = mView.findViewById(R.id.et_d_repsS3);
        Button btnokay = mView.findViewById(R.id.btn_d_okay);

        //------
        // Fill UI from DB

        exercise.setText(completed.getExercise());
        timestamp.setText(completed.getTimestamp());
        weight.setText(String.valueOf(completed.getWeight()));
        repS1.setText(String.valueOf(completed.getRepsS1()));
        repS2.setText(String.valueOf(completed.getRepsS2()));
        repS3.setText(String.valueOf(completed.getRepsS3()));


        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

        btnokay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTimestamp = timestamp.getText().toString();
                float newWeight = Float.parseFloat(weight.getText().toString());
                int newRepS1 = Integer.parseInt(repS1.getText().toString());
                int newRepS2 = Integer.parseInt(repS2.getText().toString());
                int newRepS3 = Integer.parseInt(repS3.getText().toString());

                completed.setTimestamp(newTimestamp);
                completed.setWeight(newWeight);
                completed.setRepsS1(newRepS1);
                completed.setRepsS2(newRepS2);
                completed.setRepsS3(newRepS3);

                mCompletedRepo.updateComplete(completed);
                Toast.makeText(CompletedLog.this, "Updated entry!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private int findIndex(ArrayList<Completed> CompletedArr, int t) {
        int len = CompletedArr.size();
        int i = 0;

        while (i < len) {

            if (CompletedArr.get(i).getId() == t) {
                return i;
            } else {
                i = i + 1;
            }
        }
        return -1;
    }
}
