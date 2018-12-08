package com.example.fitdemo.User;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.fitdemo.MainActivity;
import com.example.fitdemo.R;
import com.example.fitdemo.Utils.PermissionUtils;
import com.example.fitdemo.Utils.StatusBarUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 最美人间四月天 on 2018/12/8.
 */

public class UserLoginActivity extends AppCompatActivity {

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

        initEditText();
        pageClick();
    }



    private void pageClick(){
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast=Toast.makeText(UserLoginActivity.this, "忘记密码", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
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
//                if(name.getText().toString().length() == 11 &&
//                        name.getText().toString().length() > 5){
//                    login();
//                }else {
//                    Toast toast = Toast.makeText(UserLoginActivity.this, "手机号或密码格式不正确", Toast.LENGTH_SHORT);
//                    toast.show();
//                }
                Intent intent = new Intent(UserLoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(){

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
