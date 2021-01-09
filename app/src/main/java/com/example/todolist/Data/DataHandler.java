package com.example.todolist.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todolist.Model.ToDoModel;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

public class DataHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toDoDatabase";
    private static final String TABLE = "todo";
    private static final String ID = "id";
    private static final String TEXT = "text";
    private static final String LOADER = "loader";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TEXT + " TEXT, "
            + LOADER + " INTEGER)";
    private SQLiteDatabase db;

    public DataHandler(Context context){
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);

        onCreate(db);
    }

    public void openDataBase(){
        db = this.getWritableDatabase();
    }

    public void insertText(ToDoModel text) {
        ContentValues cv = new ContentValues();
        cv.put(TEXT, text.getText());
        cv.put(LOADER, 0);
        db.insert(TABLE, null, cv);
    }

    public List<ToDoModel> getAllText() {
        List<ToDoModel> textList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TABLE, null, null, null, null,null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ToDoModel text = new ToDoModel();
                        text.setId(cur.getInt(cur.getColumnIndex(ID)));
                        text.setText(cur.getString(cur.getColumnIndex(TEXT)));
                        text.setLoader(cur.getInt(cur.getColumnIndex(LOADER)));
                        textList.add(text);

                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return textList;
    }

    public void updateStatus(int id, int loader){
        ContentValues cv = new ContentValues();
        cv.put(LOADER, loader);
        db.update(TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateText(int id, String text){
        ContentValues cv = new ContentValues();
        cv.put(TEXT, text);
        db.update(TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteText(int id){
        db.delete(TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}
