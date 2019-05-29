package com.sommerfeld.gymnote.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sommerfeld.gymnote.R;
import com.sommerfeld.gymnote.models.Completed;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class LogListAdapter extends RecyclerView.Adapter<LogListAdapter.ViewHolder> implements Filterable {

    ArrayList<Completed> mCompleted = new ArrayList<>();
    ArrayList<Completed> mCompletedFull;

    private OnLogListener mOnLogListener;

    private Filter CompletedFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Completed> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mCompletedFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Completed item : mCompletedFull) {
                    if (item.getExercise().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                        Log.d(TAG, "performFiltering: Filtered: " + item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mCompleted.clear();
            mCompleted.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public LogListAdapter(ArrayList<Completed> Completed, OnLogListener onLogListener) {
        this.mCompleted = Completed;
        this.mOnLogListener = onLogListener;
        Log.d(TAG, "LogListAdapter: mCompleted: " + mCompleted);
        mCompletedFull = new ArrayList<>(mCompleted);
        Log.d(TAG, "LogListAdapter: CopyFull: " + mCompletedFull);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loglistitem, parent, false);
        return new ViewHolder(view, mOnLogListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.timestamp.setText(mCompleted.get(position).getTimestamp());
        holder.exercise.setText(mCompleted.get(position).getExercise());
        String weight = Float.toString(mCompleted.get(position).getWeight());
        holder.weight.setText(weight);
    }

    @Override
    public int getItemCount() {
        if (mCompleted != null) {
            return mCompleted.size();
        } else {
            return 0;
        }

    }

    @Override
    public Filter getFilter() {
        return CompletedFilter;
    }

    public interface OnLogListener {
        void onLogLongClick(int id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView timestamp, exercise, weight;
        OnLogListener onLogListener;

        public ViewHolder(@NonNull View itemView, OnLogListener onLogListener) {
            super(itemView);
            timestamp = itemView.findViewById(R.id.tv_timestamp);
            exercise = itemView.findViewById(R.id.tv_Exercise);
            weight = itemView.findViewById(R.id.tv_weight);
            this.onLogListener = onLogListener;
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            onLogListener.onLogLongClick(mCompleted.get(getAdapterPosition()).getId());
            return false;
        }
    }
}
