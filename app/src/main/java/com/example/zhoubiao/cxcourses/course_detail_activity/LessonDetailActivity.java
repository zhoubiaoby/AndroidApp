package com.example.zhoubiao.cxcourses.course_detail_activity;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhoubiao.cxcourses.MainActivity;
import com.example.zhoubiao.cxcourses.R;
import com.example.zhoubiao.cxcourses.database_util.LocalCourseDAo;
import com.example.zhoubiao.cxcourses.dataobject.Course;
import com.example.zhoubiao.cxcourses.dataobject.LocalCourse;
import com.example.zhoubiao.cxcourses.download.DownLoadService;
import com.example.zhoubiao.cxcourses.fragment.CourseIntroFragment;
import com.example.zhoubiao.cxcourses.fragment.CourseRecommendFragment;
import com.example.zhoubiao.cxcourses.fragment.TeacherIntrroFragment;
import com.example.zhoubiao.cxcourses.utils.NetUtil;
import com.example.zhoubiao.cxcourses.videoplayer.VideoPlayActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;

public class LessonDetailActivity extends AppCompatActivity{

	private String courseName;
	private TextView lessonName;
	private TextView teacherName;
	private ViewPager viewPager;
	private SimpleDraweeView lessionImage;
	private ImageView shareImage;
	private ImageView downloadImage;
    private RelativeLayout courseTitle;
	private TextView tvArr[] = new TextView[3];
	private int selectedColor = R.color.forestgreen;
	private int unselectorColor = R.color.lawngreen;
	private int pre = 0;
    private boolean isDownload;
	private Course course;

	private LocalCourse localCourse;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		isDownload = false;
		setContentView(R.layout.lesson_detail);
		initData();
		initView();
		downloadImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isDownload) {
					if (localCourse != null) {
						Toast.makeText(LessonDetailActivity.this, "亲，该视频已下载", Toast.LENGTH_LONG).show();
					} else {
						if (NetUtil.isNetworkConnected(LessonDetailActivity.this)) {
							if (NetUtil.isWifi(LessonDetailActivity.this)) {
								Intent intent = new Intent(LessonDetailActivity.this, DownLoadService.class);
								intent.putExtra("url", course.videoUrl);
								intent.putExtra("courseName", course.name);
								startService(intent);
								isDownload = true;
							} else {
								AlertDialog.Builder builder = new AlertDialog.Builder(LessonDetailActivity.this);
								builder.setTitle("提示");
								builder.setMessage("没有连接到wifi，很耗流量，不建议现在下载，土豪请忽略。。");
								builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								});
								builder.setNegativeButton("继续", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										Intent intent = new Intent(LessonDetailActivity.this, DownLoadService.class);
										intent.putExtra("url", course.videoUrl);
										intent.putExtra("courseName", course.name);
										startService(intent);
										dialog.dismiss();
									}
								});
								builder.show();
							}
						} else {
							Toast.makeText(LessonDetailActivity.this, "网络不可用！", Toast.LENGTH_LONG).show();
						}

					}
				}
			}
		});
		shareImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
				intent.putExtra(Intent.EXTRA_TEXT, course.videoUrl);
				startActivity(Intent.createChooser(intent, "分享"));
			}
		});


 ;
	}

	 public  void initData(){
      course = (Course)getIntent().getSerializableExtra("course");
		 LocalCourseDAo localCourseDAo = new LocalCourseDAo(this);
		 localCourse = localCourseDAo.find(course.name);
	 }

	public void initView(){
		shareImage = (ImageView)findViewById(R.id.img_share);
		downloadImage = (ImageView)findViewById(R.id.img_download);
		lessionImage = (SimpleDraweeView)findViewById(R.id.lesson_image);
		lessionImage.setImageURI(Uri.parse(course.imageUrl));
		lessonName = (TextView)findViewById(R.id.lesson_name);
		lessonName.setText("课程："+course.name);
		teacherName = (TextView)findViewById(R.id.teacher_name);
		courseTitle = (RelativeLayout)findViewById(R.id.relativeLayout_title);
		courseTitle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (NetUtil.isNetworkConnected(LessonDetailActivity.this)||localCourse !=null) {
					if(NetUtil.isWifi(LessonDetailActivity.this)||localCourse !=null) {
						Intent intent = new Intent(LessonDetailActivity.this, VideoPlayActivity.class);
						intent.putExtra("courseName", course.name);

						if (localCourse != null) {
							intent.putExtra("localPath", localCourse.getLocalPath());
						} else {
							intent.putExtra("lessonWebsite", course.videoUrl);
						}
						startActivity(intent);
					}else {
						AlertDialog.Builder builder = new AlertDialog.Builder(LessonDetailActivity.this);
						builder.setTitle("提示");
						builder.setMessage("没有连接到wifi，很耗流量，不建议现在观看，土豪请忽略。。");
						builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						builder.setNegativeButton("继续", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(LessonDetailActivity.this, DownLoadService.class);
								intent.putExtra("url", course.videoUrl);
								intent.putExtra("courseName", course.name);
								startService(intent);
								dialog.dismiss();
							}
						});
						builder.show();
					}
				}else {
					Toast.makeText(LessonDetailActivity.this,"网络无连接！",Toast.LENGTH_SHORT).show();
				}
			}
		});

		tvArr[0] = (TextView)findViewById(R.id.course_intro);
		tvArr[1] = (TextView)findViewById(R.id.teacher_intro);
		tvArr[2] = (TextView)findViewById(R.id.course_recommend);
		viewPager = (ViewPager)findViewById(R.id.course_detail_viewpager);
		viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
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
	static class MyViewPagerAdapter extends CacheFragmentStatePagerAdapter{

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}


		@Override
		protected Fragment createItem(int i) {
			Fragment fragment = null;
			switch (i){
				case 0:
               fragment = CourseIntroFragment.newInstance("课程的介绍");

					break;
				case 1:
			  fragment = TeacherIntrroFragment.newInstance("老师的介绍");
					break;
				case 2:
			  fragment = new CourseRecommendFragment();
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
