package com.example.itigao.HuDong;

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
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.DealBitmap;
import com.example.itigao.AutoProject.JsonCode;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.AutoProject.Tip;
import com.example.itigao.Personal.PersonChangeActivity;
import com.example.itigao.R;
import com.example.itigao.Utils.StatusBarUtils;
import com.example.itigao.okHttp.OkHttpBase;
import com.example.itigao.okHttp.OkHttpUtil;
import com.iceteck.silicompressorr.VideoCompress;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by 最美人间四月天 on 2018/12/26.
 */

public class HuDongPutActivity extends AppCompatActivity {

    public static final MediaType FORM_CONTENT_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

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
                    System.out.println("地址"+filePath);
                    bitmap = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);

                    jzvdStd.fullscreenButton.setVisibility(View.INVISIBLE);
                    jzvdStd.fullscreenButton.setOnClickListener(null);
                    jzvdStd.thumbImageView.setImageBitmap(bitmap);
                    jzvdStd.setUp(filePath,"", Jzvd.SCREEN_WINDOW_NORMAL);

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
            VideoCompress.compressVideoLow(filePath, createPath, new VideoCompress.CompressListener() {
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
        String postUrl = "http://tg01.tipass.com/Study/tp5/public/index.php?s=study/Study/uploadFile";

        OkHttpUtil.postFile(postUrl, (currentBytes, contentLength, done) -> {
            Log.i("上传视频", "currentBytes==" + currentBytes + "==contentLength==" + contentLength + "==done==" + done);
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
                    Log.i("video", "result===" + result);
                    if(JsonCode.getCode(result) == 200) {
                        String jsonData = JsonCode.getData(result);
                        pathVideo = jsonData;
                        upLoadPic();
                    }else {
                        Tip.showTip(HuDongPutActivity.this,"上传失败");
                        progressDialog.dismiss();
                    }

                }
            }
        }, file);
    }


    private void upLoadPic(){
        if(bitmap != null){
            String picPath = createPicPath();
            File file = saveBitmapFile(bitmap,picPath);
            if(file.exists()){
                compressWithLs(file);
            }
        }else {
            progressDialog.dismiss();
        }

    }

    /**
     * 压缩单张图片 Listener 方式
     */
    private void compressWithLs(File file) {
        Luban.with(this)
                .load(file)
                .ignoreBy(100)
                .filter(path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")))
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        upPic(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        progressDialog.dismiss();
                    }
                }).launch();
    }

    private void upPic(File file){
        String postUrl = "http://tg01.tipass.com/Study/tp5/public/index.php?s=study/Study/uploadFile";

        OkHttpUtil.postFile(postUrl, (currentBytes, contentLength, done) -> {
            Log.i("图片", "currentBytes==" + currentBytes + "==contentLength==" + contentLength + "==done==" + done);
        }, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("上传失败" + call.toString() + e);
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
                    String result = response.body().string();
                    Log.i("pic", "result===" + result);
                    if(JsonCode.getCode(result) == 200) {
                        String jsonData = JsonCode.getData(result);
                        pathPic = jsonData;
                        if(pathPic != null && pathVideo != null){
                            updateHu();
                        }
                    }else {
                        progressDialog.dismiss();
                        Tip.showTip(HuDongPutActivity.this,"上传失败");
                    }

                }
            }
        }, file);
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
                Looper.prepare();
                HashMap<String,String> formParams = new HashMap<>();
                //传参
                formParams.put("hudong_user", name);
                formParams.put("hudong_classify", String.valueOf(hudong_classify));
                formParams.put("hudong_name", finalTitle);
                formParams.put("hudong_content", finalContent);
                formParams.put("hudong_cover", pathPic);
                formParams.put("hudong_add", pathVideo);
                StringBuffer sb = new StringBuffer();
                for (String key: formParams.keySet()) {
                    sb.append(key+"="+formParams.get(key)+"&");
                }

                RequestBody requestBody = RequestBody.create(FORM_CONTENT_TYPE, sb.toString());
                String regData = OkHttpBase.getResponse(requestBody,"huDongPut");
                if(regData != null){
                    Tip.showTip(HuDongPutActivity.this,"上传成功");
                    progressDialog.dismiss();
                    finish();
                }else {
                    Tip.showTip(HuDongPutActivity.this,"上传失败，请稍后再试");
                    progressDialog.dismiss();
                }
                Looper.loop();
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
        Bitmap bitmap1 = DealBitmap.cropBitmap(bitmap);
        File file = new File(filepath);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    private String createPath(){
        String saveDir = Environment.getExternalStorageDirectory() + "/com.example.itigao/video/";
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
        String saveDir = Environment.getExternalStorageDirectory() + "/com.example.itigao/Vpic/";
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
