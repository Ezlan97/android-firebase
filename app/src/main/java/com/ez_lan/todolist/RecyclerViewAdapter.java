package com.ez_lan.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    //private static final Date LayoutInFlater = null;
    private List<Task> tasks;
    protected Context context;

    public RecyclerViewAdapter(List<Task> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        RecyclerViewHolder viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_list, parent, false);
        viewHolder = new RecyclerViewHolder(layoutView, tasks);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.textTitle.setText(tasks.get(position).getTask());
    }

    @Override
    public int getItemCount() {
        return this.tasks.size();
    }
}
