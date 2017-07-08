package com.example.zhoubiao.cxcourses.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.zhoubiao.cxcourses.R;
import com.example.zhoubiao.cxcourses.adapter.RecommendAdapter;
import com.example.zhoubiao.cxcourses.dataobject.Course;
import com.example.zhoubiao.cxcourses.manager.layout_manager.ExStaggeredGridLayoutManager;
import com.example.zhoubiao.cxcourses.manager.layout_manager.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zb-1 on 2015/9/28.
 */
public class RecommedView extends FrameLayout {
    private List<Course> list = new ArrayList<Course>();
    private RecyclerView mRecylerView;
    private ViewGroup parent;
    public RecommedView(Context context, AttributeSet attrs,List<Course> list) {
        super(context, attrs);
        this.list = list;
        initView();
    }
    public RecommedView(ViewGroup parent, List<Course> list) {
        super(parent.getContext());
        this.list = list;
        this.parent = parent;
        initView();
    }
    public void initView(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recommend_view_layout,null);
        mRecylerView = (RecyclerView)view.findViewById(R.id.recommend_courses);
        mRecylerView.setLayoutManager(new ExStaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL));
        Log.v("RecommendView-->", list.size() + "");
        mRecylerView.setAdapter(new RecommendAdapter(parent.getContext(), list));
        addView(view);

    }
}
