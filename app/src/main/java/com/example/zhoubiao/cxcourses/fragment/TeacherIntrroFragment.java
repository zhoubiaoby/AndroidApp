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
public class TeacherIntrroFragment extends Fragment {
    private TextView tv;
    private String str;

    public static TeacherIntrroFragment newInstance(String str){
        TeacherIntrroFragment fragment = new TeacherIntrroFragment();
        Bundle args = new Bundle();
        args.putString("intro",str);
        fragment.setArguments(args);
        return fragment;
    }
    public TeacherIntrroFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_intro_layout,container,false);
        tv = (TextView)view.findViewById(R.id.teacher_intro);
        str = getArguments().getString("intro");
        tv.setText(str);
        return view;
    }
}
