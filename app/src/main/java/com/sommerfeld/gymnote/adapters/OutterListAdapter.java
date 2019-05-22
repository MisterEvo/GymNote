package com.sommerfeld.gymnote.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sommerfeld.gymnote.R;
import com.sommerfeld.gymnote.editWorkout;
import com.sommerfeld.gymnote.models.Workout;
import com.sommerfeld.gymnote.persistence.WorkoutRepo;

import java.util.ArrayList;

public class OutterListAdapter extends RecyclerView.Adapter<OutterListAdapter.ViewHolder> implements WorkoutListAdapterNew.onWorkoutListener {

    ArrayList<Workout> mGroupWorkouts = new ArrayList<>();
    ArrayList<Workout> groupWorkoutsFiltered = new ArrayList<>();
    Context mContext;
    private ArrayList<String> mGroup = new ArrayList<>();
    private WorkoutRepo mWorkoutRepo;

    public OutterListAdapter(Context context, ArrayList<String> Group, ArrayList<Workout> groupWorkoutsCon) {
        mContext = context;
        this.mGroup = Group;
        mGroupWorkouts = groupWorkoutsCon;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.outter_rc_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        groupWorkoutsFiltered = new ArrayList<>();
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
        WorkoutListAdapterNew adapter = new WorkoutListAdapterNew(groupWorkoutsFiltered, this);
        holder.innerRV.setAdapter(adapter);

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

        TextView groupName;
        RecyclerView innerRV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.tv_outter_group_name);
            innerRV = itemView.findViewById(R.id.rv_inner);
        }
    }
}
