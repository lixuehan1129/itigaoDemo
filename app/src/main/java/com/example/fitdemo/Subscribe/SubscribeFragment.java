package com.example.fitdemo.Subscribe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fitdemo.Adapter.ClassVideoAdapter;
import com.example.fitdemo.R;
import com.example.fitdemo.ViewHelper.BaseFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 最美人间四月天 on 2018/11/26.
 */

public class SubscribeFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private TextView days, minutes;


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
        recyclerView = (RecyclerView) view.findViewById(R.id.subscribeFragment_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        days = (TextView) view.findViewById(R.id.subscribeFragment_days);
        minutes = (TextView) view.findViewById(R.id.subscribeFragment_minutes);

        //设置累计运动天数和运动时长
        days.setText("396");
        minutes.setText("67");
        setData();
    }


    //推荐内容列表
    private void setData(){
        ArrayList<String> introduce = new ArrayList<>();
        ArrayList<Integer> image = new ArrayList<>();
        for(int i = 0;i < 4;i++){
            introduce.add("流行的健身视频排行榜，每个人都在学，快来学习一下吧"+i);
        }

        image.add(R.mipmap.ic_t_video1);
        image.add(R.mipmap.ic_t_video2);
        image.add(R.mipmap.ic_t_video3);
        image.add(R.mipmap.ic_t_video4);


        initData(introduce,image);

    }

    private void initData(ArrayList<String> introduce,ArrayList<Integer> image){
        List<ClassVideoAdapter.Class_Video> class_videos = new ArrayList<>();
        for(int i = 0; i < introduce.size(); i++){
            ClassVideoAdapter newData = new ClassVideoAdapter(class_videos);
            ClassVideoAdapter.Class_Video class_video = newData.new Class_Video(introduce.get(i),image.get(i));
            class_videos.add(class_video);
        }

        ClassVideoAdapter classVideoAdapter = new ClassVideoAdapter(class_videos);
        recyclerView.setAdapter(classVideoAdapter);
        classVideoAdapter.setOnItemClickListener(new ClassVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                intent.putExtra("video_add",position);
                startActivity(intent);
            }
        });

    }


}
