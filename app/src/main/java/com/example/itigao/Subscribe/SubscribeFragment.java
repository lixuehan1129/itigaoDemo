package com.example.itigao.Subscribe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.itigao.Adapter.ClassVideoAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JsonCode;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.ClassAb.Classes;
import com.example.itigao.R;
import com.example.itigao.Video.VideoNewActivity;
import com.example.itigao.ViewHelper.BaseFragment;
import com.example.itigao.okHttp.OkHttpBase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by 最美人间四月天 on 2018/11/26.
 */

public class SubscribeFragment extends BaseFragment {


    private int todayTime;
    private int loginCount = 1;

    private RecyclerView recyclerView;
    private TextView days;
    private ViewFlipper viewFlipper;
    private ClassVideoAdapter classVideoAdapter;
    private List<ClassVideoAdapter.Class_Video> class_videos = new ArrayList<>();
    private List<Classes> classes;
    private static final int OVER = 1;


    @Override
    public void onStart(){
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.subscribefragment, null);
        initView(view); //界面
        return view;
    }


    //定义界面控件
    @SuppressLint("NewApi")
    private void initView(View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.subscribeFragment_mainTool);
        toolbar.setTitle("推荐内容");
        days = (TextView) view.findViewById(R.id.subscribeFragment_days);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.view_flipper);
        viewFlipper.startFlipping();

        recyclerView = (RecyclerView) view.findViewById(R.id.subscribeFragment_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        classVideoAdapter = new ClassVideoAdapter(class_videos);
        recyclerView.setAdapter(classVideoAdapter);

        //设置累计天数
        LoginCount();
    }

    @Override
    protected void onFragmentFirstVisible(){
        initView(getView());
       // connectData();
        connectVideo();
    }

    private void LoginCount(){
        loginCount = SharePreferences.getInt(getActivity(),AppConstants.USER_LOGIN_COUNT);
        isTodayFirstLogin();
    }

    /**
     * 判断是否是当日第一次登陆
     */
    @SuppressLint("SetTextI18n")
    private void isTodayFirstLogin() {
        SharedPreferences preferences = getActivity().getSharedPreferences("LastLoginTime", MODE_PRIVATE);
        int lastTime = preferences.getInt("LoginTime", 20190225);
        // Toast.makeText(MainActivity.this, "value="+date, Toast.LENGTH_SHORT).show();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
        todayTime = Integer.parseInt(df.format(new Date()));// 获取当前的日期

        if (lastTime == todayTime) { //如果两个时间段相等
            days.setText("" + loginCount);
        } else if(todayTime - lastTime == 1){
            loginCount ++;
            SharePreferences.remove(getActivity(),AppConstants.USER_LOGIN_COUNT);
            SharePreferences.putInt(getActivity(),AppConstants.USER_LOGIN_COUNT,loginCount);
            days.setText("" + loginCount);
            saveExitTime(todayTime);
        }else {
            loginCount = 1;
            SharePreferences.remove(getActivity(),AppConstants.USER_LOGIN_COUNT);
            SharePreferences.putInt(getActivity(),AppConstants.USER_LOGIN_COUNT,loginCount);
            days.setText("" + loginCount);
            saveExitTime(todayTime);
        }
    }

    /**
     * 保存每次退出的时间
     * @param extiLoginTime
     */
    private void saveExitTime(int extiLoginTime) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("LastLoginTime", MODE_PRIVATE).edit();
        editor.putInt("LoginTime", extiLoginTime);
        //这里用apply()而没有用commit()是因为apply()是异步处理提交，不需要返回结果，而我也没有后续操作
        //而commit()是同步的，效率相对较低
        //apply()提交的数据会覆盖之前的,这个需求正是我们需要的结果
        editor.apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
     //   saveExitTime(todayTime);
    }


    private void connectVideo(){
        classes = new ArrayList<>();
        new Thread() {
            public void run() {
                Looper.prepare();
                RequestBody requestBody = new FormBody.Builder()
                        .add("class_classify", "1")
                        .build();

                String regData = OkHttpBase.getResponse(requestBody,"classFour");
                if(regData != null){
                    if(JsonCode.getCode(regData) == 200){
                        String jsonData = JsonCode.getData(regData);
                        classes = JsonCode.jsonToList(jsonData,Classes.class);
                        Message message = new Message();
                        message.what = OVER;
                        handler.sendMessage(message);
                    }
                }

                Looper.loop();
            }
        }.start();
    }

    private void initDataVideo(){
        if(classes.size() > 0){
            for(int i = 0; i<classes.size(); i++){
                classVideoAdapter.addDataAt(classVideoAdapter.getItemCount(),
                        classVideoAdapter.new Class_Video(classes.get(i).getClass_name(),
                                classes.get(i).getClass_cover()));
            }

            classVideoAdapter.setOnItemClickListener((view, position) -> {
                Intent intent = new Intent(getActivity(), VideoNewActivity.class);
                intent.putExtra("video_bid",classes.get(position).getClass_bid());
                intent.putExtra("video_section",classes.get(position).getClass_section());
                intent.putExtra("video_add",classes.get(position).getClass_add());
                intent.putExtra("video_select",1);
                intent.putExtra("video_record",1);
                startActivity(intent);
            });

        }
    }


    private Handler handler = new Handler(msg -> {
        // TODO Auto-generated method stub
        switch (msg.what){
            case OVER:{
                initDataVideo();
                break;
            }
            default:
                break;
        }
        return false;
    });


}
