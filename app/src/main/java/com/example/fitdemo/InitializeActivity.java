package com.example.fitdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.fitdemo.AutoProject.AppConstants;
import com.example.fitdemo.AutoProject.SharePreferences;
import com.example.fitdemo.User.UserLoginActivity;

/**
 * Created by 最美人间四月天 on 2018/12/14.
 */

public class InitializeActivity extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(!SharePreferences.getString(InitializeActivity.this, AppConstants.USER_PHONE).isEmpty()){
            Intent intent = new Intent(InitializeActivity.this,MainActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(InitializeActivity.this,UserLoginActivity.class);
            startActivity(intent);
        }
    }
}
