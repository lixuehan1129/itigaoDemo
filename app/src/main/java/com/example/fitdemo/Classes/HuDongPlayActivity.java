package com.example.fitdemo.Classes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.fitdemo.R;
import com.example.fitdemo.Utils.StatusBarUtils;


import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


/**
 * Created by 最美人间四月天 on 2018/12/25.
 */

public class HuDongPlayActivity extends AppCompatActivity{

    private JzvdStd jzvdStd;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hudong_play);
        StatusBarUtils.setWindowStatusBarColor(HuDongPlayActivity.this, R.color.colorWhite);

        initView();
    }

    private void initView(){
        setPlay("http://39.105.213.41:8080/video/mp_test.mp4");
    }

    private void setPlay(String url){
        jzvdStd = (JzvdStd) findViewById(R.id.hudong_play_vv);
        jzvdStd.setUp(url, "", Jzvd.SCREEN_WINDOW_NORMAL);
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
        Jzvd.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
