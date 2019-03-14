package com.example.itigao.Subscribe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.itigao.R;
import com.example.itigao.Utils.StatusBarUtils;

/**
 * Created by 最美人间四月天 on 2018/12/15.
 */

public class VideoRankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_rank);
        StatusBarUtils.setWindowStatusBarColor(VideoRankActivity.this, R.color.colorWhite);
        initView();
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.rank_mainTool);
        toolbar.setTitle("排名");
        back(toolbar);

        TextView textView = (TextView) findViewById(R.id.rank_tv);
        textView.setText("详细排名");

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
}
