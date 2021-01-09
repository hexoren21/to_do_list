package com.example.todolist.ToDoAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.AddNewText;
import com.example.todolist.Data.DataHandler;
import com.example.todolist.MainActivity;
import com.example.todolist.Model.ToDoModel;
import com.example.todolist.R;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder>{
    private List<ToDoModel> todoList;
    private MainActivity mainActivity;
    private DataHandler db;

    public ToDoAdapter(DataHandler db, MainActivity mainActivity) {
        this.db = db;
        this.mainActivity = mainActivity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_layout, parent, false);
        return new ViewHolder(itemView);
    }
    public void onBindViewHolder(ViewHolder holder, int position) {
        db.openDataBase();
        ToDoModel item = todoList.get(position);
        holder.task.setText(item.getText());
        holder.task.setChecked(toBoolean(item.getLoader()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    public int getItemCount(){
        return todoList.size();
    }
    private boolean toBoolean(int n){
        return n!=0;
    }
    public Context getContext(){
        return mainActivity;
    }
    public void deleteItem(int position){
        ToDoModel item = todoList.get(position);
        db.deleteText(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void setText(List<ToDoModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public void editItem(int position){
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("text", item.getText());
        AddNewText fragment = new AddNewText();
        fragment.setArguments(bundle);
        fragment.show(mainActivity.getSupportFragmentManager(), AddNewText.TAG);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;

        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
