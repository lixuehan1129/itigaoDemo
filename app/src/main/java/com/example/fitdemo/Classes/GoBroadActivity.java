package com.example.fitdemo.Classes;

import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fitdemo.AutoProject.Tip;
import com.example.fitdemo.R;
import com.github.faucamp.simplertmp.RtmpHandler;
import com.seu.magicfilter.utils.MagicFilterType;

import net.ossrs.yasea.SrsCameraView;
import net.ossrs.yasea.SrsEncodeHandler;
import net.ossrs.yasea.SrsPublisher;
import net.ossrs.yasea.SrsRecordHandler;

import java.io.IOException;
import java.net.SocketException;
import java.util.Random;

/**
 * Created by 最美人间四月天 on 2019/1/10.
 */

public class GoBroadActivity extends AppCompatActivity implements RtmpHandler.RtmpListener,
        SrsRecordHandler.SrsRecordListener, SrsEncodeHandler.SrsEncodeListener {

    private static final String TAG = "Yasea";

    private Button btnPublish;
   // private Button btnSwitchCamera;
    private ImageView btnSwitchCamera;
    private Toolbar mToolbar;

  //  private Button btnRecord;
  //  private Button btnSwitchEncoder;

    private SrsPublisher mPublisher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.go_broad);

        // response screen rotation event
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);


        //2、通过Resources获取
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int heigth = dm.heightPixels;
        int width = dm.widthPixels;


        btnPublish = (Button) findViewById(R.id.publish);
        btnSwitchCamera = (ImageView) findViewById(R.id.swCam);

      //  btnRecord = (Button) findViewById(R.id.record);
      //  btnSwitchEncoder = (Button) findViewById(R.id.swEnc);


        mPublisher = new SrsPublisher((SrsCameraView) findViewById(R.id.glsurfaceview_camera));
        //设置编码消息处理
        mPublisher.setEncodeHandler(new SrsEncodeHandler(this));
        //设置RTMP消息处理
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        //设置记录消息处理
        mPublisher.setRecordHandler(new SrsRecordHandler(this));
        //设置展示界面大小
        mPublisher.setPreviewResolution(width/2, heigth/2);
        //设置横屏推流 1为竖屏 2为横屏
        mPublisher.setScreenOrientation(1);
        //设置输出界面大小
        mPublisher.setOutputResolution(width/2, heigth/2);
        //设置视频高清模式
        mPublisher.setVideoHDMode();
        //打开摄像头
        mPublisher.startCamera();


        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnPublish.getText().toString().contentEquals("Push")) {
                    mPublisher.startPublish("rtmp://39.105.213.41:1935/live/1233");
                    mPublisher.startCamera();
                    Tip.showTip(GoBroadActivity.this,"开始直播");

//                    if (btnSwitchEncoder.getText().toString().contentEquals("soft encoder")) {
//                        Toast.makeText(getApplicationContext(), "Use hard encoder", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Use soft encoder", Toast.LENGTH_SHORT).show();
//                    }
                    btnPublish.setText("Stop");
//                    btnSwitchEncoder.setEnabled(false);
                } else if (btnPublish.getText().toString().contentEquals("Stop")) {
                    mPublisher.stopPublish();
                    mPublisher.stopRecord();
                    btnPublish.setText("Push");
                    Tip.showTip(GoBroadActivity.this,"停止直播");
//                    btnRecord.setText("record");
//                    btnSwitchEncoder.setEnabled(true);
                }
            }
        });

        btnSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPublisher.switchCameraFace((mPublisher.getCamraId() + 1) % Camera.getNumberOfCameras());
            }
        });

//        btnSwitchEncoder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (btnSwitchEncoder.getText().toString().contentEquals("soft encoder")) {
//                    mPublisher.switchToSoftEncoder();
//                    btnSwitchEncoder.setText("hard encoder");
//                } else if (btnSwitchEncoder.getText().toString().contentEquals("hard encoder")) {
//                    mPublisher.switchToHardEncoder();
//                    btnSwitchEncoder.setText("soft encoder");
//                }
//            }
//        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        } else {
//            switch (id) {
//                case R.id.cool_filter:
//                    mPublisher.switchCameraFilter(MagicFilterType.COOL);
//                    break;
//                case R.id.beauty_filter:
//                    mPublisher.switchCameraFilter(MagicFilterType.BEAUTY);
//                    break;
//                case R.id.early_bird_filter:
//                    mPublisher.switchCameraFilter(MagicFilterType.EARLYBIRD);
//                    break;
//                case R.id.evergreen_filter:
//                    mPublisher.switchCameraFilter(MagicFilterType.EVERGREEN);
//                    break;
//                case R.id.n1977_filter:
//                    mPublisher.switchCameraFilter(MagicFilterType.N1977);
//                    break;
//                case R.id.nostalgia_filter:
//                    mPublisher.switchCameraFilter(MagicFilterType.NOSTALGIA);
//                    break;
//                case R.id.romance_filter:
//                    mPublisher.switchCameraFilter(MagicFilterType.ROMANCE);
//                    break;
//                case R.id.sunrise_filter:
//                    mPublisher.switchCameraFilter(MagicFilterType.SUNRISE);
//                    break;
//                case R.id.sunset_filter:
//                    mPublisher.switchCameraFilter(MagicFilterType.SUNSET);
//                    break;
//                case R.id.tender_filter:
//                    mPublisher.switchCameraFilter(MagicFilterType.TENDER);
//                    break;
//                case R.id.toast_filter:
//                    mPublisher.switchCameraFilter(MagicFilterType.TOASTER2);
//                    break;
//                case R.id.valencia_filter:
//                    mPublisher.switchCameraFilter(MagicFilterType.VALENCIA);
//                    break;
//                case R.id.walden_filter:
//                    mPublisher.switchCameraFilter(MagicFilterType.WALDEN);
//                    break;
//                case R.id.warm_filter:
//                    mPublisher.switchCameraFilter(MagicFilterType.WARM);
//                    break;
//                case R.id.original_filter:
//                default:
//                    mPublisher.switchCameraFilter(MagicFilterType.NONE);
//                    break;
//            }
//        }
//        setTitle(item.getTitle());
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        final Button btn = (Button) findViewById(R.id.publish);
        btn.setEnabled(true);
        mPublisher.resumeRecord();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPublisher.pauseRecord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPublisher.stopPublish();
        mPublisher.stopRecord();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPublisher.stopEncode();
        mPublisher.stopRecord();
    //    btnRecord.setText("record");
        mPublisher.setScreenOrientation(newConfig.orientation);
        if (btnPublish.getText().toString().contentEquals("stop")) {
            mPublisher.startEncode();
        }
        mPublisher.startCamera();
    }

    private static String getRandomAlphaString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    private static String getRandomAlphaDigitString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    private void handleException(Exception e) {
        try {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            mPublisher.stopPublish();
            mPublisher.stopRecord();
            btnPublish.setText("publish");
            //btnRecord.setText("record");
           // btnSwitchEncoder.setEnabled(true);
        } catch (Exception e1) {
            //
        }
    }

    // Implementation of SrsRtmpListener.

    @Override
    public void onRtmpConnecting(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpConnected(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpVideoStreaming() {
    }

    @Override
    public void onRtmpAudioStreaming() {
    }

    @Override
    public void onRtmpStopped() {
        Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpDisconnected() {
        Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRtmpVideoFpsChanged(double fps) {
        Log.i(TAG, String.format("Output Fps: %f", fps));
    }

    @Override
    public void onRtmpVideoBitrateChanged(double bitrate) {
        int rate = (int) bitrate;
        if (rate / 1000 > 0) {
            Log.i(TAG, String.format("Video bitrate: %f kbps", bitrate / 1000));
        } else {
            Log.i(TAG, String.format("Video bitrate: %d bps", rate));
        }
    }

    @Override
    public void onRtmpAudioBitrateChanged(double bitrate) {
        int rate = (int) bitrate;
        if (rate / 1000 > 0) {
            Log.i(TAG, String.format("Audio bitrate: %f kbps", bitrate / 1000));
        } else {
            Log.i(TAG, String.format("Audio bitrate: %d bps", rate));
        }
    }

    @Override
    public void onRtmpSocketException(SocketException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIOException(IOException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    @Override
    public void onRtmpIllegalStateException(IllegalStateException e) {
        handleException(e);
    }

    // Implementation of SrsRecordHandler.

    @Override
    public void onRecordPause() {
        Toast.makeText(getApplicationContext(), "Record paused", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordResume() {
        Toast.makeText(getApplicationContext(), "Record resumed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordStarted(String msg) {
        Toast.makeText(getApplicationContext(), "Recording file: " + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordFinished(String msg) {
        Toast.makeText(getApplicationContext(), "MP4 file saved: " + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecordIOException(IOException e) {
        handleException(e);
    }

    @Override
    public void onRecordIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    // Implementation of SrsEncodeHandler.

    @Override
    public void onNetworkWeak() {
        Toast.makeText(getApplicationContext(), "Network weak", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkResume() {
        Toast.makeText(getApplicationContext(), "Network resume", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }
}
