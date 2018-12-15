package com.example.fitdemo.Personal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.fitdemo.R;
import com.example.fitdemo.Utils.StatusBarUtils;

/**
 * Created by 最美人间四月天 on 2018/12/15.
 */

public class PersonIndoorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_change_activity);
        StatusBarUtils.setWindowStatusBarColor(PersonIndoorActivity.this, R.color.colorWhite);
        initView();
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.person_change_mainTool);
        toolbar.setTitle("室内监测");
        back(toolbar);

        TextView textView = (TextView) findViewById(R.id.person_change_tv);
        textView.setText("室内环境监测");

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
