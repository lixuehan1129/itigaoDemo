package com.example.fitdemo.Subscribe;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.design.widget.TabLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.fitdemo.Adapter.TabLayoutAdapter;
import com.example.fitdemo.R;
import com.example.fitdemo.Recommend.FitFragment;
import com.example.fitdemo.Recommend.RunningFragment;
import com.example.fitdemo.Utils.StatusBarUtils;

import org.yczbj.ycvideoplayerlib.ConstantKeys;
import org.yczbj.ycvideoplayerlib.VideoPlayer;
import org.yczbj.ycvideoplayerlib.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.VideoPlayerManager;
import org.yczbj.ycvideoplayerlib.listener.OnCompletedListener;
import org.yczbj.ycvideoplayerlib.listener.OnVideoBackListener;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by 最美人间四月天 on 2018/11/29.
 */

public class VideoPlayActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    private VideoPlayerController videoPlayerController;
    private VideoPlayer videoPlayer;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private LinearLayout linearLayout;
    private EditText editText;
    private ImageView imageView;

    private String[] titles = new String[]{"    视频    ", "    互动    "};
    private List<Fragment> fragments=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_activity);
        StatusBarUtils.setWindowStatusBarColor(VideoPlayActivity.this, R.color.colorWhite);

        Intent intent = getIntent();
        int id = intent.getIntExtra("video_add",0);

        initView(id);
    }

    private void initView(int id){
        videoPlayer = (VideoPlayer) findViewById(R.id.video_play_vv);
        tabLayout = (TabLayout) findViewById(R.id.video_play_layout);
        mViewPager = (ViewPager) findViewById(R.id.video_play_viewpager);
        linearLayout = (LinearLayout) findViewById(R.id.video_play_li);
        editText = (EditText) findViewById(R.id.video_play_et);
        imageView = (ImageView) findViewById(R.id.video_play_iv);

        //获取屏幕宽
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        setWidthHeightWithRatio(videoPlayer,width,16,9);

        imageOnClick();
        initTab();


        //只是几个地址
        switch (id){
            case 0:{
                setPlay("http://39.105.213.41:8080/video/mp_fit1.mp4");
                break;
            }
            case 1:{
                setPlay("http://39.105.213.41:8080/video/mp_fit2.mp4");
                break;
            }
            case 2:{
                setPlay("http://39.105.213.41:8080/video/mp_fit3.mp4");
                break;
            }
            case 3:{
                setPlay("http://39.105.213.41:8080/video/mp_fit4.mp4");
                break;
            }
            default:
                break;
        }
    }

    private void setPlay(String url){
        //设置播放类型
        //IJKPlayer or MediaPlayer
        videoPlayer.setPlayerType(ConstantKeys.IjkPlayerType.TYPE_NATIVE);
        //视频地址
   //     Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + mp4);
        //设置视频地址和请求头部
        videoPlayer.setUp(url,null);
        //是否从上一次的位置播放
        videoPlayer.continueFromLastPosition(true);
        //设置播放速度
        videoPlayer.setSpeed(1.0f);
        //

        //创建视频控制器
        videoPlayerController = new VideoPlayerController(this);
        videoPlayerController.setTitle("");
        //设置5秒不操作后隐藏头部和底部布局视图
        videoPlayerController.setHideTime(5000);
       // videoPlayerController.setLoadingType(2);
        //返回监听
        videoPlayerController.setOnVideoBackListener(new OnVideoBackListener() {
            @Override
            public void onBackClick() {
                onBackPressed();
                finish();
            }
        });
        videoPlayer.setController(videoPlayerController);


//            解析 res 文件夹下的资源 Uri
//        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + mp4);
        // 设置资源
//        videoView.setVideoURI(uri);
//
//
//        videoView.setMediaController(new MediaController(VideoPlayActivity.this));//显示控制栏
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                videoView.start();//开始播放视频
//            }
//        });
//
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
//        {
//            @Override
//            public void onPrepared(MediaPlayer mp)
//            {
//                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
//                //TODO
//            }
//        });


    }

    private void imageOnClick(){

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(editText.getText())){
                    ProgressDialog progressDialog = ProgressDialog.show(VideoPlayActivity.this,"","正在上传...",true);
                    progressDialog.setCancelable(true);// 设置是否可以通过点击Back键取消
                    //将输入法隐藏，mPasswordEditText 代表密码输入框
                    InputMethodManager imm =(InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                }else {
                    Toast.makeText(VideoPlayActivity.this,"内容不能为空",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void initTab(){
        mViewPager.setOffscreenPageLimit(2);
        //设置TabLayout标签的显示方式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (String tab:titles){
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
        //设置TabLayout点击事件
        tabLayout.setOnTabSelectedListener(this);
        fragments.add(new VideoFragment());
        fragments.add(new InteractFragment());
        TabLayoutAdapter mTabLayoutAdapter = new TabLayoutAdapter(getSupportFragmentManager(),titles, fragments);
        mViewPager.setAdapter(mTabLayoutAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    //根据宽高比设置控件宽高, 如设置宽高比为5:4，那么widthRatio为5，heightRatio为4
    public static void setWidthHeightWithRatio(View view, int width, int widthRatio, int heightRatio) {
        if (width <= 0) width = view.getWidth();
        int height = width * heightRatio / widthRatio;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = height;
            layoutParams.width = width;
            view.setLayoutParams(layoutParams);
        }
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        videoPlayer.release();
        videoPlayer.releasePlayer();
        VideoPlayerManager.instance().releaseVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (VideoPlayerManager.instance().onBackPressed()) return;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
// TODO Auto-generated method stub
        super.onDestroy();

    }

}


