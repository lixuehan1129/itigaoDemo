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

import org.yczbj.ycvideoplayerlib.ConstantKeys;
import org.yczbj.ycvideoplayerlib.VideoPlayer;
import org.yczbj.ycvideoplayerlib.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.VideoPlayerManager;
import org.yczbj.ycvideoplayerlib.listener.OnVideoBackListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/12/14.
 */

public class BroadcastActivity extends AppCompatActivity {
    private VideoPlayerController videoPlayerController;
    private VideoPlayer videoPlayer;
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
        videoPlayer = (VideoPlayer) findViewById(R.id.video_broad_vv);
        recyclerView = (RecyclerView) findViewById(R.id.video_broad_rv) ;
        linearLayout = (LinearLayout) findViewById(R.id.video_broad_li);
        editText = (EditText) findViewById(R.id.video_broad_et);
        imageView = (ImageView) findViewById(R.id.video_broad_iv);

        recyclerView.setLayoutManager(new LinearLayoutManager(BroadcastActivity.this));
        //获取屏幕宽
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        setWidthHeightWithRatio(videoPlayer,width,16,9);

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
        //设置播放类型
        //IJKPlayer or MediaPlayer
        videoPlayer.setPlayerType(ConstantKeys.IjkPlayerType.TYPE_NATIVE);
        //视频地址
        //     Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + mp4);
        //设置视频地址和请求头部
        videoPlayer.setUp(url,null);
        //是否从上一次的位置播放
        videoPlayer.continueFromLastPosition(true);
        //设置播放速度
        videoPlayer.setSpeed(1.0f);

        //创建视频控制器
        videoPlayerController = new VideoPlayerController(this);
        videoPlayerController.setTitle("");
        //设置5秒不操作后隐藏头部和底部布局视图
        videoPlayerController.setHideTime(5000);
        // videoPlayerController.setLoadingType(2);
        //返回监听
        videoPlayerController.setOnVideoBackListener(new OnVideoBackListener() {
            @Override
            public void onBackClick() {
                onBackPressed();
                finish();
            }
        });
        videoPlayer.setController(videoPlayerController);

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
        videoPlayer.release();
        videoPlayer.releasePlayer();
        VideoPlayerManager.instance().releaseVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (VideoPlayerManager.instance().onBackPressed()) return;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
