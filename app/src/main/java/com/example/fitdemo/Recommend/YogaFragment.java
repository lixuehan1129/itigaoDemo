package com.example.fitdemo.Recommend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitdemo.R;
import com.example.fitdemo.ViewHelper.BaseFragment;

/**
 * Created by ROBOSOFT on 2018/11/27.
 */

public class YogaFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.class_check, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initView(View view){

    }

    @Override
    public void onStart(){
        super.onStart();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
