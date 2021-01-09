package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.todolist.Data.DataHandler;
import com.example.todolist.Model.ToDoModel;
import com.example.todolist.ToDoAdapter.ToDoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView recyclerView;
    private ToDoAdapter toDoAdapter;
    private FloatingActionButton actionButton;
    private List<ToDoModel> textList;
    private DataHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();

        db = new DataHandler(this);
        db.openDataBase();
        textList = new ArrayList<>();
        recyclerView = findViewById(R.id.tasksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toDoAdapter = new ToDoAdapter(db,this);
        recyclerView.setAdapter(toDoAdapter);
        actionButton = findViewById(R.id.actionButton);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecycleItemTouchHelper(toDoAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        textList = db.getAllText();
        Collections.reverse(textList);
        toDoAdapter.setText(textList);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewText.newInstance().show(getSupportFragmentManager(), AddNewText.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        textList = db.getAllText();
        Collections.reverse(textList);
        toDoAdapter.setText(textList);
        toDoAdapter.notifyDataSetChanged();
    }
}