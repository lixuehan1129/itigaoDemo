package com.example.itigao.Classes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.itigao.AutoProject.Tip;
import com.example.itigao.R;
import com.example.itigao.ViewHelper.BaseFragment;


/**
 * Created by 最美人间四月天 on 2018/11/26.
 */

public class ClassesFragment extends BaseFragment {

    private AlertDialog.Builder builder;

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

        imageView3.setOnClickListener(view15 -> {
            final String[] items = {"一年级", "二年级", "三年级", "四年级", "五年级", "六年级"};
            builder = new AlertDialog.Builder(getActivity())
                    .setItems(items, (dialogInterface, i) -> {
                        i = i + 1;
                        Intent intent = new Intent(getActivity(),BaseActivity.class);
                        intent.putExtra("NianJi",i);
                        startActivity(intent);
                    });
            builder.create().show();

        });

        imageView2.setOnClickListener(view14 -> {
            final String[] items = {"一年级", "二年级", "三年级"};
            builder = new AlertDialog.Builder(getActivity())
                    .setItems(items, (dialogInterface, i) -> {
                        i = i + 7;
                        Intent intent = new Intent(getActivity(),BaseActivity.class);
                        intent.putExtra("NianJi",i);
                        startActivity(intent);

                    });
            builder.create().show();

        });

        imageView1.setOnClickListener(view13 -> {
            final String[] items = {"一年级", "二年级", "三年级"};
            builder = new AlertDialog.Builder(getActivity())
                    .setItems(items, (dialogInterface, i) -> {
                        i = i + 10;
                        Intent intent = new Intent(getActivity(),BaseActivity.class);
                        intent.putExtra("NianJi",i);
                        startActivity(intent);
                    });
            builder.create().show();

        });


        imageView4.setOnClickListener(view12 -> {
            final String[] items = {"一年级", "二年级", "三年级", "四年级"};
            builder = new AlertDialog.Builder(getActivity())
                    .setItems(items, (dialogInterface, i) -> {
                        i = i + 13;
                        Intent intent = new Intent(getActivity(),BaseActivity.class);
                        intent.putExtra("NianJi",i);
                        startActivity(intent);
                    });
            builder.create().show();

        });

        imageView5.setOnClickListener(view1 -> {
            final String[] items = {"一年级", "二年级", "三年级"};
            builder = new AlertDialog.Builder(getActivity())
                    .setItems(items, (dialogInterface, i) -> {
                        i = i + 17;
                        Intent intent = new Intent(getActivity(),BaseActivity.class);
                        intent.putExtra("NianJi",i);
                        startActivity(intent);
                    });
            builder.create().show();

        });


    }

}
