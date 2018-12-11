package com.example.fitdemo.Classes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.fitdemo.R;
import com.example.fitdemo.Utils.StatusBarUtils;

/**
 * Created by 最美人间四月天 on 2018/12/11.
 */

public class ChildActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity);
        StatusBarUtils.setWindowStatusBarColor(ChildActivity.this, R.color.colorWhite);
        Toolbar toolbar = (Toolbar)findViewById(R.id.child_mainTool);
        back(toolbar);
    }





    private void back(Toolbar toolbar){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
