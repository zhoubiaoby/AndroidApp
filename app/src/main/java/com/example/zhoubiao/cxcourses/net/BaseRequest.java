package com.example.zhoubiao.cxcourses.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Iterator;


/**
 * Created by yujinyang on 2015/6/17.
 */
public  class BaseRequest {

    private Context context;
    public static final String TAG = "BaseRequest";

    public interface RequestCallback {
        void onRequestResponse(JSONArray array);

        void onRequestError(String errorMsg);
    }

    /**
     * 线上服务器地址
     */
    public static final String URL_HEADER_ONLINE = "http://182.92.242.146:8080";
    /**
     * 开发服务器地址
     */
    private String tag;
    private String urlStr;

    private RequestCallback requestCallback;




    public BaseRequest() {

    }

    public BaseRequest(Context context,String urlStr,String tag) {
              this.context = context;
              this.urlStr = urlStr;
              this.tag = tag;
    }

    public void setRequestCallback(RequestCallback callback) {
        this.requestCallback = callback;
    }

    public String getUrl() {

        return URL_HEADER_ONLINE+urlStr;
    }

    /**
     * 添加客户端设备相关参数
     *
     * @param paramJson
     */
    private void addCommonDevicesParams(JSONObject paramJson) {

    }

    public void connect() {
        connect(1);
    }

    public void connect(int maxRetryCount) {
        String url = getUrl();
        Log.v(TAG, "SEND URL:" + url);
        Request request = getRequest(url);

        request.setRetryPolicy(new DefaultRetryPolicy(1500, maxRetryCount, 1f));
        request.setShouldCache(false);
        RequestManager.addRequest(request, this.tag);
    }

    private  Request getRequest(String requestUrl) {
//        return new JsonArrayRequest(requestUrl,new );
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(requestUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.v("BaseRequest", response.toString());
                        if (requestCallback != null) {
                            requestCallback.onRequestResponse(response);
                            requestCallback = null;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("BaseRequest", error.getMessage());
                if (requestCallback != null) {
                    requestCallback.onRequestError(error.getMessage());
                }
            }
        });
        return jsonArrayRequest;
    }

    /**
     * 服务端暂不支持application/json格式。使用传统form格式提交
     * 将JSONObject类型BodyContent 转换为 form表单格式字符串 param1=value1&param2=value2...
     *
     * @param object
     * @return
     */
    public static String jsonToGetParamString(JSONObject object) {
        if (object == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = object.keys();
        if (iterator != null) {
            while (iterator.hasNext()) {
                String key = iterator.next();
                builder.append(key);
                builder.append("=");
                builder.append(object.opt(key));
                builder.append("&");
            }
        }
        return builder.toString();
    }

}
