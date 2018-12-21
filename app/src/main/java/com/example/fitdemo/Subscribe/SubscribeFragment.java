package com.example.fitdemo.Subscribe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fitdemo.Adapter.ClassVideoAdapter;
import com.example.fitdemo.AutoProject.JDBCTools;
import com.example.fitdemo.AutoProject.Tip;
import com.example.fitdemo.Classes.RunActivity;
import com.example.fitdemo.R;
import com.example.fitdemo.ViewHelper.BaseFragment;
import com.mysql.jdbc.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 最美人间四月天 on 2018/11/26.
 */

public class SubscribeFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private TextView days, minutes;
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
        minutes = (TextView) view.findViewById(R.id.subscribeFragment_minutes);

        //设置累计运动天数和运动时长
        days.setText("396");
        minutes.setText("67");

        if(name.size() > 0){
            initData();
        }
    }

    @Override
    protected void onFragmentFirstVisible(){
        initView(getView());
        connectData();
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
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
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
