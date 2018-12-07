package com.example.fitdemo.Subscribe;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.fitdemo.R;
import com.example.fitdemo.Utils.StatusBarUtils;

import org.yczbj.ycvideoplayerlib.ConstantKeys;
import org.yczbj.ycvideoplayerlib.VideoPlayer;
import org.yczbj.ycvideoplayerlib.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.VideoPlayerManager;
import org.yczbj.ycvideoplayerlib.listener.OnVideoBackListener;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;


/**
 * Created by 最美人间四月天 on 2018/11/29.
 */

public class VideoPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_activity);
        StatusBarUtils.setWindowStatusBarColor(VideoPlayActivity.this, R.color.colorWhite);
        Intent intent = getIntent();
        int id = intent.getIntExtra("video_add",0);
        final VideoPlayer videoView = (VideoPlayer) findViewById(R.id.video_play_vv);


        switch (id){
            case 0:{
                setPlay(videoView,R.raw.mp_fit1);
                break;
            }
            case 1:{
                setPlay(videoView,R.raw.mp_fit2);
                break;
            }
            case 2:{
                setPlay(videoView,R.raw.mp_fit3);
                break;
            }
            case 3:{
                setPlay(videoView,R.raw.mp_fit4);
                break;
            }
            default:
                break;
        }



    }

    private void setPlay(final VideoPlayer videoPlayer, Integer mp4){
        //设置播放类型
        //IJKPlayer or MediaPlayer
        videoPlayer.setPlayerType(ConstantKeys.IjkPlayerType.TYPE_NATIVE);
        //视频地址
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + mp4);
        //设置视频地址和请求头部
        videoPlayer.setUp(String.valueOf(uri),null);
        //是否从上一次的位置播放
        videoPlayer.continueFromLastPosition(false);
        //设置播放速度
        videoPlayer.setSpeed(1.0f);
        //

        //创建视频控制器
        VideoPlayerController videoPlayerController = new VideoPlayerController(this);
        videoPlayerController.setTitle("流行视频");
        //设置5秒不操作后隐藏头部和底部布局视图
        videoPlayerController.setHideTime(5000);
        //返回监听
        videoPlayerController.setOnVideoBackListener(new OnVideoBackListener() {
            @Override
            public void onBackClick() {
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


    @Override
    protected void onStop() {
        super.onStop();
        VideoPlayerManager.instance().releaseVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (VideoPlayerManager.instance().onBackPressed()) return;
        super.onBackPressed();
    }



}
