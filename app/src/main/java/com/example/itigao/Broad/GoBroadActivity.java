package com.example.itigao.Broad;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.content.pm.ActivityInfo;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.itigao.R;


import me.lake.librestreaming.core.listener.RESConnectionListener;
import me.lake.librestreaming.ws.StreamAVOption;
import me.lake.librestreaming.ws.StreamLiveCameraView;

/**
 * Created by 最美人间四月天 on 2019/1/10.
 */

//public class GoBroadActivity extends AppCompatActivity implements RtmpHandler.RtmpListener,
//        SrsRecordHandler.SrsRecordListener, SrsEncodeHandler.SrsEncodeListener {

public class GoBroadActivity extends AppCompatActivity{

    private Button btnPublish;
    private ImageView btnSwitchCamera;

    private StreamLiveCameraView mLiveCameraView;
    private StreamAVOption streamAVOption;

    private int GoBid;

    private String url = "rtmp://zb.tipass.com:1935/live/";

   // private SrsPublisher mPublisher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.go_broad);

        Intent intent = getIntent();
        GoBid = intent.getIntExtra("GoBroadBid",0);

        // response screen rotation event
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);


        //2、通过Resources获取
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        int width = dm.widthPixels;


        btnPublish = (Button) findViewById(R.id.publish);
        btnSwitchCamera = (ImageView) findViewById(R.id.swCam);

        initLiveConfig(height,width);

//
    }

    /**
     * 设置推流参数
     */
    public void initLiveConfig(int height, int width) {
        mLiveCameraView = (StreamLiveCameraView) findViewById(R.id.stream_previewView);

        //参数配置 start
        streamAVOption = new StreamAVOption();
       // streamAVOption.videoHeight = height;
       // streamAVOption.videoWidth = width;
        streamAVOption.streamUrl = url + GoBid;
        //参数配置 end

        mLiveCameraView.init(this, streamAVOption);
        mLiveCameraView.addStreamStateListener(resConnectionListener);

//        LinkedList<BaseHardVideoFilter> files = new LinkedList<>();
//        files.add(new GPUImageCompatibleFilter(new GPUImageBeautyFilter()));
//        files.add(new WatermarkFilter(BitmapFactory.decodeResource(getResources(),R.mipmap.live),new Rect(100,100,200,200)));
//        mLiveCameraView.setHardVideoFilter(new HardVideoGroupFilter(files));

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnPublish.getText().toString().contentEquals("Push")) {
                    if(!mLiveCameraView.isStreaming()){
                        mLiveCameraView.startStreaming(url + GoBid);
                    }
                    //    Tip.showTip(GoBroadActivity.this,"开始直播");
                    btnPublish.setText("Stop");
                } else if (btnPublish.getText().toString().contentEquals("Stop")) {
                    if(mLiveCameraView.isStreaming()){
                        mLiveCameraView.stopStreaming();
                    }
                    btnPublish.setText("Push");
                    //   Tip.showTip(GoBroadActivity.this,"停止直播");

                }
            }
        });

        btnSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLiveCameraView.swapCamera();
            }
        });
    }

    RESConnectionListener resConnectionListener = new RESConnectionListener() {
        @Override
        public void onOpenConnectionResult(int result) {
            //result 0成功  1 失败
            Toast.makeText(GoBroadActivity.this,"打开推流连接 状态："+result+ " 推流地址："+ url + GoBid,Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWriteError(int errno) {
            Toast.makeText(GoBroadActivity.this,"推流出错,请尝试重连",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCloseConnectionResult(int result) {
            //result 0成功  1 失败
            Toast.makeText(GoBroadActivity.this,"关闭推流连接 状态："+result,Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLiveCameraView.destroy();
    }

}

