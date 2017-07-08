package com.example.zhoubiao.cxcourses.course_search;

import android.app.Activity;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.zhoubiao.cxcourses.R;
import com.example.zhoubiao.cxcourses.adapter.DividerItemDecoration;
import com.example.zhoubiao.cxcourses.adapter.SearchCourseAdapter;
import com.example.zhoubiao.cxcourses.dataobject.Course;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhoubiao on 2015/10/12.
 */
public class SearchActivity extends Activity {
    private ImageView backImg;
    private ImageView searchImg;
    private ImageView clearImg;

    private EditText editText;

    private RecyclerView recyclerView;
    private List<Course> searchList;
    private List<Course> list;

    private SearchCourseAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        initData();
        initView();
    }

    void initData(){
        searchList = new ArrayList<Course>();
        list = new ArrayList<Course>();
        list.add(new Course("第一个创新课程","http://vimg1.ws.126.net/image/snapshot_movie/2013/11/Q/4/M9DMBN3Q4.jpg",true,
                "http://218.249.255.24/course/video/ChuangXinCourse1.mp4"));
        list.add(new Course("第二个创新课程","http://img3.cache.netease.com/cnews/2013/8/30/2013083011052137217.jpg",true,
                "http://218.249.255.24/course/video/ChuangXinCourse1.mp4"));
        list.add(new Course("第三个创新课程","http://vimg3.ws.126.net/image/snapshot_movie/2011/7/Q/H/M78B47QQH.jpg",true,
                "http://218.249.255.24/course/video/ChuangXinCourse1.mp4"));
        list.add(new Course("第四个创新课程","http://img2.cache.netease.com/video/2013/6/13/20130613111123f6c37.jpg",false,
                "http://218.249.255.24/course/video/ChuangXinCourse1.mp4"));
        list.add(new Course("第五个创新课程","http://vimg1.ws.126.net/image/snapshot_movie/2014/2/H/I/M9KER89HI.jpg",false,
                "http://218.249.255.24/course/video/ChuangXinCourse1.mp4"));
        list.add(new Course("第六个创新课程","http://img3.cache.netease.com/cnews/2013/5/17/2013051710344030168.jpg",false,
                "http://218.249.255.24/course/video/ChuangXinCourse1.mp4"));
        list.add(new Course("第七个创新课程", "http://img2.cache.netease.com/video/2013/6/4/2013060416312939557.jpg", false,
                "http://218.249.255.24/course/video/ChuangXinCourse1.mp4"));
        list.add(new Course("第八个创新课程", "http://vimg1.ws.126.net/image/snapshot_movie/2013/6/T/R/M8VRNC2TR.jpg", false,
                "http://218.249.255.24/course/video/ChuangXinCourse1.mp4"));
        list.add(new Course("第九个创新课程", "http://img4.cache.netease.com/video/2013/6/13/20130613111231a992f.jpg", false,
                "http://218.249.255.24/course/video/ChuangXinCourse1.mp4"));



    }
    void initView() {
        recyclerView = (RecyclerView)findViewById(R.id.recommend);
        backImg = (ImageView)findViewById(R.id.search_back);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });
        searchImg = (ImageView)findViewById(R.id.search_back);
        clearImg = (ImageView)findViewById(R.id.search_clear);
        clearImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
        editText = (EditText)findViewById(R.id.common_title_searchEdt);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchData(s);
                if(TextUtils.isEmpty(s)){
                    clearImg.setVisibility(View.GONE);
                }else{
                    clearImg.setVisibility(View.VISIBLE   );
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        adapter = new SearchCourseAdapter(this,searchList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, R.drawable.divider));
        recyclerView.setAdapter(adapter);
    }
    void searchData(CharSequence str){
        searchList.clear();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()){
            Course course = (Course) iterator.next();
            if(course.name.contains(str)){
                searchList.add(course);
            }
        }
        Log.v("SearchActivity", "searchList的size-->" + searchList.size());
      adapter.notifyDataSetChanged();
    }
}
