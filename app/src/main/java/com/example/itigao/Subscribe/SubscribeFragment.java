package com.example.itigao.Subscribe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.itigao.Adapter.ClassVideoAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JDBCTools;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.AutoProject.Tip;
import com.example.itigao.MainActivity;
import com.example.itigao.R;
import com.example.itigao.Video.VideoNewActivity;
import com.example.itigao.ViewHelper.BaseFragment;
import com.mysql.jdbc.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by 最美人间四月天 on 2018/11/26.
 */

public class SubscribeFragment extends BaseFragment {


    private int todayTime;
    private int loginCount = 1;

    private RecyclerView recyclerView;
    private TextView days;
    private ViewFlipper viewFlipper;
    ClassVideoAdapter classVideoAdapter;
    List<ClassVideoAdapter.Class_Video> class_videos;
    private static final int OVER = 1;

    ArrayList<String> name = new ArrayList<>();
    ArrayList<Integer> bid = new ArrayList<>();
    ArrayList<String> cover = new ArrayList<>();
    ArrayList<String> add = new ArrayList<>();
    ArrayList<Integer> section = new ArrayList<>();


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


    //定义界面控件
    @SuppressLint("NewApi")
    private void initView(View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.subscribeFragment_mainTool);
        toolbar.setTitle("推荐内容");
        recyclerView = (RecyclerView) view.findViewById(R.id.subscribeFragment_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        days = (TextView) view.findViewById(R.id.subscribeFragment_days);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.view_flipper);

        viewFlipper.startFlipping();

        //设置累计天数
        LoginCount();

        if(name.size() > 0){
            initData();
        }
    }

    @Override
    protected void onFragmentFirstVisible(){
        initView(getView());
        connectData();
    }

    private void LoginCount(){
        loginCount = SharePreferences.getInt(getActivity(),AppConstants.USER_LOGIN_COUNT);
        isTodayFirstLogin();
    }

    /**
     * 判断是否是当日第一次登陆
     */
    @SuppressLint("SetTextI18n")
    private void isTodayFirstLogin() {
        SharedPreferences preferences = getActivity().getSharedPreferences("LastLoginTime", MODE_PRIVATE);
        int lastTime = preferences.getInt("LoginTime", 20190225);
        // Toast.makeText(MainActivity.this, "value="+date, Toast.LENGTH_SHORT).show();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
        todayTime = Integer.parseInt(df.format(new Date()));// 获取当前的日期

        if (lastTime == todayTime) { //如果两个时间段相等
            days.setText("" + loginCount);
        } else if(todayTime - lastTime == 1){
            loginCount ++;
            SharePreferences.remove(getActivity(),AppConstants.USER_LOGIN_COUNT);
            SharePreferences.putInt(getActivity(),AppConstants.USER_LOGIN_COUNT,loginCount);
            days.setText("" + loginCount);
            saveExitTime(todayTime);
        }else {
            loginCount = 1;
            SharePreferences.remove(getActivity(),AppConstants.USER_LOGIN_COUNT);
            SharePreferences.putInt(getActivity(),AppConstants.USER_LOGIN_COUNT,loginCount);
            days.setText("" + loginCount);
            saveExitTime(todayTime);
        }
    }

    /**
     * 保存每次退出的时间
     * @param extiLoginTime
     */
    private void saveExitTime(int extiLoginTime) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("LastLoginTime", MODE_PRIVATE).edit();
        editor.putInt("LoginTime", extiLoginTime);
        //这里用apply()而没有用commit()是因为apply()是异步处理提交，不需要返回结果，而我也没有后续操作
        //而commit()是同步的，效率相对较低
        //apply()提交的数据会覆盖之前的,这个需求正是我们需要的结果
        editor.apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
     //   saveExitTime(todayTime);
    }



    private void connectData(){

        new Thread(){
            public void run(){
                Looper.prepare();
                try{
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        Statement stmt = conn.createStatement();
                        String sql = "SELECT class_name,class_bid,class_cover,class_section,class_add FROM class " +
                                "ORDER BY class_num DESC LIMIT 4";
                        ResultSet resultSet = stmt.executeQuery(sql);
                        while(resultSet.next()){
                            String rName = resultSet.getString("class_name");
                            int rBid = resultSet.getInt("class_bid");
                            String rCover = resultSet.getString("class_cover");
                            String rAdd = resultSet.getString("class_add");
                            int rSection = resultSet.getInt("class_section");
                            name.add(rName);
                            bid.add(rBid);
                            cover.add(rCover);
                            section.add(rSection);
                            add.add(rAdd);
                        }

                        Message message = new Message();
                        message.what = OVER;
                        handler.sendMessage(message);

                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Tip.showTip(getActivity(),"请检查网络");
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
    }

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case OVER:{
                    initData();
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });

    private void initData(){
        class_videos = new ArrayList<>();


        for(int i = 0; i < name.size(); i++){
            ClassVideoAdapter newData = new ClassVideoAdapter(class_videos);
            ClassVideoAdapter.Class_Video class_video = newData.new Class_Video(name.get(i),cover.get(i));
            class_videos.add(class_video);

        }

        classVideoAdapter = new ClassVideoAdapter(class_videos);
        recyclerView.setAdapter(classVideoAdapter);
        classVideoAdapter.setOnItemClickListener(new ClassVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), VideoNewActivity.class);
                intent.putExtra("video_bid",bid.get(position));
                intent.putExtra("video_section",section.get(position));
                intent.putExtra("video_add",add.get(position));
                intent.putExtra("video_select",1);
                intent.putExtra("video_record",0);
                startActivity(intent);
            }
        });

    }



}
