package com.example.itigao.Personal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JsonCode;
import com.example.itigao.ClassAb.Register;
import com.example.itigao.R;
import com.example.itigao.Utils.DateUtils;
import com.example.itigao.Utils.StatusBarUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.jmessage.support.google.gson.Gson;
import cn.jmessage.support.google.gson.JsonArray;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.options.RegisterOptionalUserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 最美人间四月天 on 2018/12/15.
 */

public class PersonDeviceActivity extends AppCompatActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType FORM_CONTENT_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_device);
        StatusBarUtils.setWindowStatusBarColor(PersonDeviceActivity.this, R.color.colorWhite);
        initView();
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.person_device_mainTool);
        toolbar.setTitle("学习设备");
        back(toolbar);

        TextView textView = (TextView) findViewById(R.id.person_device_tv);
        textView.setText("学习机\n手机\n平板电脑\n电子书\n");


        new Thread(){
            public void run(){
                postParams();
            }
        }.start();

    }

    private void postParams() {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("class_bid", "100001")
                .build();
        //构建一个请求对象
        Request request = new Request.Builder()
                .url("http://39.105.213.41:8080/StudyAppService/StudyServlet/classOne")
                .post(requestBody)
                .build();
        //发送请求获取响应
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if(response.isSuccessful()){
                assert response.body() != null;
                String regData = response.body().string();
                System.out.println("返回"+regData);
                if(JsonCode.getCode(regData) == 200){
                    String jsonData = JsonCode.getData(regData);

//                            Message message = new Message();
//                            message.what = 11;
//                            handler.sendMessage(message);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
