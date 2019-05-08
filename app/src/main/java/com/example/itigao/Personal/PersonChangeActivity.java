package com.example.itigao.Personal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.DealBitmap;
import com.example.itigao.AutoProject.JsonCode;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.AutoProject.Tip;
import com.example.itigao.Database.DataBaseHelper;
import com.example.itigao.R;
import com.example.itigao.Utils.StatusBarUtils;
import com.example.itigao.okHttp.OkHttpBase;
import com.example.itigao.okHttp.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.example.itigao.AutoProject.AbsolutePath.getImageAbsolutePath;

/**
 * Created by 最美人间四月天 on 2018/12/15.
 */

public class PersonChangeActivity extends AppCompatActivity{

    public static final MediaType FORM_CONTENT_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private DataBaseHelper dataBaseHelper;
    private Uri photoUri;
    private String picPath = "http://ty.tipass.com/images/head/head_name(2).PNG";
    private String phone;

    private ProgressDialog progressDialog;

    private RelativeLayout relativeLayout1,relativeLayout2,relativeLayout3,relativeLayout4,relativeLayout5;
    private CircleImageView circleImageView;
    private EditText editText;
    private TextView ok,sta,textView1,textView2,birth;

    private int userLevel,userSta,userSex,anchorId;
    private String userName,userPicture,userBirth;
    private boolean picIs;
    private String picImagePath = null; //这是拍照的照片地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_change_activity);
        StatusBarUtils.setWindowStatusBarColor(PersonChangeActivity.this, R.color.colorWhite);
        Intent intent = getIntent();
        userLevel = intent.getIntExtra("userLevel",1);
        userSta = intent.getIntExtra("userSta",0);
        phone = SharePreferences.getString(PersonChangeActivity.this,AppConstants.USER_PHONE);
        anchorId = intent.getIntExtra("userGoBid",0);

        initView();
    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.person_change_mainTool);
        toolbar.setTitle("个人信息");
        back(toolbar);

        relativeLayout1 = (RelativeLayout) findViewById(R.id.person_change_rv1);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.person_change_rv2);
        relativeLayout3 = (RelativeLayout) findViewById(R.id.person_change_rv3);
        relativeLayout4 = (RelativeLayout) findViewById(R.id.person_change_rv4);
        relativeLayout5 = (RelativeLayout) findViewById(R.id.person_change_rv5);
        circleImageView = (CircleImageView) findViewById(R.id.person_change_ci);
        ok = (TextView) findViewById(R.id.person_change_ok);
        sta = (TextView) findViewById(R.id.person_change_sta);
        editText = (EditText) findViewById(R.id.person_change_name);
        textView1 = (TextView) findViewById(R.id.person_change_sex); //性别
        textView2 = (TextView) findViewById(R.id.person_change_level); //等级
        birth = (TextView) findViewById(R.id.person_change_bir);

        picIs = false;

        editText.setCursorVisible(false);

        dataBaseHelper = new DataBaseHelper(PersonChangeActivity.this,AppConstants.SQL_VISION);


        localData();
        onClick();

    }

    private void localData(){
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("user",null,"user_phone = ?",new String[]{
                 phone
        },null,null,null,"1");
        if(cursor.moveToFirst()){
            userName = cursor.getString(cursor.getColumnIndex("user_name"));
            userPicture = cursor.getString(cursor.getColumnIndex("user_picture"));
            userSex = cursor.getInt(cursor.getColumnIndex("user_sex"));
            userBirth = cursor.getString(cursor.getColumnIndex("user_birth"));
        }
        picPath = userPicture;
        cursor.close();
        sqLiteDatabase.close();


        editText.setText(userName);
        if(!TextUtils.isEmpty(userBirth)){
            birth.setText(userBirth);
        }else {
            birth.setText("1990-01-01");
        }
        if(Util.isOnMainThread()) {
            Glide.with(PersonChangeActivity.this)
                    .load(userPicture)
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(R.mipmap.ic_touxiang11)
                    .error(R.mipmap.ic_touxiang11)
                    .into(circleImageView);
        }
        textView1.setText(userSex == 0 ? "男":"女");

        switch (userLevel){
            case 1:textView2.setText("废铁会员1级");
                break;
            case 2:textView2.setText("废铁会员2级");
                break;
            case 3:textView2.setText("黄金会员1级");
                break;
            case 4:textView2.setText("黄金会员2级");
                break;
            case 5:textView2.setText("钻石会员1级");
                break;
            case 6:textView2.setText("钻石会员2级");
                break;
            case 7:textView2.setText("超级会员1级");
                break;
            default:textView2.setText("废铁会员1级");
                break;
        }

        sta.setText(userSta == 0 ? "普通会员":"正式主播  : " + "房间号（" + anchorId + ")");
    }


    private void onClick(){
        //修改头像
        relativeLayout1.setOnClickListener(view -> {
            final String[] items = new String[] {"选择图片","拍摄图片"};
            AlertDialog.Builder builder = new AlertDialog.Builder(PersonChangeActivity.this);
            builder.setItems(items, (dialogInterface, i) -> {
                if(i == 0){
                    select_photo();
                }else if(i == 1){
                    take_photo();
                }
            }).create().show();
        });

        //修改昵称
        relativeLayout2.setOnClickListener(view -> {
            editText.setCursorVisible(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
        });
        //修改昵称，同上
        editText.setOnClickListener(view -> {
            editText.setCursorVisible(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
        });

        //修改性别
        relativeLayout3.setOnClickListener(view -> {
            final String[] items = new String[] {"男","女"};
            AlertDialog.Builder builder = new AlertDialog.Builder(PersonChangeActivity.this);
            builder.setItems(items, (dialogInterface, i) -> {
                userSex = i;
                textView1.setText(userSex == 0 ? "男":"女");
            }).create().show();
        });

        relativeLayout5.setOnClickListener(View ->{
            showDatePickerDialog(this,3,birth,Calendar.getInstance());
        });


        //申请主播
        if(sta.getText().toString().equals("普通会员")){
            relativeLayout4.setOnClickListener(view -> {
             //   Tip.showTip(PersonChangeActivity.this,"申请");
                showDialog();
            });
        }

        //保存
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(PersonChangeActivity.this,"","修改中...",true);
                if(picIs){
                    compressWithLs(new File(picImagePath));
                }else {
                    upload();
                }

            }
        });
    }

    private void showDialog(){
        final String[] items = new String[] { "小学", "初中", "高中", "大学", "研究生" };
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置参数
        builder.setTitle("选择后不能修改，请慎重~~~")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder1;
                        switch (which){
                            case 0:{
                                final String[] item = {"一年级", "二年级", "三年级", "四年级", "五年级", "六年级"};
                                builder1 = new AlertDialog.Builder(PersonChangeActivity.this)
                                        .setItems(item, (dialogInterface, i) -> {
                                            i = i + 1;
                                            setData(i);
                                            Tip.showTip(PersonChangeActivity.this,items[which]+item[i-1]);
                                        });
                                builder1.create().show();

                                break;
                            }
                            case 1:{
                                final String[] item = {"一年级", "二年级", "三年级"};
                                builder1 = new AlertDialog.Builder(PersonChangeActivity.this)
                                        .setItems(item, (dialogInterface, i) -> {
                                            i = i + 7;
                                            setData(i);
                                            Tip.showTip(PersonChangeActivity.this,items[which]+item[i-7]);
                                        });
                                builder1.create().show();
                                break;
                            }
                            case 2:{
                                final String[] item = {"一年级", "二年级", "三年级"};
                                builder1 = new AlertDialog.Builder(PersonChangeActivity.this)
                                        .setItems(item, (dialogInterface, i) -> {
                                            i = i + 10;
                                            setData(i);
                                            Tip.showTip(PersonChangeActivity.this,items[which]+item[i-10]);
                                        });
                                builder1.create().show();
                                break;
                            }
                            case 3:{
                                final String[] item = {"一年级", "二年级", "三年级", "四年级"};
                                builder1 = new AlertDialog.Builder(PersonChangeActivity.this)
                                        .setItems(item, (dialogInterface, i) -> {
                                            i = i + 13;
                                            setData(i);
                                            Tip.showTip(PersonChangeActivity.this,items[which]+item[i-13]);
                                        });
                                builder1.create().show();
                                break;
                            }
                            case 4:{
                                final String[] item = {"一年级", "二年级", "三年级"};
                                builder1 = new AlertDialog.Builder(PersonChangeActivity.this)
                                        .setItems(item, (dialogInterface, i) -> {
                                            i = i + 17;
                                            setData(i);
                                            Tip.showTip(PersonChangeActivity.this,items[which]+item[i-17]);
                                        });
                                builder1.create().show();
                                break;
                            }
                        }

                    }
                });
        builder.create().show();
    }



    @SuppressLint("SetTextI18n")
    private void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity
                ,  themeResId
                // 绑定监听器(How the parent is notified that the date is set.)
                , (view, year, monthOfYear, dayOfMonth) -> {
                    // 此处得到选择的时间，可以进行你想要的操作

                    monthOfYear = monthOfYear + 1;
                    tv.setText(year + "-" + monthOfYear
                            + "-" + dayOfMonth);
                }
                // 设置初始日期
                ,calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH )
                ,calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void setData(int which){
        final ProgressDialog progressDialog = ProgressDialog.show(PersonChangeActivity.this,"","请稍等...",true);

        new Thread(){
            public void run(){
                Looper.prepare();
                HashMap<String,String> formParams = new HashMap<>();
                //传参
                formParams.put("anchor_classify", String.valueOf(which));
                formParams.put("anchor_phone", phone);
                formParams.put("anchor_name", userName);
                formParams.put("anchor_cover", picPath);

                StringBuffer sb = new StringBuffer();
                for (String key: formParams.keySet()) {
                    sb.append(key+"="+formParams.get(key)+"&");
                }

                RequestBody requestBody = RequestBody.create(FORM_CONTENT_TYPE, sb.toString());
                String regData = OkHttpBase.getResponse(requestBody,"toAnchor");
                if(regData != null){
                    if(JsonCode.getCode(regData) == 200){
                        Message message = new Message();
                        message.what = 101;
                        handler.sendMessage(message);

                        Tip.showTip(PersonChangeActivity.this,"修改成功");
                        progressDialog.dismiss();
                    }else {
                        Tip.showTip(PersonChangeActivity.this,"请稍后再试");
                        progressDialog.dismiss();
                    }
                }else {
                    Tip.showTip(PersonChangeActivity.this,"请稍后再试");
                    progressDialog.dismiss();
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
                case 101:{
                    sta.setText("正式主播");
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });

    private void upload(){
        new Thread(){
            public void run(){
                HashMap<String,String> formParams = new HashMap<>();
                //传参
                formParams.put("user_phone", phone);
                formParams.put("user_name", editText.getText().toString());
                formParams.put("user_sex", String.valueOf(userSex));
                formParams.put("user_picture", picPath);
                formParams.put("user_sort", String.valueOf(userSta));
                formParams.put("user_birth", birth.getText().toString());

                StringBuffer sb = new StringBuffer();
                for (String key: formParams.keySet()) {
                    sb.append(key+"="+formParams.get(key)+"&");
                }

                RequestBody requestBody = RequestBody.create(FORM_CONTENT_TYPE, sb.toString());
                String regData = OkHttpBase.getResponse(requestBody,"userUpdate");
                if(regData != null){
                    if(JsonCode.getCode(regData) == 200){
                        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("user_name",editText.getText().toString());
                        contentValues.put("user_sex",userSex);
                        contentValues.put("user_picture",picPath);
                        contentValues.put("user_sort",userSta);
                        contentValues.put("user_birth",birth.getText().toString());
                        sqLiteDatabase.update("user",contentValues,"user_phone = ?",new String[]{phone});
                        sqLiteDatabase.close();

                        Intent intent_broad = new Intent(AppConstants.BROAD_CHANGE);
                        LocalBroadcastManager.getInstance(PersonChangeActivity.this).sendBroadcast(intent_broad);

                        Im();
                    }else {
                        progressDialog.dismiss();
                    }
                }else {
                    progressDialog.dismiss();
                }
            }
        }.start();
    }


    //调用相机拍照
    private void take_photo(){
        // 获取 SD 卡根目录
        String saveDir = Environment.getExternalStorageDirectory() + "/com.example.itigao/";
        // 新建目录
        File dir = new File(saveDir);
        if (! dir.exists()) {
            dir.mkdirs();
        }
        // 生成文件名
        SimpleDateFormat t = new SimpleDateFormat("yyyyMMddssSSS");
        String filename = "IMG_" + (t.format(new Date())) + ".jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoUri = Uri.fromFile(new File(saveDir + filename));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        System.out.println(photoUri);
        startActivityForResult(intent, AppConstants.CAMERA);
    }
    //调用系统相册
    private void select_photo(){
        Intent intent = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, AppConstants.ALBUM);
    }

    //从拍照或相册获取图片
    //找到图片路径
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppConstants.CAMERA:{
                Uri uri = null;
                if (data != null && data.getData() != null) {
                    uri = data.getData();
                }
                if (uri == null) {
                    if (photoUri != null) {
                        uri = photoUri;
                    }
                }
                System.out.println(uri);
                showImage(DealBitmap.getRealFilePath(PersonChangeActivity.this,uri));
                break;
            }
            case AppConstants.ALBUM:
                if (data != null) {
                    Uri uri = data.getData();
                    String imagePath;
                    imagePath = getImageAbsolutePath(this, uri);
                    showImage(imagePath);
                }
                break;
        }
    }

    /*
  * 加载更换头像
  * */
    private void showImage(String imagePath){
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        // compressWithLs(new File(imagePath));
        picIs = true;
        picImagePath = imagePath;
        circleImageView.setImageBitmap(bm);
    }

    /**
     * 压缩单张图片 Listener 方式
     */
    private void compressWithLs(File file) {
        Luban.with(this)
                .load(file)
                .ignoreBy(100)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        //  picPath = file.getAbsolutePath();
                        //  Log.i("path", picPath);
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
                    if(JsonCode.getCode(result) == 200) {
                        String jsonData = JsonCode.getData(result);
                        picPath = jsonData;
                        upload();
                    }else {
                        progressDialog.dismiss();
                        Tip.showTip(PersonChangeActivity.this,"上传失败");
                    }
                }
            }
        }, file);
    }


    private void Im(){
        UserInfo userInfo = JMessageClient.getMyInfo();
        userInfo.setNickname(editText.getText().toString());
        UserInfo.Field updateField = UserInfo.Field.nickname;

        JMessageClient.updateMyInfo(updateField, userInfo, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                progressDialog.dismiss();
                finish();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Util.isOnMainThread()) {
            Glide.with(getApplicationContext()).pauseRequests();
        }
    }
}
