package com.example.itigao.Personal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.itigao.AutoProject.AppConstants;
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
              //  postParams();
                postLogin();
            }
        }.start();

    }

    private void postParams() {
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //构建一个请求体 add参数1 key 参数2 value 发送字段

//        RequestBody requestBody = new FormBody.Builder()
//                .add("user_phone", "17888836861")
//                .add("user_password", "123456")
//                .add("user_name", "智慧云用户")
//                .add("user_create_time", DateUtils.StringData())
//                .add("user_level", "0")
//                .add("user_sex","0")
//                .add("user_sort","0")
//                .add("user_online_time","0")
////                .add("user_birth", null)
//                .add("user_picture", "http://ty01.tipass.com/images/head/head_name(2).PNG")
//                .build();

//        Register register = new Register("17888836861","123456","智慧云用户",DateUtils.StringData(),
//                "http://ty01.tipass.com/images/head/head_name(2).PNG",
//                0,0,0,0);
//
//        Gson gson = new Gson();
//        String json = gson.toJson(register);
//        System.out.println("json"+json);
//        RequestBody requestBody = RequestBody.create(JSON, json);

        HashMap<String,String> formParams = new HashMap<>();
            //传参
        formParams.put("user_phone", "17888836861");
        formParams.put("user_password", "123456");
        formParams.put("user_name", "智慧云用户");
        formParams.put("user_create_time", DateUtils.StringData());
        formParams.put("user_level", "0");
        formParams.put("user_sex","0");
        formParams.put("user_sort","0");
        formParams.put("user_online_time","0");
        formParams.put("user_picture", "http://ty01.tipass.com/images/head/head_name(2).PNG");

        StringBuffer sb = new StringBuffer();
        for (String key: formParams.keySet()) {
            sb.append(key+"="+formParams.get(key)+"&");
        }

        RequestBody requestBody = RequestBody.create(FORM_CONTENT_TYPE, sb.toString());

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
           //     Log.i("返回",response.body().string());
                assert response.body() != null;
                String regData = response.body().string();
                System.out.println("返回"+regData);



            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void postLogin() {
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //构建一个请求体 add参数1 key 参数2 value 发送字段
        RequestBody requestBody = new FormBody.Builder()
                .add("user_phone", "17888836861")
                .add("user_password", "123456")
                .build();
        //构建一个请求对象
        Request request = new Request.Builder()
                .url("http://39.105.213.41:8080/StudyAppService/StudyServlet/login")
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
