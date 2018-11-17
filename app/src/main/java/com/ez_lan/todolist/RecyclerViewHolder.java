package com.ez_lan.todolist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = RecyclerViewHolder.class.getSimpleName();
    public ImageView deleteIcon;
    public TextView textTitle;
    public List<Task> taskObject;

    public RecyclerViewHolder(@NonNull View itemView, final List<Task> taskObject) {
        super(itemView);
        this.taskObject = taskObject;
        textTitle = itemView.findViewById(R.id.txt_title);
        deleteIcon = itemView.findViewById(R.id.task_delete);

        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Delete Icon Has Been Triggered", Toast.LENGTH_SHORT).show();
                String taskTitle = taskObject.get(getAdapterPosition()).getTask();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                final Query appleQuery = ref.orderByChild("task").equalTo(taskTitle);
                appleQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot applesnapshot : dataSnapshot.getChildren()) {
                            applesnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        });
    }
}
