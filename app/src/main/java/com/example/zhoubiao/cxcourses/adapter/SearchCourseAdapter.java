package com.example.zhoubiao.cxcourses.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhoubiao.cxcourses.R;
import com.example.zhoubiao.cxcourses.course_detail_activity.LessonDetailActivity;
import com.example.zhoubiao.cxcourses.dataobject.Course;

import java.util.List;

/**
 * Created by zhoubiao on 2015/10/12.
 */
public class SearchCourseAdapter extends  RecyclerView.Adapter<SearchCourseAdapter.SearchCourseViewHolder>{

    private Context context;
    private List<Course> list;
    public SearchCourseAdapter(Context context,List<Course> list){
        this.list = list;
        this.context = context;
    }
    @Override
    public SearchCourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SearchCourseViewHolder holder = new SearchCourseViewHolder(LayoutInflater.from(context).inflate(R.layout.search_item,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchCourseViewHolder holder, final int position) {
        holder.tv.setText(list.get(position).name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LessonDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("course",list.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SearchCourseViewHolder extends RecyclerView.ViewHolder{
       TextView tv;
        public SearchCourseViewHolder(View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.search_tv);
        }
    }
}
