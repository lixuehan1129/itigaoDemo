package com.example.fitdemo.Classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.example.fitdemo.Adapter.ClassActivityAdapter;
import com.example.fitdemo.Adapter.ClassVideoAdapter;
import com.example.fitdemo.AutoProject.AppConstants;
import com.example.fitdemo.AutoProject.JDBCTools;
import com.example.fitdemo.AutoProject.SharePreferences;
import com.example.fitdemo.R;
import com.example.fitdemo.Subscribe.VideoPlayActivity;
import com.example.fitdemo.Utils.StatusBarUtils;
import com.example.fitdemo.AutoProject.Tip;
import com.mysql.jdbc.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/11/30.
 */

public class YogaActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.mActivity = YogaActivity.this;
        super.setmClassify(4);
        super.onCreate(savedInstanceState);
    }
}
