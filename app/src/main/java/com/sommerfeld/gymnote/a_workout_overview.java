package com.sommerfeld.gymnote;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sommerfeld.gymnote.adapters.OutterListAdapter;
import com.sommerfeld.gymnote.models.Workout;
import com.sommerfeld.gymnote.persistence.WorkoutRepo;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class a_workout_overview extends AppCompatActivity {

    private static final String TAG = "a_workout_overview";

    //UI Components
    private RecyclerView mRecyclerview;
    FloatingActionButton fab;
    Button mBtnOk;
    Button mBtnCancel;
    public static final String SPINNER_ITEMS = "spinner_items";
    EditText mEtName;
    EditText mEtWeight;
    EditText mEtSet1;
    EditText mEtSet2;
    EditText mEtSet3;
    Button mBtnAddSpinnerItem;

    //Vars
    private ArrayList<Workout> mWorkout = new ArrayList<>();
    private ArrayList<Workout> mWorkoutTemp = new ArrayList<>();
    Spinner mSpinner;
    private RecyclerView.LayoutManager mLayoutManager;
    private ItemTouchHelper mItemTouchHelper;
    private WorkoutRepo mWorkoutRepo;
    private Workout mWorkoutItem;
    ArrayList<String> spinnerEntry = new ArrayList<>();

    private SharedPreferences mSharedPrefs;
    private SharedPreferences.Editor mEditor;
    public static final String LIST_SORTED_DATA_ID = "json_list_sorted_data_id";
    private OutterListAdapter mAdapter;
    public static final String PREFS_FILE = "pref_file";



    private TextWatcher addTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(mEtName.getText()) && !TextUtils.isEmpty(mEtWeight.getText()) && !TextUtils.isEmpty(mEtSet1.getText()) && !TextUtils.isEmpty(mEtSet2.getText()) && !TextUtils.isEmpty(mEtSet3.getText())) {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_overview);

        mSharedPrefs = this.getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mEditor = mSharedPrefs.edit();
        mEditor.apply();

        fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        mWorkoutRepo = new WorkoutRepo(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List of Workouts");

        //Load the test obj
        retrieveWorkouts();

        BottomNavigationView bnw = findViewById(R.id.bottomNavViewBar);
        bnw.setSelectedItemId(R.id.ic_new_plan);


        //Set OnClickListener to bottom toolbar
        //This will switch the clicked menu item and then will intent the respective activity
        bnw.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.ic_dashboard:
                        Intent intent_Dashboard = new Intent(a_workout_overview.this, MainActivity.class);
                        startActivity(intent_Dashboard);
                        break;
                    case R.id.ic_new_plan:
                        //Current view
                        break;
                    case R.id.ic_workout:
                        Intent intent_log = new Intent(a_workout_overview.this, CompletedLog.class);
                        startActivity(intent_log);

                        break;
                    case R.id.ic_analysis:
                        Intent intent_analysis = new Intent(a_workout_overview.this, graphics.class);
                        startActivity(intent_analysis);
                        break;

                }
                return false;
            }
        });

    }

    public void openDialog() {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(a_workout_overview.this);
        final View mView = getLayoutInflater().inflate(R.layout.d_new_workout, null);
        mSpinner = mView.findViewById(R.id.spinner_group);
        mBtnOk = mView.findViewById(R.id.btn_okay);
        mBtnCancel = mView.findViewById(R.id.btn_cancel);
        mBtnAddSpinnerItem = mView.findViewById(R.id.btn_add_Spinner_item);
        mEtName = mView.findViewById(R.id.et_name);
        mEtWeight = mView.findViewById(R.id.et_weight);
        mEtSet1 = mView.findViewById(R.id.et_repsS1);
        mEtSet2 = mView.findViewById(R.id.et_repsS2);
        mEtSet3 = mView.findViewById(R.id.et_repsS3);
        mBtnOk.setEnabled(false);
        mBtnOk.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGrey));

        mEtName.addTextChangedListener(addTextWatcher);
        mEtWeight.addTextChangedListener(addTextWatcher);
        mEtSet1.addTextChangedListener(addTextWatcher);
        mEtSet2.addTextChangedListener(addTextWatcher);
        mEtSet3.addTextChangedListener(addTextWatcher);


        //Load Spinner Items from SharedPrefs
        String LjsonSpinnerItems = mSharedPrefs.getString(SPINNER_ITEMS, "");

        if (!LjsonSpinnerItems.isEmpty()) {
            Gson gson = new Gson();
            spinnerEntry = gson.fromJson(LjsonSpinnerItems, new TypeToken<List<String>>() {
            }.getType());
        } else {
            spinnerEntry.add("(none)");
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerEntry);
        mSpinner.setAdapter(spinnerAdapter);


        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

        //Create new spinner item and save the list to shared prefs
        mBtnAddSpinnerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show dialog to enter the new Spinner Item Name
                final AlertDialog.Builder SpinnerBuilder = new AlertDialog.Builder(a_workout_overview.this);
                final View SpinnerView = getLayoutInflater().inflate(R.layout.d_add_spinner_icon, null);
                SpinnerBuilder.setView(SpinnerView);
                final AlertDialog SpinnerDialog = SpinnerBuilder.create();
                SpinnerDialog.show();
                final EditText et_SpinnerItem = SpinnerView.findViewById(R.id.et_spinner_item_name);
                Button SpinnerOkay = SpinnerView.findViewById(R.id.btn_spinner_okay);
                Button SpinnerCancel = SpinnerView.findViewById(R.id.btn_spinner_cancel);

                SpinnerOkay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Triggers when the okay button inside the new SpinnerItemDialog is pressed
                        if (TextUtils.isEmpty(et_SpinnerItem.getText())) {
                            et_SpinnerItem.setError("Field required");
                        } else {
                            spinnerEntry.add(et_SpinnerItem.getText().toString().trim());
                            SpinnerDialog.dismiss();
                        }
                    }
                });
            }
        });


        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createWorkoutItem();
                mWorkoutRepo.insertWorkoutTask(mWorkoutItem);
                Log.d(TAG, "onClick: Generated item: " + mWorkoutItem.toString());
                saveSpinnerItems();
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

    private void saveSpinnerItems() {
        Gson gson = new Gson();
        String jsonSpinnerItems = gson.toJson(spinnerEntry);
        mEditor.putString(SPINNER_ITEMS, jsonSpinnerItems).commit();
        mEditor.commit();
    }

    private void createWorkoutItem() {
        mWorkoutItem = new Workout();
        mWorkoutItem.setTitle(mSpinner.getSelectedItem().toString());
        mWorkoutItem.setColor("orange");
        mWorkoutItem.setExercise(mEtName.getText().toString());
        mWorkoutItem.setWeight(Float.parseFloat(mEtWeight.getText().toString()));
        mWorkoutItem.setRepsS1(Integer.parseInt(mEtSet1.getText().toString()));
        mWorkoutItem.setRepsS2(Integer.parseInt(mEtSet2.getText().toString()));
        mWorkoutItem.setRepsS3(Integer.parseInt(mEtSet3.getText().toString()));
    }

    private void retrieveWorkouts() {
        mWorkoutRepo.retrieveWorkoutsTask().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workouts) {
                if (mWorkoutTemp.size() > 0) {
                    mWorkoutTemp.clear();
                }
                if (workouts != null) {
                    mWorkoutTemp.addAll(workouts);
                    mWorkout = new ArrayList<>(mWorkoutTemp);
                    initRecyclerView();
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    private void initRecyclerView() {
        mRecyclerview = findViewById(R.id.recyclerView);
        mRecyclerview.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLayoutManager);

        ArrayList<String> GroupArray = new ArrayList<>();

        // Creates an Array with every single title (Group of exercises)
        for (Workout workout : mWorkout) {
            if (!GroupArray.contains(workout.getTitle())) {
                GroupArray.add(workout.getTitle());
            }
        }


        //setup Adapter with empty list
        mAdapter = new OutterListAdapter(this, GroupArray, mWorkout, mWorkoutRepo);
        mRecyclerview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.colorPrimaryDark).size(2).build());
        mRecyclerview.setAdapter(mAdapter);

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
                break;
        }
        return super.onOptionsItemSelected(item);
    }


//        private ArrayList<Workout> getSortedData () {
//            ArrayList<Workout> sortedWorkout = new ArrayList<>();
//
//            String jsonListOfSortedWorkoutId = mSharedPrefs.getString(LIST_SORTED_DATA_ID, "");
//
//            if (!jsonListOfSortedWorkoutId.isEmpty()) {
//                Gson gson = new Gson();
//                List<Integer> listOfSortedWorkout = gson.fromJson(jsonListOfSortedWorkoutId, new TypeToken<List<Integer>>() {
//                }.getType());
//                Log.d(TAG, "getSortedData: Decrypted JSON: " + listOfSortedWorkout);
//
//                //build sorted list
//                if (listOfSortedWorkout != null && listOfSortedWorkout.size() > 0) {
//
//                    for (int id : listOfSortedWorkout) {
//                        for (Workout workout : mWorkoutTemp) {
//                            if (workout.getId() == id) {
//                                sortedWorkout.add(workout);
//                                mWorkoutTemp.remove(workout);
//                                break;
//                            }
//                        }
//                    }
//
//                }
//
//                if (mWorkoutTemp.size() > 0) {
//                    sortedWorkout.addAll(mWorkoutTemp);
//                }
//                Log.d(TAG, "getSortedData: SortedList: " + sortedWorkout);
//                return sortedWorkout;
//
//            } else {
//                return mWorkoutTemp;
//            }
//
//        }

}

