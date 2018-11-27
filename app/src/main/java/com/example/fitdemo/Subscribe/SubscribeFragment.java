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

import com.example.fitdemo.Adapter.ClassVideoAdapter;
import com.example.fitdemo.R;
import com.example.fitdemo.ViewHelper.BaseFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 最美人间四月天 on 2018/11/26.
 */

public class SubscribeFragment extends BaseFragment {

    private List<ClassVideoAdapter.Class_Video> class_videos;
    private RecyclerView recyclerView;
    private ClassVideoAdapter classVideoAdapter;
    private ArrayList<String> introduce = new ArrayList<>();
    private ArrayList<Integer> image = new ArrayList<>();

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

    @SuppressLint("NewApi")
    private void initView(View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.subscribeFragment_mainTool);
        toolbar.setTitle("推荐");
        recyclerView = (RecyclerView) view.findViewById(R.id.subscribeFragment_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setData();
        initData();
        setAdapter();
    }

    private void setData(){
        for(int i = 0;i < 7;i++){
            introduce.add("最新的健身课程，你值得学习"+i);
            image.add(R.mipmap.ic_cycling);
        }

    }

    private void initData(){
        class_videos = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            ClassVideoAdapter newData = new ClassVideoAdapter(class_videos);
            ClassVideoAdapter.Class_Video class_video = newData.new Class_Video(introduce.get(i),image.get(i));
            class_videos.add(class_video);
        }
    }

    private void setAdapter(){
        classVideoAdapter = new ClassVideoAdapter(class_videos);
        recyclerView.setAdapter(classVideoAdapter);
        classVideoAdapter.setOnItemClickListener(new ClassVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(getActivity(), SocietyNewMessagePage.class);
//                intent.putExtra("put_data_mes_id",card_message_id.get(position));
//                intent.putExtra("put_data_mes_select",1);
//                startActivity(intent);
            }
        });
    }



}
