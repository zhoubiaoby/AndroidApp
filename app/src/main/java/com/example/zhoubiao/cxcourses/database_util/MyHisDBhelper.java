package com.example.zhoubiao.cxcourses.database_util;

/**
 * Created by zb-1 on 2015/10/7.
 */
/*收藏夹列表的存储使用Sqlite，此类用于创建数据库，并定义对数据库的操作  记录的表  dataID  播放时间  duration
 * 包括插入、删除 * */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MyHisDBhelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "his.db";

    // 表名

    private static final String TBL_NAME = "his";

    // 创建SQL语句

    private static final String CREATE_TBL = "create table his " +

            "(_id integer primary key autoincrement,course_Name text, lesson_Id text, lesson_Num text, lesson_Name text, duration text,desc text)";

    // SQLiteDatabase实例

    private SQLiteDatabase db;

    public MyHisDBhelper(Context context) {

        super(context, DB_NAME, null, 2);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TBL);

    }

    // 打开数据库

    public void open() {

        db = getWritableDatabase();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // TODO Auto-generated method stub

    }

    // 插入方法

    public void insert(ContentValues values) {

        db.insert(TBL_NAME, null, values);

    }

    // 查询方法

    public Cursor query() {

        Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);

        return c;

    }

    // 删除方法

    public void del(int id) {

        db.delete(TBL_NAME, "_id=?", new String[] { String.valueOf(id) });
        Log.d("删除", "00");

    }
    public void del(String lesson_id) {

        db.delete(TBL_NAME, "lesson_Id=?", new String[] { lesson_id });
        Log.d("删除", "00");

    }

    public void delall() {

        db.delete(TBL_NAME, "desc=?", new String[] { "bupt" });
        Log.d("删除", "00");

    }

    // 关闭数据库

    @Override
    public void close() {

        if (db != null) {

            db.close();

        }

    }

}
