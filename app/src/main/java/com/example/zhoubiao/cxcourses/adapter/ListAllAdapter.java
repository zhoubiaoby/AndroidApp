package com.example.zhoubiao.cxcourses.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhoubiao.cxcourses.R;
import com.example.zhoubiao.cxcourses.course_detail_activity.LessonDetailActivity;
import com.example.zhoubiao.cxcourses.dataobject.Course;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by zb-1 on 2015/9/20.
 */
public class ListAllAdapter extends RecyclerView.Adapter<ListAllAdapter.ListAllViewHodler> {
    private List<Course> list;
    private Context context;
    public ListAllAdapter(Context context,List<Course> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public ListAllViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        ListAllViewHodler hodler = new ListAllViewHodler(LayoutInflater.from(context).inflate(R.layout.list_all_item,null));
        return hodler;
    }

    @Override
    public void onBindViewHolder(ListAllViewHodler holder, final int position) {
        holder.imv.setImageURI(Uri.parse(list.get(position).imageUrl));
        holder.tv.setText(list.get(position).name);
        holder.imv.setOnClickListener(new View.OnClickListener() {
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
    class ListAllViewHodler extends RecyclerView.ViewHolder{
       SimpleDraweeView imv;
        TextView tv;
        public ListAllViewHodler(View itemView) {
            super(itemView);
            imv = (SimpleDraweeView)itemView.findViewById(R.id.course_imv);
            tv = (TextView)itemView.findViewById(R.id.course_name);
        }
    }
}
