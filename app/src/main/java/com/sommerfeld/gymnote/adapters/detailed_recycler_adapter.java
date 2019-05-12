package com.sommerfeld.gymnote.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sommerfeld.gymnote.R;
import com.sommerfeld.gymnote.models.Workout;

import java.util.ArrayList;

public class detailed_recycler_adapter extends RecyclerView.Adapter<detailed_recycler_adapter.ViewHolder> {
    private static final String TAG = "detailed_recycler_adapt";

    private ArrayList<String> mExercise;
    private ArrayList<String> mWeight;

    public detailed_recycler_adapter(ArrayList<String> exercise, ArrayList<String> weight) {
        this.mExercise = exercise;
        this.mWeight = weight;
       // Log.d(TAG, "detailed_recycler_adapter: " + mExercise + mWeight);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_workout_detail_list_item,viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: loaded: " + mExercise + mWeight);
        viewHolder.exercise.setText(mExercise.get(i));
        viewHolder.weight.setText(mWeight.get(i));
    }

    @Override
    public int getItemCount() {
        return mExercise.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView exercise, weight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exercise = itemView.findViewById(R.id.exercise_title);
            weight = itemView.findViewById(R.id.exercise_weight);
        }
    }
}
