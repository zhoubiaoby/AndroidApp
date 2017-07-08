package com.example.zhoubiao.cxcourses.videoplayer;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.os.MessageQueue.IdleHandler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;

import com.example.zhoubiao.cxcourses.R;
import com.example.zhoubiao.cxcourses.database_util.MyAccountDBHelper;
import com.example.zhoubiao.cxcourses.database_util.MyHisDBhelper;
import com.example.zhoubiao.cxcourses.utils.NetUtil;

/**
 * 重写videoview的方式   播放视频，  在包com.bupt.videoplayer中
 */
public class VideoPlayActivity extends Activity {

	private final static String TAG = "VideoPlayerActivity";

	//	public static LinkedList<MovieInfo> playList = new LinkedList<MovieInfo>();
//	public class MovieInfo{
//		String displayName;
//		String path;
//	}
//	private Uri videoListUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//	private static int position ;
	private static boolean backFromAD = false;
	private int playedTime;

	private boolean isActive;//判断Activity是否在前台
	//private AdView adView;

	private VideoView vv = null;
	private SeekBar seekBar = null;
	private TextView durationTextView = null;
	private TextView playedTextView = null;
	private GestureDetector mGestureDetector = null;
	private AudioManager mAudioManager = null;

	private int maxVolume = 0;
	private int currentVolume = 0;

	private ImageButton bn1 = null;
	private ImageButton btn_Kuaijin = null;//2
	private ImageButton btn_PlayPause = null;//3
	private ImageButton btn_Kuaitui = null;//4
	private ImageButton btn_Vol = null;//5

	//视频控制菜单：控制栏、音量、课程题目等。     与播放界面的层次问题    使用PopupWindow的方法
	private View controlView = null; // 控制栏
	private PopupWindow controlerWindow = null;

	private SoundView mSoundView = null;  // 音量
	private PopupWindow mSoundWindow = null;

	private View extralView = null;  // 视屏上面的课程题目
	private PopupWindow extralWindow = null;

	private static int screenWidth = 0;
	private static int screenHeight = 0;
	private static int controlHeight = 0;

	private final static int TIME = 5000;

	private boolean isControllerShow = true;
	private boolean isPaused = false;
	private boolean isFullScreen = false;
	private boolean isSilent = false;  // 是否静音
	private boolean isSoundShow = false;

	// 自己加的
	private TextView tvLessonName_top;
	private LinearLayout linearLayoutBeforPlay;

	//private String courseID;
	private String courseName;
	private String lesson_website;
	private String localPath;
	String playTimeStr = "0";
//	Long startPlayingTime;

	private TextView tvlessonName;
	private ImageButton fullScreenSwitch;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return false;// 禁止显示菜单
	}

	/**
	 * Called when the activity is first created.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		//取消顶部标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.play_main);
//		if (!NetUtil.isWifi(this)) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setMessage("确认退出吗？");
//			builder.setTitle("提示");
//			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					finish();
//				}
//			});
//			builder.show();
//		}
		Log.d("OnCreate", getIntent().toString());



		// 获取传递过来的参数,课时id和名称
		Intent intent = getIntent();
		localPath = intent.getStringExtra("localPath");
		courseName = intent.getStringExtra("courseName");
		lesson_website = intent.getStringExtra("lessonWebsite");

//		lessonID = intent.getStringExtra("lessonId");
//		lessonNum = intent.getStringExtra("lessonNum");
//		lesson_name = intent.getStringExtra("lessonName");
//		startPlayingTime=intent.getLongExtra("startTime", 0);
		if (intent.getStringExtra("playTime") != null)
			playTimeStr = intent.getStringExtra("playTime");
		//横屏  播放视屏的代码
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//包含进度条和传递来得课时名称
		linearLayoutBeforPlay = (LinearLayout) findViewById(R.id.linearLayoutBeforPlay);
		tvLessonName_top = (TextView) findViewById(R.id.lesson_name_top);
		tvLessonName_top.setText(courseName);


		// 在播放视频前取消显示  控制栏和课时栏
		Looper.myQueue().addIdleHandler(new IdleHandler() {
			@Override
			public boolean queueIdle() {
				// TODO Auto-generated method stub
				if(isActive) {
					if (controlerWindow != null) { // 显示控制栏的位置  vv.isShown()
						controlerWindow.showAtLocation(vv, Gravity.BOTTOM, 0, 0);
						//controler.update(screenWidth, controlHeight);
						controlerWindow.update(vv, 0, 100, screenWidth, controlHeight);
					}
					if (extralWindow != null) { //
						extralWindow.showAtLocation(vv, Gravity.TOP, 0, 0);
						extralWindow.update(vv, 0, 0, screenWidth, 60);
					}
				}
				//myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
				return false;
			}
		});


		// 增加控制栏的布局
		controlView = getLayoutInflater().inflate(R.layout.play_controler, null);
		controlerWindow = new PopupWindow(controlView);
		durationTextView = (TextView) controlView.findViewById(R.id.duration);
		playedTextView = (TextView) controlView.findViewById(R.id.has_played);

		//显示音量的大小  重写的关于音量的View
		mSoundView = new SoundView(this);
		// 在SoundView 自己写的方法
		mSoundView.setOnVolumeChangeListener(new SoundView.OnVolumeChangedListener() {
			@Override
			public void setYourVolume(int index) {
				cancelDelayHide();
				updateVolume(index);
				hideControllerDelay();
			}
		});

		mSoundWindow = new PopupWindow(mSoundView);

		//上方需要显示的界面  课程题目  和  取消键
		extralView = getLayoutInflater().inflate(R.layout.play_extral, null);
		extralWindow = new PopupWindow(extralView);
		extralView.setAlpha((float)(1.0));

		//ImageButton backButton = (ImageButton) extralView.findViewById(R.id.back);
		tvlessonName = (TextView) extralView.findViewById(R.id.lessonTitle);  //这个TextView控件表示课程的标题
		tvlessonName.setText(courseName);

		// 用于记录播放视频列表
		//position = -1;

		//关闭程序
//        backButton.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				VideoPlay.this.finish();
//			}
//
//        });


		btn_Kuaitui = (ImageButton) controlView.findViewById(R.id.button_kuaitui);
		btn_PlayPause = (ImageButton) controlView.findViewById(R.id.button_play_pause);
		btn_Kuaijin = (ImageButton) controlView.findViewById(R.id.button_kuaijin);
		btn_Vol = (ImageButton) controlView.findViewById(R.id.button_vol);
		//fullScreenSwitch=(ImageButton) controlView.findViewById(R.id.button_fullscreen);
		vv = (VideoView) findViewById(R.id.vv);

		// 播放视频的网址
//        Uri uri = Uri.parse(AppConstant.URL_APACHE+lesson_website);
//		File file = new File(Environment.getExternalStorageDirectory()+File.separator+"CXCourse"+File.separator+courseName+".mp4");

		if(localPath != null){
			Log.v("localPath","localPath-->"+localPath);
				if (vv.getVideoHeight() == 0) {
					vv.setVideoPath(localPath);
				}
				btn_PlayPause.setImageResource(R.drawable.pause);

		}else {
			Log.v("videoplay", "播放视频Uri地址：" + lesson_website);
		 Uri uri = Uri.parse(lesson_website);
			if (uri != null) {
				if (vv.getVideoHeight() == 0) {
					vv.setVideoURI(uri);
				}
				btn_PlayPause.setImageResource(R.drawable.pause);
			} else {
				btn_PlayPause.setImageResource(R.drawable.play);
			}
		}

//        Uri uri = Uri.parse(lesson_website);



		// 取消扫描文件
/*        getVideoFile(playList, new File("/sdcard/"));
        MovieInfo internetVideoInfo = new MovieInfo();
        internetVideoInfo.displayName="网络视频测试";
        internetVideoInfo.path="http://down.wymp4.net:81/jianjian/20130522/History%20Dream%20Concert%20%E9%A5%AD%E6%8B%8D%E7%89%88%2013%2005%2011%20-%20EXO.mp4";
        playList.add(internetVideoInfo);

        Cursor cursor = getContentResolver().query(videoListUri, new String[]{"_display_name","_data"}, null, null, null);
        int n = cursor.getCount();
        cursor.moveToFirst();
        LinkedList<MovieInfo> playList2 = new LinkedList<MovieInfo>();
        for(int i = 0 ; i != n ; ++i){
        	MovieInfo mInfo = new MovieInfo();
        	mInfo.displayName = cursor.getString(cursor.getColumnIndex("_display_name"));
        	mInfo.path = cursor.getString(cursor.getColumnIndex("_data"));
        	playList2.add(mInfo);
        	cursor.moveToNext();
        }

        if(playList2.size() > playList.size()){
        	playList = playList2;
        }*/

		vv.setMySizeChangeLinstener(new VideoView.MySizeChangeLinstener() {
			@Override
			public void doMyThings() {
				// TODO Auto-generated method stub
				setVideoScale(SCREEN_FULL);
			}

		});
		//显示的透明度
//        bn1.setAlpha(0xBB);
		btn_Kuaitui.setAlpha(0xBB);
		btn_PlayPause.setAlpha(0xBB);
		btn_Kuaijin.setAlpha(0xBB);

		// 音量的设置
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		btn_Vol.setAlpha(findAlphaFromSound());

		// 视频列表按钮
/*        bn1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(VideoPlayerActivity.this, VideoChooseActivity.class);
				VideoPlayerActivity.this.startActivityForResult(intent, 0);

				cancelDelayHide();
			}

        });*/

		// 快进按钮
		btn_Kuaijin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	/*			int n = playList.size();
				if(++position < n){
					vv.setVideoPath(playList.get(position).path);
					cancelDelayHide();
					hideControllerDelay();
				}else{
					VideoPlayerActivity.this.finish();
				}*/
				int presentTime = vv.getCurrentPosition();
				if ((presentTime + 15000) < vv.getDuration())
					vv.seekTo(presentTime + 15000);
				else vv.seekTo(vv.getDuration());
			}

		});

		// 播放暂停按钮
		btn_PlayPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancelDelayHide();
				if (isPaused) {
					vv.start();
					btn_PlayPause.setImageResource(R.drawable.pause);
					hideControllerDelay();
				} else {
					vv.pause();
					btn_PlayPause.setImageResource(R.drawable.play);
				}
				isPaused = !isPaused;

			}

		});
		// 快退按钮
		btn_Kuaitui.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
/*				if(--position>=0){
					vv.setVideoPath(playList.get(position).path);
					lessonTitle.setText(playList.get(position).displayName);
					cancelDelayHide();
					hideControllerDelay();
				}else{
					VideoPlayerActivity.this.finish();
				}*/
				int presentTime = vv.getCurrentPosition();
				if ((presentTime - 15000) < 0)
					vv.seekTo(0);
				else vv.seekTo(presentTime - 15000);
			}
		});

		// 声音按钮   单击效果
		btn_Vol.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancelDelayHide();
				if (isSoundShow) {
					mSoundWindow.dismiss();
				} else {
					if (mSoundWindow.isShowing()) {
						mSoundWindow.update(15, 0, SoundView.MY_WIDTH, SoundView.MY_HEIGHT);
					} else {
						mSoundWindow.showAtLocation(vv, Gravity.RIGHT | Gravity.CENTER_VERTICAL, 15, 0);
						mSoundWindow.update(15, 0, SoundView.MY_WIDTH, SoundView.MY_HEIGHT);
					}
				}
				isSoundShow = !isSoundShow;
				hideControllerDelay();
			}
		});
		// 声音按钮   长按效果   静音
		btn_Vol.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				if (isSilent) {
					btn_Vol.setImageResource(R.drawable.soundenable);
				} else {
					btn_Vol.setImageResource(R.drawable.sounddisable);
				}
				isSilent = !isSilent;
				updateVolume(currentVolume);
				cancelDelayHide();
				hideControllerDelay();
				return true;
			}
		});
		//在设置或播放过程中发生错误时调用的回调函数。
		vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int arg1, int arg2) {
				// TODO Auto-generated method stub
				//mp.stop();
				mp.reset();
//				mp.release();
//           	 	Intent intent = new Intent(VideoPlay.this, CourseDetail.class);
//           	 	startActivity(intent);
				VideoPlayActivity.this.finish();
				return true;
			}

		});
		// 全屏键
/*        fullScreenSwitch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isFullScreen){
					setVideoScale(SCREEN_DEFAULT);
				}else{
					setVideoScale(SCREEN_FULL);
				}
				isFullScreen = !isFullScreen;

			}
		});*/
		// 进度条的变化
		seekBar = (SeekBar) controlView.findViewById(R.id.seekbar);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				if (fromUser) {
					vv.seekTo(progress);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				myHandler.removeMessages(HIDE_CONTROLER);
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
			}
		});

		getScreenSize();

		// 手势识别
		mGestureDetector = new GestureDetector(new SimpleOnGestureListener() {

			@Override
			public boolean onDoubleTap(MotionEvent e) { //双击   的第二下Touch down时触发
				// TODO Auto-generated method stub
				//取消双击   全屏   用于电视端时的情况
//				if(isFullScreen){
//					setVideoScale(SCREEN_DEFAULT);
//				}else{
//					setVideoScale(SCREEN_FULL);
//				}
//				isFullScreen = !isFullScreen;
//				Log.d(TAG, "onDoubleTap");

				if (isControllerShow) {
					showController();
				}
				//return super.onDoubleTap(e);
				return true;
			}

			//这个函数都是在touch down后又没有滑动（onScroll），又没有长按（onLongPress），然后Touch up时触发。 单击效果
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				// TODO Auto-generated method stub
				if (!isControllerShow) {
					showController();
					hideControllerDelay();
				} else {
					cancelDelayHide();
					hideController();
				}
				//return super.onSingleTapConfirmed(e);
				return true;
			}

			//Touch了不移动一直Touch down时触发
			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				if (isPaused) {
					vv.start();
					btn_PlayPause.setImageResource(R.drawable.pause);
					cancelDelayHide();
					hideControllerDelay();
				} else {
					vv.pause();
					btn_PlayPause.setImageResource(R.drawable.play);
					cancelDelayHide();
					showController();
				}
				isPaused = !isPaused;
				//super.onLongPress(e);
			}
		});

		// 播放视频前的准备
		vv.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				linearLayoutBeforPlay.setVisibility(View.GONE);

				setVideoScale(SCREEN_FULL);
				//isFullScreen = false;
//					if(isControllerShow){
//						showController();
//					}


				int i = vv.getDuration();
				Log.d("onCompletion", "" + i);
				seekBar.setMax(i);
				i /= 1000;
				int minute = i / 60;
				int hour = minute / 60;
				int second = i % 60;
				minute %= 60;
				durationTextView.setText(String.format("%02d:%02d:%02d", hour, minute, second));

					/*controler.showAtLocation(vv, Gravity.BOTTOM, 0, 0);
					controler.update(screenWidth, controlHeight);
					myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);*/
				vv.start();
				if (playTimeStr.equals("0")) {

				} else {
					vv.seekTo(Integer.parseInt(playTimeStr));
					System.out.println("-----------------------" + playTimeStr);
				}


				btn_PlayPause.setImageResource(R.drawable.pause);
				showController();
				hideControllerDelay();
				myHandler.sendEmptyMessage(PROGRESS_CHANGED);
			}
		});

		// 视频播放完成后的效果   关闭播放界面
		vv.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				arg0.reset();
				VideoPlayActivity.this.finish();
//					int n = playList.size();
//					if(++position < n){
//						vv.setVideoPath(playList.get(position).path);
//						lessonTitle.setText(playList.get(position).displayName);
//					}else{
//						Toast.makeText(VideoPlayerActivity.this, "结束", 1000).show();
//						vv.stopPlayback();
//					}
			}
		});


	}// onCreate 最后


	//回调函数 onActivityResult
//    @Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//    	if(requestCode==0&&resultCode==Activity.RESULT_OK){
//    		int result = data.getIntExtra("CHOOSE", -1);
//    		Log.d("RESULT", ""+result);
//    		if(result!=-1){
//    			vv.setVideoPath(playList.get(result).path);
//    			position = result;
//    			lessonTitle.setText(playList.get(position).displayName);
//    		}
//
//    		return ;
//    	}
//		super.onActivityResult(requestCode, resultCode, data);
//	}

	private final static int PROGRESS_CHANGED = 0;
	private final static int HIDE_CONTROLER = 1;

	// 更新UI界面  控制栏和上面的课程栏       还有进度条的更新
	Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
				case PROGRESS_CHANGED:// 控制栏和上面的课程栏显示
					int i = vv.getCurrentPosition();
					playedTime = i;
					seekBar.setProgress(i);
					// 显示缓存
					seekBar.setSecondaryProgress(vv.getBufferPercentage() * vv.getDuration() / 100);
					//System.out.println("buffering:"+vv.getBufferPercentage()+"playing"+i);
					i /= 1000;
					int minute = i / 60;
					int hour = minute / 60;
					int second = i % 60;
					minute %= 60;
					playedTextView.setText(String.format("%02d:%02d:%02d", hour, minute, second));

					sendEmptyMessage(PROGRESS_CHANGED);
					break;

				case HIDE_CONTROLER:  // 隐藏控制栏
					hideController();
					break;
			}

			super.handleMessage(msg);
		}
	};

	// 判定是否有手势操作？
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		boolean result = mGestureDetector.onTouchEvent(event);

		if (!result) {
			if (event.getAction() == MotionEvent.ACTION_UP) {

				/*if(!isControllerShow){
					showController();
					hideControllerDelay();
				}else {
					cancelDelayHide();
					hideController();
				}*/
			}
			result = super.onTouchEvent(event);
		}

		return result;
	}

	//onConfigurationChanged事件是在改变屏幕方向、弹出软件盘和隐藏软键盘时，不再去执行onCreate()方法，
	//而是直接执行onConfigurationChanged()。有的时候，当横、竖屏转换时，程序会报错或者终止，就是因为重新执行了onCreat()方法，导致系统崩溃。
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub

		getScreenSize();
		if (isControllerShow) {

			cancelDelayHide();
			hideController();
			showController();
			hideControllerDelay();
		}

		super.onConfigurationChanged(newConfig);
	}

	// activity生命周期  不同的用法   onPause  onResume    onDestroy
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		isActive = false;
		playedTime = vv.getCurrentPosition();
//		vv.pause();
//		btn_PlayPause.setImageResource(R.drawable.play);
		if (controlerWindow.isShowing()) {
			controlerWindow.dismiss();
			extralWindow.dismiss();
		}
		if (mSoundWindow.isShowing()) {
			mSoundWindow.dismiss();
		}
		myHandler.removeMessages(PROGRESS_CHANGED);
		myHandler.removeMessages(HIDE_CONTROLER);
		super.onPause();

		// 添加 播放记录到数据库
		//打开播放数据
		MyHisDBhelper helper = new MyHisDBhelper(VideoPlayActivity.this);
		Cursor c;
		// 获取播放时间，存储到本地数据库
		//添加的内容
		ContentValues values = new ContentValues();
		values.put("course_Name", courseName);
		values.put("lesson_Id", "1");
		values.put("lesson_Num", "1");
		values.put("lesson_Name", courseName);
//		if (playedTime == vv.getDuration()) {
//
//		}
		values.put("duration", playedTime);
		values.put("desc", "bupt");

		// 实例化数据库帮助类

		// 打开数据库
		helper.open();
		// 插入数据
		//int flag = 0;
		c = helper.query();
		c.moveToFirst();
		while (!c.isAfterLast()) {
			int index = c.getColumnIndex("lesson_Id");
			//Log.d("SQLite", c.getString(index));
			if (!"1".equals(c.getString(index))) {// 查找是否相同，如果不相同，查询下一条；如果相同定义为1，更新数据
				c.moveToNext();
			} else {
				//flag = 1;
				helper.del("1");
				break;
			}
		}
		// 只要退出界面就添加数据
		helper.insert(values);
//		if (flag == 0){
//			helper.insert(values);
//			Toast.makeText(getApplicationContext(), "收藏成功",Toast.LENGTH_SHORT).show();
//		}else {
//
//		}
		c.close();
		helper.close();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		isActive = true;
		vv.seekTo(playedTime);
		vv.start();
		if (vv.getVideoHeight() != 0) {
			btn_PlayPause.setImageResource(R.drawable.pause);
			hideControllerDelay();
		}
		Log.d("REQUEST", "NEW AD !");
		//adView.requestFreshAd();

		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//	playedTime=vv.getCurrentPosition();
		if (controlerWindow.isShowing()) {
			controlerWindow.dismiss();
			extralWindow.dismiss();
		}
		if (mSoundWindow.isShowing()) {
			mSoundWindow.dismiss();
		}

		myHandler.removeMessages(PROGRESS_CHANGED);
		myHandler.removeMessages(HIDE_CONTROLER);

		//playList.clear();
		Runnable uploadPlayedTime = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String userName = null;
				MyAccountDBHelper myAccountDBHelper = new MyAccountDBHelper(getApplicationContext());
				myAccountDBHelper.open(); // 打开数据库
				Cursor accountCursor = myAccountDBHelper.query();
				if (accountCursor.getCount() > 0) {
					if (accountCursor.moveToFirst()) {
						int index = accountCursor.getColumnIndex("userName");
						userName = accountCursor.getString(index);
					}
				}
				;
				accountCursor.close();
				myAccountDBHelper.close();
//				HttpClient client = new DefaultHttpClient();
//				client.getParams().setParameter(
//						CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
//				client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
//						10000);
//				HttpUriRequest req = null;
//				req = new HttpPost(AppConstant.URL_APACHE + "buptcourses/"+"identifier="+userName+"/reports");
////				req.setHeader("Cookie",AppConstant.SESSION_ID);
////				Log.v("放置的sessionid",AppConstant.SESSION_ID );
//				/*JSONObject coursePlaying=new JSONObject();
//				try {
//
//					coursePlaying.put("course_name", VideoPlay.this.courseName);
//					coursePlaying.put("learning_time", playedTime);
//					Log.v("播放记录JSON信息", coursePlaying.toString());
//				} catch (JSONException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}*/
//				List<NameValuePair> params = new ArrayList<NameValuePair>();
//				params.add(new BasicNameValuePair("course_name",VideoPlay.this.courseName));
//				params.add(new BasicNameValuePair("learning_time", playedTime+""));
//				Log.v("播放时长上传的数据", VideoPlay.this.courseName+playedTime);
//				HttpEntity dataEntity = null;
//				try {
//					dataEntity = new UrlEncodedFormEntity(params,
//							HTTP.UTF_8);
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				((HttpPost) req).setEntity(dataEntity);
//				try {
//					client.execute(req);
//				} catch (ClientProtocolException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		};
		new Thread(uploadPlayedTime).start();


		super.onDestroy();
	}


	// 隐藏控制栏  和  课时栏
	private void hideController() {
		if (controlerWindow.isShowing()) {
			controlerWindow.update(0, 0, 0, 0);
			extralWindow.update(0, 0, screenWidth, 0);
			isControllerShow = false;
		}
		if (mSoundWindow.isShowing()) {
			mSoundWindow.dismiss();
			isSoundShow = false;
		}
	}

	// 过一段时间   隐藏控制栏
	private void hideControllerDelay() {
		myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
	}

	// 显示   控制栏和课时题目栏
	private void showController() {
		controlerWindow.update(0, screenHeight - controlHeight, screenWidth, controlHeight);
		extralWindow.update(0, 0, screenWidth, 60);
/*		if(isFullScreen){
			extralWindow.update(0,0,screenWidth, 60);
		}else{
			extralWindow.update(0,25,screenWidth, 60);
		}*/

		isControllerShow = true;
	}

	// 取消 过段时间时间的隐藏
	private void cancelDelayHide() {
		myHandler.removeMessages(HIDE_CONTROLER);
	}

	// 获取机器屏幕的大小
	@SuppressWarnings("deprecation")
	private void getScreenSize() {
		Display display = getWindowManager().getDefaultDisplay();
		screenHeight = display.getHeight();
		screenWidth = display.getWidth();
		controlHeight = 90;// 控制栏的高度

		//adView = (AdView) extralView.findViewById(R.id.ad);
		//LayoutParams lp = adView.getLayoutParams();
		//lp.width = screenWidth*3/5;
	}

	private final static int SCREEN_FULL = 0;
	private final static int SCREEN_DEFAULT = 1;

	// 判定时候全屏
	private void setVideoScale(int flag) {
		LayoutParams lp = vv.getLayoutParams();
		switch (flag) {
			case SCREEN_FULL:
				Log.d(TAG, "screenWidth: " + screenWidth + " screenHeight: " + screenHeight);
				vv.setVideoScale(screenWidth, screenHeight);
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
				break;

			case SCREEN_DEFAULT:
				int videoWidth = vv.getVideoWidth();
				int videoHeight = vv.getVideoHeight();
				int mWidth = screenWidth;
				int mHeight = screenHeight;

				if (videoWidth > 0 && videoHeight > 0) {
					if (videoWidth * mHeight > mWidth * videoHeight) {
						//Log.i("@@@", "image too tall, correcting");
						mHeight = mWidth * videoHeight / videoWidth;
					} else if (videoWidth * mHeight < mWidth * videoHeight) {
						//Log.i("@@@", "image too wide, correcting");
						mWidth = mHeight * videoWidth / videoHeight;
					} else {

					}
				}
				vv.setVideoScale(mWidth, mHeight);
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
				break;
		}
	}

	private int findAlphaFromSound() {
		if (mAudioManager != null) {
			//int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			int alpha = currentVolume * (0xCC - 0x55) / maxVolume + 0x55;
			return alpha;
		} else {
			return 0xCC;
		}
	}

	// 更新音量
	private void updateVolume(int index) {
		if (mAudioManager != null) {
			if (isSilent) {
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
			} else {
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
			}
			currentVolume = index;
			btn_Vol.setAlpha(findAlphaFromSound());
		}
	}

	//这就是文件系统的遍历：
/*    private void getVideoFile(final LinkedList<MovieInfo> list,File file){

    	file.listFiles(new FileFilter(){

			@Override
			public boolean accept(File file) {
				// TODO Auto-generated method stub
				String name = file.getName();
				int i = name.indexOf('.');
				if(i != -1){
					name = name.substring(i);
					if(name.equalsIgnoreCase(".mp4")||name.equalsIgnoreCase(".3gp")){

						MovieInfo mi = new MovieInfo();
						mi.displayName = file.getName();
						mi.path = file.getAbsolutePath();
						list.add(mi);
						return true;
					}
				}else if(file.isDirectory()){
					getVideoFile(list, file);
				}
				return false;
			}
    	});
    }*/

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}


}