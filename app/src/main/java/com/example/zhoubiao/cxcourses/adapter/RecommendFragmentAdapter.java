package com.example.zhoubiao.cxcourses.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhoubiao.cxcourses.dataobject.Course;
import com.example.zhoubiao.cxcourses.view.LunboView;
import com.example.zhoubiao.cxcourses.view.RecommedView;

import java.util.List;

/**
 * Created by zb-1 on 2015/9/28.
 */
public class RecommendFragmentAdapter extends RecyclerView.Adapter<RecommendFragmentAdapter.MyViewHolder> {

    private List<Course> titleList;
    private List<Course> list;
    private  Context context;
    public RecommendFragmentAdapter(Context context,List<Course> titleList, List<Course> list){
         this.titleList = titleList;
        this.list = list;
        this.context = context;
    }
    @Override
    public int getItemViewType(int position) {
        return  position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, //width
                ViewGroup.LayoutParams.WRAP_CONTENT);//height
        if(viewType == 0){
            LunboView lunboView = new LunboView(context,titleList);
            lunboView.setLayoutParams(lp);
           return new MyViewHolder(lunboView);
        }else {
            RecommedView recommedView = new RecommedView(parent,list);
            recommedView.setLayoutParams(lp);
          return new MyViewHolder(recommedView);
        }

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(View itemView) {
            super(itemView);

        }
    }
}
