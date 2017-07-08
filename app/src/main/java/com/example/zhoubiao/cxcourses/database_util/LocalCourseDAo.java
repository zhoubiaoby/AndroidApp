package com.example.zhoubiao.cxcourses.database_util;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.zhoubiao.cxcourses.dataobject.LocalCourse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoubiao on 2015/10/10.
 */
public class LocalCourseDAo {

    public static final Uri COURSE_CONTENT_URI = Uri
            .parse("content://com.example.zhoubiao.cxcourses/localcourses");
    private static final int COURSES = 1;
    private static final UriMatcher uriMatcher;
    private Context context;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.zhoubiao.cxcourses/localcourses", "courses", COURSES);
    }
    private DBOpenHelper helper;
    private SQLiteDatabase db;
    public LocalCourseDAo(Context context){
        this.context = context;
        helper = new DBOpenHelper(context);
    }
    /*
    添加本地下载课程
    @param  localCourse
     */
    public void add(LocalCourse localCourse){
    db = helper.getWritableDatabase();
        db.execSQL("insert into local_course(name,local_path,is_finish) values(?,?,?)",new Object[]{
                localCourse.getName(),localCourse.getLocalPath(),localCourse.getIsFinished()
        });
        context.getContentResolver().notifyChange(COURSE_CONTENT_URI,null);
    }
   public void delete(String name){
       db = helper.getWritableDatabase();
       db.execSQL("delete from local_course where name = ?",new Object[]{name});
   }
    public LocalCourse find(String name){
        db= helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select name,local_path,is_finish from local_course where name = ?",new String[]{
                name});
        if(cursor.moveToNext()){
            return new LocalCourse(cursor.getString(cursor.getColumnIndex("name")),cursor.getString(cursor.getColumnIndex("local_path")),
                    cursor.getInt(cursor.getColumnIndex("is_finish")));
        }
        return null;
    }

    public List<LocalCourse> getScrollData(){
        List<LocalCourse> localCourses = new ArrayList<LocalCourse>();
        db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from local_course",null);
        while (cursor.moveToNext()){
            localCourses.add(new LocalCourse(cursor.getString(cursor.getColumnIndex("name")),cursor.getString(cursor.getColumnIndex("local_path")),
                    cursor.getInt(cursor.getColumnIndex("is_finish"))));
        }
        return localCourses;
    }

}
