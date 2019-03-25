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
import com.example.itigao.AutoProject.JDBCTools;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.AutoProject.Tip;
import com.example.itigao.Database.DataBaseHelper;
import com.example.itigao.R;
import com.example.itigao.Utils.StatusBarUtils;
import com.example.itigao.okHttp.OkHttpUtil;
import com.mysql.jdbc.Connection;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.example.itigao.AutoProject.AbsolutePath.getImageAbsolutePath;

/**
 * Created by 最美人间四月天 on 2018/12/15.
 */

public class PersonChangeActivity extends AppCompatActivity{

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
    }


    private void onClick(){
        //修改头像
        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = new String[] {"选择图片","拍摄图片"};
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonChangeActivity.this);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            select_photo();
                        }else if(i == 1){
                            take_photo();
                        }
                    }
                }).create().show();
            }
        });

        //修改昵称
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setCursorVisible(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
            }
        });
        //修改昵称，同上
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setCursorVisible(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
            }
        });

        //修改性别
        relativeLayout3.setOnClickListener(view -> {
            final String[] items = new String[] {"男","女"};
            AlertDialog.Builder builder = new AlertDialog.Builder(PersonChangeActivity.this);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    userSex = i;
                    textView1.setText(userSex == 0 ? "男":"女");
                }
            }).create().show();
        });

        relativeLayout5.setOnClickListener(View ->{
            showDatePickerDialog(this,3,birth,Calendar.getInstance());
        });


        //申请主播
        if(sta.getText().toString().equals("普通会员")){
            relativeLayout4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 //   Tip.showTip(PersonChangeActivity.this,"申请");
                    showDialog();
                }
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
                        setData(which+1);
                        Tip.showTip(PersonChangeActivity.this,items[which]);
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
                try {
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null) {
                        Statement stmt = conn.createStatement();
                        String sqlSize = "SELECT anchor_bid FROM anchor WHERE anchor_classify = " +
                                which +
                                " ORDER BY anchor_bid DESC LIMIT 1";
                        ResultSet resultSet = stmt.executeQuery(sqlSize);
                        int size = 0;
                        if(resultSet.first()){
                            size = resultSet.getInt("anchor_bid") + 1;
                        }

                        String sql = "SELECT room_num FROM room WHERE room_sta = 0 LIMIT 1";
                        ResultSet resultSet1 = stmt.executeQuery(sql);
                        if(resultSet1.first()){
                            int roomId = resultSet1.getInt("room_num");

                            String sql1 = "INSERT INTO anchor (anchor_classify,anchor_phone,anchor_name,anchor_bid,anchor_num,anchor_cover,anchor_state,anchor_room) VALUES (?,?,?,?,?,?,?,?)";
                            PreparedStatement preparedStatement = conn.prepareStatement(sql1,Statement.RETURN_GENERATED_KEYS);
                            preparedStatement.setInt(1,which);
                            preparedStatement.setString(2,phone);
                            preparedStatement.setString(3,userName);
                            preparedStatement.setInt(4,size);
                            preparedStatement.setInt(5,0);
                            preparedStatement.setString(6,picPath);
                            preparedStatement.setInt(7,0);
                            preparedStatement.setInt(8,roomId);
                            preparedStatement.executeUpdate();
                            preparedStatement.close();

                            String sql2 = "UPDATE room SET room_sta = ? WHERE room_num = " +
                                    roomId +
                                    "";
                            PreparedStatement preparedStatement1 = conn.prepareStatement(sql2,Statement.RETURN_GENERATED_KEYS);
                            preparedStatement1.setInt(1,1);
                            preparedStatement1.executeUpdate();
                            preparedStatement1.close();

//                            SharePreferences.remove(PersonChangeActivity.this,AppConstants.USER_sta);
//                            SharePreferences.putInt(PersonChangeActivity.this,AppConstants.USER_sta,1);
//                            SharePreferences.putInt(PersonChangeActivity.this,AppConstants.USER_STYLE,which);
//                            SharePreferences.putInt(PersonChangeActivity.this,AppConstants.USER_ID,size);

                            Message message = new Message();
                            message.what = 101;
                            handler.sendMessage(message);
                        }

                        resultSet.close();
                        resultSet1.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }
                    progressDialog.dismiss();
                }catch (SQLException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Tip.showTip(PersonChangeActivity.this,"请检查网络");
                }
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
                Looper.prepare();
                try{
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        Statement stmt = conn.createStatement();
                        String sql = "UPDATE user SET user_name = ?, user_sex = ?, user_picture = ?, user_sort = ? , user_birth = ? WHERE user_phone = '" +
                                phone +
                                "'";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql);
                        preparedStatement.setString(1,editText.getText().toString());
                        preparedStatement.setInt(2,userSex);
                        preparedStatement.setString(3,picPath);
                        preparedStatement.setInt(4,userSta);
                        preparedStatement.setString(5,birth.getText().toString());
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                        JDBCTools.releaseConnection(stmt,conn);
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
                        Tip.showTip(PersonChangeActivity.this,"请检查网络");
                        progressDialog.dismiss();
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                Looper.loop();
            }
        }.start();
    }


    //调用相机拍照
    private void take_photo(){
        // 获取 SD 卡根目录
        String saveDir = Environment.getExternalStorageDirectory() + "/com.example.fitDemo.park/";
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
                    picPath = result;
                    upload();
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
