package com.example.zhoubiao.cxcourses.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhoubiao.cxcourses.R;

/**
 * Created by zb-1 on 2015/10/6.
 */
public class CourseIntroFragment extends Fragment {
    private TextView tv;
    private  String str;

    public static CourseIntroFragment newInstance(String str){
        CourseIntroFragment fragment = new CourseIntroFragment();
        Bundle args = new Bundle();
        args.putString("intro",str);
        fragment.setArguments(args);
        return fragment;
    }
    public CourseIntroFragment(){

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_intro_layout,container,false);
        tv = (TextView)view.findViewById(R.id.course_intro);
        str = getArguments().getString("intro");
        tv.setText(str);
        return view;
    }
}
