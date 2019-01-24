package com.example.fitdemo.Classes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fitdemo.AutoProject.AppConstants;
import com.example.fitdemo.AutoProject.Tip;
import com.example.fitdemo.R;
import com.example.fitdemo.Utils.StatusBarUtils;
import com.example.fitdemo.okHttp.OkHttpUtil;
import com.example.fitdemo.okHttp.ProgressListener;
import com.iceteck.silicompressorr.VideoCompress;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.validation.TypeInfoProvider;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 最美人间四月天 on 2018/12/26.
 */

public class HuDongPutActivity extends AppCompatActivity {

    private TextView button;
    private EditText editText;
    private ImageView imageView;

    private  String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hudong_put);
        StatusBarUtils.setWindowStatusBarColor(HuDongPutActivity.this, R.color.colorWhite);
        initView();
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.hudong_put_mainTool);
        toolbar.setTitle("互动天地");
        back(toolbar);
        button = (TextView) findViewById(R.id.hudong_put_ok);
        imageView = (ImageView) findViewById(R.id.hudong_put_iv);


        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 120);
        startActivityForResult(intent, AppConstants.VIDEO);

        setOnClick();
        //
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode ==  AppConstants.VIDEO) {
                Uri uri = data.getData();
                Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
                    // 视频路径
                    filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                    // ThumbnailUtils类2.2以上可用  Todo 获取视频缩略图
                    System.out.println("地址"+filePath);
                    Bitmap bitmap = null;
                    bitmap = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MICRO_KIND);
                    // 图片Bitmap转file
                    imageView.setImageBitmap(bitmap);
                  //  File file = CommonUtils.compressImage(bitmap);
                    // 保存成功后插入到图库，其中的file是保存成功后的图片path。这里只是插入单张图片
                    // 通过发送广播将视频和图片插入相册
//                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                    cursor.close();
                }
            }
        }
    }

    private void setOnClick(){
        final String createPath = createPath();
        button.setOnClickListener(view -> {
            VideoCompress.compressVideoMedium(filePath, createPath, new VideoCompress.CompressListener() {
                @Override
                public void onStart() {
                    System.out.println("开始");
                }

                @Override
                public void onSuccess() {
                    System.out.println("成功");
                    upLoad(createPath);
                }

                @Override
                public void onFail() {
                    System.out.println("失败");
                }

                @Override
                public void onProgress(float percent) {

                }
            });
        });
    }

    private void upLoad(String path){
        File file = new File(path);
        String postUrl = "http://39.105.213.41:8080/uploadVideo/UploadFileServlet";

        OkHttpUtil.postFile(postUrl, (currentBytes, contentLength, done) -> {
            Log.i("看一下", "currentBytes==" + currentBytes + "==contentLength==" + contentLength + "==done==" + done);
            int progress = (int) (currentBytes * 100 / contentLength);
//                post_progress.setProgress(progress);
//                post_text.setText(progress + "%");
        }, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("开始上传失败" + call.toString() + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
                    String result = response.body().string();
                    Log.i("再看一下", "result===" + result);
                }
            }
        }, file);
    }

    private String createPath(){
        String saveDir = Environment.getExternalStorageDirectory() + "/com.example.fitdemo/video/";
        // 新建目录
        File dir = new File(saveDir);
        if (! dir.exists()) {
            dir.mkdirs();
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date) + ".mp4";
        return saveDir + filename;
    }

    //返回注销事件
    private void back(Toolbar toolbar){
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
