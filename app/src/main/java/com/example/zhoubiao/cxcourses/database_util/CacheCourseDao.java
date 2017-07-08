package com.example.zhoubiao.cxcourses.database_util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.zhoubiao.cxcourses.dataobject.Course;
import com.example.zhoubiao.cxcourses.dataobject.LocalCourse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoubiao on 2015/10/21.
 */
public class CacheCourseDao {
    private Context context;
    private DBOpenHelper helper;
    private SQLiteDatabase db;
    public CacheCourseDao(Context context){
        this.context = context;
        helper = new DBOpenHelper(context);
    }
    /*
    添加本地下载课程
    @param  localCourse
     */
    public void add(Course course){
        db = helper.getWritableDatabase();
        db.execSQL("insert into cache_course(name,image_path,video_path,is_finish) values(?,?,?,?)",new Object[]{
                course.name,course.imageUrl,course.videoUrl,course.isRecommend == true ?1:0
        });

    }
    public void delete(String name){
        db = helper.getWritableDatabase();
        db.execSQL("delete from cache_course where name = ?",new Object[]{name});
    }
    public void truncateTable(){
        db = helper.getWritableDatabase();
        db.execSQL("truncate table cache_course");
    }
    public Course find(String name){
        db= helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select name,image_path,video_path,is_finish from cache_course where name = ?",new String[]{
                name});
        if(cursor.moveToNext()){
            return new Course(cursor.getString(cursor.getColumnIndex("name")),cursor.getString(cursor.getColumnIndex("image_path"))
                    ,(cursor.getInt(cursor.getColumnIndex("is_finish")) == 1)?true:false,cursor.getString(cursor.getColumnIndex("video_path")));
        }
        return null;
    }

    public List<Course> getScrollData(){
        List<Course> Courses = new ArrayList<Course>();
        db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from cache_course",null);
        while (cursor.moveToNext()){
            Courses.add(new Course(cursor.getString(cursor.getColumnIndex("name")),cursor.getString(cursor.getColumnIndex("image_path"))
                    ,(cursor.getInt(cursor.getColumnIndex("is_finish")) == 1)?true:false,cursor.getString(cursor.getColumnIndex("video_path"))));
        }
        return Courses;
    }
}
