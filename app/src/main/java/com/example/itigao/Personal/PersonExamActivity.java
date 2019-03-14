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

public class PersonExamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_exam);
        StatusBarUtils.setWindowStatusBarColor(PersonExamActivity.this, R.color.colorWhite);
        initView();
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.person_exam_mainTool);
        toolbar.setTitle("校园生活");
        back(toolbar);

        TextView textView = (TextView) findViewById(R.id.person_exam_tv);
        textView.setText("2014年9月，来到北京科技大学\n开始军训\n慢慢适应了大学生活，认识了许多同学\n时间过得很快，转眼间四年就过去了\n开始读研究生\n。。。。。。");

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
