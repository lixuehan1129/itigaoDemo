package com.example.itigao.Classes;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.example.itigao.Adapter.ClassActivityAdapter;
import com.example.itigao.Adapter.ClassVideoAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JDBCTools;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.AutoProject.Tip;
import com.example.itigao.Broad.BroadNewActivity;
import com.example.itigao.HuDong.HuDongActivity;
import com.example.itigao.HuDong.HuDongPlayActivity;
import com.example.itigao.R;
import com.example.itigao.Subscribe.VideoActivity;
import com.example.itigao.Utils.StatusBarUtils;
import com.example.itigao.Broad.BroadcastActivity;
import com.example.itigao.Video.VideoNewActivity;
import com.mysql.jdbc.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2019/1/17.
 */

public class BaseActivity extends AppCompatActivity {


    private int mClassify;

    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private ImageView imageView1, imageView2;
    private TextView textView;

    private LocalBroadcastManager broadcastManager;
    private IntentFilter intentFilter;
    private BroadcastReceiver mReceiver;

    ArrayList<String> name1;
    ArrayList<Integer> bid1;
    ArrayList<String> cover1;
    ArrayList<Integer> roomId;

    ArrayList<String> name2;
    ArrayList<Integer> bid2;
    ArrayList<String> cover2;
    ArrayList<Integer> section2;
    ArrayList<String> add2;
    ArrayList<Integer> select2;


    ArrayList<String> name3;
    ArrayList<Integer> bid3;
    ArrayList<String> cover3;
    ArrayList<Integer> section3;
    ArrayList<String> add3;

    ArrayList<String> cover4;
    ArrayList<String> add4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_activity);
        Intent intent = getIntent();
        mClassify = intent.getIntExtra("NianJi",1);

        //mActivity = this;
        StatusBarUtils.setWindowStatusBarColor(BaseActivity.this, R.color.colorWhite);
        initView();

        broadcastManager = LocalBroadcastManager.getInstance(BaseActivity.this.getApplication());
        intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.BROAD_RECORD);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                //收到广播后所作的操作
                connectData2();
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mReceiver);
        if (Util.isOnMainThread()) {
            Glide.with(getApplicationContext()).pauseRequests();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    private void initView(){
        String[] item = {"小学一年级","小学二年级","小学三年级","小学四年级","小学五年级","小学六年级",
                "初中一年级","初中二年级","初中三年级","高中一年级","高中二年级","高中三年级",
                "大学一年级","大学二年级","大学三年级","大学四年级","研究生一年级","研究生二年级","研究生三年级"
                };
        Toolbar toolbar = (Toolbar) findViewById(R.id.class_activity_mainTool);
        toolbar.setTitle(item[mClassify-1]);
        back(toolbar);


        recyclerView1 = (RecyclerView) findViewById(R.id.class_activity_rv1);
        recyclerView2 = (RecyclerView) findViewById(R.id.class_activity_rv2);
        recyclerView3 = (RecyclerView) findViewById(R.id.class_activity_rv3);
        imageView1 = (ImageView) findViewById(R.id.class_activity_iv1);
        imageView2 = (ImageView) findViewById(R.id.class_activity_iv2);
        textView = (TextView) findViewById(R.id.class_activity_tv1);


        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(linearLayoutManager1);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        recyclerView3.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView3.setNestedScrollingEnabled(false);

        connectData1();
        connectData2();
        connectData3();
        connectHu();
        setClick();
    }

    private void setClick(){
        imageView1.setImageResource(R.mipmap.ic_touxiang41);
        imageView2.setImageResource(R.mipmap.ic_touxiang51);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BaseActivity.this, HuDongActivity.class);
                intent.putExtra("hudong_classify",mClassify);
                startActivity(intent);
            }
        });

//        to.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(userSta == 1 && userStyle == mClassify){
//                    Intent intent = new Intent(mActivity,GoBroadActivity.class);
//                    startActivity(intent);
//                }else {
//                    Tip.showTip(mActivity,"还不是主播");
//                }
//            }
//        });

    }


    private void connectHu(){
        cover4 = new ArrayList<>();
        add4 = new ArrayList<>();
        new Thread(){
            public void run(){
                Looper.prepare();
                try{
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        Statement stmt = conn.createStatement();
                        String sql = "SELECT hudong_cover,hudong_add FROM hudong WHERE hudong_classify = " +
                                mClassify +
                                " ORDER BY hudong_id DESC LIMIT 2";
                        ResultSet resultSet = stmt.executeQuery(sql);
                        while(resultSet.next()){
                            cover4.add(resultSet.getString("hudong_cover"));
                            add4.add(resultSet.getString("hudong_add"));
                        }
                        Message message = new Message();
                        message.what = 4;
                        handler.sendMessage(message);
                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Tip.showTip(BaseActivity.this,"请检查网络");
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
                case 1:{
                    initData1(name1,cover1);
                    break;
                }
                case 2:{
                    initData2(name2,cover2,bid2,section2,add2,select2);
                    break;
                }
                case 3:{
                    initData3(name3,cover3,bid3,section3,add3);
                    break;
                }
                case 4:{
                    setImage();
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });

    private void setImage(){
//        if(Util.isOnMainThread()) {
//            if (cover4.size() == 2) {
//                Glide.with(BaseActivity.this)
//                        .load(cover4.get(0))
//                        .asBitmap()  //不可加载动图
//                        .dontAnimate()//取消淡入淡出动画
//                        .placeholder(R.mipmap.ic_touxiang11)
//                        .error(R.mipmap.ic_touxiang11)
//                        //         .thumbnail(0.1f) //先加载十分之一作为缩略图
//                        .into(imageView1);
//                Glide.with(BaseActivity.this)
//                        .load(cover4.get(1))
//                        .asBitmap()  //不可加载动图
//                        .dontAnimate()//取消淡入淡出动画
//                        .placeholder(R.mipmap.ic_touxiang11)
//                        .error(R.mipmap.ic_touxiang11)
//                        //        .thumbnail(0.1f) //先加载十分之一作为缩略图
//                        .into(imageView2);
//            } else if (cover4.size() == 1) {
//                Glide.with(BaseActivity.this)
//                        .load(cover4.get(0))
//                        .asBitmap()  //不可加载动图
//                        .dontAnimate()//取消淡入淡出动画
//                        .placeholder(R.mipmap.ic_touxiang11)
//                        .error(R.mipmap.ic_touxiang11)
//                        //       .thumbnail(0.1f) //先加载十分之一作为缩略图
//                        .into(imageView2);
//            }
//        }

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BaseActivity.this, HuDongPlayActivity.class);
                if(cover4.size() ==2){
                    intent.putExtra("hudong_c_add",add4.get(0));
                }
                startActivity(intent);
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BaseActivity.this, HuDongPlayActivity.class);
                if(cover4.size() == 2){
                    intent.putExtra("hudong_c_add",add4.get(1));
                }else if(cover4.size() == 1){
                    intent.putExtra("hudong_c_add",add4.get(0));
                }

                startActivity(intent);
            }
        });

    }


    //主播列表
    private void connectData1(){
        name1 = new ArrayList<>();
        cover1 = new ArrayList<>();
        bid1 = new ArrayList<>();
        roomId = new ArrayList<>();
        new Thread(){
            public void run(){
                Looper.prepare();
                try{
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        Statement stmt = conn.createStatement();
                        String sql = "SELECT * FROM anchor WHERE anchor_classify = " +
                                mClassify +
                                " and anchor_state = 1";
                        ResultSet resultSet = stmt.executeQuery(sql);
                        while(resultSet.next()){
                            name1.add(resultSet.getString("anchor_name"));
                            bid1.add(resultSet.getInt("anchor_bid"));
                            roomId.add(resultSet.getInt("anchor_room"));
                            cover1.add(resultSet.getString("anchor_cover"));
                        }
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Tip.showTip(BaseActivity.this,"请检查网络");
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
    }

    private void initData1(ArrayList<String> introduce,ArrayList<String> image){
        List<ClassActivityAdapter.Class_Activity> class_activities1 = new ArrayList<>();
        if(introduce.size() > 0){
            for (int i = 0; i<introduce.size(); i++){
                ClassActivityAdapter newData = new ClassActivityAdapter(class_activities1);
                ClassActivityAdapter.Class_Activity class_activity = newData.new Class_Activity(introduce.get(i),image.get(i));
                class_activities1.add(class_activity);
                ClassActivityAdapter classActivityAdapter1 = new ClassActivityAdapter(class_activities1);
                recyclerView1.setAdapter(classActivityAdapter1);
                classActivityAdapter1.setOnItemClickListener(new ClassActivityAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(BaseActivity.this, BroadNewActivity.class);
                        intent.putExtra("anchor_bid",bid1.get(position));
                        intent.putExtra("anchor_room",roomId.get(position));
                        startActivity(intent);
                    }
                });
            }
        }else {
            ClassActivityAdapter newData = new ClassActivityAdapter(class_activities1);
            ClassActivityAdapter.Class_Activity class_activity = newData.new Class_Activity("暂无",null);
            class_activities1.add(class_activity);
            ClassActivityAdapter classActivityAdapter1 = new ClassActivityAdapter(class_activities1);
            recyclerView1.setAdapter(classActivityAdapter1);
        }
    }

    //观看记录
    private void connectData2(){

        name2 = new ArrayList<>();
        bid2 = new ArrayList<>();
        cover2 = new ArrayList<>();
        section2 = new ArrayList<>();
        add2 = new ArrayList<>();
        select2 = new ArrayList<>();

        new Thread(){
            public void run(){
                Looper.prepare();
                try{
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        Statement stmt = conn.createStatement();
                        String sql = "SELECT * FROM record WHERE record_user = '" +
                                SharePreferences.getString(BaseActivity.this, AppConstants.USER_PHONE) +
                                "' AND record_classify = " +
                                mClassify +
                                "";
                        ResultSet resultSet = stmt.executeQuery(sql);
                        while(resultSet.next()){
                            name2.add(resultSet.getString("record_name"));
                            bid2.add(resultSet.getInt("record_bid"));
                            cover2.add(resultSet.getString("record_cover"));
                            section2.add(resultSet.getInt("record_section"));
                            add2.add(resultSet.getString("record_add"));
                            select2.add(resultSet.getInt("record_select"));
                        }
                        Message message = new Message();
                        message.what = 2;
                        handler.sendMessage(message);
                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Tip.showTip(BaseActivity.this,"请检查网络");
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
    }

    private void initData2(ArrayList<String> introduce, ArrayList<String> image, final ArrayList<Integer> id,
                           final ArrayList<Integer> section,final ArrayList<String> add,final ArrayList<Integer> select){

        List<ClassActivityAdapter.Class_Activity> class_activities2 = new ArrayList<>();

        if(introduce.size() > 0){
            for (int i = 0; i<introduce.size(); i++){
                ClassActivityAdapter newData = new ClassActivityAdapter(class_activities2);
                ClassActivityAdapter.Class_Activity class_activity = newData.new Class_Activity(introduce.get(i),image.get(i));
                class_activities2.add(class_activity);
                ClassActivityAdapter classActivityAdapter2 = new ClassActivityAdapter(class_activities2);
                recyclerView2.setAdapter(classActivityAdapter2);
                classActivityAdapter2.setOnItemClickListener(new ClassActivityAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(BaseActivity.this, VideoNewActivity.class);
                        intent.putExtra("video_bid",id.get(position));
                        intent.putExtra("video_section",section.get(position));
                        intent.putExtra("video_add",add.get(position));
                        intent.putExtra("video_select",select.get(position));
                        intent.putExtra("video_record",2);
                        startActivity(intent);
                    }
                });
            }
        }else {
            ClassActivityAdapter newData = new ClassActivityAdapter(class_activities2);
            ClassActivityAdapter.Class_Activity class_activity = newData.new Class_Activity("暂无",null);
            class_activities2.add(class_activity);
            ClassActivityAdapter classActivityAdapter2 = new ClassActivityAdapter(class_activities2);
            recyclerView2.setAdapter(classActivityAdapter2);
        }



    }

    //课程列表
    private void connectData3(){

        name3 = new ArrayList<>();
        bid3 = new ArrayList<>();
        cover3 = new ArrayList<>();
        section3 = new ArrayList<>();
        add3 = new ArrayList<>();

        new Thread(){
            public void run(){
                Looper.prepare();
                try{
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        Statement stmt = conn.createStatement();
                        String sql = "SELECT * FROM class WHERE class_classify = " +
                                mClassify +
                                " AND class_select = 1";
                        ResultSet resultSet = stmt.executeQuery(sql);
                        while(resultSet.next()){
                            String rName = resultSet.getString("class_name");
                            int rBid = resultSet.getInt("class_bid");
                            String rCover = resultSet.getString("class_cover");
                            String rAdd = resultSet.getString("class_add");
                            int rSection = resultSet.getInt("class_section");
                            name3.add(rName);
                            bid3.add(rBid);
                            cover3.add(rCover);
                            section3.add(rSection);
                            add3.add(rAdd);
                        }
                        Message message = new Message();
                        message.what = 3;
                        handler.sendMessage(message);
                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Tip.showTip(BaseActivity.this,"请检查网络");
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
    }



    private void initData3(ArrayList<String> introduce, ArrayList<String> image, final ArrayList<Integer> id,
                           final ArrayList<Integer> section,final ArrayList<String> add){
        List<ClassVideoAdapter.Class_Video> class_videos = new ArrayList<>();
        for(int i = 0; i < introduce.size(); i++){
            ClassVideoAdapter newData = new ClassVideoAdapter(class_videos);
            ClassVideoAdapter.Class_Video class_video = newData.new Class_Video(introduce.get(i),image.get(i));
            class_videos.add(class_video);
        }
        ClassVideoAdapter classVideoAdapter = new ClassVideoAdapter(class_videos);
        recyclerView3.setAdapter(classVideoAdapter);
        classVideoAdapter.setOnItemClickListener(new ClassVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(BaseActivity.this, VideoNewActivity.class);
                intent.putExtra("video_bid",id.get(position));
                intent.putExtra("video_section",section.get(position));
                intent.putExtra("video_add",add.get(position));
                intent.putExtra("video_select",1);
                intent.putExtra("video_record",1);
                startActivity(intent);
            }
        });


    }



    //返回注销事件
    private void back(Toolbar toolbar){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

