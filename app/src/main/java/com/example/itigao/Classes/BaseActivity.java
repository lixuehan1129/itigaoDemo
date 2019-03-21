package com.example.itigao.Classes;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.example.itigao.Adapter.ClassActivityAdapter;
import com.example.itigao.Adapter.ClassVideoAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JDBCTools;
import com.example.itigao.AutoProject.JsonCode;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.AutoProject.Tip;
import com.example.itigao.Broad.BroadNewActivity;
import com.example.itigao.ClassAb.Anchor;
import com.example.itigao.ClassAb.Classes;
import com.example.itigao.ClassAb.Record;
import com.example.itigao.HuDong.HuDongActivity;
import com.example.itigao.HuDong.HuDongPlayActivity;
import com.example.itigao.R;
import com.example.itigao.Utils.StatusBarUtils;
import com.example.itigao.Video.VideoNewActivity;
import com.mysql.jdbc.Connection;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    private ClassVideoAdapter videoAdapter;
    private ClassActivityAdapter anchorAdapter, recordAdapter;

    private List<ClassActivityAdapter.Class_Activity> anchors = new ArrayList<>();
    private List<ClassActivityAdapter.Class_Activity> records = new ArrayList<>();
    private List<ClassVideoAdapter.Class_Video> videos = new ArrayList<>();

    private List<Anchor> anchors_get;
    private List<Record> records_get;
    private List<Classes> classes_get;

    private ArrayList<String> cover4;
    private ArrayList<String> add4;



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
                connectRecord();
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


        recyclerView1 = (RecyclerView) findViewById(R.id.class_activity_rv1);//anchor
        recyclerView2 = (RecyclerView) findViewById(R.id.class_activity_rv2);//record
        recyclerView3 = (RecyclerView) findViewById(R.id.class_activity_rv3);//video
        imageView1 = (ImageView) findViewById(R.id.class_activity_iv1);
        imageView2 = (ImageView) findViewById(R.id.class_activity_iv2);
        textView = (TextView) findViewById(R.id.class_activity_tv1);


        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        anchorAdapter = new ClassActivityAdapter(anchors);
        recyclerView1.setLayoutManager(linearLayoutManager1);
        recyclerView1.setAdapter(anchorAdapter);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recordAdapter = new ClassActivityAdapter(records);
        recyclerView2.setLayoutManager(linearLayoutManager2);
        recyclerView2.setAdapter(recordAdapter);

        recyclerView3.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView3.setNestedScrollingEnabled(false);
        videoAdapter = new ClassVideoAdapter(videos);
        recyclerView3.setAdapter(videoAdapter);

        connectAnchor();
        connectRecord();
        connectVideo();
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
                    initDataAnchor();
                    break;
                }
                case 2:{
                    initDataRecord();
                    break;
                }
                case 3:{
                    initDataVideo();
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

    //获取主播列表
    private void connectAnchor(){
        anchors_get = new ArrayList<>();
        new Thread() {
            public void run() {
                Looper.prepare();

                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("anchor_classify", String.valueOf(mClassify))
                        .add("anchor_state","1")
                        .build();
                //构建一个请求对象
                Request request = new Request.Builder()
                        .url("http://39.105.213.41:8080/StudyAppService/StudyServlet/anchor")
                        .post(requestBody)
                        .build();
                //发送请求获取响应
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    //判断请求是否成功
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        String regData = response.body().string();
                        System.out.println("返回anchor"+regData);
                        if(JsonCode.getCode(regData) == 200){
                            //
                            String jsonData = JsonCode.getData(regData);
                            anchors_get = JsonCode.jsonToList(jsonData,Anchor.class);
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Looper.loop();
            }
        }.start();
    }

    private void initDataAnchor(){
        if(anchors_get.size() > 0){
            for(int i = 0; i<anchors_get.size(); i++){
                anchorAdapter.addDataAt(anchorAdapter.getItemCount(),
                        anchorAdapter.new Class_Activity(anchors_get.get(i).getAnchor_name(),
                                anchors_get.get(i).getAnchor_cover()));
            }

            anchorAdapter.setOnItemClickListener(new ClassActivityAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(BaseActivity.this, BroadNewActivity.class);
                    intent.putExtra("anchor_bid",anchors_get.get(position).getAnchor_bid());
                    intent.putExtra("anchor_room",anchors_get.get(position).getAnchor_room());
                    startActivity(intent);
                }
            });
        }else {
            anchorAdapter.addDataAt(0,anchorAdapter.new Class_Activity("暂无",null));
        }
    }

    //record
    private void connectRecord(){
        records_get = new ArrayList<>();
        new Thread() {
            public void run() {
                Looper.prepare();

                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("record_user", SharePreferences.getString(BaseActivity.this,AppConstants.USER_PHONE))
                        .add("record_classify", String.valueOf(mClassify))
                        .build();
                //构建一个请求对象
                Request request = new Request.Builder()
                        .url("http://39.105.213.41:8080/StudyAppService/StudyServlet/record")
                        .post(requestBody)
                        .build();
                //发送请求获取响应
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    //判断请求是否成功
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        String regData = response.body().string();
                        System.out.println("返回record"+regData);
                        if(JsonCode.getCode(regData) == 200){
                            String jsonData = JsonCode.getData(regData);
                            records_get = JsonCode.jsonToList(jsonData,Record.class);
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Looper.loop();
            }
        }.start();
    }

    private void initDataRecord(){
        if(records_get.size() > 0){
            for(int i = 0; i<records_get.size(); i++){
                recordAdapter.addDataAt(recordAdapter.getItemCount(),
                        recordAdapter.new Class_Activity(records_get.get(i).getRecord_name(),
                                records_get.get(i).getRecord_cover()));
            }

            recordAdapter.setOnItemClickListener(new ClassActivityAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(BaseActivity.this, VideoNewActivity.class);
                    intent.putExtra("video_bid",records_get.get(position).getRecord_bid());
                    intent.putExtra("video_section",records_get.get(position).getRecord_section());
                    intent.putExtra("video_add",records_get.get(position).getRecord_add());
                    intent.putExtra("video_select",records_get.get(position).getRecord_select());
                    intent.putExtra("video_record",2);
                    startActivity(intent);
                }
            });
        }else {
            recordAdapter.addDataAt(0,recordAdapter.new Class_Activity("暂无",null));
        }
    }

    //video
    private void connectVideo(){
        classes_get = new ArrayList<>();
        new Thread() {
            public void run() {
                Looper.prepare();

                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("class_classify", String.valueOf(mClassify))
                        .add("class_select", "1")
                        .build();
                //构建一个请求对象
                Request request = new Request.Builder()
                        .url("http://39.105.213.41:8080/StudyAppService/StudyServlet/class")
                        .post(requestBody)
                        .build();
                //发送请求获取响应
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    //判断请求是否成功
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        String regData = response.body().string();
                        System.out.println("返回classes"+regData);
                        if(JsonCode.getCode(regData) == 200){
                            String jsonData = JsonCode.getData(regData);
                            classes_get = JsonCode.jsonToList(jsonData,Classes.class);
                            Message message = new Message();
                            message.what = 3;
                            handler.sendMessage(message);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Looper.loop();
            }
        }.start();
    }

    private void initDataVideo(){
        if(classes_get.size() > 0){
            for(int i = 0; i<classes_get.size(); i++){
                videoAdapter.addDataAt(videoAdapter.getItemCount(),
                        videoAdapter.new Class_Video(classes_get.get(i).getClass_name(),
                                classes_get.get(i).getClass_cover()));
            }

            videoAdapter.setOnItemClickListener(new ClassVideoAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(BaseActivity.this, VideoNewActivity.class);
                    intent.putExtra("video_bid",classes_get.get(position).getClass_bid());
                    intent.putExtra("video_section",classes_get.get(position).getClass_section());
                    intent.putExtra("video_add",classes_get.get(position).getClass_add());
                    intent.putExtra("video_select",1);
                    intent.putExtra("video_record",1);
                    startActivity(intent);
                }
            });

        }
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
