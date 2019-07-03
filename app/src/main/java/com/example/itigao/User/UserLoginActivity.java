package com.example.itigao.User;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JsonCode;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.AutoProject.Tip;
import com.example.itigao.ClassAb.Register;
import com.example.itigao.Database.DataBaseHelper;
import com.example.itigao.MainActivity;
import com.example.itigao.R;
import com.example.itigao.Utils.PermissionUtils;
import com.example.itigao.Utils.PhoneUtils;
import com.example.itigao.Utils.StatusBarUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 最美人间四月天 on 2018/12/8.
 */

public class UserLoginActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;
    private TextInputLayout name_l, password_l;
    private TextInputEditText name, password;
    private Button register, forget, login;
    private ImageView imageView;
    private ProgressDialog progressDialog;

    private List<Register> registers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        StatusBarUtils.setWindowStatusBarColor(UserLoginActivity.this, R.color.colorWhite);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //不弹出输入法
        PermissionUtils.setPer(UserLoginActivity.this);//获取权限
        initView();
        problem_jiaodian();

    }

    private void initView(){

        Toolbar toolbar = (Toolbar)findViewById(R.id.user_login_mainTool);
        toolbar.setTitle("用户登录");

        imageView = (CircleImageView) findViewById(R.id.user_login_picture);
        name_l = (TextInputLayout)findViewById(R.id.user_login_name_layout);
        password_l = (TextInputLayout)findViewById(R.id.user_login_password_layout);
        name = (TextInputEditText)findViewById(R.id.user_login_name);
        password = (TextInputEditText)findViewById(R.id.user_login_password);
        forget = (Button)findViewById(R.id.user_login_forget);
        register = (Button)findViewById(R.id.user_login_regist);
        login = (Button)findViewById(R.id.user_login_login);

        dataBaseHelper = new DataBaseHelper(UserLoginActivity.this,AppConstants.SQL_VISION);

        initEditText();
        pageClick();
    }



    private void pageClick(){
        //忘记密码
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tip.showTip(UserLoginActivity.this,"忘记密码？");
            }
        });
        //注册
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLoginActivity.this,UserRegistActivity.class);
                startActivity(intent);
   //             Tip.showTip(UserLoginActivity.this,"暂时不支持注册");
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PhoneUtils.isMobileNO(name.getText().toString()) &&
                        name.getText().toString().length() > 5){
                    loginGo();
                }else {
                    Tip.showTip(UserLoginActivity.this,"手机号或密码错误");
                }
            }
        });
    }

    private void loginGo(){
        progressDialog = ProgressDialog.show(UserLoginActivity.this,"","正在登录...",true);
        new Thread(){
            public void run(){
                Looper.prepare();//用于toast
                postLogin();
                Looper.loop();
            }
        }.start();
    }

    private void postLogin() {
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //构建一个请求体 add参数1 key 参数2 value 发送字段
        RequestBody requestBody = new FormBody.Builder()
                .add("user_phone", name.getText().toString())
                .add("user_password", password.getText().toString())
                .build();
        //构建一个请求对象
        Request request = new Request.Builder()
                .url(AppConstants.URL + "login")
                .post(requestBody)
                .build();
        //发送请求获取响应
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if(response.isSuccessful()){
                //打印服务端返回结果
                assert response.body() != null;
                String regData = response.body().string();
                System.out.println("返回login"+regData);
                if(JsonCode.getCode(regData) == 200){
                    String jsonData = JsonCode.getData(regData);
                    registers = JsonCode.jsonToList(jsonData,Register.class);
                    setLocal();
                }else if(JsonCode.getCode(regData) == 300){
                    Tip.showTip(UserLoginActivity.this,"密码错误");
                    progressDialog.dismiss();
                }else {
                    Tip.showTip(UserLoginActivity.this,"手机号不存在");
                    progressDialog.dismiss();
                }

            }
        } catch (IOException e) {
            Tip.showTip(UserLoginActivity.this,"请检查网络");
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void setLocal(){
        SharePreferences.putString(UserLoginActivity.this, AppConstants.USER_PHONE, name.getText().toString());
        SharePreferences.putString(UserLoginActivity.this, AppConstants.USER_PASSWORD, password.getText().toString());
        SharePreferences.putInt(UserLoginActivity.this, AppConstants.USER_CLASSIFY, registers.get(0).getUser_classify());
        SharePreferences.putInt(UserLoginActivity.this, AppConstants.USER_LOGIN_COUNT, 0);

        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        String delete = "delete from user where user_phone ='" +
                name.getText().toString() +
                "'";
        sqLiteDatabase.execSQL(delete);

        ContentValues values = new ContentValues();
        values.put("user_name",registers.get(0).getUser_name());
        values.put("user_phone",registers.get(0).getUser_phone());
        values.put("user_sort",registers.get(0).getUser_sort());
        values.put("user_classify",registers.get(0).getUser_classify());
        values.put("user_sex",registers.get(0).getUser_sex());
        values.put("user_picture",registers.get(0).getUser_picture());
        values.put("user_birth",registers.get(0).getUser_birth());
        sqLiteDatabase.insert("user",null,values);
        sqLiteDatabase.close();


        progressDialog.dismiss();
        Intent intent = new Intent(UserLoginActivity.this,MainActivity.class);
        startActivity(intent);
    }


    private void initEditText(){
        name_l.setCounterEnabled(true);  //设置可以计数
        name_l.setCounterMaxLength(11); //计数的最大值

        name.setText(getIntent().getStringExtra("put_extra"));

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name_l.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password_l.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    ///Android按返回键，程序进入后台运行，不关闭程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //  moveTaskToBack(false);
            //方式二：返回手机的主屏幕
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /*
   * 点击空白区域 Edittext失去焦点 关闭输入法
   * */
    @SuppressLint("ClickableViewAccessibility")
    private void problem_jiaodian() {
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.edit_relativeLayout);
        relativeLayout.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                relativeLayout.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                return false;
            }
        });
    }

}
