package com.example.easytodo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;//该类封装了一些操作数据库的API：增删改查等，可以执行SQL命令
import android.database.sqlite.SQLiteOpenHelper;//是一个数据库创建和版本管理的帮助类


//SQLite是一个进程内的轻量级嵌入式数据库，它的数据库就是一个文件，实现了自给自足、无服务器、零配置的、事务性的SQL数据库引擎。
//不需要配置，不需要安装和管理
//不需要一个单独的服务器进程或操作的系统（无服务器的）
// 一个完整的SQLite数据库存储在一个单一的跨平台的磁盘文件上
//SQLite是非常小的，轻量级的数据库，完全配置时小于400KB
//SQLite是一个自给自足的数据库，这也就意味着不需要任何外部的依赖

//SQLiteOpenHelper是一个抽象类，使用时需要创建自己的类去继承它
//创建和升级数据库
public class NoteDatabase extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "notes";
    public static final String CONTENT = "content";
    public static final String ID = "_id";
    public static final String TIME = "time";
    public static final String MODE = "mode";

//    创建数据库
    /**
     * 作为SQLiteOpenHelper子类必须有的构造方法
     * context 上下文参数
     * name 数据库名字
     * factory 游标工厂 ，通常是null
     * version 数据库的版本
     */
    public NoteDatabase(Context context){
        super(context, TABLE_NAME, null, 1);
    }

//    使用标准SQL语句创建表格
//    数据库第一次被创建时调用该方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME
                + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CONTENT + " TEXT NOT NULL,"
                + TIME + " TEXT NOT NULL,"
                + MODE + " INTEGER DEFAULT 1)"
        );
    }

    //检测已有版本和新版本，升级数据库
    //当数据库版本号增加时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

