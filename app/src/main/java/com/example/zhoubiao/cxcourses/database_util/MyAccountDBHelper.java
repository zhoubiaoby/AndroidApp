package com.example.zhoubiao.cxcourses.database_util;

/**
 * Created by zb-1 on 2015/10/7.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyAccountDBHelper extends SQLiteOpenHelper{

    //数据库名
    private static final String DATABASE_NAME = "account.db";
    // 表名
    private static final String TABLE_NAME = "account";

    // 创建SQL语句
    private static final String CREATE_TABLE =
            "create table account (_id integer primary key autoincrement,userName text, password text,desc text)";
    // SQLiteDatabase实例
    private SQLiteDatabase db;
    // 创建数据库
//	public MyAccountDBHelper(Context context, String name,
//			CursorFactory factory, int version) {
    public MyAccountDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        // TODO Auto-generated constructor stub
    }

    // 创建数据表
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TABLE);
    }
    // 更新数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    // 打开数据库
    public void open() {
        db = getWritableDatabase();
    }

    // 插入方法
    public void insert(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
    }

    // 查询方法
    public Cursor query() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        return c;
    }

    // 删除方法
    public void del(int id) {
        db.delete(TABLE_NAME, "_id=?", new String[] { String.valueOf(id) });
        Log.d("删除", "00");
    }
    public void del(String user_Name) {
        db.delete(TABLE_NAME, "userName=?", new String[] { user_Name });
        Log.d("删除", "00");
    }
//	public void del2(String data_userId) {
//		db.delete(TABLE_NAME, "user_Id=?", new String[] { data_userId });
//		Log.d("删除", "00");
//	}

    public void delall() {
        db.delete(TABLE_NAME, "desc=?", new String[] { "bupt" });
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
