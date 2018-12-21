package com.example.fitdemo.Subscribe;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.example.fitdemo.AutoProject.Tip;
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

public class VideoPlayActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,VideoFragment.CallBackValue{

    private VideoPlayerController videoPlayerController;
    private VideoPlayer videoPlayer;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private LinearLayout linearLayout;
    private EditText editText;
    private ImageView imageView;

    private String[] titles = new String[]{"    视频    ", "    互动    "};
    private List<Fragment> fragments=new ArrayList<>();

    private String video_add;
    private int video_bid;
    private int video_section;
    private int video_select;
    private int video_record; //0表示主页推荐内容不计，1表示课程列表加入记录，2观看记录

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_activity);
        StatusBarUtils.setWindowStatusBarColor(VideoPlayActivity.this, R.color.colorWhite);

        Intent intent = getIntent();
        video_add = intent.getStringExtra("video_add");
        video_bid = intent.getIntExtra("video_bid",0);
        video_section = intent.getIntExtra("video_section",0);
        video_select = intent.getIntExtra("video_select",1);
        video_record = intent.getIntExtra("video_record",1);

        initView();
    }

    private void initView(){
        videoPlayer = (VideoPlayer) findViewById(R.id.video_play_vv);
        tabLayout = (TabLayout) findViewById(R.id.video_play_layout);
        mViewPager = (ViewPager) findViewById(R.id.video_play_viewpager);
        linearLayout = (LinearLayout) findViewById(R.id.video_play_li);
        editText = (EditText) findViewById(R.id.video_play_et);
        imageView = (ImageView) findViewById(R.id.video_play_iv);

        //获取屏幕宽
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);

        assert wm != null;
        int width = wm.getDefaultDisplay().getWidth();
        setWidthHeightWithRatio(videoPlayer,width,16,9);

        imageOnClick();
        initTab();

        setPlay(video_add);

    }

    private void setPlay(String url){
        //设置播放类型
        //IJKPlayer or MediaPlayer
        videoPlayer.setPlayerType(ConstantKeys.IjkPlayerType.TYPE_IJK);
        //设置视频地址和请求头部
        videoPlayer.setUp(url,null);
        //是否从上一次的位置播放
        videoPlayer.continueFromLastPosition(false);
        //设置播放速度
        videoPlayer.setSpeed(1.0f);

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
        videoPlayer.start();
        videoPlayer.setController(videoPlayerController);


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
                    assert imm != null;
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

        Fragment fragment1 = new VideoFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("video_play_section",video_section);
        bundle1.putInt("video_play_bid",video_bid);
        bundle1.putInt("video_play_record",video_record);
        bundle1.putInt("video_play_select",video_select);
        fragment1.setArguments(bundle1);

        Fragment fragment2 = new InteractFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("video_play_bid",video_bid);
        fragment2.setArguments(bundle2);

        fragments.add(fragment1);
        fragments.add(fragment2);
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

    //要获取的值  就是这个参数的值
    @Override
    public void SendMessageValue(String strValue) {
        // TODO Auto-generated method stub
        videoPlayer.release();
        videoPlayer.releasePlayer();
        VideoPlayerManager.instance().releaseVideoPlayer();
        setPlay(strValue);
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


