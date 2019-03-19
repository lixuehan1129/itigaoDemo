package com.example.itigao.Personal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.itigao.R;
import com.example.itigao.Utils.DateUtils;
import com.example.itigao.Utils.StatusBarUtils;

import java.io.IOException;

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
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //构建一个请求体 add参数1 key 参数2 value 发送字段
        RequestBody requestBody = new FormBody.Builder()
                .add("user_phone", "17888836861")
                .add("user_password", "123456")
                .add("user_name", "智慧云用户")
                .add("user_create_time", DateUtils.StringData())
                .add("user_level", "0")
                .add("user_sex","0")
                .add("user_sort","0")
                .add("user_online_time","0")
                .add("user_birth", null)
                .add("user_picture", "http://ty.tipass.com/images/head/head_name(1).PNG")
                .build();
        //构建一个请求对象
        Request request = new Request.Builder()
                .url("http://39.105.213.41:8080/StudyAppService/StudyServlet/register")
                .post(requestBody)
                .build();
        //发送请求获取响应
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if(response.isSuccessful()){
                //打印服务端返回结果
                Log.i("返回",response.body().string());

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
