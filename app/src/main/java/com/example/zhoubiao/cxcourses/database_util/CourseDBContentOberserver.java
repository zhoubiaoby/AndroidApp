package com.example.zhoubiao.cxcourses.database_util;

import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * Created by zhoubiao on 2015/10/13.
 */
public class CourseDBContentOberserver extends ContentObserver {
    private Handler handler;
    /**
     * Creates a content observer.
     *
     * @param activity
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public CourseDBContentOberserver(FragmentActivity activity, Handler handler) {
        super(handler);
        this.handler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.v("CourseDBContentObserver", "onChange");
        Message message = handler.obtainMessage();
        message.what = 1;
        handler.sendMessage(message);
    }
}
