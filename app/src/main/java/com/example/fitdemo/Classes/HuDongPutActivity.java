package com.example.fitdemo.Classes;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fitdemo.AutoProject.AppConstants;
import com.example.fitdemo.AutoProject.JDBCTools;
import com.example.fitdemo.AutoProject.SharePreferences;
import com.example.fitdemo.AutoProject.Tip;
import com.example.fitdemo.Personal.PersonChangeActivity;
import com.example.fitdemo.R;
import com.example.fitdemo.Utils.StatusBarUtils;
import com.example.fitdemo.okHttp.OkHttpUtil;
import com.example.fitdemo.okHttp.ProgressListener;
import com.iceteck.silicompressorr.VideoCompress;
import com.mysql.jdbc.Connection;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.validation.TypeInfoProvider;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 最美人间四月天 on 2018/12/26.
 */

public class HuDongPutActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private TextView button;
    private EditText editTextTitle,editTextContent;
    private JzvdStd jzvdStd;
    private int hudong_classify;
    private String pathVideo,pathPic;

    private String name;

    private Bitmap bitmap = null;

    private  String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hudong_put);
        StatusBarUtils.setWindowStatusBarColor(HuDongPutActivity.this, R.color.colorWhite);
        Intent intent = getIntent();
        hudong_classify = intent.getIntExtra("hudong_ccc",0);
        initView();
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.hudong_put_mainTool);
        toolbar.setTitle("互动天地");
        back(toolbar);
        button = (TextView) findViewById(R.id.hudong_put_ok);
        jzvdStd = (JzvdStd) findViewById(R.id.hudong_put_iv);
        editTextTitle = (EditText) findViewById(R.id.hudong_put_title);
        editTextContent = (EditText) findViewById(R.id.hudong_put_content);

        name = SharePreferences.getString(HuDongPutActivity.this,AppConstants.USER_PHONE);


        //获取屏幕宽
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);

        assert wm != null;
        int width = wm.getDefaultDisplay().getWidth();
        setWidthHeightWithRatio(jzvdStd,width,16,9);

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
                    bitmap = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
                    // 图片Bitmap转file
                 //   imageView.setImageBitmap(bitmap);

                    jzvdStd.fullscreenButton.setVisibility(View.INVISIBLE);
                    jzvdStd.fullscreenButton.setOnClickListener(null);
                    jzvdStd.thumbImageView.setImageBitmap(bitmap);
                    jzvdStd.setUp(filePath,"", Jzvd.SCREEN_WINDOW_NORMAL);


                  //  File file = CommonUtils.compressImage(bitmap);
                    // 保存成功后插入到图库，其中的file是保存成功后的图片path。这里只是插入单张图片
                    // 通过发送广播将视频和图片插入相册
//                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                    cursor.close();
                }
            }
        }else {
            finish();
        }
    }

    private void setOnClick(){
        final String createPath = createPath();
        button.setOnClickListener(view -> {
            VideoCompress.compressVideoMedium(filePath, createPath, new VideoCompress.CompressListener() {
                @Override
                public void onStart() {
                    progressDialog = ProgressDialog.show(HuDongPutActivity.this,"","请稍等...",true);
                    System.out.println("开始");
                }

                @Override
                public void onSuccess() {
                    System.out.println("成功");
                    upLoadVideo(createPath);
                }

                @Override
                public void onFail() {
                    System.out.println("失败");
                    progressDialog.dismiss();
                }

                @Override
                public void onProgress(float percent) {

                }
            });
        });
    }

    private void upLoadVideo(String path){
        File file = new File(path);
        String postUrl = "http://39.105.213.41:8080/upLoadVideo/UploadFileServlet";

        OkHttpUtil.postFile(postUrl, (currentBytes, contentLength, done) -> {
            Log.i("看一下", "currentBytes==" + currentBytes + "==contentLength==" + contentLength + "==done==" + done);
            int progress = (int) (currentBytes * 100 / contentLength);
//                post_progress.setProgress(progress);
//                post_text.setText(progress + "%");
        }, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
                System.out.println("开始上传失败" + call.toString() + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
                    String result = response.body().string();
                    Log.i("再看一下", "result===" + result);
                    pathVideo = result;
                    upLoadPic();
                }
            }
        }, file);
    }

    private void upLoadPic(){
        if(bitmap != null){
            String picPath = createPicPath();
            File file = saveBitmapFile(bitmap,picPath);
            if(file.exists()){
                String postUrl = "http://39.105.213.41:8080/upLoadVideo/UploadFileServlet";

                OkHttpUtil.postFile(postUrl, (currentBytes, contentLength, done) -> {
                    Log.i("看一下图片", "currentBytes==" + currentBytes + "==contentLength==" + contentLength + "==done==" + done);
                }, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("开始上传失败" + call.toString() + e);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response != null) {
                            String result = response.body().string();
                            Log.i("再看一下", "result===" + result);
                            pathPic = result;
                            updateHu();
                        }
                    }
                }, file);
            }
        }

    }

    private void updateHu(){
        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();
        if(title.equals("")) {
            title = "暂无";
        }
        if(content.equals("")){
            content = "暂无";
        }

        String finalTitle = title;
        String finalContent = content;

        new Thread(){
            public void run(){
                try{
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null) {
                        String sql = "INSERT INTO hudong (hudong_user,hudong_classify,hudong_name,hudong_content,hudong_cover,hudong_add) VALUES (?,?,?,?,?,?)";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                        preparedStatement.setString(1,name);
                        preparedStatement.setInt(2,hudong_classify);
                        preparedStatement.setString(3, finalTitle);
                        preparedStatement.setString(4, finalContent);
                        preparedStatement.setString(5,pathPic);
                        preparedStatement.setString(6,pathVideo);
                        preparedStatement.executeUpdate();
                        preparedStatement.close();

                        Intent intent_broad = new Intent(AppConstants.BROAD_HU);
                        LocalBroadcastManager.getInstance(HuDongPutActivity.this).sendBroadcast(intent_broad);
                        progressDialog.dismiss();
                        finish();
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Tip.showTip(HuDongPutActivity.this,"请检查网络");
                }
            }
        }.start();
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

    /**
     * 把batmap 转file
     * @param bitmap
     * @param filepath
     */
    public static File saveBitmapFile(Bitmap bitmap, String filepath){
        File file = new File(filepath);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
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
        String filename = name + format.format(date) + ".mp4";
        return saveDir + filename;
    }

    private String createPicPath(){
        String saveDir = Environment.getExternalStorageDirectory() + "/com.example.fitdemo/Vpic/";
        // 新建目录
        File dir = new File(saveDir);
        if (! dir.exists()) {
            dir.mkdirs();
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = name + format.format(date) + ".png";
        return saveDir + filename;
    }

    //返回注销事件
    private void back(Toolbar toolbar){
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
