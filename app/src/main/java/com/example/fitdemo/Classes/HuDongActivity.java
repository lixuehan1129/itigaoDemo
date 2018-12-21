package com.example.fitdemo.Classes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.fitdemo.Adapter.HuDongAdapter;
import com.example.fitdemo.AutoProject.JDBCTools;
import com.example.fitdemo.AutoProject.Tip;
import com.example.fitdemo.R;
import com.example.fitdemo.Utils.StatusBarUtils;
import com.mysql.jdbc.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/12/14.
 */

public class HuDongActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<String> image;
    private ArrayList<String> title;
    private ArrayList<String> content;
    private ArrayList<String> add;

    private int hudong_classify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hudong_activity);
        StatusBarUtils.setWindowStatusBarColor(HuDongActivity.this, R.color.colorWhite);
        Intent intent = getIntent();
        hudong_classify = intent.getIntExtra("hudong_classify",0);
        initView();
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

        connectData();
    }

    private void connectData(){
        image = new ArrayList<>();
        title = new ArrayList<>();
        content = new ArrayList<>();
        add = new ArrayList<>();

        new Thread(){
            public void run(){
                Looper.prepare();
                try{
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        Statement stmt = conn.createStatement();
                        String sql = "SELECT * FROM hudong WHERE hudong_classify = " +
                                hudong_classify +
                                " ORDER BY hudong_id DESC LIMIT 20";
                        ResultSet resultSet = stmt.executeQuery(sql);
                        while(resultSet.next()){
                            image.add(resultSet.getString("hudong_cover"));
                            title.add(resultSet.getString("hudong_name"));
                            content.add(resultSet.getString("hudong_content"));
                            add.add(resultSet.getString("hudong_add"));
                        }
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Tip.showTip(HuDongActivity.this,"请检查网络");
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
        List<HuDongAdapter.HuDong> huDongs = new ArrayList<>();
        for(int i=0; i<image.size(); i++){
            HuDongAdapter huDongAdapter = new HuDongAdapter(huDongs);
            HuDongAdapter.HuDong huDong = huDongAdapter.new HuDong(image.get(i), title.get(i), content.get(i));
            huDongs.add(huDong);
        }

        HuDongAdapter huDongAdapter = new HuDongAdapter(huDongs);
        recyclerView.setAdapter(huDongAdapter);
        huDongAdapter.setOnItemClickListener(new HuDongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(HuDongActivity.this, BroadcastActivity.class);
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
