package com.example.fitdemo.Classes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.fitdemo.Media.JZMediaIjkplayer;
import com.example.fitdemo.R;
import com.example.fitdemo.Utils.StatusBarUtils;


import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


/**
 * Created by 最美人间四月天 on 2018/12/25.
 */

public class HuDongPlayActivity extends AppCompatActivity{

    private String url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hudong_play);
        StatusBarUtils.setWindowStatusBarColor(HuDongPlayActivity.this, R.color.colorWhite);
        Jzvd.setMediaInterface(new JZMediaIjkplayer());
        Jzvd.SAVE_PROGRESS = false;
        Intent intent = getIntent();
        url = intent.getStringExtra("hudong_c_add");
        initView();
    }

    private void initView(){
        if(url != null){
            setPlay(url);
        }

    }

    private void setPlay(String url){
        JzvdStd jzvdStd = (JzvdStd) findViewById(R.id.hudong_play_vv);
        jzvdStd.setUp(url, "", Jzvd.SCREEN_WINDOW_NORMAL);
        jzvdStd.startVideo();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JzvdStd.goOnPlayOnPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JzvdStd.goOnPlayOnResume();
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        JzvdStd.releaseAllVideos();
        super.onDestroy();
    }
}
