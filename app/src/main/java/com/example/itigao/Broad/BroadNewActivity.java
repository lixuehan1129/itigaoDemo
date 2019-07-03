package com.example.itigao.Broad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.itigao.Adapter.TabLayoutAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JsonCode;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.ClassAb.Anchor;
import com.example.itigao.Emotion.fragment.BackHandleFragment;
import com.example.itigao.Emotion.fragment.BackHandleInterface;
import com.example.itigao.Media.JZMediaIjkplayer;
import com.example.itigao.R;
import com.example.itigao.Utils.StatusBarUtils;
import com.example.itigao.Video.HuDongFragment;
import com.example.itigao.Video.RankFragment;
import com.example.itigao.Video.RoomFragment;
import com.example.itigao.okHttp.OkHttpBase;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;

import static cn.jzvd.JZUtils.dip2px;

/**
 * Created by 最美人间四月天 on 2019/3/11.
 */

public class BroadNewActivity extends AppCompatActivity implements
        TabLayout.OnTabSelectedListener,AboutFragment.CallBackValueAn, BackHandleInterface {


    private BackHandleFragment backHandleFragment;

    private int anchorId, anchorRoom, anchorFocus;
    private Anchor anchor;
    private String url;
    //private String URL_F = "rtmp://114.115.150.93:1935/live/";

    private StandardGSYVideoPlayer videoPlayer;
    private OrientationUtils orientationUtils;

    //private JzvdStd jzvdStd;

    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private TextView focus;


    private String[] titles = new String[]{" 聊天 ", " 简介 ", " 排行 ", " 推荐 "};
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broad_activity);
        StatusBarUtils.setWindowStatusBarColor(BroadNewActivity.this, R.color.colorWhite);
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);//ijk模式
        Intent intent = getIntent();
        anchor = (Anchor) intent.getSerializableExtra("anchor_all");
        //Jzvd.setMediaInterface(new JZMediaIjkplayer());
        initView();

    }

    @Override
    public void SendMessageValueAn(Anchor strValue) {
        anchor = strValue;
        initView();
    }

    private void initView(){
        anchorId = anchor.getAnchor_bid();
        anchorRoom = anchor.getAnchor_room();
        anchorFocus = anchor.getAnchor_focus();

        videoPlayer = (StandardGSYVideoPlayer) findViewById(R.id.broad_new_vv);
        tabLayout = (TabLayout) findViewById(R.id.broad_new_layout);
        mViewPager = (ViewPager) findViewById(R.id.broad_new_viewpager);
        focus = (TextView) findViewById(R.id.focus);
      //  jzvdStd = (JzvdStd) findViewById(R.id.jz_vv);

        if(anchorFocus == 1){
            focus.setText(" 取消关注 ");
        }else {
            focus.setText(" + 关注 ");
        }


        //获取屏幕宽
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        setWidthHeightWithRatio(videoPlayer,width,16,9);

        viewClick();
        initTab();
        setPlay();
    }

    private void initTab(){
        tabLayout.clearOnTabSelectedListeners();
        mViewPager.clearOnPageChangeListeners();
        fragments = new ArrayList<>();

        mViewPager.setOffscreenPageLimit(4);
        //设置TabLayout标签的显示方式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (String tab:titles){
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
        //设置TabLayout点击事件
        tabLayout.setOnTabSelectedListener(this);

        Fragment fragment1 = new BroadRoomFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("broad_new_bid",anchorRoom);
        fragment1.setArguments(bundle1);

        Fragment fragment2 = new IntroduceFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("broad_intro_all",anchor);
        fragment2.setArguments(bundle2);

        Fragment fragment3 = new RankFragment();

        Fragment fragment4 = new AboutFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putInt("broad_go_classify",anchor.getAnchor_classify());
        fragment4.setArguments(bundle4);

        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);

        TabLayoutAdapter mTabLayoutAdapter = new TabLayoutAdapter(getSupportFragmentManager(),titles, fragments);
        mViewPager.setAdapter(mTabLayoutAdapter);

        tabLayout.setupWithViewPager(mViewPager);

        reflex(tabLayout);
    }

    private void setPlay(){
        url = AppConstants.BROAD_URL + anchorId;

        /**
         * 设置右下角 显示切换到全屏 的按键资源
         * 必须在setUp之前设置
         * 不设置使用默认
         */
        videoPlayer.setEnlargeImageRes(R.mipmap.ic_full);
        /**
         * 设置右下角 显示退出全屏 的按键资源
         * 必须在setUp之前设置
         * 不设置使用默认
         */
        videoPlayer.setShrinkImageRes(R.mipmap.ic_full_b);

        videoPlayer.setUp(url,false,null);
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        videoPlayer.setNeedShowWifiTip(true);
        videoPlayer.setIsTouchWiget(false);


        videoPlayer.setBottomProgressBarDrawable(null);

        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);


        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayer.startWindowFullscreen(BroadNewActivity.this,true,true);
            }
        });

        //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
        videoPlayer.setAutoFullWithSize(true);

        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(v -> onBackPressed());
        videoPlayer.startPlayLogic();

     //   JzvdStd jzvdStd = (JzvdStd) findViewById(R.id.person_indoor_ij);
    //    jzvdStd.setUp(url,"",Jzvd.SCREEN_WINDOW_NORMAL);
    //    jzvdStd.startVideo();
    }

    private void viewClick(){

        if(focus.getText().toString().contentEquals(" 取消关注 ")){
            focus.setOnClickListener(v -> {
                AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(this);
                normalDialog.setMessage("\n取消关注，我是认真的!\n");
                normalDialog.setPositiveButton("去意已决",
                        (dialog, which) -> {
                            //...To-do
                            new Thread(){
                                public void run(){
                                    RequestBody requestBody = new FormBody.Builder()
                                            .add("focus_user", SharePreferences.getString(BroadNewActivity.this, AppConstants.USER_PHONE))
                                            .add("focus_anchor", String.valueOf(anchorId))
                                            .build();
                                    String regData = OkHttpBase.getResponse(requestBody,"deleteFocus");
                                    if(JsonCode.getCode(regData) == 200){
                                        Message message = new Message();
                                        message.what = 105;
                                        handler.sendMessage(message);
                                        Intent intent_broad = new Intent(AppConstants.BROAD_FOCUS);
                                        LocalBroadcastManager.getInstance(BroadNewActivity.this).sendBroadcast(intent_broad);
                                    }
                                }
                            }.start();
                        });
                normalDialog.setNegativeButton("再三考虑",
                        (dialog, which) -> {
                            //...To-do
                        });
                // 显示
                normalDialog.show();
            });
        }else {
            focus.setOnClickListener(v -> {
                AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(this);
                normalDialog.setMessage("\n关注主播，不再迷路!\n");
                normalDialog.setPositiveButton("确定关注",
                        (dialog, which) -> {
                            //...To-do
                            new Thread(){
                                public void run(){
                                    RequestBody requestBody = new FormBody.Builder()
                                            .add("focus_user", SharePreferences.getString(BroadNewActivity.this, AppConstants.USER_PHONE))
                                            .add("focus_anchor", String.valueOf(anchorId))
                                            .build();
                                    String regData = OkHttpBase.getResponse(requestBody,"insertFocus");
                                    if(JsonCode.getCode(regData) == 200){
                                        Message message = new Message();
                                        message.what = 106;
                                        handler.sendMessage(message);
                                        Intent intent_broad = new Intent(AppConstants.BROAD_FOCUS);
                                        LocalBroadcastManager.getInstance(BroadNewActivity.this).sendBroadcast(intent_broad);
                                    }
                                }
                            }.start();
                        });
                normalDialog.setNegativeButton("残忍放弃",
                        (dialog, which) -> {
                            //...To-do
                        });
                // 显示
                normalDialog.show();
            });
        }

    }

    private Handler handler = new Handler(msg -> {
        // TODO Auto-generated method stub
        switch (msg.what){
            case 105:{
                anchorFocus = 0;
                focus.setText(" + 关注 ");
                viewClick();
                break;
            }
            case 106:{
                anchorFocus = 1;
                focus.setText(" 取消关注 ");
                viewClick();
                break;
            }
            default:
                break;
        }
        return false;
    });

    @Override
    public void onSelectedFragment(BackHandleFragment backHandleFragment) {
        this.backHandleFragment = backHandleFragment;
    }


    public void reflex(final TabLayout tabLayout){
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp10 = dip2px(tabLayout.getContext(), 10);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width ;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);
                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        videoPlayer.onVideoResume();
        GSYVideoManager.releaseAllVideos();
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
       // JzvdStd.goOnPlayOnPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
       // JzvdStd.goOnPlayOnResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        //videoPlayer.onVid
        GSYVideoManager.releaseAllVideos();
       // JzvdStd.releaseAllVideos();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }else if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }else if(backHandleFragment == null || !backHandleFragment.onBackPressed()){
            if(getSupportFragmentManager().getBackStackEntryCount() == 0){
               // videoPlayer.setVideoAllCallBack(null);
                GSYVideoManager.releaseAllVideos();
                super.onBackPressed();
            }else{
                getSupportFragmentManager().popBackStack();
            }
        }

        //释放所有
     //   videoPlayer.setVideoAllCallBack(null);
      //  jzvdStd.release();
     //   super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //点击返回或不允许后台播放时 释放资源
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}

