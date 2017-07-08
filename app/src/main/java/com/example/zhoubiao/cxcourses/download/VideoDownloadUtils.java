package com.example.zhoubiao.cxcourses.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.zhoubiao.cxcourses.R;
import com.example.zhoubiao.cxcourses.database_util.LocalCourseDAo;
import com.example.zhoubiao.cxcourses.dataobject.LocalCourse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by zhoubiao on 2015/8/24.
 */
public class VideoDownloadUtils extends BroadcastReceiver{
    private Context context;
    private int progress;
    private int size;
    private int current;
    private int times = 0;
    private String courseName;
    private static RemoteViews contentView;
    private NotificationManager manager;
    private Notification notification;
    private File file;
    private boolean isfinished =false;
    private static ExecutorService pool = Executors.newFixedThreadPool(3);
    private  String urlStr ;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(contentView == null){
                Log.v("VideoDownloadUtils","创建contentView");
                contentView = new RemoteViews(context.getPackageName(), R.layout.notyfy_view);
                contentView.setProgressBar(R.id.pb,100,0,false);
                notification.icon = R.mipmap.icon_default;
                notification.tickerText = courseName+"下载进度";
//              notification.flags = notification.DEFAULT_ALL;
                notification.flags = Notification.FLAG_ONGOING_EVENT;
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                notification.contentView = contentView;

                PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                        new Intent(), 0);

                notification.setLatestEventInfo(context, "课程下载",
                        "下载", contentIntent);
                manager.notify(courseName.hashCode(), notification);
                           }
//            if(times++ % 100 == 0 || progress==100){

               Log.v("VodepDownloadUtils","更新下载进度条");
                contentView.setTextViewText(R.id.tv,courseName+"_下载进度:"+progress+"%");
                contentView.setProgressBar(R.id.pb, 100, progress, false);
                notification.contentView = contentView;
                manager.notify(courseName.hashCode(), notification);
//              }
            if(progress == 100){
                Log.v("VideoDownloadUtils","下载进度"+progress);
                progress = 0;
                isfinished = true;
                manager.cancel(courseName.hashCode());
                try {
                    LocalCourse localCourse = new LocalCourse(courseName,file.getAbsolutePath(),1);
                    Log.v("VideoDownload","localPath-->"+file.getAbsolutePath());
                    LocalCourseDAo localCourseDAo = new LocalCourseDAo(context);
                    localCourseDAo.add(localCourse);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
            super.handleMessage(msg);
            }


    };
    public VideoDownloadUtils(){

    }

    public static VideoDownloadUtils getVideoDownloadUtils(){

           return new VideoDownloadUtils();

    }

    public void download(Context context,String url,String courseName){
        this.context = context;
        this.urlStr = url;
        this.courseName = courseName;

        manager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        notification = new Notification(R.drawable.icon_default, "视频下载",System.currentTimeMillis());
        DownloadThread thread = new DownloadThread();
        pool.execute(thread);

    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    class DownloadThread implements Runnable{

        @Override
        public void run() {
            if(urlStr == null){
                return;
            }
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if(conn == null){
                    return;
                }
                conn.setReadTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                size = conn.getContentLength();
                Log.v("ApkDownloadUtils--size:",size+"");
                InputStream is = conn.getInputStream();
                byte[] buff = new byte[1024];
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    File folder = new File(Environment.getExternalStorageDirectory()+File.separator+"CXCourse");
                    if(!folder.exists()){
                        folder.mkdir();
                    }
                    file = new File(folder,courseName+".mp4");
                    if(file.exists()){
                        file.delete();
                    }
//                  Log.v("ApkDownloadUtils--SDfile:",file.getAbsolutePath());
                }else{
                    file = new File(Environment.getDownloadCacheDirectory(),courseName+".mp4");
                    if(file.exists()){
                        file.delete();
                    }
//                  Log.v("ApkDownloadUtils--CACHEfile:",file.getAbsolutePath());
                }
                FileOutputStream fos = new FileOutputStream(file);
                int len = 0;
                while((len = is.read(buff)) > 0){
                    current += len;
                    progress = (int)(current*100.0/size);
                    fos.write(buff,0,len);
                    if(times++ % 600 ==0 || progress == 100) {
                        Message msg = mHandler.obtainMessage();
                        mHandler.sendMessage(msg);
                    }
                }
                fos.flush();
                is.close();
                fos.close();
                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//  class MyAsysTask extends AsyncTask<String,Integer,String>{
//
//      @Override
//      protected String doInBackground(String... params) {
//          try {
//              URL url = new URL(params[0]);
//              HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//              if(conn == null){
//                  return null;
//              }
//              conn.setReadTimeout(10000);
//              conn.setRequestMethod("GET");
//              conn.setDoInput(true);
//              conn.connect();
//              size = conn.getContentLength();
//              Log.v("ApkDownloadUtils--size:",size+"");
//              InputStream is = conn.getInputStream();
//              byte[] buff = new byte[1024];
//              if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//                  File folder = new File(Environment.getExternalStorageDirectory()+File.separator+"CXCourse");
//                  if(!folder.exists()){
//                      folder.mkdir();
//                  }
//               file = new File(folder,courseName+".mp4");
//                  if(file.exists()){
//                      file.delete();
//                  }
////                  Log.v("ApkDownloadUtils--SDfile:",file.getAbsolutePath());
//              }else{
//                  file = new File(Environment.getDownloadCacheDirectory(),courseName+".mp4");
//                  if(file.exists()){
//                      file.delete();
//                  }
////                  Log.v("ApkDownloadUtils--CACHEfile:",file.getAbsolutePath());
//              }
//              FileOutputStream fos = new FileOutputStream(file);
//              int len = 0;
//              while((len = is.read(buff)) > 0){
//                  current += len;
//                  progress = (int)(current*100.0/size);
//                  fos.write(buff,0,len);
//                  publishProgress(progress);
//                  Message msg= mHandler.obtainMessage();
//                  mHandler.sendMessage(msg);
//              }
//              fos.flush();
//              is.close();
//              fos.close();
//              conn.disconnect();
//          } catch (MalformedURLException e) {
//              e.printStackTrace();
//          } catch (IOException e) {
//              e.printStackTrace();
//          }
//          return progress+"";
//      }
//
//      @Override
//      protected void onPostExecute(String s) {
////          Log.v("ApkDownloadUtils--onPostExecute():result", s);
//          if(isfinished){
//              manager.cancel(R.id.notify_icon);
////              Uri.fromFile(file,
////                      "application/vnd.android.package-archive");
////              PackageUtils.install(context,file.getAbsolutePath());
//
//          }
//          super.onPostExecute(s);
//      }
//
//      @Override
//      protected void onProgressUpdate(Integer... values) {
//
//          super.onProgressUpdate(values);
//      }
//
//      @Override
//      protected void onPreExecute() {
//          mHandler.sendMessage(mHandler.obtainMessage());
//          super.onPreExecute();
//      }
//  }
}
