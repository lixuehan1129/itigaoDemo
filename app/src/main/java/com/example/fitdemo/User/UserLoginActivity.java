package com.example.fitdemo.User;

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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.fitdemo.AutoProject.AppConstants;
import com.example.fitdemo.AutoProject.JDBCTools;
import com.example.fitdemo.AutoProject.SharePreferences;
import com.example.fitdemo.AutoProject.Tip;
import com.example.fitdemo.Database.DataBaseHelper;
import com.example.fitdemo.MainActivity;
import com.example.fitdemo.R;
import com.example.fitdemo.Utils.PermissionUtils;
import com.example.fitdemo.Utils.PhoneUtils;
import com.example.fitdemo.Utils.StatusBarUtils;
import com.mysql.jdbc.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cn.smssdk.SMSSDK;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 最美人间四月天 on 2018/12/8.
 */

public class UserLoginActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;
    private TextInputLayout name_l, password_l;
    private TextInputEditText name, password;
    private Button register, forget, login;
    private ImageView imageView;


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
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PhoneUtils.isMobileNO(name.getText().toString()) &&
                        name.getText().toString().length() > 5){
                    login();
                }else {
                    Tip.showTip(UserLoginActivity.this,"手机号或密码错误");
                }
            }
        });
    }

    private void login(){
        final ProgressDialog progressDialog = ProgressDialog.show(UserLoginActivity.this,"","正在登录...",true);
        new Thread(){
            public void run(){
                Looper.prepare();//用于toast
                try{
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        //根据手机号查找数据库
                        Statement stmt = conn.createStatement(); //根据返回的Connection对象创建 Statement对象
                        String sql = "SELECT * FROM user WHERE user_phone = '" +
                                name.getText().toString() +
                                "'";
                        ResultSet resultSet = stmt.executeQuery(sql);
                        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
                        String delete = "delete from user where user_phone ='" +
                                name.getText().toString() +
                                "'";
                        sqLiteDatabase.execSQL(delete);
                        if(resultSet.next()){
                            //判断输入的密码和数据库是否匹配
                            String user_password = resultSet.getString("user_password");
                            if(user_password.equals(password.getText().toString())){
                                //密码正确，记录手机号和密码
                                ContentValues values = new ContentValues();
                                SharePreferences.putString(UserLoginActivity.this, AppConstants.USER_PHONE, name.getText().toString());
                                SharePreferences.putString(UserLoginActivity.this, AppConstants.USER_PASSWORD, password.getText().toString());
                                String phone = resultSet.getString("user_phone");
                                String name = resultSet.getString("user_name");
                                String picture = resultSet.getString("user_picture");
                                int sex = resultSet.getInt("user_sex");
                                int sort = resultSet.getInt("user_sort");
                                values.put("user_name",name);
                                values.put("user_phone",phone);
                                values.put("user_sort",sort);
                                values.put("user_sex",sex);
                                values.put("user_picture",picture);
                                sqLiteDatabase.insert("user",null,values);
                                sqLiteDatabase.close();
                                resultSet.close();
                                JDBCTools.releaseConnection(stmt,conn);
                                progressDialog.dismiss();
                                Intent intent = new Intent(UserLoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            }else {
                                Tip.showTip(UserLoginActivity.this,"密码错误");
                                progressDialog.dismiss();
                            }
                        }else {
                            sqLiteDatabase.close();
                            Tip.showTip(UserLoginActivity.this,"手机号不存在");
                            progressDialog.dismiss();
                        }
                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Tip.showTip(UserLoginActivity.this,"请检查网络");
                        progressDialog.dismiss();
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
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
