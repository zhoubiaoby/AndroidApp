package com.example.zhoubiao.cxcourses.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.zhoubiao.cxcourses.R;
import com.example.zhoubiao.cxcourses.course_detail_activity.LessonDetailActivity;
import com.example.zhoubiao.cxcourses.dataobject.Course;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zb-1 on 2015/9/27.
 */
public class LunboView extends FrameLayout implements View.OnClickListener{
    private List<View> lunboViewList = new ArrayList<View>();
    private ViewPager mViewPager;
    private DotView mDotView;
    private  List<Course> list;
    private boolean isLunboContinue = true;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int i = 0;

    public LunboView(Context context, AttributeSet attrs,List<Course> list) {
        super(context, attrs);
        this.list = list;
        initView();
    }
    public LunboView(Context context,List<Course> list) {
        super(context);
        this.list = list;
        initView();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(isLunboContinue) {
                    mViewPager.setCurrentItem(i++ % lunboViewList.size());
                    handler.postDelayed(runnable, 3000);
                }
            }
        };
        handler.postDelayed(runnable, 3000);
    }
   public void  initView(){
       LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View view = inflater.inflate(R.layout.lunbo_view_layout,null);
              mViewPager = (ViewPager)view.findViewById(R.id.recommend_viewpager) ;
             mDotView = (DotView)view.findViewById(R.id.mDotView);
       initData(inflater);
       addView(view);
   }
   public void initData(LayoutInflater inflater){


       for(int i =0; i<list.size();i++){

               View tempView =  inflater.inflate(R.layout.lunbo_item_layout, null);
               ((TextView) tempView.findViewById(R.id.course_name)).setText(list.get(i).name);
               ((SimpleDraweeView) tempView.findViewById(R.id.course_imv)).setImageURI(Uri.parse(list.get(i).imageUrl));
               tempView.setOnClickListener(this);
               lunboViewList.add(tempView);

       }
       mDotView.setNum(lunboViewList.size());
       MyLunboAdapter myLunboAdapter = new MyLunboAdapter();
       mViewPager.setAdapter(myLunboAdapter);
       mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

           }

           @Override
           public void onPageSelected(int position) {

               mDotView.setSelected(position);
           }

           @Override
           public void onPageScrollStateChanged(int state) {

           }
       });
       mViewPager.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               isLunboContinue = false;
               return false;
           }
       });
   }

    @Override
    public void onClick(View v) {
       for(int i = 0;i < lunboViewList.size(); i++){
           if(lunboViewList.get(i) == v){
               Intent intent = new Intent(getContext(), LessonDetailActivity.class);
               Bundle bundle = new Bundle();
               bundle.putSerializable("course",list.get(i));
               intent.putExtras(bundle);
               getContext().startActivity(intent);
           }
       }
    }

    class MyLunboAdapter extends PagerAdapter {

        public MyLunboAdapter(){

        }

        @Override
        public int getCount() {
            return  list.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(lunboViewList.get(position),0);
            return lunboViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(lunboViewList.get(position));
        }
    }
}
