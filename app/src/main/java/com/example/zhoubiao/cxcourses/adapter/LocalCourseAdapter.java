package com.example.zhoubiao.cxcourses.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhoubiao.cxcourses.R;
import com.example.zhoubiao.cxcourses.database_util.LocalCourseDAo;
import com.example.zhoubiao.cxcourses.dataobject.LocalCourse;
import com.example.zhoubiao.cxcourses.videoplayer.VideoPlayActivity;

import java.io.File;
import java.util.List;

/**
 * Created by zhoubiao on 2015/10/10.
 */
public class LocalCourseAdapter extends RecyclerView.Adapter<LocalCourseAdapter.LocalCourseViewHolder> {

    private Context context;
    private List<LocalCourse> list;
    public LocalCourseAdapter(Context context,List<LocalCourse> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public LocalCourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LocalCourseViewHolder holder = new LocalCourseViewHolder(LayoutInflater.from(context).inflate(R.layout.local_course_item,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(LocalCourseViewHolder holder, final int position) {
           holder.tv.setText(list.get(position).getName());
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(context, VideoPlayActivity.class);
                   intent.putExtra("courseName", list.get(position).getName());
                   intent.putExtra("localPath", list.get(position).getLocalPath());
                   context.startActivity(intent);
               }
           });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalCourseDAo localCourseDAo = new LocalCourseDAo(context);
                localCourseDAo.delete(list.get(position).getName());
                File file = new File(list.get(position).getLocalPath());
                if(file.exists()){
                    file.delete();
                }
                list = localCourseDAo.getScrollData();
                LocalCourseAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class LocalCourseViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        ImageView delete;
        public LocalCourseViewHolder(View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.local_course_name);
            delete = (ImageView)itemView.findViewById(R.id.delete);
        }
    }
}
