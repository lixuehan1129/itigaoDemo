package com.example.fitdemo.Classes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.fitdemo.Adapter.HuDongAdapter;
import com.example.fitdemo.R;
import com.example.fitdemo.Utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/12/14.
 */

public class HuDongActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<Integer> image = new ArrayList<>();
    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> content = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hudong_activity);
        StatusBarUtils.setWindowStatusBarColor(HuDongActivity.this, R.color.colorWhite);
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

        initData();
    }

    private void initData(){
        for(int i=0; i<9; i++){
            title.add("互动视频" + i);
            content.add("我在这里发布了的健身视频，快来看吧" + i);
        }
        image.add(R.mipmap.hudong_head1);
        image.add(R.mipmap.hudong_head2);
        image.add(R.mipmap.hudong_head3);
        image.add(R.mipmap.hudong_head4);
        image.add(R.mipmap.hudong_head5);
        image.add(R.mipmap.hudong_head6);
        image.add(R.mipmap.hudong_head1);
        image.add(R.mipmap.hudong_head3);
        image.add(R.mipmap.hudong_head4);

        setAdapter();
    }

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
