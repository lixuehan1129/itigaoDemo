package com.example.itigao.Personal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.itigao.Media.JZMediaIjkplayer;
import com.example.itigao.R;
import com.example.itigao.Utils.StatusBarUtils;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * Created by 最美人间四月天 on 2018/12/15.
 */

public class PersonIndoorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_indoor);
        StatusBarUtils.setWindowStatusBarColor(PersonIndoorActivity.this, R.color.colorWhite);
        Jzvd.setMediaInterface(new JZMediaIjkplayer());
        initView();
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.person_indoor_mainTool);
        toolbar.setTitle("室内监测");
        back(toolbar);

        TextView textView = (TextView) findViewById(R.id.person_indoor_tv);
        textView.setText("室内环境监测");

        JzvdStd jzvdStd = (JzvdStd) findViewById(R.id.person_indoor_ij);
        jzvdStd.setUp("rtmp://zb.tipass.com:1935/live/1233","",Jzvd.SCREEN_WINDOW_NORMAL);
        jzvdStd.startVideo();
    }

    //返回注销事件
    private void back(Toolbar toolbar){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
