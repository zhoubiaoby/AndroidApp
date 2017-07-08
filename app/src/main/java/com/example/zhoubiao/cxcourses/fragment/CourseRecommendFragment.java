package com.example.zhoubiao.cxcourses.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhoubiao.cxcourses.R;

/**
 * Created by zb-1 on 2015/10/6.
 */
public class CourseRecommendFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView none_tv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_recommend_layout,container,false);
        return view;
    }
}
