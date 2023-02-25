package com.example.easytodo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.textclassifier.ConversationAction;

import java.util.ArrayList;
import java.util.List;

public class CRUD {
    SQLiteOpenHelper dbHandler;//数据库处理器
    SQLiteDatabase db;//数据库
    private static final String[] columns = {
            NoteDatabase.ID,
            NoteDatabase.CONTENT,
            NoteDatabase.TIME,
            NoteDatabase.MODE
    };

//    在这里调用NoteDatabase的构造函数，创建数据库
    public CRUD(Context context) {
        dbHandler = new NoteDatabase(context);
    }
    //打开数据库
    public void open(){
        db = dbHandler.getWritableDatabase();
    }
    //关闭数据库
    public void close(){
        dbHandler.close();
    }

    //把一个note元组加入到database里面
    public Note addNote(Note note){
        //ContentValues是啥？内容值吗
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteDatabase.CONTENT, note.getContent());
        contentValues.put(NoteDatabase.TIME, note.getTime());
        contentValues.put(NoteDatabase.MODE, note.getTag());
//        插入一个contentValues返回一个long类型的值，代表插入元组的ID
        long insertId = db.insert(NoteDatabase.TABLE_NAME, null, contentValues);
        note.setId(insertId);
        return note;
    }
//    根据ID查询一个数组
    public Note getNote(long id){
        //定义一个指针
        Cursor cursor = db.query(NoteDatabase.TABLE_NAME, columns, NoteDatabase.ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);
//        查询出来的cursor的初始位置是指向第一条记录的前一个位置的
//        cursor.moveToFirst（）指向查询结果的第一个位置。
//        一般通过判断cursor.moveToFirst()的值为true或false来确定查询结果是否为空。cursor.moveToNext()是用来做循环的，一般这样来用：while(cursor.moveToNext()){ }
//        cursor.moveToPrevious()是指向当前记录的上一个记录，是和moveToNext相对应的；
//        cursor.moveToLast()指向查询结果的最后一条记录 使用cursor可以很方便的处理查询结果以便得到想要的数据
        if (cursor != null) cursor.moveToFirst();
        Note e = new Note(cursor.getString(1), cursor.getString(2), cursor.getInt(3));
        return e;
    }
//    获取所有的笔记记录
    @SuppressLint("Range")
    public List<Note> getAllNotes(){
        Cursor cursor = db.query(NoteDatabase.TABLE_NAME, columns, null, null, null, null, null);
        List<Note> notes = new ArrayList<>();
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                Note note = new Note();
                note.setId(cursor.getLong(cursor.getColumnIndex(NoteDatabase.ID)));
                note.setContent(cursor.getString(cursor.getColumnIndex(NoteDatabase.CONTENT)));
                note.setTime(cursor.getString(cursor.getColumnIndex(NoteDatabase.TIME)));
                note.setTag(cursor.getInt(cursor.getColumnIndex(NoteDatabase.MODE)));
                notes.add(note);
            }
        }
        return notes;
    }

    public int updateNote(Note note) {
        //update the info of an existing note
        ContentValues values = new ContentValues();
        values.put(NoteDatabase.CONTENT, note.getContent());
        values.put(NoteDatabase.TIME, note.getTime());
        values.put(NoteDatabase.MODE, note.getTag());
        //updating row
        return db.update(NoteDatabase.TABLE_NAME, values,
                NoteDatabase.ID + "=?", new String[] { String.valueOf(note.getId())});
    }

    public void removeNote(Note note){
        //remove a note according to ID value
        db.delete(NoteDatabase.TABLE_NAME, NoteDatabase.ID + "=" + note.getId(), null);
    }

}



