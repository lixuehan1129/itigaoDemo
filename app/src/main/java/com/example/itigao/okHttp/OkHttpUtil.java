package com.example.itigao.okHttp;


import android.util.Log;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by 最美人间四月天 on 2019/1/24.
 */

public class OkHttpUtil {

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10000, TimeUnit.MILLISECONDS)
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .writeTimeout(10000,TimeUnit.MILLISECONDS).build();

    //下载文件方法
//    public static void downloadFile(String url, final RecoverySystem.ProgressListener listener, Callback callback){
//        //增加拦截器
//        OkHttpClient client = okHttpClient.newBuilder().addNetworkInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Response response = chain.proceed(chain.request());
//                return response.newBuilder().body(new ProgressResponseBody(response.body(),listener)).build();
//            }
//        }).build();
//
//        Request request  = new Request.Builder().url(url).build();
//        client.newCall(request).enqueue(callback);
//    }

    public static void postFile(String url, ProgressListener listener, Callback callback, File...files){

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        Log.i("huang","files[0].getName()=="+files[0].getName());
        //第一个参数要与Servlet中的一致
        builder.addFormDataPart("file",files[0].getName(), RequestBody.create(MediaType.parse("application/octet-stream"),files[0]));

        MultipartBody multipartBody = builder.build();

        Request request  = new Request.Builder().url(url).post(new ProgressRequestBody(multipartBody,listener)).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
