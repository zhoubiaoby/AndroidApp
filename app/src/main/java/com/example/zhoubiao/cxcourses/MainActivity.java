package com.example.zhoubiao.cxcourses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhoubiao.cxcourses.course_search.SearchActivity;
import com.example.zhoubiao.cxcourses.fragment.AllCourseFragment;
import com.example.zhoubiao.cxcourses.fragment.MyFragment;
import com.example.zhoubiao.cxcourses.fragment.RecommendFragment;
import com.example.zhoubiao.cxcourses.net.RequestManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    public static ExecutorService pool = Executors.newFixedThreadPool(3);
    private boolean mHashToastShow = false;
    private ViewPager viewPager;
//    private TextView recommendTv;
//    private TextView allTv;
//    private TextView myTv;
    private TextView tvArr[] = new TextView[3];
    private int selectedColor = R.color.forestgreen;
    private int unselectorColor = R.color.lawngreen;
    private int pre = 0;

    private ImageView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        RequestManager.init(this);
        initData();
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public  void initData(){

    }
    public void initView(){
        search = (ImageView)findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
        tvArr[0] = (TextView)findViewById(R.id.recommend);
        tvArr[1] =(TextView)findViewById(R.id.all);
        tvArr[2] = (TextView)findViewById(R.id.my);

        tvArr[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        tvArr[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        tvArr[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });
        viewPager = (ViewPager)findViewById(R.id.hot_course);
        NavigationAdapter navigationAdapter = new NavigationAdapter(getSupportFragmentManager());
        viewPager.setAdapter(navigationAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                   setTitleColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
  public void setTitleColor(int index){
//      for(int i = 0; i< 3; i++){
//          if(i == index ){
//              tvArr[i].setBackgroundResource(selectedColor);
//          }else {
//              tvArr[i].setBackgroundResource(unselectorColor);
//          }
//      }
        if(pre != index) {
            tvArr[pre].setBackgroundResource(unselectorColor);
            tvArr[index].setBackgroundResource(selectedColor);
            pre = index;
        }
  }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {


            if (!mHashToastShow) {
                mHashToastShow = true;
                Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHashToastShow = false;
                    }
                }, 5000);

                return true;
            }
            finish();

            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    class NavigationAdapter extends CacheFragmentStatePagerAdapter{

    public NavigationAdapter(FragmentManager fm){
        super(fm);

     }
     @Override
     protected Fragment createItem(int i) {
         Fragment fragment = null;
         switch (i){
             case 0:
                 fragment = new RecommendFragment();
                 break;
             case 1:
                 fragment = new AllCourseFragment();
                 break;
             case 2:
                 fragment = new MyFragment();
                 break;
         }
         return fragment;
     }

     @Override
     public int getCount() {
         return 3;
     }
 }
}
