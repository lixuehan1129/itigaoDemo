package com.example.itigao.okHttp;

import android.os.Message;

import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JsonCode;
import com.example.itigao.AutoProject.SharePreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpBase {

    public static String getResponse(RequestBody requestBody, String url){

        String regData = null;

        OkHttpClient okHttpClient = new OkHttpClient();
        //构建一个请求对象
        Request request = new Request.Builder()
                .url(AppConstants.URL + url)
                .post(requestBody)
                .build();
        //发送请求获取响应
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if(response.isSuccessful()){
                assert response.body() != null;
                regData = response.body().string();
                System.out.println("返回"+regData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return regData;
    }
}
