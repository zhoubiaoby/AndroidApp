package com.example.zhoubiao.cxcourses.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhoubiao.cxcourses.MainActivity;
import com.example.zhoubiao.cxcourses.R;
import com.example.zhoubiao.cxcourses.adapter.RecommendFragmentAdapter;
import com.example.zhoubiao.cxcourses.database_util.CacheCourseDao;
import com.example.zhoubiao.cxcourses.dataobject.Course;
import com.example.zhoubiao.cxcourses.net.BaseRequest;
import com.example.zhoubiao.cxcourses.utils.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoubiao on 2015/9/18.
 */
@SuppressLint("NewApi")
public class RecommendFragment extends Fragment {

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1) {
                if(lunboList.size() > 0 && list.size() > 0) {
                    progress.setVisibility(View.GONE);
                    mRecylerView.setVisibility(View.VISIBLE);
                    RecommendFragmentAdapter adapter = new RecommendFragmentAdapter(getActivity(), lunboList, list);
                    mRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRecylerView.setAdapter(adapter);
                }else {
                    progress.setVisibility(View.GONE);
                    errorTextView.setVisibility(View.VISIBLE);
                }
            }
        }
    };
    private List<Course> list =new ArrayList<Course>();

    private List<Course> lunboList = new ArrayList<Course>();

    private RelativeLayout progress;

    private RecyclerView mRecylerView;

    private TextView errorTextView;

    private CacheCourseDao cacheCourseDao;


    public RecommendFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cacheCourseDao =new CacheCourseDao(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recommend_layout,container,false);
        initData();
        initView(view);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void initData(){
        MainActivity.pool.execute(new Runnable() {
            @Override
            public void run() {
                Log.v("RecommendFragment", "-->run()开始");
                if (NetUtil.isNetworkConnected(getActivity())) {
                    final BaseRequest baseRequest = new BaseRequest(getActivity(), "/mybatis/courseList", "RecommendFragment");
                    baseRequest.setRequestCallback(new BaseRequest.RequestCallback() {
                        @Override
                        public void onRequestResponse(JSONArray array) {
                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    JSONObject object = array.getJSONObject(i);
                                    Log.v("RecommendFragment", "Course.name-->" + object.getString("name"));
                                    if (i < 4) {
                                        Course course = new Course(object.getString("name"), object.getString("imageurl"), true, object.getString("videourl"));
                                        lunboList.add(course);
                                        if(!isFirstCache()){
                                            cacheCourseDao.add(course);
                                        }
                                    } else {
                                        Course course = new Course(object.getString("name"), object.getString("imageurl"), false, object.getString("videourl"));
                                        list.add(course);
                                        if(!isFirstCache()){
                                            cacheCourseDao.add(course);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            setIsFirstCache();
                            Message message = handler.obtainMessage();
                            message.what = 1;
                            handler.sendMessage(message);
                        }

                        @Override
                        public void onRequestError(String errorMsg) {

                        }
                    });
                    baseRequest.connect();
                } else {
                    List<Course> listTemp = cacheCourseDao.getScrollData();
                    for (int i = 0; i < listTemp.size(); i++) {
                        if (i < 4) {
                            lunboList.add(listTemp.get(i));
                        } else {
                            list.add(listTemp.get(i));
                        }
                    }
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    handler.sendMessage(message);

                }
            }
        });




    }
    public void initView(View view ){
        progress = (RelativeLayout)view.findViewById(R.id.progress);
      mRecylerView = (RecyclerView)view.findViewById(R.id.recommend_courses);

        errorTextView = (TextView)view.findViewById(R.id.error_tv);

    }

    public boolean isFirstCache(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("isForstCache",Activity.MODE_PRIVATE );
        return  sharedPreferences.getBoolean("cache",false);
    }
    public void setIsFirstCache(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("isForstCache",Activity.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("cache",true);
        editor.commit();
    }

}
