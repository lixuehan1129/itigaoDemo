package com.example.itigao.Video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.itigao.Adapter.SelectAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JsonCode;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.ClassAb.Classes;
import com.example.itigao.R;
import com.example.itigao.ViewHelper.BaseFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 最美人间四月天 on 2018/12/11.
 */

public class VideoFragment extends BaseFragment {

    private TextView title, itr, advice;
    private RecyclerView recyclerView;
    private int video_section;
    private int video_bid;
    private int video_select;
    private int video_record;   //0表示主页推荐内容不计，1表示课程列表加入记录，2观看记录

    private List<Classes> classes;

    CallBackValue callBackValue;

    /**
     * fragment与activity产生关联是  回调这个方法
     */
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue =(CallBackValue) getActivity();
    }

    //定义一个回调接口
    public interface CallBackValue{
        void SendMessageValue(String strValue);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, container, false);
        Bundle bundle = getArguments();
        assert bundle != null;
        video_section = bundle.getInt("video_play_section",0);
        video_bid = bundle.getInt("video_play_bid",0);
        video_record = bundle.getInt("video_play_record",1);
        video_select = bundle.getInt("video_play_select",1);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View view){
        title = (TextView) view.findViewById(R.id.video_fragment_title);
        itr = (TextView) view.findViewById(R.id.video_fragment_itr);
        advice = (TextView) view.findViewById(R.id.video_fragment_advice);
        recyclerView = (RecyclerView) view.findViewById(R.id.video_fragment_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        connectData();
    }

    private void connectData(){
        classes = new ArrayList<>();
        new Thread(){
            public void run(){
                Looper.prepare();
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("class_bid", String.valueOf(video_bid))
                        .add("record_user", SharePreferences.getString(getActivity(),AppConstants.USER_PHONE))
                        .build();
                //构建一个请求对象
                Request request = new Request.Builder()
                        .url(AppConstants.URL + "setRecord")
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
                        System.out.println("返回classes_record"+regData);
                        if(JsonCode.getCode(regData) == 200){
                            String jsonData = JsonCode.getData(regData);
                            classes = JsonCode.jsonToList(jsonData,Classes.class);
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Looper.loop();
            }
        }.start();

    }

    private void Update(final int select){
        new Thread() {
            public void run() {
                Looper.prepare();
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("record_add", classes.get(select).getClass_add())
                        .add("record_select", String.valueOf(select + 1))
                        .add("record_bid", String.valueOf(video_bid))
                        .add("record_user", SharePreferences.getString(getActivity(),AppConstants.USER_PHONE))
                        .build();
                //构建一个请求对象
                Request request = new Request.Builder()
                        .url(AppConstants.URL + "recordUpdate")
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
                        System.out.println("返回record_update"+regData);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
    }


    private Handler handler = new Handler(msg -> {
        // TODO Auto-generated method stub
        switch (msg.what){
            case 1:{
                initData();
                break;
            }
            default:
                break;
        }
        return false;
    });


    private void initData(){

        title.setText(classes.get(video_select-1).getClass_name());
        itr.setText(classes.get(video_select-1).getClass_content());
        advice.setText(classes.get(video_select-1).getClass_nut());

        ArrayList<Integer> id = new ArrayList<>();
        for (int i=1; i<video_section+1; i++){
            id.add(i);
        }
        setAdapter(id);
    }

    private void setAdapter(final ArrayList<Integer> id){
        List<SelectAdapter.Select> selects = new ArrayList<>();
        for(int i=0; i<id.size(); i++){
            SelectAdapter selectAdapter = new SelectAdapter(selects);
            SelectAdapter.Select select = selectAdapter.new Select(id.get(i));
            selects.add(select);
        }
        final SelectAdapter selectAdapter = new SelectAdapter(selects);
        recyclerView.setAdapter(selectAdapter);
        selectAdapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                title.setText(classes.get(position).getClass_name());
                itr.setText(classes.get(position).getClass_content());
                advice.setText(classes.get(position).getClass_nut());

                callBackValue.SendMessageValue(classes.get(position).getClass_add());
              //  System.out.println("返回classes_update");
                if(video_record != 0){
                    Update(position);
                }
            }
        });
    }


}
