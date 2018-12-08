package com.example.fitdemo.User;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitdemo.R;
import com.example.fitdemo.Utils.StatusBarUtils;

/**
 * Created by 最美人间四月天 on 2018/12/8.
 */

public class UserRegistActivity extends AppCompatActivity{

    boolean hasFocus_pre_password = false;
    boolean hasFocus_pre_password_again = false;

    private TextInputLayout name_l, password_l, again_1, yan_1;
    private TextInputEditText name, password, again, yan;
    private Button register;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_regist);
        StatusBarUtils.setWindowStatusBarColor(UserRegistActivity.this, R.color.colorWhite);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //不弹出输入法
        initView();
        problem_jiaodian();
    }

    private void initView(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.user_regist_mainTool);
        back(toolbar);

        name_l = (TextInputLayout)findViewById(R.id.user_regist_number_layout);
        password_l = (TextInputLayout)findViewById(R.id.user_regist_password_layout);
        again_1 = (TextInputLayout)findViewById(R.id.user_regist_again_layout);
        yan_1 = (TextInputLayout)findViewById(R.id.user_regist_yan_layout);
        name = (TextInputEditText) findViewById(R.id.user_regist_number);
        password = (TextInputEditText) findViewById(R.id.user_regist_password);
        again = (TextInputEditText) findViewById(R.id.user_regist_again);
        yan = (TextInputEditText) findViewById(R.id.user_regist_yan);

        register = (Button) findViewById(R.id.user_regist_button);
        textView = (TextView) findViewById(R.id.user_regist_get);

        initEdit();
        upload();
    }




    private void upload(){
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserRegistActivity.this, "获取验证码", Toast.LENGTH_SHORT).show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(name.getText().toString().length() == 11  && password.getText().toString().length()  > 5){
                    if(password.getText().toString().equals(again.getText().toString()) ){
                        Toast.makeText(UserRegistActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast toast=Toast.makeText(UserRegistActivity.this, "两次密码不相同", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }else {
                    Toast toast=Toast.makeText(UserRegistActivity.this, "手机号，密码格式不正确", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
    }



    private void initEdit(){

        name_l.setCounterEnabled(true);  //设置可以计数
        name_l.setCounterMaxLength(11); //计数的最大值

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus_pre_password==false && hasFocus){
                    ((ScrollView)findViewById(R.id.register_scrollview)).fullScroll(ScrollView.FOCUS_DOWN);
                    hasFocus_pre_password = hasFocus;
                    password.requestFocus();
                }
            }
        });

        again.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus_pre_password_again==false && hasFocus){
                    ((ScrollView)findViewById(R.id.register_scrollview)).fullScroll(ScrollView.FOCUS_DOWN);
                    hasFocus_pre_password_again = hasFocus;
                    again.requestFocus();
                }
            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((ScrollView)findViewById(R.id.register_scrollview)).fullScroll(ScrollView.FOCUS_DOWN);
                password.requestFocus();
                return false;
            }
        });

        again.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((ScrollView)findViewById(R.id.register_scrollview)).fullScroll(ScrollView.FOCUS_DOWN);
                again.requestFocus();
                return false;
            }
        });

        /*
        * 手机号
        * */
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
                String number = name.getText().toString();
                if(number.length()<1){
                    name_l.setError("手机号");
                }
            }
        });

        /*
        * 密码输入监听
        * */
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
                String password1 = password.getText().toString();
                String password_again = again.getText().toString();
                if(password.length()>5){
                    if(password_again.equals(password1)){
                        again_1.setErrorEnabled(false);
                    }else {
                        if(password_again.length()>0)
                        {
                            again_1.setError("两次密码输入不一致");
                        }
                    }
                }else {
                    password_l.setError("密码错误(不少于6位)");
                }
            }
        });

        /*
        * 再次输入密码监听
        * */
        again.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                again_1.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
                String password1 = password.getText().toString();
                String password_again = password.getText().toString();
                if(!password_again.equals(password1)){
                    password_l.setError("两次密码输入不一致");
                }
            }
        });

        yan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus_pre_password_again==false && hasFocus){
                    ((ScrollView)findViewById(R.id.register_scrollview)).fullScroll(ScrollView.FOCUS_DOWN);
                    hasFocus_pre_password_again = hasFocus;
                    yan.requestFocus();
                }
            }
        });

        yan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((ScrollView)findViewById(R.id.register_scrollview)).fullScroll(ScrollView.FOCUS_DOWN);
                yan.requestFocus();
                return false;
            }
        });

        yan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                yan_1.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
                String number = yan.getText().toString();
                if(number.length()<1){
                    yan_1.setError("验证码");
                }
            }
        });

    }

    /*
  * 点击空白区域 Edittext失去焦点 关闭输入法
  * */
    private void problem_jiaodian() {
        final ScrollView scrollView = (ScrollView) findViewById(R.id.register_scrollview);
        scrollView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                return false;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
