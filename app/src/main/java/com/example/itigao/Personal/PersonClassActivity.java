package com.example.itigao.Personal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.itigao.Adapter.ClassSelectAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JsonCode;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.ClassAb.Appoint;
import com.example.itigao.R;
import com.example.itigao.Utils.DateUtils;
import com.example.itigao.Utils.StatusBarUtils;
import com.example.itigao.okHttp.OkHttpBase;


import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by 最美人间四月天 on 2018/12/15.
 */

public class PersonClassActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClassSelectAdapter classSelectAdapter;
    private List<Appoint> class_selects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_class);
        StatusBarUtils.setWindowStatusBarColor(PersonClassActivity.this, R.color.colorWhite);
        initView();
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.person_class_mainTool);
        toolbar.setTitle("课程");
        back(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.person_class_rv);
        classSelectAdapter = new ClassSelectAdapter(class_selects);
        recyclerView.setLayoutManager(new LinearLayoutManager(PersonClassActivity.this));
        recyclerView.setAdapter(classSelectAdapter);
        connectClass();
    }

    private void connectClass(){
        new Thread(){
            public void run(){
                RequestBody requestBody = new FormBody.Builder()
                        .add("yu_user",SharePreferences.getString(PersonClassActivity.this, AppConstants.USER_PHONE))
                        .add("yu_time", String.valueOf(DateUtils.IntTime(0)-1))
                        .build();
                String regData = OkHttpBase.getResponse(requestBody,"http://39.105.213.41:8080/StudyAppService/StudyServlet/appointAll");
                if(regData != null){
                    if(JsonCode.getCode(regData) == 200){
                        String jsonData = JsonCode.getData(regData);
                        class_selects = JsonCode.jsonToList(jsonData, Appoint.class);

                        Message message = new Message();
                        message.what = 117;
                        handler.sendMessage(message);
                    }
                }
            }
        }.start();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case 117:{
                    if(class_selects.size() != 0){
                        classSelectAdapter.addDataAt(class_selects);
                    }
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });

    //返回注销事件
    private void back(Toolbar toolbar){
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
