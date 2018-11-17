package com.ez_lan.todolist;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private  RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    private EditText addTaskBox;
    private Button addTaskButton;
    private List<Task> allTask;
    //reference to firebase database
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        allTask = new ArrayList<Task>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.task_list);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        addTaskBox = findViewById(R.id.et_add_task);
        addTaskButton = findViewById(R.id.btn_add_task);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 // get text from editText
                                                 String enteredTask = addTaskBox.getText().toString();

                                                 //check if it is empty or less than 6 character
                                                 if (TextUtils.isEmpty(enteredTask)) {
                                                     Toast.makeText(MainActivity.this, "You need ot input something", Toast.LENGTH_SHORT).show();
                                                     return;
                                                 } else if (enteredTask.length() < 6) {
                                                     Toast.makeText(MainActivity.this, "Too short please make it longer", Toast.LENGTH_SHORT).show();
                                                 } else {
                                                     //create data in firebase
                                                     Task taskObject = new Task(enteredTask);
                                                     databaseReference.push().setValue(taskObject);
                                                     addTaskBox.setText("");
                                                 }
                                             }
                                         });

                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        getAllTask(dataSnapshot);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        getAllTask(dataSnapshot);
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        taskDeletion(dataSnapshot);
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    //create task
    private  void getAllTask(DataSnapshot dataSnapshot) {
        for(DataSnapshot singlesnapshots : dataSnapshot.getChildren()) {
            String tasktitle = singlesnapshots.getValue(String.class);
            allTask.add(new Task(tasktitle));
            recyclerViewAdapter = new RecyclerViewAdapter(allTask, MainActivity.this);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }

    //delete task
    private void taskDeletion(DataSnapshot dataSnapshot) {
        for (DataSnapshot singlesnapshots : dataSnapshot.getChildren()) {
            String taskTitle = singlesnapshots.getValue(String.class);
            for (int i=0; i<allTask.size(); i++){
                if (allTask.get(i).getTask().equals(taskTitle)) {
                    allTask.remove(i);
                }
            }
            Log.d(TAG, "Task Title" + taskTitle);
            recyclerViewAdapter.notifyDataSetChanged();
            recyclerViewAdapter = new RecyclerViewAdapter(allTask, MainActivity.this);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }
}
