package com.example.fitdemo.Classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fitdemo.Adapter.InteractAdapter;

import com.example.fitdemo.R;
import com.example.fitdemo.Utils.StatusBarUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;


import java.util.ArrayList;
import java.util.List;



/**
 * Created by 最美人间四月天 on 2018/12/14.
 */

public class BroadcastActivity extends AppCompatActivity {


    private LinearLayout linearLayout;
    private RecyclerView recyclerView;
    private EditText editText;
    private ImageView imageView;

    private StandardGSYVideoPlayer videoPlayer;
    private OrientationUtils orientationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_broad_activity);
        StatusBarUtils.setWindowStatusBarColor(BroadcastActivity.this, R.color.colorWhite);
        PlayerFactory.setPlayManager(IjkPlayerManager.class);//ijk模式
        initView();
    }

    private void initView(){
        videoPlayer = (StandardGSYVideoPlayer) findViewById(R.id.video_broad_vv);
        recyclerView = (RecyclerView) findViewById(R.id.video_broad_rv) ;
        linearLayout = (LinearLayout) findViewById(R.id.video_broad_li);
        editText = (EditText) findViewById(R.id.video_broad_et);
        imageView = (ImageView) findViewById(R.id.video_broad_iv);

        recyclerView.setLayoutManager(new LinearLayoutManager(BroadcastActivity.this));
        //获取屏幕宽
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        //   setWidthHeightWithRatio(ijkVideoView,width,16,9);

        imageOnClick();
        initData();

        setPlay();

    }

    private void initData(){
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> content = new ArrayList<>();
        for(int i=0; i<8; i++){
            name.add("用户名" + i);
            content.add("看完这个视频，我想说点什么呢" + i);
        }
        name.add("用户名");
        content.add("看完这个视频，我想说点什么呢，我觉得真的很好很有用，6666666666");
        setAdapter(name,content);
    }

    private void setAdapter(final ArrayList<String> name, ArrayList<String> content){
        List<InteractAdapter.Interact> interacts = new ArrayList<>();
        for(int i=0; i<name.size(); i++){
            InteractAdapter interactAdapter = new InteractAdapter(interacts);
            InteractAdapter.Interact interact = interactAdapter.new Interact(name.get(i), content.get(i));
            interacts.add(interact);
        }

        InteractAdapter interactAdapter = new InteractAdapter(interacts);
        recyclerView.setAdapter(interactAdapter);
        interactAdapter.setOnItemClickListener(new InteractAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    private void setPlay(){

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


        videoPlayer.setUp("rtmp://39.105.213.41:1935/live/1233",false,null);
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        videoPlayer.setNeedShowWifiTip(true);
        videoPlayer.setIsTouchWiget(false);


        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);


        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayer.startWindowFullscreen(BroadcastActivity.this,true,true);
            }
        });

        //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
        videoPlayer.setAutoFullWithSize(true);
//        //音频焦点冲突时是否释放
//        videoPlayer.setReleaseWhenLossAudio(false);
//        //自动转屏
//        videoPlayer.setRotateViewAuto(true);

        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        videoPlayer.startPlayLogic();
    }

    private void imageOnClick(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(editText.getText())){
                    ProgressDialog progressDialog = ProgressDialog.show(BroadcastActivity.this,"","正在上传...",true);
                    progressDialog.setCancelable(true);// 设置是否可以通过点击Back键取消
                }else {
                    Toast.makeText(BroadcastActivity.this,"内容不能为空",Toast.LENGTH_LONG).show();
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
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        videoPlayer.onVideoResume();
    }

    @Override
    public void onBackPressed() {

//        先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }

        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }

        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }
    @Override
    protected void onStop() {
        super.onStop();
        //点击返回或不允许后台播放时 释放资源
    }

}
