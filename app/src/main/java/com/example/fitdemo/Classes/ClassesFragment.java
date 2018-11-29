package com.example.fitdemo.Classes;

import android.annotation.SuppressLint;
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

    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5;

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
        toolbar.setTitle("课程");
        imageView1 = (ImageView)view.findViewById(R.id.classFragment_i1);
        imageView2 = (ImageView)view.findViewById(R.id.classFragment_i2);
        imageView3 = (ImageView)view.findViewById(R.id.classFragment_i3);
        imageView4 = (ImageView)view.findViewById(R.id.classFragment_i4);
        imageView5 = (ImageView)view.findViewById(R.id.classFragment_i5);


    }


}
