package com.sommerfeld.gymnote;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sommerfeld.gymnote.adapters.WorkoutListAdapter;
import com.sommerfeld.gymnote.listener.OnStartDragListener;
import com.sommerfeld.gymnote.listener.OnWorkoutListChangedListener;
import com.sommerfeld.gymnote.models.Workout;
import com.sommerfeld.gymnote.persistence.WorkoutRepo;
import com.sommerfeld.gymnote.util.SimpleItemTouchHelperCallback;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class a_workout_overview extends AppCompatActivity implements WorkoutListAdapter.OnWorkoutListenerNew, OnStartDragListener, OnWorkoutListChangedListener {

    private static final String TAG = "a_workout_overview";

    //UI Components
    private RecyclerView mRecyclerview;
    FloatingActionButton fab;
    Button mBtnOk;
    Button mBtnCancel;
    EditText mEtName;
    EditText mEtWeight;
    EditText mEtSet1;
    EditText mEtSet2;
    EditText mEtSet3;

    //Vars
    private ArrayList<Workout> mWorkout = new ArrayList<>();
    private ArrayList<Workout> mWorkoutTemp = new ArrayList<>();
    private WorkoutListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ItemTouchHelper mItemTouchHelper;
    private WorkoutRepo mWorkoutRepo;
    private Workout mWorkoutItem;

    private SharedPreferences mSharedPrefs;
    private SharedPreferences.Editor mEditor;
    public static final String LIST_SORTED_DATA_ID = "json_list_sorted_data_id";
    public static final String PREFS_FILE = "pref_file";


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        saveOrder(mWorkout);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_overview);

        mSharedPrefs = this.getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mEditor = mSharedPrefs.edit();
        mEditor.apply();

        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        mWorkoutRepo = new WorkoutRepo(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Load the test obj
        retrieveWorkouts();

        BottomNavigationView bnw = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);


        //Set OnClickListener to bottom toolbar
        //This will switch the clicked menu item and then will intent the respective activity
        bnw.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()) {
                    case R.id.ic_new_plan:
                        break;
                    case R.id.ic_workout:
                        Intent intent_log = new Intent(a_workout_overview.this, CompletedLog.class);
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

    public void openDialog() {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(a_workout_overview.this);
        final View mView = getLayoutInflater().inflate(R.layout.d_new_workout, null);
        mBtnOk = (Button)mView.findViewById(R.id.btn_okay);
        mBtnCancel = (Button) mView.findViewById(R.id.btn_cancel);
        mEtName = (EditText)mView.findViewById(R.id.et_exercise_name);
        mEtWeight = (EditText) mView.findViewById(R.id.et_weight);
        mEtSet1 = (EditText) mView.findViewById(R.id.et_repsS1);
        mEtSet2 = (EditText) mView.findViewById(R.id.et_repsS2);
        mEtSet3 = (EditText) mView.findViewById(R.id.et_repsS3);
        mBtnOk.setEnabled(false);
        mBtnOk.setTextColor(getApplication().getResources().getColor(R.color.colorDarkGrey));

        mEtName.addTextChangedListener(addTextWatcher);
        mEtWeight.addTextChangedListener(addTextWatcher);
        mEtSet1.addTextChangedListener(addTextWatcher);
        mEtSet2.addTextChangedListener(addTextWatcher);
        mEtSet3.addTextChangedListener(addTextWatcher);


        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();


        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   createWorkoutItem();
                   mWorkoutRepo.insertWorkoutTask(mWorkoutItem);
                   Log.d(TAG, "onClick: Generated item: " + mWorkoutItem.toString());
                   dialog.dismiss();
               }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private TextWatcher addTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(mEtName.getText()) && !TextUtils.isEmpty(mEtWeight.getText()) && !TextUtils.isEmpty(mEtSet1.getText())&& !TextUtils.isEmpty(mEtSet2.getText())&& !TextUtils.isEmpty(mEtSet3.getText())) {
                mBtnOk.setTextColor(getApplication().getResources().getColor(R.color.colorRed));
                mBtnOk.setEnabled(true);
            } else {
                mBtnOk.setTextColor(getApplication().getResources().getColor(R.color.colorDarkGrey));
                mBtnOk.setEnabled(false);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void createWorkoutItem() {
        mWorkoutItem = new Workout();
        mWorkoutItem.setTitle("TestTitle");
        mWorkoutItem.setColor("orange");
        mWorkoutItem.setExercise(mEtName.getText().toString());
        mWorkoutItem.setWeight(Float.parseFloat(mEtWeight.getText().toString()));
        mWorkoutItem.setRepsS1( Integer.parseInt(mEtSet1.getText().toString()));
        mWorkoutItem.setRepsS2( Integer.parseInt(mEtSet2.getText().toString()));
        mWorkoutItem.setRepsS3( Integer.parseInt(mEtSet3.getText().toString()));
    }

    private void retrieveWorkouts() {
        String getorder = mSharedPrefs.getString(LIST_SORTED_DATA_ID, "");
        Toast.makeText(this,"Loaded PREFS: " +getorder, Toast.LENGTH_SHORT).show();
        mWorkoutRepo.retrieveWorkoutsTask().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workouts) {
                if(mWorkoutTemp.size() > 0){
                    mWorkoutTemp.clear();
                }
                if(workouts != null){
                    mWorkoutTemp.addAll(workouts);
                    mWorkout = getSortedData();
                    initRecyclerView();
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    private void initRecyclerView() {
        mRecyclerview = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerview.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLayoutManager);

        //setup Adapter with empty list
        Log.d(TAG, "initRecyclerView: mWorkout: " + mWorkout);
        mAdapter = new WorkoutListAdapter(mWorkout,this, this, this, this);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerview);
        mRecyclerview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
        .colorResId(R.color.colorPrimaryDark).size(2).build());
        mRecyclerview.setAdapter(mAdapter);

    }


    @Override
    public void onWorkoutClick(int position) {
        //OnClick für Einträge in der Liste
        Log.d(TAG, "onWorkoutClick: " + mWorkout.get(position));
        int id = mWorkout.get(position).getId();
        Intent i = new Intent(this, editWorkout.class);
        i.putExtra("selected_item", id);
        startActivity(i);
    }



    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onWorkoutListChanged(List<Workout> workouts) {

    }
    public void saveOrder(List<Workout> workouts) {
        List<Integer> sortedList = new ArrayList<>();
        for (Workout allWorkouts: workouts) {
            sortedList.add(allWorkouts.getId());
        }
        //Put Ids to JSON
        Gson gson = new Gson();
        String jsonSortedList = gson.toJson(sortedList);

        //Save json to SharedPrefs
        mEditor.putString(LIST_SORTED_DATA_ID, jsonSortedList).commit();
        mEditor.commit();
        Toast.makeText(this, "Saved order:" + jsonSortedList, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overview_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_Order:
                saveOrder(mWorkout);
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Workout> getSortedData() {
        ArrayList<Workout> sortedWorkout = new ArrayList<>();

        String jsonListOfSortedWorkoutId = mSharedPrefs.getString(LIST_SORTED_DATA_ID, "");

        if(!jsonListOfSortedWorkoutId.isEmpty()){
            Gson gson = new Gson();
            List<Integer> listOfSortedWorkout = gson.fromJson(jsonListOfSortedWorkoutId, new TypeToken<List<Integer>>(){}.getType());
            Log.d(TAG, "getSortedData: Decrypted JSON: " + listOfSortedWorkout);

            //build sorted list
            if (listOfSortedWorkout != null && listOfSortedWorkout.size() > 0){

                for(int id:listOfSortedWorkout) {
                    for (Workout workout:mWorkoutTemp) {
                        if(workout.getId() == id) {
                            sortedWorkout.add(workout);
                            mWorkoutTemp.remove(workout);
                            break;
                        }
                    }
                }

            }

            if(mWorkoutTemp.size() > 0) {
                sortedWorkout.addAll(mWorkoutTemp);
            }
            Log.d(TAG, "getSortedData: SortedList: " + sortedWorkout);
            return sortedWorkout;

        } else{
            return mWorkoutTemp;
        }

    }

}

