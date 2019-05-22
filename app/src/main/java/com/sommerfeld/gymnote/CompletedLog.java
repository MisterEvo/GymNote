package com.sommerfeld.gymnote;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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


public class CompletedLog extends AppCompatActivity {
    private static final String TAG = "CompletedLog";

    //UI
    private RecyclerView mRecyclerView;
    private TextView mTVCount;
    private TextView mTVTotal;

    //vars
    private ArrayList<Completed> mCompleteds;
    private LogListAdapter mLogListAdapter;
    private CompletedRepo mCompletedRepo;
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
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

        retrieveLog();


        BottomNavigationView bnw = findViewById(R.id.bottomNavViewBar);
        bnw.setSelectedItemId(R.id.ic_workout);


        //Set OnClickListener to bottom toolbar
        //This will switch the clicked menu item and then will intent the respective activity
        bnw.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.ic_new_plan:
                        Intent intent_newPlan = new Intent(CompletedLog.this, a_workout_overview.class);
                        startActivity(intent_newPlan);
                        break;
                    case R.id.ic_workout:
                        Intent intent_log = new Intent(CompletedLog.this, CompletedLog.class);
                        startActivity(intent_log);

                        break;
                    case R.id.ic_analysis:
                        //   Intent intent_analysis = new Intent(MainActivity.this, aAnalysis.class);
                        //   startActivity(intent_analysis);
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
        mLogListAdapter = new LogListAdapter(mCompleteds);
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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mLogListAdapter.getFilter().filter(newText);
                String searchSize = String.valueOf(mCompleteds.size());
                mTVCount.setText(searchSize);
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
}
