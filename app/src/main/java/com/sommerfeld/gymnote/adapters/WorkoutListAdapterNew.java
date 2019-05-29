package com.sommerfeld.gymnote.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.sommerfeld.gymnote.R;
import com.sommerfeld.gymnote.models.Workout;
import com.sommerfeld.gymnote.persistence.WorkoutRepo;
import com.sommerfeld.gymnote.util.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.List;

public class WorkoutListAdapterNew extends RecyclerView.Adapter<WorkoutListAdapterNew.ItemViewHolder> implements ItemTouchHelperAdapter {

    private static final String PREFS_FILE = "pref_file";
    private final ArrayList<Workout> mWorkouts;
    private final onWorkoutListener mOnWorkoutListener;
    private static final String TAG = "WorkoutListAdapterNew";
    private ItemTouchHelper mTouchHelper;
    private final WorkoutRepo mWorkoutRepo;
    private final Context mContext;


    public WorkoutListAdapterNew(Context context, ArrayList<Workout> mWorkouts, onWorkoutListener onWorkoutListener, WorkoutRepo workoutRepo) {
        this.mContext = context;
        this.mWorkouts = mWorkouts;
        this.mOnWorkoutListener = onWorkoutListener;
        this.mWorkoutRepo = workoutRepo;
    }

    @NonNull
    @Override
    public WorkoutListAdapterNew.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_workout_list_item, parent, false);
        //noinspection UnnecessaryLocalVariable
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

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Workout fromWorkout = mWorkouts.get(fromPosition);
        mWorkouts.remove(fromWorkout);
        mWorkouts.add(toPosition, fromWorkout);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        Workout tempWorkout = mWorkouts.get(position);
        mWorkoutRepo.deleteWorkouts(tempWorkout);
        mWorkouts.remove(position);

        notifyItemRemoved(position);
    }

    @Override
    public void saveOrder() {
        //mWorkouts contains only the workouts out of the respective recview
        SharedPreferences mSharedPrefs = mContext.getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPrefs.edit();
        mEditor.apply();

        List<Integer> sortedList = new ArrayList<>();

        //Collects the ids of all the workouts in their current order
        for (Workout workout : mWorkouts) {
            sortedList.add(workout.getId());
        }
        //Put IDs into JSON
        Gson gson = new Gson();
        String jsonSortedList = gson.toJson(sortedList);

        //Save json to Shared Prefs
        mEditor.putString(mWorkouts.get(0).getTitle(), jsonSortedList).commit();
        mEditor.commit();
        Toast.makeText(mContext, "Saved order: " + jsonSortedList + " in: " + mWorkouts.get(0).getTitle(), Toast.LENGTH_SHORT).show();
    }

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.mTouchHelper = touchHelper;
    }

    public interface onWorkoutListener {
        void onWorkoutClick(int position, int itemID);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements
            View.OnTouchListener,
            GestureDetector.OnGestureListener {

        final TextView workoutTitle;
        final ImageView handleView;
        final onWorkoutListener onWorkoutListener;
        final GestureDetector mGestureDetector;


        ItemViewHolder(@NonNull View itemView, onWorkoutListener onWorkoutListener) {
            super(itemView);
            workoutTitle = itemView.findViewById(R.id.workout_title);
            handleView = itemView.findViewById(R.id.handle);
            this.onWorkoutListener = onWorkoutListener;

            mGestureDetector = new GestureDetector(itemView.getContext(), this);
            itemView.setOnTouchListener(this);
        }


        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d(TAG, "onSingleTapUp: Tapped");
            onWorkoutListener.onWorkoutClick(getAdapterPosition(), mWorkouts.get(getAdapterPosition()).getId());
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            mTouchHelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mGestureDetector.onTouchEvent(event);
            return true;
        }
    }
}
