package com.example.fitdemo.Classes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.fitdemo.Adapter.ClassVideoAdapter;
import com.example.fitdemo.R;
import com.example.fitdemo.ViewHelper.BaseFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 最美人间四月天 on 2018/11/26.
 */

public class ClassesFragment extends BaseFragment {

    @Override
    public void onStart(){
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.classesfragment, null);
        initView(view); //界面
        return view;
    }

    @SuppressLint("NewApi")
    private void initView(View view){
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.classesFragment_mainTool);
        toolbar.setTitle("课程列表");

        ImageView imageView1 = (ImageView) view.findViewById(R.id.classFragment_i1);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.classFragment_i2);
        ImageView imageView3 = (ImageView) view.findViewById(R.id.classFragment_i3);
        ImageView imageView4 = (ImageView) view.findViewById(R.id.classFragment_i5);
        ImageView imageView5 = (ImageView) view.findViewById(R.id.classFragment_i6);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),FitActivity.class);
                startActivity(intent);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CyclingActivity.class);
                startActivity(intent);
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),RunActivity.class);
                startActivity(intent);
            }
        });


        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),YogaActivity.class);
                startActivity(intent);
            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ChildActivity.class);
                startActivity(intent);
            }
        });


    }

}
