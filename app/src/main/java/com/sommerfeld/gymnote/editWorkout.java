package com.sommerfeld.gymnote;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sommerfeld.gymnote.models.Completed;
import com.sommerfeld.gymnote.models.Workout;
import com.sommerfeld.gymnote.persistence.CompletedRepo;
import com.sommerfeld.gymnote.persistence.WorkoutRepo;
import com.sommerfeld.gymnote.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class editWorkout extends AppCompatActivity {
    private static final String TAG = "editWorkout";

    //UI
    TextView tv_exercise;
    EditText et_weight;
    Button btn_add05;
    Button btn_add0625;
    Button btn_add1;
    Button btn_add25;
    Button btn_sub05;
    Button btn_sub0625;
    Button btn_sub1;
    Button btn_sub25;
    Button btn_add_rep1;
    Button btn_add_rep2;
    Button btn_add_rep3;
    Button btn_sub_rep1;
    Button btn_sub_rep2;
    Button btn_sub_rep3;
    EditText et_reps1;
    EditText et_reps2;
    EditText et_reps3;
    Button btn_okay;
    Button btn_cancel;

    //vars
    private ArrayList<Workout> mWorkout = new ArrayList<>();
    private WorkoutRepo mWorkoutRepo;
    private Workout mWorkoutItem;
    private int id;
    private Completed mCompleted;
    private CompletedRepo mCompletedRepo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_view);
        Log.d(TAG, "onCreate: ");
        mWorkoutRepo = new WorkoutRepo(this);
        mWorkoutItem = new Workout();
        mCompleted = new Completed();
        mCompletedRepo = new CompletedRepo(this);

        //Load selected item from intent
        Intent intent = getIntent();
        id = intent.getIntExtra("selected_item", 0);
        Log.d(TAG, "onCreate: Passed pos: " + id);

        //Load database entries
        retrieveWorkouts();


    }

    private void retrieveWorkouts() {

        //Loads entries from DB into the ArrayList
        Log.d(TAG, "retrieveWorkouts: in new class");
        mWorkoutRepo.retrieveWorkoutsTask().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workouts) {

                if (mWorkout.size() > 0) {
                    mWorkout.clear();
                }
                if (workouts != null) {
                    mWorkout.addAll(workouts);
                    int index = findIndex(mWorkout, id);

                    mWorkoutItem = mWorkout.get(index);
                    Log.d(TAG, "onChanged: In Retriever: " + mWorkoutItem);
                    loadUI();
                }
            }
        });

    }

    public int findIndex(ArrayList<Workout> WorkoutArr, int t) {
        int len = WorkoutArr.size();
        int i = 0;

        while (i < len) {

            if (WorkoutArr.get(i).getId() == t) {
                return i;
            } else {
                i = i + 1;
            }
        }
        return -1;
    }

    private void loadUI() {
        tv_exercise = (TextView) findViewById(R.id.exercise);
        et_weight = (EditText) findViewById(R.id.et_weight);
        btn_add05 = (Button) findViewById(R.id.btn_add05);
        btn_add0625 = (Button) findViewById(R.id.btn_add0625);
        btn_add1 = (Button) findViewById(R.id.btn_add1);
        btn_add25 = (Button) findViewById(R.id.btn_add25);
        btn_sub05 = (Button) findViewById(R.id.btn_sub05);
        btn_sub0625 = (Button) findViewById(R.id.btn_sub0625);
        btn_sub1 = (Button) findViewById(R.id.btn_sub1);
        btn_sub25 = (Button) findViewById(R.id.btn_sub25);
        btn_add_rep1 = (Button) findViewById(R.id.btn_add_rep1);
        btn_add_rep2 = (Button) findViewById(R.id.btn_add_rep2);
        btn_add_rep3 = (Button) findViewById(R.id.btn_add_rep3);
        btn_sub_rep1 = (Button) findViewById(R.id.btn_sub_rep1);
        btn_sub_rep2 = (Button) findViewById(R.id.btn_sub_rep2);
        btn_sub_rep3 = (Button) findViewById(R.id.btn_sub_rep3);
        et_reps1 = (EditText) findViewById(R.id.et_RepS1);
        et_reps2 = (EditText) findViewById(R.id.et_RepS2);
        et_reps3 = (EditText) findViewById(R.id.et_RepS3);
        btn_okay = (Button) findViewById(R.id.btn_okay);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        tv_exercise.setText(mWorkoutItem.getExercise());
        Log.d(TAG, "loadUI: " + mWorkoutItem.getExercise());
        String Weight = Float.toString(mWorkoutItem.getWeight());
        Log.d(TAG, "loadUI: Weight String: " + Weight);
        et_weight.setText(Weight);
        et_reps1.setText(String.valueOf(mWorkoutItem.getRepsS1()));
        et_reps2.setText(String.valueOf(mWorkoutItem.getRepsS2()));
        et_reps3.setText(String.valueOf(mWorkoutItem.getRepsS3()));

        Log.d(TAG, "loadUI: UI loaded.");

        // Okay button to confirm the changes
        // Will update the current workout values to the new values of the UI
        // Will call the Update function of the repo and go back
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Update the workout db
                float newWeight = Float.parseFloat(et_weight.getText().toString());
                int newRepS1 = Integer.parseInt(et_reps1.getText().toString());
                int newRepS2 = Integer.parseInt(et_reps2.getText().toString());
                int newRepS3 = Integer.parseInt(et_reps3.getText().toString());
                mWorkoutItem.setWeight(newWeight);
                mWorkoutItem.setRepsS1(newRepS1);
                mWorkoutItem.setRepsS2(newRepS2);
                mWorkoutItem.setRepsS3(newRepS3);

                Log.d(TAG, "onClick: WorkoutItem: " + mWorkoutItem);

                mWorkoutRepo.updateWorkout(mWorkoutItem);

                //Insert the new training log in the completed db
                mCompleted.setExercise(tv_exercise.getText().toString());
                mCompleted.setTimestamp(Utility.getCurrentTimestamp());
                mCompleted.setRepsS1(newRepS1);
                mCompleted.setRepsS2(newRepS2);
                mCompleted.setRepsS3(newRepS3);
                mCompleted.setTitle(mWorkoutItem.getTitle());
                mCompleted.setWeight(newWeight);

                Log.d(TAG, "onClick: Completed obj: " + mCompleted);

                mCompletedRepo.insertCompleteTask(mCompleted);

                onBackPressed();
            }
        });

        btn_add05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float weight = Float.parseFloat(et_weight.getText().toString());
                weight = weight + 0.5f;
                String update = Float.toString(weight);
                et_weight.setText(update);

            }
        });

        btn_add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float weight = Float.parseFloat(et_weight.getText().toString());
                weight = weight + 1f;
                String update = Float.toString(weight);
                et_weight.setText(update);

            }
        });

        btn_add0625.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float weight = Float.parseFloat(et_weight.getText().toString());
                weight = weight + 0.625f;
                String update = Float.toString(weight);
                et_weight.setText(update);

            }
        });

        btn_add25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float weight = Float.parseFloat(et_weight.getText().toString());
                weight = weight + 2.5f;
                String update = Float.toString(weight);
                et_weight.setText(update);

            }
        });

        btn_sub05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float weight = Float.parseFloat(et_weight.getText().toString());
                weight = weight - 0.5f;
                String update = Float.toString(weight);
                et_weight.setText(update);

            }
        });

        btn_sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float weight = Float.parseFloat(et_weight.getText().toString());
                weight = weight - 1f;
                String update = Float.toString(weight);
                et_weight.setText(update);

            }
        });
        btn_sub0625.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float weight = Float.parseFloat(et_weight.getText().toString());
                weight = weight - 0.625f;
                String update = Float.toString(weight);
                et_weight.setText(update);

            }
        });
        btn_sub25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float weight = Float.parseFloat(et_weight.getText().toString());
                weight = weight - 2.5f;
                String update = Float.toString(weight);
                et_weight.setText(update);

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
