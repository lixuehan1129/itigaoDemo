package com.example.itigao.Personal;

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

public class PersonDeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_device);
        StatusBarUtils.setWindowStatusBarColor(PersonDeviceActivity.this, R.color.colorWhite);
        initView();
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.person_device_mainTool);
        toolbar.setTitle("智能设备");
        back(toolbar);

        TextView textView = (TextView) findViewById(R.id.person_device_tv);
        textView.setText("查看并添加智能设备");

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
