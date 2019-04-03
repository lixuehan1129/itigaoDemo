package com.example.itigao.HuDong;

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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.itigao.Adapter.HuDongAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JsonCode;
import com.example.itigao.ClassAb.HuDong;
import com.example.itigao.R;
import com.example.itigao.Utils.StatusBarUtils;
import com.example.itigao.okHttp.OkHttpBase;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by 最美人间四月天 on 2018/12/14.
 */

public class HuDongActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView imageView;


    private HuDongAdapter huDongAdapter;
    private List<HuDong> huDongs = new ArrayList<>();

    private int hudong_classify;


    private LocalBroadcastManager broadcastManager;
    private IntentFilter intentFilter;
    private BroadcastReceiver mReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hudong_activity);
        StatusBarUtils.setWindowStatusBarColor(HuDongActivity.this, R.color.colorWhite);
        Intent intent = getIntent();
        hudong_classify = intent.getIntExtra("hudong_classify",0);
        initView();

        broadcastManager = LocalBroadcastManager.getInstance(HuDongActivity.this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.BROAD_HU);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                //收到广播后所作的操作
                connectData();
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mReceiver);
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.hudong_mainTool);
        toolbar.setTitle("互动天地");
        back(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.hudong_rv);
        //使用瀑布流布局,第一个参数 spanCount 列数,第二个参数 orentation 排列方向
        StaggeredGridLayoutManager recyclerViewLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        huDongAdapter = new HuDongAdapter(huDongs);
        recyclerView.setAdapter(huDongAdapter);


        imageView = (ImageView) findViewById(R.id.hudong_video);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HuDongActivity.this,HuDongPutActivity.class);
                intent.putExtra("hudong_ccc",hudong_classify);
                startActivity(intent);
            }
        });

        connectData();
    }

    private void connectData(){
        new Thread(){
            public void run(){
                Looper.prepare();
                RequestBody requestBody = new FormBody.Builder()
                        .add("hudong_classify", String.valueOf(hudong_classify))
                        .build();
                String regData = OkHttpBase.getResponse(requestBody,"http://39.105.213.41:8080/StudyAppService/StudyServlet/hudong");
                if(regData != null){
                    if(JsonCode.getCode(regData) == 200){
                        String jsonString = JsonCode.getData(regData);
                        huDongs = JsonCode.jsonToList(jsonString, HuDong.class);
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
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
                    setAdapter();
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });


    private void setAdapter(){
        if(huDongs.size() > 0){
            huDongAdapter.addDataAt(huDongs);
        }
        huDongAdapter.setOnItemClickListener(new HuDongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(HuDongActivity.this, HuDongPlayActivity.class);
                intent.putExtra("hudong_c_add",huDongs.get(position).getHudong_add());
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
}
