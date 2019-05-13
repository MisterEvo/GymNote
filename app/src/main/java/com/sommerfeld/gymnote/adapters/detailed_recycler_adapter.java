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
    private OnExerciseListner mOnExerciseListener;

    public detailed_recycler_adapter(ArrayList<String> exercise, ArrayList<String> weight, OnExerciseListner onExerciseListner) {
        this.mExercise = exercise;
        this.mWeight = weight;
        this.mOnExerciseListener = onExerciseListner;
       // Log.d(TAG, "detailed_recycler_adapter: " + mExercise + mWeight);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_workout_detail_list_item,viewGroup, false);
        return new ViewHolder(view, mOnExerciseListener);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView exercise, weight;
        OnExerciseListner onExerciseListner;

        public ViewHolder(@NonNull View itemView, OnExerciseListner onExerciseListner) {
            super(itemView);
            exercise = itemView.findViewById(R.id.exercise_title);
            weight = itemView.findViewById(R.id.exercise_weight);
            this.onExerciseListner = onExerciseListner;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onExerciseListner.onExerciseClick(getAdapterPosition());
        }
    }

    public interface OnExerciseListner {
        void onExerciseClick(int position);
    }
}
