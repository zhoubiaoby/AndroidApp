package com.example.zhoubiao.cxcourses.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by zb-1 on 2015/10/7.
 */
public class NetUtil {
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    public static boolean isWifi(Context context){
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if(wifi == NetworkInfo.State.CONNECTED){
            return true;
        }else{
            return false;
        }
    }
}
