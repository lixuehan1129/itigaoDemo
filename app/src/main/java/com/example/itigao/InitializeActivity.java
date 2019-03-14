package com.example.itigao;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.User.UserLoginActivity;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by 最美人间四月天 on 2018/12/14.
 */

public class InitializeActivity extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        JMessageClient.setDebugMode(true);
        JMessageClient.init(this); //极光IM

        if(!SharePreferences.getString(InitializeActivity.this, AppConstants.USER_PHONE).isEmpty()){
            Intent intent = new Intent(InitializeActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(InitializeActivity.this,UserLoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
