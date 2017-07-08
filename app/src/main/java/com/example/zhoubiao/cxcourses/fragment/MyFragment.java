package com.example.zhoubiao.cxcourses.fragment;

import android.net.Uri;
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

import com.example.zhoubiao.cxcourses.R;
import com.example.zhoubiao.cxcourses.adapter.DividerItemDecoration;
import com.example.zhoubiao.cxcourses.adapter.LocalCourseAdapter;
import com.example.zhoubiao.cxcourses.database_util.CourseDBContentOberserver;
import com.example.zhoubiao.cxcourses.database_util.LocalCourseDAo;
import com.example.zhoubiao.cxcourses.dataobject.LocalCourse;

import java.util.List;

/**
 * Created by zb-1 on 2015/9/20.
 */
public class MyFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<LocalCourse> list;
    private CourseDBContentOberserver oberserver;
    private LocalCourseDAo localCourseDAo;
    private LocalCourseAdapter adapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    list.clear();
                    list.addAll(localCourseDAo.getScrollData());
                    adapter.notifyDataSetChanged();
                    Log.v("MyFragment","更新数据");
                    break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localCourseDAo = new LocalCourseDAo(getActivity());
        list = localCourseDAo.getScrollData();
        oberserver = new CourseDBContentOberserver(getActivity(),handler);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_course_layout,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.local_course);
        adapter = new LocalCourseAdapter(getActivity(),list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST, R.drawable.divider));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
       registerContentObserver();
    }
   private void registerContentObserver(){
     Uri uri = Uri
               .parse("content://com.example.zhoubiao.cxcourses/localcourses");
       getActivity().getContentResolver().registerContentObserver(uri,true,oberserver);
    }
}
