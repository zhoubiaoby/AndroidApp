package com.example.zhoubiao.cxcourses.database_util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhoubiao on 2015/10/10.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DBNAME = "mycourse.db";
    public DBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table local_course(name varchar(50) primary key,local_path varchar(225),is_finish integer)");
        db.execSQL("create table cache_course(name varchar(50) primary key,image_path varchar(225),video_path varchar(225),is_finish integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
