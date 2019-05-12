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

public class WorkoutRecyclerAdapter extends RecyclerView.Adapter<WorkoutRecyclerAdapter.ViewHolder> {

    private static final String TAG = "WorkoutRecyclerAdapter";


    private ArrayList<Workout> mWorkout = new ArrayList<>();
    private OnWorkoutListener mOnWorkoutListener;

    public WorkoutRecyclerAdapter(ArrayList<Workout> workout, OnWorkoutListener onWorkoutListener) {
        this.mWorkout = workout;
        this.mOnWorkoutListener = onWorkoutListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_workout_list_item, viewGroup, false);
        return new ViewHolder(view, mOnWorkoutListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
       // Log.d(TAG, "onBindViewHolder: " + mWorkout.get(i).getTitle());
        viewHolder.title.setText(mWorkout.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return mWorkout.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        OnWorkoutListener onWorkoutListener;

        public ViewHolder(@NonNull View itemView, OnWorkoutListener onWorkoutListener) {
            super(itemView);
            title = itemView.findViewById(R.id.workout_title);
            this.onWorkoutListener = onWorkoutListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onWorkoutListener.onWorkoutClick(getAdapterPosition());

        }
    }

    public interface OnWorkoutListener {
        void onWorkoutClick(int position);
    }

}
