package com.example.fitdemo.Personal;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.example.fitdemo.Adapter.AnchorAdapter;
import com.example.fitdemo.AutoProject.AppConstants;
import com.example.fitdemo.AutoProject.SharePreferences;
import com.example.fitdemo.AutoProject.Tip;
import com.example.fitdemo.Classes.BroadcastActivity;
import com.example.fitdemo.Classes.RunActivity;
import com.example.fitdemo.Database.DataBaseHelper;
import com.example.fitdemo.R;
import com.example.fitdemo.ViewHelper.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by 最美人间四月天 on 2018/11/26.
 */

public class PersonalFragment extends BaseFragment {

    private DataBaseHelper dataBaseHelper;

    private LocalBroadcastManager broadcastManager;
    private IntentFilter intentFilter;
    private BroadcastReceiver mReceiver;

    private ImageView code, set;
    private CircleImageView picture;
    private TextView name, level;
    private LinearLayout per, device1, class0, exam1, indoor1;
    private TextView class1, class2, class3, class4, device2, exam2, indoor2;
    private RecyclerView recyclerView;

    private String userName,userPicture;
    private int userLevel = 1,userSta = 0;

    @Override
    public void onStart(){
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.personalfragment, null);
        initView(view); //界面
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.BROAD_CHANGE);
        // intentFilter.addAction(AppConstants.BROAD_LOGIN);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                //收到广播后所作的操作
                localData();
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);
    }



    @SuppressLint("NewApi")
    private void initView(View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.personalFragment_mainTool);
        toolbar.setTitle("个人信息");

        code = (ImageView) view.findViewById(R.id.personalFragment_code);
        set = (ImageView) view.findViewById(R.id.personalFragment_set);

        picture = (CircleImageView) view.findViewById(R.id.personalFragment_picture);
        name = (TextView) view.findViewById(R.id.personalFragment_name);
        level = (TextView) view.findViewById(R.id.personalFragment_level);

        per = (LinearLayout) view.findViewById(R.id.personalFragment_l);
        class0 = (LinearLayout) view.findViewById(R.id.personalFragment_class);
        device1 = (LinearLayout) view.findViewById(R.id.personalFragment_device1);
        exam1 = (LinearLayout) view.findViewById(R.id.personalFragment_exam1);
        indoor1 = (LinearLayout) view.findViewById(R.id.personalFragment_indoor1);

        class1 = (TextView) view.findViewById(R.id.personalFragment_class1);
        class2 = (TextView) view.findViewById(R.id.personalFragment_class2);
        class3 = (TextView) view.findViewById(R.id.personalFragment_class3);
        class4 = (TextView) view.findViewById(R.id.personalFragment_class4);
        device2 = (TextView) view.findViewById(R.id.personalFragment_device2);
        exam2 = (TextView) view.findViewById(R.id.personalFragment_exam2);
        indoor2 = (TextView) view.findViewById(R.id.personalFragment_indoor2);

        recyclerView = (RecyclerView) view.findViewById(R.id.personalFragment_rv);
        recyclerView.setNestedScrollingEnabled(false); //禁止滑动
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),7));

        dataBaseHelper = new DataBaseHelper(getActivity(), AppConstants.SQL_VISION);
        localData();


        setData();
        setClick();
    }

    private void localData(){
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("user",null,"user_phone = ?",new String[]{
                SharePreferences.getString(getActivity(),AppConstants.USER_PHONE)
        },null,null,null,"1");
        if(cursor.moveToFirst()){
            userName = cursor.getString(cursor.getColumnIndex("user_name"));
            userPicture = cursor.getString(cursor.getColumnIndex("user_picture"));
        }
        cursor.close();
        sqLiteDatabase.close();

        name.setText(userName);
        if(Util.isOnMainThread()) {
            Glide.with(getActivity())
                    .load(userPicture)
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(R.mipmap.ic_touxiang11)
                    .error(R.mipmap.ic_touxiang11)
                    .into(picture);
        }
    }

    private void setClick(){
        //个人资料
        per.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),PersonChangeActivity.class);
                intent.putExtra("userLevel",userLevel);
                intent.putExtra("userSta",userSta);
                startActivity(intent);
            }
        });
        //设备
        device1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),PersonDeviceActivity.class);
                startActivity(intent);
            }
        });
        //课程
        class0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),PersonClassActivity.class);
                startActivity(intent);
            }
        });
        //体检
        exam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),PersonExamActivity.class);
                startActivity(intent);
            }
        });
        //室内
        indoor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),PersonIndoorActivity.class);
                startActivity(intent);
            }
        });
    }



    //关注的主播列表
    private void setData(){
        ArrayList<Integer> image = new ArrayList<>();
        ArrayList<Integer> state = new ArrayList<>();
        image.add(R.mipmap.ic_touxiang11);
        state.add(0);
        image.add(R.mipmap.ic_touxiang21);
        state.add(0);
        image.add(R.mipmap.ic_touxiang3);
        state.add(0);
        image.add(R.mipmap.ic_touxiang41);
        state.add(1);
        image.add(R.mipmap.ic_touxiang51);
        state.add(1);

        setAdapter(image, state);
    }
    private void setAdapter(ArrayList<Integer> image, final ArrayList<Integer> state){
        List<AnchorAdapter.Anchor> anchors = new ArrayList<>();
        for(int i=0; i<image.size(); i++){
            AnchorAdapter nAnchorAdapter = new AnchorAdapter(anchors);
            AnchorAdapter.Anchor anchor = nAnchorAdapter.new Anchor(image.get(i), state.get(i));
            anchors.add(anchor);
        }


        AnchorAdapter anchorAdapter = new AnchorAdapter(anchors);
        recyclerView.setAdapter(anchorAdapter);
        anchorAdapter.setOnItemClickListener(new AnchorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(state.get(position) == 0){
                    Intent intent = new Intent(getActivity(),BroadcastActivity.class);
                    startActivity(intent);
                }else {
                    Tip.showTip(getActivity(),"当前未开播");
                }

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mReceiver);
        if (Util.isOnMainThread()) {
            Glide.with(getActivity()).pauseRequests();
        }
    }
}
