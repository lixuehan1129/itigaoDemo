package com.example.fitdemo.Classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fitdemo.Adapter.InteractAdapter;

import com.example.fitdemo.R;
import com.example.fitdemo.Utils.StatusBarUtils;



import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * Created by 最美人间四月天 on 2018/12/14.
 */

public class BroadcastActivity extends AppCompatActivity {

    private JzvdStd jzvdStd;
    private LinearLayout linearLayout;
    private RecyclerView recyclerView;
    private EditText editText;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_broad_activity);
        StatusBarUtils.setWindowStatusBarColor(BroadcastActivity.this, R.color.colorWhite);

        initView();
    }

    private void initView(){
       // videoPlayer = (VideoPlayer) findViewById(R.id.video_broad_vv);
        jzvdStd = (JzvdStd) findViewById(R.id.video_broad_vv);
        recyclerView = (RecyclerView) findViewById(R.id.video_broad_rv) ;
        linearLayout = (LinearLayout) findViewById(R.id.video_broad_li);
        editText = (EditText) findViewById(R.id.video_broad_et);
        imageView = (ImageView) findViewById(R.id.video_broad_iv);

        recyclerView.setLayoutManager(new LinearLayoutManager(BroadcastActivity.this));
        //获取屏幕宽
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        setWidthHeightWithRatio(jzvdStd,width,16,9);

        imageOnClick();
        initData();
    }

    private void initData(){
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> content = new ArrayList<>();
        for(int i=0; i<8; i++){
            name.add("用户名" + i);
            content.add("看完这个视频，我想说点什么呢" + i);
        }
        name.add("用户名");
        content.add("看完这个视频，我想说点什么呢，我觉得真的很好很有用，6666666666");
        setAdapter(name,content);
    }

    private void setAdapter(final ArrayList<String> name, ArrayList<String> content){
        List<InteractAdapter.Interact> interacts = new ArrayList<>();
        for(int i=0; i<name.size(); i++){
            InteractAdapter interactAdapter = new InteractAdapter(interacts);
            InteractAdapter.Interact interact = interactAdapter.new Interact(name.get(i), content.get(i));
            interacts.add(interact);
        }

        InteractAdapter interactAdapter = new InteractAdapter(interacts);
        recyclerView.setAdapter(interactAdapter);
        interactAdapter.setOnItemClickListener(new InteractAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    private void setPlay(String url){
        jzvdStd.setUp(url, "", Jzvd.SCREEN_WINDOW_NORMAL);
        jzvdStd.startVideo();

    }

    private void imageOnClick(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(editText.getText())){
                    ProgressDialog progressDialog = ProgressDialog.show(BroadcastActivity.this,"","正在上传...",true);
                    progressDialog.setCancelable(true);// 设置是否可以通过点击Back键取消
                }else {
                    Toast.makeText(BroadcastActivity.this,"内容不能为空",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //根据宽高比设置控件宽高, 如设置宽高比为5:4，那么widthRatio为5，heightRatio为4
    public static void setWidthHeightWithRatio(View view, int width, int widthRatio, int heightRatio) {
        if (width <= 0) width = view.getWidth();
        int height = width * heightRatio / widthRatio;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = height;
            layoutParams.width = width;
            view.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
        JzvdStd.goOnPlayOnPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //home back
        JzvdStd.goOnPlayOnResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
