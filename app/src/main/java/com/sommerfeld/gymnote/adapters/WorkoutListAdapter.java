package com.sommerfeld.gymnote.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sommerfeld.gymnote.R;
import com.sommerfeld.gymnote.listener.OnStartDragListener;
import com.sommerfeld.gymnote.listener.OnWorkoutListChangedListener;
import com.sommerfeld.gymnote.models.Workout;
import com.sommerfeld.gymnote.persistence.WorkoutRepo;
import com.sommerfeld.gymnote.util.ItemTouchHelperAdapter;
import com.sommerfeld.gymnote.util.ItemTouchHelperViewHolder;

import java.util.ArrayList;
import java.util.Collections;

public class WorkoutListAdapter extends RecyclerView.Adapter<WorkoutListAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

    private static final String TAG = "WorkoutListAdapter";
    private ArrayList<Workout> mWorkouts;
    private Context mContext;
    private WorkoutRepo mWorkoutRepo;
    private OnStartDragListener mDragStartListener;
    private OnWorkoutListChangedListener mListChangedListener;
    private OnWorkoutListenerNew mOnWorkoutListenerNew;


    public WorkoutListAdapter(ArrayList<Workout> mWorkouts, Context mContext, OnStartDragListener mDragStartListener, OnWorkoutListChangedListener mListChangedListener, OnWorkoutListenerNew mOnWorkoutListenerNew) {
        this.mWorkouts = mWorkouts;
        this.mContext = mContext;
        this.mDragStartListener = mDragStartListener;
        this.mListChangedListener = mListChangedListener;
        this.mOnWorkoutListenerNew = mOnWorkoutListenerNew;
    }

    @NonNull
    @Override
    public WorkoutListAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_workout_list_item, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(rowView, mOnWorkoutListenerNew);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final WorkoutListAdapter.ItemViewHolder holder, int position) {
        final Workout selectedWorkout = mWorkouts.get(position);

        holder.workoutTitle.setText(selectedWorkout.getExercise());
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWorkouts.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mWorkouts, fromPosition, toPosition);
        mListChangedListener.onWorkoutListChanged(mWorkouts);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        Log.d(TAG, "onItemDismiss: Position:" + position);
        //Convert position to ID
        int id = mWorkouts.get(position).getId();
        Log.d(TAG, "onItemDismiss: ID des gelöschen Elements: " + id);
        int index = findIndex(mWorkouts, id);
        Log.d(TAG, "onItemDismiss: Index in DB: " + index);
        Log.d(TAG, "onItemDismiss: delete exercise: " + mWorkouts.get(index).getExercise());
        mWorkoutRepo = new WorkoutRepo(mContext);
        Log.d(TAG, "onItemDismiss: List Del Item: " + mWorkouts.get(index));
        mWorkoutRepo.deleteWorkouts(mWorkouts.get(index));
        mWorkouts.remove(index);
        notifyDataSetChanged();
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

    public interface OnWorkoutListenerNew {
        void onWorkoutClick(int position);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder, View.OnClickListener {

        public final TextView workoutTitle;
        public final ImageView handleView;
        OnWorkoutListenerNew OnWorkoutListenerneu;

        public ItemViewHolder(@NonNull View itemView, OnWorkoutListenerNew onWorkoutListenerNew) {
            super(itemView);
            workoutTitle = itemView.findViewById(R.id.workout_title);
            handleView = itemView.findViewById(R.id.handle);
            this.OnWorkoutListenerneu = onWorkoutListenerNew;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);

        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.rgb(19, 51, 55));
        }

        @Override
        public void onClick(View v) {
            OnWorkoutListenerneu.onWorkoutClick(getAdapterPosition());
        }
    }
}
