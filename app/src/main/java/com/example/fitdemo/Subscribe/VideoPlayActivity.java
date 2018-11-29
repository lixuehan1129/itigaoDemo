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
        final VideoView videoView = (VideoView) findViewById(R.id.video_play_vv);


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

    private void setPlay(final VideoView videoView, Integer mp4){
        //   解析 res 文件夹下的资源 Uri
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + mp4);
        // 设置资源
        videoView.setVideoURI(uri);


        videoView.setMediaController(new MediaController(VideoPlayActivity.this));//显示控制栏
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();//开始播放视频
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                //TODO
            }
        });


    }


}
