package com.example.zhoubiao.cxcourses.fragment;

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
import com.example.zhoubiao.cxcourses.adapter.DividerItemDecoration;
import com.example.zhoubiao.cxcourses.adapter.ListAllAdapter;
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
 * Created by zb-1 on 2015/9/20.
 */
public class AllCourseFragment extends Fragment {

    private List<Course> list;
    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;
    private ListAllAdapter adapter;
    private CacheCourseDao cacheCourseDao;
    private TextView errorTextView;

   private Handler handler = new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           switch(msg.what){
               case 1:
                   if(list.size() > 0) {
                       relativeLayout.setVisibility(View.GONE);
                       recyclerView.setVisibility(View.VISIBLE);
                       adapter = new ListAllAdapter(getActivity(), list);
                       recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                       recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST, R.drawable.divider));
                       recyclerView.setAdapter(adapter);
                   }else {
                       relativeLayout.setVisibility(View.GONE);
                       errorTextView.setVisibility(View.VISIBLE);
                   }
                   break;
           }
       }
   };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cacheCourseDao = new CacheCourseDao(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_course_layout,container,false);
        initData();
        initView(view);
        return view;
    }
    void initData(){
        list = new ArrayList<Course>();
        MainActivity.pool.execute(new Runnable() {
            @Override
            public void run() {
                Log.v("AllcourseFragment", "-->run()开始");
                if (NetUtil.isNetworkConnected(getActivity())) {
                    final BaseRequest baseRequest = new BaseRequest(getActivity(), "/mybatis/courseList", "RecommendFragment");
                    baseRequest.setRequestCallback(new BaseRequest.RequestCallback() {
                        @Override
                        public void onRequestResponse(JSONArray array) {
                            Log.v("AllCourseFragment", "-->onRequestResponse");
                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    JSONObject object = array.getJSONObject(i);
                                    Log.v("AllCourseFragment", "Course.name-->" + object.getString("name"));
                                    Course course = new Course(object.getString("name"), object.getString("imageurl"), false, object.getString("videourl"));
                                    list.add(course);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            Message message = handler.obtainMessage();
                            message.what = 1;
                            handler.sendMessage(message);
                        }

                        @Override
                        public void onRequestError(String errorMsg) {

                        }
                    });
                    baseRequest.connect();
                }else {
                    if (isFirstCache()) {
                        List<Course> listTemp = cacheCourseDao.getScrollData();
                        for (int i = 0; i < listTemp.size(); i++) {
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
    void initView(View view){
        relativeLayout = (RelativeLayout)view.findViewById(R.id.progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_all);
        errorTextView = (TextView)view.findViewById(R.id.error_tv);

    }
    public boolean isFirstCache(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("isForstCache", Activity.MODE_PRIVATE );
        return  sharedPreferences.getBoolean("cache",false);
    }
    public void setIsFirstCache(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("isForstCache",Activity.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("cache",true);
        editor.commit();
    }
}
