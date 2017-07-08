package com.example.zhoubiao.cxcourses.net;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.example.zhoubiao.cxcourses.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;


/**
 * Created by yujinyang on 2015/6/18.
 */
public class VolleyErrorHelper {

    public static String getMessage(Object error, Context context) {
        if(context == null && error == null){
            return context.getResources().getString(R.string.generic_error);
        }
        if (error instanceof TimeoutException) {
            return "generic_server_down:服务器请求超时";
        } else if ((error instanceof ServerError) || (error instanceof AuthFailureError)) {
            return handleServerError(error, context);
        } else if ((error instanceof NetworkError) || (error instanceof NoConnectionError)) {
            return "no_internet";
        }
        return "generic_error";
    }

    private static String handleServerError(Object err, Context context) {
        VolleyError error = (VolleyError) err;

        NetworkResponse response = error.networkResponse;

        if (response != null) {
            switch (response.statusCode) {
                case 404:
                case 422:
                case 401:
                    try {
                        HashMap<String, String> result = new Gson().fromJson(
                                new String(response.data), new TypeToken<Map<String, String>>() {
                                }.getType());

                        if (result != null && result.containsKey("error")) {
                            return result.get("error");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return error.getMessage();
                default:
                    return context.getResources().getString(R.string.generic_server_down);
            }
        }
        return context.getResources().getString(R.string.generic_error);
    }

    private static boolean isNetWorkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }

}
