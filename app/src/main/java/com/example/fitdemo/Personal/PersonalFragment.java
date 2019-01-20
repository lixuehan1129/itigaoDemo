package com.example.fitdemo.Personal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.SQLException;
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
import com.example.fitdemo.Adapter.AnchorAdapter;
import com.example.fitdemo.AutoProject.AppConstants;
import com.example.fitdemo.AutoProject.JDBCTools;
import com.example.fitdemo.AutoProject.SharePreferences;
import com.example.fitdemo.AutoProject.Tip;
import com.example.fitdemo.Classes.BroadcastActivity;
import com.example.fitdemo.Classes.GoBroadActivity;
import com.example.fitdemo.Classes.RunActivity;
import com.example.fitdemo.Database.DataBaseHelper;
import com.example.fitdemo.R;
import com.example.fitdemo.ViewHelper.BaseFragment;
import com.mysql.jdbc.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
    private LinearLayout per, device1, class0, exam1, indoor1, go1;
    private TextView class1, class2, class3, class4, device2, exam2, indoor2, go2, go3;
    private RecyclerView recyclerView;

    private String userName,userPicture;
    private int userLevel = 1, userSta, goGo = 2;
    private int goBid;

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
                connect();
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
        go1 = (LinearLayout) view.findViewById(R.id.personalFragment_go);
        go1.setVisibility(View.GONE);

        class1 = (TextView) view.findViewById(R.id.personalFragment_class1);
        class2 = (TextView) view.findViewById(R.id.personalFragment_class2);
        class3 = (TextView) view.findViewById(R.id.personalFragment_class3);
        class4 = (TextView) view.findViewById(R.id.personalFragment_class4);
        device2 = (TextView) view.findViewById(R.id.personalFragment_device2);
        exam2 = (TextView) view.findViewById(R.id.personalFragment_exam2);
        indoor2 = (TextView) view.findViewById(R.id.personalFragment_indoor2);
        go2 = (TextView) view.findViewById(R.id.personalFragment_go2);
        go3 = (TextView) view.findViewById(R.id.personalFragment_go3);


        recyclerView = (RecyclerView) view.findViewById(R.id.personalFragment_rv);
        recyclerView.setNestedScrollingEnabled(false); //禁止滑动
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),7));

        dataBaseHelper = new DataBaseHelper(getActivity(), AppConstants.SQL_VISION);
        localData();

        connect();

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
    }

    @SuppressLint("SetTextI18n")
    private void setClick(){
        //个人资料
        per.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),PersonChangeActivity.class);
                intent.putExtra("userLevel",userLevel);
                intent.putExtra("userSta",userSta);
                intent.putExtra("userGoBid",goBid);
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
                                go2.setText("直播地址:rtmp://39.105.213.41:1935/live" + "\n" + "直播码:" + goBid);
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
                try {
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null) {
                        Statement stmt = conn.createStatement();
                        String sql = "SELECT anchor_state,anchor_bid FROM anchor WHERE anchor_phone = " +
                                SharePreferences.getString(getActivity(),AppConstants.USER_PHONE) +
                                " LIMIT 1";
                        ResultSet resultSet = stmt.executeQuery(sql);
                        if(resultSet.first()){
                            goGo = resultSet.getInt("anchor_state");
                            goBid = resultSet.getInt("anchor_bid");
                        }

                        Message message = new Message();
                        message.what = 112;
                        handler.sendMessage(message);

                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }
                }catch (java.sql.SQLException e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void updateSta(final int i){
        new Thread(){
            public void run(){
                try {
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null) {
                        Statement stmt = conn.createStatement();
                        String sql = "UPDATE anchor SET anchor_state = ? WHERE anchor_phone = " +
                                SharePreferences.getString(getActivity(),AppConstants.USER_PHONE) +
                                "";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql);
                        preparedStatement.setInt(1,i);
                        preparedStatement.executeUpdate();

                        preparedStatement.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }
                }catch (java.sql.SQLException e){
                    e.printStackTrace();
                }
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
                    setClick();
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
