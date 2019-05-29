package com.sommerfeld.gymnote.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sommerfeld.gymnote.R;
import com.sommerfeld.gymnote.editWorkout;
import com.sommerfeld.gymnote.models.Workout;
import com.sommerfeld.gymnote.persistence.WorkoutRepo;
import com.sommerfeld.gymnote.util.WorkoutItemTouchHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class OutterListAdapter extends RecyclerView.Adapter<OutterListAdapter.ViewHolder> implements WorkoutListAdapterNew.onWorkoutListener {

    private static final String PREFS_FILE = "pref_file";
    private static final String TAG = "OutterAdapter";
    private ArrayList<Workout> mGroupWorkouts = new ArrayList<>();
    private final Context mContext;
    private ArrayList<String> mGroup = new ArrayList<>();
    private final WorkoutRepo mWorkoutRepo;

    public OutterListAdapter(Context context, ArrayList<String> Group, ArrayList<Workout> groupWorkoutsCon, WorkoutRepo workoutRepo) {
        mContext = context;
        this.mGroup = Group;
        mGroupWorkouts = groupWorkoutsCon;
        this.mWorkoutRepo = workoutRepo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.outter_rc_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ArrayList<Workout> groupWorkoutsFiltered = new ArrayList<>();

        holder.groupName.setText(mGroup.get(position));
        holder.innerRV.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(holder.innerRV.getContext());
        holder.innerRV.setLayoutManager(layoutManager);
        for (Workout workout : mGroupWorkouts) {
            if (workout.getTitle().equals(mGroup.get(position))) {
                groupWorkoutsFiltered.add(workout);
            }
        }
        Log.d(TAG, "onBindViewHolder: Bind groupWorkout: " + groupWorkoutsFiltered);
        ArrayList<Workout> innerWorkouts = loadSorted(groupWorkoutsFiltered);

        WorkoutListAdapterNew adapter = new WorkoutListAdapterNew(mContext, innerWorkouts, this, mWorkoutRepo);
        ItemTouchHelper.Callback callback = new WorkoutItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        adapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(holder.innerRV);
        holder.innerRV.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .colorResId(R.color.colorPrimaryDark).size(2).build());
        holder.innerRV.setAdapter(adapter);

    }

    private ArrayList<Workout> loadSorted(ArrayList<Workout> unsorted) {
        SharedPreferences mSharedPrefs = mContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPrefs.edit();
        mEditor.apply();

        ArrayList<Workout> sortedWorkout = new ArrayList<>();
        String key = unsorted.get(0).getTitle();
        Log.d(TAG, "loadSorted: Key for SP: " + key);

        Log.d(TAG, "loadSorted: Get JSON:" + mSharedPrefs.getString(key, ""));
        String jsonListOfSortedWorkoutId = mSharedPrefs.getString(unsorted.get(0).getTitle(), "");

        if (!jsonListOfSortedWorkoutId.isEmpty()) {
            Gson gson = new Gson();
            List<Integer> listOfSortedWorkout = gson.fromJson(jsonListOfSortedWorkoutId, new TypeToken<List<Integer>>() {
            }.getType());

            //build sorted list
            if (listOfSortedWorkout != null && listOfSortedWorkout.size() > 0) {

                for (int id : listOfSortedWorkout) {
                    for (Workout workout : unsorted) {
                        if (workout.getId() == id) {
                            sortedWorkout.add(workout);
                            unsorted.remove(workout);
                            break;
                        }
                    }
                }

            }

            if (unsorted.size() > 0) {
                sortedWorkout.addAll(unsorted);
            }
            Log.d(TAG, "getSortedData: SortedList: " + sortedWorkout);
            return sortedWorkout;

        } else {
            return unsorted;
        }
    }


    @Override
    public int getItemCount() {
        return mGroup.size();
    }

    @Override
    public void onWorkoutClick(int position, int itemID) {

        Intent i = new Intent(mContext, editWorkout.class);
        i.putExtra("selected_item", itemID);
        mContext.startActivity(i);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView groupName;
        final RecyclerView innerRV;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.tv_outter_group_name);
            innerRV = itemView.findViewById(R.id.rv_inner);
        }
    }
}

