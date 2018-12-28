package com.example.fitdemo.Classes;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.example.fitdemo.Adapter.ClassActivityAdapter;
import com.example.fitdemo.Adapter.ClassVideoAdapter;
import com.example.fitdemo.AutoProject.AppConstants;
import com.example.fitdemo.AutoProject.JDBCTools;
import com.example.fitdemo.AutoProject.SharePreferences;
import com.example.fitdemo.R;
import com.example.fitdemo.Subscribe.VideoPlayActivity;
import com.example.fitdemo.Utils.StatusBarUtils;
import com.example.fitdemo.AutoProject.Tip;
import com.mysql.jdbc.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/11/30.
 */

public class YogaActivity extends AppCompatActivity {

    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private ImageView imageView1, imageView2;
    private TextView textView;

    private LocalBroadcastManager broadcastManager;
    private IntentFilter intentFilter;
    private BroadcastReceiver mReceiver;

    ArrayList<String> name1;
    ArrayList<Integer> bid1;
    ArrayList<String> cover1;

    ArrayList<String> name2;
    ArrayList<Integer> bid2;
    ArrayList<String> cover2;
    ArrayList<Integer> section2;
    ArrayList<String> add2;
    ArrayList<Integer> select2;

    ArrayList<String> name3 = new ArrayList<>();
    ArrayList<Integer> bid3 = new ArrayList<>();
    ArrayList<String> cover3 = new ArrayList<>();
    ArrayList<Integer> section3 = new ArrayList<>();
    ArrayList<String> add3 = new ArrayList<>();

    ArrayList<String> cover4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_activity);
        StatusBarUtils.setWindowStatusBarColor(YogaActivity.this, R.color.colorWhite);
        initView();

        broadcastManager = LocalBroadcastManager.getInstance(YogaActivity.this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.BROAD_RECORD4);
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.class_activity_mainTool);
        toolbar.setTitle("瑜伽课程");
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
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YogaActivity.this, BroadcastActivity.class);
                startActivity(intent);
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YogaActivity.this, BroadcastActivity.class);
                startActivity(intent);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YogaActivity.this, HuDongActivity.class);
                intent.putExtra("hudong_classify",4);
                startActivity(intent);
            }
        });

    }


    private void connectHu(){
        cover4 = new ArrayList<>();
        new Thread(){
            public void run(){
                Looper.prepare();
                try{
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        Statement stmt = conn.createStatement();
                        String sql = "SELECT hudong_cover FROM hudong WHERE hudong_classify = 4 ORDER BY hudong_id DESC LIMIT 2";
                        ResultSet resultSet = stmt.executeQuery(sql);
                        while(resultSet.next()){
                            cover4.add(resultSet.getString("hudong_cover"));
                        }
                        Message message = new Message();
                        message.what = 4;
                        handler.sendMessage(message);
                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Tip.showTip(YogaActivity.this,"请检查网络");
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
        if(cover4.size() == 2){
            Glide.with(YogaActivity.this)
                    .load(cover4.get(0))
                    .asBitmap()  //不可加载动图
                    .dontAnimate()//取消淡入淡出动画
                    .placeholder(R.mipmap.ic_touxiang11)
                    .error(R.mipmap.ic_touxiang11)
             //       .thumbnail(0.1f) //先加载十分之一作为缩略图
                    .into(imageView1);
            Glide.with(YogaActivity.this)
                    .load(cover4.get(1))
                    .asBitmap()  //不可加载动图
                    .dontAnimate()//取消淡入淡出动画
                    .placeholder(R.mipmap.ic_touxiang11)
                    .error(R.mipmap.ic_touxiang11)
              //      .thumbnail(0.1f) //先加载十分之一作为缩略图
                    .into(imageView2);
        }else if(cover4.size() == 1){
            Glide.with(YogaActivity.this)
                    .load(cover4.get(0))
                    .asBitmap()  //不可加载动图
                    .dontAnimate()//取消淡入淡出动画
                    .placeholder(R.mipmap.ic_touxiang11)
                    .error(R.mipmap.ic_touxiang11)
              //      .thumbnail(0.1f) //先加载十分之一作为缩略图
                    .into(imageView2);
        }

    }


    //主播列表
    private void connectData1(){
        name1 = new ArrayList<>();
        cover1 = new ArrayList<>();
        new Thread(){
            public void run(){
                Looper.prepare();
                try{
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        Statement stmt = conn.createStatement();
                        String sql = "SELECT * FROM anchor WHERE anchor_classify = 4 and anchor_state = 0";
                        ResultSet resultSet = stmt.executeQuery(sql);
                        while(resultSet.next()){
                            name1.add(resultSet.getString("anchor_name"));
                            cover1.add(resultSet.getString("anchor_cover"));
                        }
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Tip.showTip(YogaActivity.this,"请检查网络");
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
                        Intent intent = new Intent(YogaActivity.this, BroadcastActivity.class);
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
                                SharePreferences.getString(YogaActivity.this, AppConstants.USER_PHONE) +
                                "' AND record_classify = 4";
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
                        Tip.showTip(YogaActivity.this,"请检查网络");
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
                        Intent intent = new Intent(YogaActivity.this, VideoPlayActivity.class);
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
                        String sql = "SELECT * FROM class WHERE class_classify = 4 AND class_select = 1";
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
                        Tip.showTip(YogaActivity.this,"请检查网络");
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
                Intent intent = new Intent(YogaActivity.this, VideoPlayActivity.class);
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
