package com.example.itigao.Sport;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.itigao.R;
import com.example.itigao.ViewHelper.BaseFragment;


/**
 * Created by 最美人间四月天 on 2018/11/26.
 */

public class SportFragment extends BaseFragment {

    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12, tv13, tv14;

    @Override
    public void onStart(){
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.sportfragment, null);
        initView(view); //界面
        return view;
    }

    @SuppressLint("NewApi")
    private void initView(View view){
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.sportFragment_mainTool);
        toolbar.setTitle("学习目标");

        //运动界面textView
        tv1 = (TextView) view.findViewById(R.id.sportFragment_data1);
        tv2 = (TextView) view.findViewById(R.id.sportFragment_data2);
        tv3 = (TextView) view.findViewById(R.id.sportFragment_data3);
        tv4 = (TextView) view.findViewById(R.id.sportFragment_data4);
//        tv5 = (TextView) view.findViewById(R.id.sportFragment_data5);
//        tv6 = (TextView) view.findViewById(R.id.sportFragment_data6);
//        tv7 = (TextView) view.findViewById(R.id.sportFragment_data7);
//        tv8 = (TextView) view.findViewById(R.id.sportFragment_data8);
//        tv9 = (TextView) view.findViewById(R.id.sportFragment_data9);
//        tv10 = (TextView) view.findViewById(R.id.sportFragment_data10);
//        tv11 = (TextView) view.findViewById(R.id.sportFragment_data11);
//        tv12 = (TextView) view.findViewById(R.id.sportFragment_data12);
//        tv13 = (TextView) view.findViewById(R.id.sportFragment_data13);
//        tv14 = (TextView) view.findViewById(R.id.sportFragment_data14);

        setData();

    }

    private void setData(){
        tv1.setText("165");
        tv2.setText("127");
        tv3.setText("15");
        tv4.setText("85");
//        tv5.setText("6337");
//        tv6.setText("32400");
//        tv7.setText("1.7");
//        tv8.setText("12");
//        tv9.setText("0");
//        tv10.setText("8");
//        tv11.setText("2.3");
//        tv12.setText("35");
//        tv13.setText("30");
//        tv14.setText("130");
    }
}
