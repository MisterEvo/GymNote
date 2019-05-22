package com.sommerfeld.gymnote.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sommerfeld.gymnote.R;
import com.sommerfeld.gymnote.models.Workout;
import com.sommerfeld.gymnote.persistence.WorkoutRepo;

import java.util.ArrayList;

public class WorkoutListAdapterNew extends RecyclerView.Adapter<WorkoutListAdapterNew.ItemViewHolder> {

    private static final String TAG = "WorkoutListAdapter";
    private ArrayList<Workout> mWorkouts;
    private WorkoutRepo mWorkoutRepo;
    private onWorkoutListener mOnWorkoutListener;


    public WorkoutListAdapterNew(ArrayList<Workout> mWorkouts, onWorkoutListener onWorkoutListener) {
        this.mWorkouts = mWorkouts;
        this.mOnWorkoutListener = onWorkoutListener;
    }

    @NonNull
    @Override
    public WorkoutListAdapterNew.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_workout_list_item, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(rowView, mOnWorkoutListener);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final WorkoutListAdapterNew.ItemViewHolder holder, int position) {
        final Workout selectedWorkout = mWorkouts.get(position);

        holder.workoutTitle.setText(selectedWorkout.getExercise());
    }

    @Override
    public int getItemCount() {
        return mWorkouts.size();
    }


    public int findIndex(ArrayList<Workout> WorkoutArr, int t) {

        int len = WorkoutArr.size();
        int i = 0;

        while (i < len) {
            Log.d(TAG, "findIndex: " + WorkoutArr.get(i).getId());
            if (WorkoutArr.get(i).getId() == t) {
                return i;
            } else {
                i = i + 1;
            }
        }
        return -1;
    }

    public interface onWorkoutListener {
        void onWorkoutClick(int position, int itemID);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView workoutTitle;
        public final ImageView handleView;
        onWorkoutListener onWorkoutListener;

        public ItemViewHolder(@NonNull View itemView, onWorkoutListener onWorkoutListener) {
            super(itemView);
            workoutTitle = itemView.findViewById(R.id.workout_title);
            handleView = itemView.findViewById(R.id.handle);
            this.onWorkoutListener = onWorkoutListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onWorkoutListener.onWorkoutClick(getAdapterPosition(), mWorkouts.get(getAdapterPosition()).getId());
        }
    }
}
