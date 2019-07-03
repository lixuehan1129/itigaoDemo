package com.example.itigao.Classes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.AutoProject.Tip;
import com.example.itigao.R;
import com.example.itigao.ViewHelper.BaseFragment;


/**
 * Created by 最美人间四月天 on 2018/11/26.
 */

public class ClassesFragment extends BaseFragment {

    private LocalBroadcastManager broadcastManager;
    private IntentFilter intentFilter;
    private BroadcastReceiver mReceiver;

    private AlertDialog.Builder builder;
    private String[] titles = {"小学课程","初中课程","高中课程","大学课程","研究生课程"};
    private int[] ids = {0,6,9,12,16};
    private int Id = 0;

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.BROAD_CHANGE);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                //收到广播后所作的操作
                initView(getView());
            }
        };

        broadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mReceiver);
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
        Id = SharePreferences.getInt(getActivity(), AppConstants.USER_CLASSIFY);
        toolbar.setTitle(titles[Id]);

        TextView textView1 = (TextView) view.findViewById(R.id.classfra_text1);
        TextView textView2 = (TextView) view.findViewById(R.id.classfra_text2);
        TextView textView3 = (TextView) view.findViewById(R.id.classfra_text3);
        TextView textView4 = (TextView) view.findViewById(R.id.classfra_text4);
        TextView textView5 = (TextView) view.findViewById(R.id.classfra_text5);
        TextView textView6 = (TextView) view.findViewById(R.id.classfra_text6);

        ImageView imageView1 = (ImageView) view.findViewById(R.id.classFragment_i1);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.classFragment_i2);
        ImageView imageView3 = (ImageView) view.findViewById(R.id.classFragment_i3);
        ImageView imageView4 = (ImageView) view.findViewById(R.id.classFragment_i5);
        ImageView imageView5 = (ImageView) view.findViewById(R.id.classFragment_i6);
        imageView1.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);
        imageView3.setVisibility(View.GONE);
        imageView4.setVisibility(View.GONE);
        imageView5.setVisibility(View.GONE);

        switch (Id){
            case 0:{
                imageView3.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
                textView4.setVisibility(View.VISIBLE);
                textView5.setVisibility(View.VISIBLE);
                textView6.setVisibility(View.VISIBLE);
                break;
            }
            case 1:{
                imageView2.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
                textView4.setVisibility(View.INVISIBLE);
                textView5.setVisibility(View.INVISIBLE);
                textView6.setVisibility(View.INVISIBLE);
                break;
            }
            case 2:{
                imageView1.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
                textView4.setVisibility(View.INVISIBLE);
                textView5.setVisibility(View.INVISIBLE);
                textView6.setVisibility(View.INVISIBLE);
                break;
            }
            case 3:{
                imageView4.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
                textView4.setVisibility(View.VISIBLE);
                textView5.setVisibility(View.INVISIBLE);
                textView6.setVisibility(View.INVISIBLE);
                break;
            }
            case 4:{
                imageView5.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
                textView4.setVisibility(View.INVISIBLE);
                textView5.setVisibility(View.INVISIBLE);
                textView6.setVisibility(View.INVISIBLE);
                break;
            }
            default:break;

        }

        textView1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),BaseActivity.class);
            intent.putExtra("NianJi",ids[Id] + 1);
            startActivity(intent);
        });

        textView2.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),BaseActivity.class);
            intent.putExtra("NianJi",ids[Id] + 2);
            startActivity(intent);
        });

        textView3.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),BaseActivity.class);
            intent.putExtra("NianJi",ids[Id] + 3);
            startActivity(intent);
        });

        textView4.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),BaseActivity.class);
            intent.putExtra("NianJi",ids[Id] + 4);
            startActivity(intent);
        });

        textView5.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),BaseActivity.class);
            intent.putExtra("NianJi",ids[Id] + 5);
            startActivity(intent);
        });

        textView6.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),BaseActivity.class);
            intent.putExtra("NianJi",ids[Id] + 6);
            startActivity(intent);
        });


//
//        imageView3.setOnClickListener(view15 -> {
//            final String[] items = {"一年级", "二年级", "三年级", "四年级", "五年级", "六年级"};
//            builder = new AlertDialog.Builder(getActivity())
//                    .setItems(items, (dialogInterface, i) -> {
//                        i = i + 1;
//                        Intent intent = new Intent(getActivity(),BaseActivity.class);
//                        intent.putExtra("NianJi",i);
//                        startActivity(intent);
//                    });
//            builder.create().show();
//
//        });
//
//        imageView2.setOnClickListener(view14 -> {
//            final String[] items = {"一年级", "二年级", "三年级"};
//            builder = new AlertDialog.Builder(getActivity())
//                    .setItems(items, (dialogInterface, i) -> {
//                        i = i + 7;
//                        Intent intent = new Intent(getActivity(),BaseActivity.class);
//                        intent.putExtra("NianJi",i);
//                        startActivity(intent);
//
//                    });
//            builder.create().show();
//
//        });
//
//        imageView1.setOnClickListener(view13 -> {
//            final String[] items = {"一年级", "二年级", "三年级"};
//            builder = new AlertDialog.Builder(getActivity())
//                    .setItems(items, (dialogInterface, i) -> {
//                        i = i + 10;
//                        Intent intent = new Intent(getActivity(),BaseActivity.class);
//                        intent.putExtra("NianJi",i);
//                        startActivity(intent);
//                    });
//            builder.create().show();
//
//        });
//
//
//        imageView4.setOnClickListener(view12 -> {
//            final String[] items = {"一年级", "二年级", "三年级", "四年级"};
//            builder = new AlertDialog.Builder(getActivity())
//                    .setItems(items, (dialogInterface, i) -> {
//                        i = i + 13;
//                        Intent intent = new Intent(getActivity(),BaseActivity.class);
//                        intent.putExtra("NianJi",i);
//                        startActivity(intent);
//                    });
//            builder.create().show();
//
//        });
//
//        imageView5.setOnClickListener(view1 -> {
//            final String[] items = {"一年级", "二年级", "三年级"};
//            builder = new AlertDialog.Builder(getActivity())
//                    .setItems(items, (dialogInterface, i) -> {
//                        i = i + 17;
//                        Intent intent = new Intent(getActivity(),BaseActivity.class);
//                        intent.putExtra("NianJi",i);
//                        startActivity(intent);
//                    });
//            builder.create().show();
//
//        });


    }

}
