package com.example.fitdemo.Classes;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fitdemo.AutoProject.AppConstants;
import com.example.fitdemo.R;
import com.example.fitdemo.Utils.StatusBarUtils;

import java.io.File;

/**
 * Created by 最美人间四月天 on 2018/12/26.
 */

public class HuDongPutActivity extends AppCompatActivity {

    private TextView button;
    private EditText editText;
    private ImageView imageView;

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
                    String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                    // ThumbnailUtils类2.2以上可用  Todo 获取视频缩略图
                    System.out.println("地址"+filePath);
                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MICRO_KIND);
                    // 图片Bitmap转file
                    imageView.setImageBitmap(bitmap);
                  //  File file = CommonUtils.compressImage(bitmap);
                    // 保存成功后插入到图库，其中的file是保存成功后的图片path。这里只是插入单张图片
                    // 通过发送广播将视频和图片插入相册
                   // sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

                    cursor.close();
                }
            }
        }
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
