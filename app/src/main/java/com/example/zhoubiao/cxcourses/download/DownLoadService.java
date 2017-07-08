package com.example.zhoubiao.cxcourses.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhoubiao on 2015/10/8.
 */
public class DownLoadService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("DownLoadService","onStartCommadn()方法");
        String url = intent.getStringExtra("url");
        String courseName = intent.getStringExtra("courseName");

        Log.v("DownLoadService","url-->"+url);
        Log.v("DownLoadService","courName-->"+courseName);
        VideoDownloadUtils videoDownloadUtils = VideoDownloadUtils.getVideoDownloadUtils();
        videoDownloadUtils.download(this,url,courseName);
        return START_NOT_STICKY;
    }
}
