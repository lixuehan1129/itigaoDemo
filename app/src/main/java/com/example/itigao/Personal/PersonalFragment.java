package com.example.itigao.Personal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.itigao.Adapter.AnchorAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JsonCode;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.AutoProject.Tip;
import com.example.itigao.Broad.BroadNewActivity;
import com.example.itigao.Broad.GoBroadActivity;
import com.example.itigao.ClassAb.Anchor;
import com.example.itigao.Database.DataBaseHelper;
import com.example.itigao.R;
import com.example.itigao.ViewHelper.BaseFragment;
import com.example.itigao.okHttp.OkHttpBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * Created by 最美人间四月天 on 2018/11/26.
 */

public class PersonalFragment extends BaseFragment {

    private DataBaseHelper dataBaseHelper;

    private LocalBroadcastManager broadcastManager;
    private IntentFilter intentFilter,intentFilterF;
    private BroadcastReceiver mReceiver,mReceiverF;

    private ImageView code, set;
    private CircleImageView picture;
    private TextView name, level;
    private LinearLayout per, device1, class0, exam1, indoor1, go1;
    private TextView class1, class2, class3, class4, device2, exam2, indoor2, go2, go3;
    private RecyclerView recyclerView;

    private String class01,class02;

    private String userName,userPicture;
    private int userLevel = 1, userSta, goGo = 2;
    private int goBid;

    private AnchorAdapter anchorAdapter;
    private List<Anchor> anchors = new ArrayList<>();

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
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                //收到广播后所作的操作
                connect();
                localData();
            }
        };

        intentFilterF = new IntentFilter();
        intentFilterF.addAction(AppConstants.BROAD_FOCUS);
        mReceiverF = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                //收到广播后所作的操作
                setData();
            }
        };
        broadcastManager.registerReceiver(mReceiverF, intentFilterF);
    }



    @SuppressLint("NewApi")
    private void initView(View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.personalFragment_mainTool);
        toolbar.setTitle("个人信息");


//        code = (ImageView) view.findViewById(R.id.personalFragment_code);
//        set = (ImageView) view.findViewById(R.id.personalFragment_set);

        picture = (CircleImageView) view.findViewById(R.id.personalFragment_picture);
        name = (TextView) view.findViewById(R.id.personalFragment_name);
        level = (TextView) view.findViewById(R.id.personalFragment_level);

        per = (LinearLayout) view.findViewById(R.id.personalFragment_l);
        class0 = (LinearLayout) view.findViewById(R.id.personalFragment_class);
        device1 = (LinearLayout) view.findViewById(R.id.personalFragment_device1);
        exam1 = (LinearLayout) view.findViewById(R.id.personalFragment_exam1);
        indoor1 = (LinearLayout) view.findViewById(R.id.personalFragment_indoor1);
        go1 = (LinearLayout) view.findViewById(R.id.personalFragment_go);
        go1.setVisibility(View.GONE);

        class1 = (TextView) view.findViewById(R.id.personalFragment_class1);
        class2 = (TextView) view.findViewById(R.id.personalFragment_class2);
//        class3 = (TextView) view.findViewById(R.id.personalFragment_class3);
//        class4 = (TextView) view.findViewById(R.id.personalFragment_class4);
        device2 = (TextView) view.findViewById(R.id.personalFragment_device2);
        exam2 = (TextView) view.findViewById(R.id.personalFragment_exam2);
        indoor2 = (TextView) view.findViewById(R.id.personalFragment_indoor2);
        go2 = (TextView) view.findViewById(R.id.personalFragment_go2);
        go3 = (TextView) view.findViewById(R.id.personalFragment_go3);


        recyclerView = (RecyclerView) view.findViewById(R.id.personalFragment_rv);
        recyclerView.setNestedScrollingEnabled(false); //禁止滑动
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),7));
        anchorAdapter = new AnchorAdapter(anchors);
        recyclerView.setAdapter(anchorAdapter);

        dataBaseHelper = new DataBaseHelper(getActivity(), AppConstants.SQL_VISION);
        localData();

        connect();
        connectClass();

        setData();
       // setClick();
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

        setClick();
    }

    @SuppressLint("SetTextI18n")
    private void setClick(){
        //个人资料
        per.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),PersonChangeActivity.class);
            intent.putExtra("userLevel",userLevel);
            intent.putExtra("userSta",userSta);
            intent.putExtra("userGoBid",goBid);
            startActivity(intent);
        });
        //设备
        device1.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),PersonDeviceActivity.class);
            startActivity(intent);
        });
        //课程
        class0.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),PersonClassActivity.class);
            startActivity(intent);
        });
        //校园生活
        exam1.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),PersonExamActivity.class);
            startActivity(intent);
        });
        //学习历程
        indoor1.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),PersonIndoorActivity.class);
            startActivity(intent);
        });

        //开播
        go1.setOnClickListener(view -> {
            if(go3.getText().toString().equals("关闭直播")){
                //关闭直播
                go3.setText("开启直播");
                go3.setTextColor(getResources().getColor(R.color.colorBlack));
                go2.setVisibility(View.GONE);
                updateSta(0);
            }else if(go3.getText().toString().equals("开启直播")){
                //开启直播
                if(userSta == 1){
                    String[] item = new String[]{"使用手机端直播", "使用电脑端直播"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setItems(item, (dialog, which) -> {
                        switch (which){
                            case 0:{
                                updateSta(1);
                                go3.setText("关闭直播");
                                go3.setTextColor(getResources().getColor(R.color.colorRed));
                                Intent intent = new Intent(getActivity(),GoBroadActivity.class);
                                intent.putExtra("GoBroadBid",goBid);
                                startActivity(intent);
                                break;
                            }
                            case 1:{
                                updateSta(1);
                                go3.setText("关闭直播");
                                go3.setTextColor(getResources().getColor(R.color.colorRed));
                                go2.setText("直播地址:rtmp://114.115.150.93:1935/live" + "\n" + "直播码:" + goBid);
                                go2.setVisibility(View.VISIBLE);
                                break;
                            }
                            default:
                                break;

                        }
                    });
                    builder.create().show();
                }else {
                    Tip.showTip(getActivity(),"还不是主播");
                }

            }
        });
    }

    private void connect(){
        new Thread(){
            public void run(){
                RequestBody requestBody = new FormBody.Builder()
                        .add("anchor_phone",SharePreferences.getString(getActivity(),AppConstants.USER_PHONE))
                        .build();
                String regData = OkHttpBase.getResponse(requestBody,"anchorOnline");
                //构建一个请求对象
                if(regData != null){
                    if(JsonCode.getCode(regData) == 200) {
                        String jsonData = JsonCode.getData(regData);
                        try {
                            JSONArray jsonArray = new JSONArray(jsonData);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            goBid = jsonObject.getInt("anchor_bid");
                            goGo = jsonObject.getInt("anchor_state");

                            Message message = new Message();
                            message.what = 112;
                            handler.sendMessage(message);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }.start();

    }

    private void connectClass(){
        new Thread(){
            public void run(){
                RequestBody requestBody = new FormBody.Builder()
                        .add("yu_user",SharePreferences.getString(getActivity(),AppConstants.USER_PHONE))
                        .build();
                String regData = OkHttpBase.getResponse(requestBody,"appointTop1");
                if(regData != null){
                    if(JsonCode.getCode(regData) == 200) {
                        String jsonData = JsonCode.getData(regData);
                        try {
                            JSONArray jsonArray = new JSONArray(jsonData);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            class01 = jsonObject.getString("appoint_name");
                            class02 = jsonObject.getString("appoint_coach");

                            Message message = new Message();
                            message.what = 113;
                            handler.sendMessage(message);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        }.start();
    }

    private void updateSta(final int i){

        new Thread(){
            public void run(){
                RequestBody requestBody = new FormBody.Builder()
                        .add("anchor_phone",SharePreferences.getString(getActivity(),AppConstants.USER_PHONE))
                        .add("anchor_state", String.valueOf(i))
                        .build();
                OkHttpBase.getResponse(requestBody,"anchorUpdate");
            }
        }.start();

    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case 112:{
                    if(goGo == 1){
                        userSta = 1;
                        go1.setVisibility(View.VISIBLE);
                        go3.setText("关闭直播");
                        go3.setTextColor(getResources().getColor(R.color.colorRed));
                    }else if(goGo == 0){
                        userSta = 1;
                        go1.setVisibility(View.VISIBLE);
                        go3.setText("开启直播");
                        go3.setTextColor(getResources().getColor(R.color.colorBlack));
                    }
                    break;
                }
                case 113:{
                    class1.setText(class01);
                    class2.setText(class02);
                    break;
                }
                case 10256:{
                    setAdapter();
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });



    //关注的主播列表
    private void setData(){
        anchors = new ArrayList<>();
        new Thread(){
            public void run(){
                RequestBody requestBody = new FormBody.Builder()
                        .add("focus_user",SharePreferences.getString(getActivity(),AppConstants.USER_PHONE))
                        .build();
                String reg = OkHttpBase.getResponse(requestBody,"getAnchorByFocusUser");
                if(reg != null){
                    if(JsonCode.getCode(reg) == 200){
                        String jsonData = JsonCode.getData(reg);
                        anchors = JsonCode.jsonToList(jsonData,Anchor.class);

                        Message message = new Message();
                        message.what = 10256;
                        handler.sendMessage(message);
                    }
                }
            }
        }.start();
    }
    private void setAdapter(){
        for (int i = 0; i<anchors.size(); i++){
            anchors.get(i).setAnchor_focus(1);
        }

        Collections.sort(anchors, (o1, o2) -> o2.getAnchor_state() - o1.getAnchor_state());

        anchorAdapter.addDataAt(anchors);
        recyclerView.setAdapter(anchorAdapter);
        anchorAdapter.setOnItemClickListener((view, position) -> {
            if(anchors.get(position).getAnchor_state() == 1){
                Intent intent = new Intent(getActivity(), BroadNewActivity.class);
                intent.putExtra("anchor_all",anchors.get(position));
                startActivity(intent);
            }else {
                Tip.showTip(getActivity(),"当前未开播");
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
