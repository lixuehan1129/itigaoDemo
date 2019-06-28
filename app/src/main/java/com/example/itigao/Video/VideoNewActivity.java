package com.example.itigao.Video;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.itigao.Adapter.TabLayoutAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JsonCode;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.ClassAb.Classes;
import com.example.itigao.Emotion.fragment.BackHandleFragment;
import com.example.itigao.Emotion.fragment.BackHandleInterface;
import com.example.itigao.Media.JZMediaIjkplayer;
import com.example.itigao.R;
import com.example.itigao.Utils.StatusBarUtils;
import com.example.itigao.ViewHelper.BaseFragment;
import com.example.itigao.okHttp.OkHttpBase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static cn.jzvd.JZUtils.dip2px;

/**
 * Created by 最美人间四月天 on 2019/3/9.
 */

public class VideoNewActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,VideoFragment.CallBackValue
        ,HuDongFragment.CallBackValueHu, BackHandleInterface {

    private JzvdStd jzvdStd;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private TextView textView;


    private String[] titles = new String[]{" 聊天 ", " 视频 ", " 排行 ", " 推荐 "};
    private List<Fragment> fragments = new ArrayList<>();

    private BackHandleFragment backHandleFragment;
    private String userPhone = null;
    private String video_add;
    private int video_classify;
    private int video_bid;
    private int video_section;
    private int video_select;
    private int video_record; //0表示主页推荐内容不计，1表示课程列表加入记录，2观看记录

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_new);
        StatusBarUtils.setWindowStatusBarColor(VideoNewActivity.this, R.color.colorWhite);
        Jzvd.setMediaInterface(new JZMediaIjkplayer());
        // Jzvd.SAVE_PROGRESS = false;

        Intent intent = getIntent();
        video_add = intent.getStringExtra("video_add");
        video_bid = intent.getIntExtra("video_bid",0);
        video_section = intent.getIntExtra("video_section",0);
        video_select = intent.getIntExtra("video_select",1);
        video_record = intent.getIntExtra("video_record",1);

        video_classify = intent.getIntExtra("video_classify",1);

        userPhone = SharePreferences.getString(VideoNewActivity.this, AppConstants.USER_PHONE);

        initView();
    }

    private void initView(){
        jzvdStd = (JzvdStd) findViewById(R.id.video_new_vv);
        tabLayout = (TabLayout) findViewById(R.id.video_new_layout);
        mViewPager = (ViewPager) findViewById(R.id.video_new_viewpager);
        textView = (TextView) findViewById(R.id.video_new_go);
        textView.setClickable(false);
        //获取屏幕宽
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);

        assert wm != null;
        int width = wm.getDefaultDisplay().getWidth();
        setWidthHeightWithRatio(jzvdStd,width,16,9);
        initTab();

        setPlay(video_add);

        classFocus();

        textView.setOnClickListener(v -> {
            if(textView.getText().toString().equals(" + 收藏 ")){
                ProgressDialog progressDialog = ProgressDialog.show(VideoNewActivity.this,
                        "","",true);
                new Thread(){
                    public void run(){
                        RequestBody requestBody = new FormBody.Builder()
                                .add("classFocus_user", userPhone)
                                .add("classFocus_bid", String.valueOf(video_bid))
                                .build();
                        String reg = OkHttpBase.getResponse(requestBody,"insertClassFocus");
                        if(reg != null){
                            if(JsonCode.getCode(reg) == 200){
                                runOnUiThread(() -> {
                                    textView.setText("已收藏");
                                    textView.setBackgroundColor(getResources().getColor(R.color.colorGray_1));
                                });
                            }
                        }
                        progressDialog.dismiss();
                    }
                }.start();

            }else {
                ProgressDialog progressDialog = ProgressDialog.show(VideoNewActivity.this,
                        "","",true);
                new Thread(){
                    public void run(){
                        RequestBody requestBody = new FormBody.Builder()
                                .add("classFocus_user", userPhone)
                                .add("classFocus_bid", String.valueOf(video_bid))
                                .build();
                        String reg = OkHttpBase.getResponse(requestBody,"deleteClassFocus");
                        if(reg != null){
                            if(JsonCode.getCode(reg) == 200){
                                runOnUiThread(() -> {
                                    textView.setText(" + 收藏 ");
                                    textView.setBackgroundColor(getResources().getColor(R.color.colorOrange));
                                });
                            }
                        }
                        progressDialog.dismiss();
                    }
                }.start();

            }
        });

    }

    private void classFocus(){

        new Thread(){
            public void run(){
                System.out.println("fdsfdfs"+userPhone+String.valueOf(video_bid));
                RequestBody requestBody = new FormBody.Builder()
                        .add("classFocus_user", userPhone)
                        .add("classFocus_bid", String.valueOf(video_bid))
                        .build();
                String reg = OkHttpBase.getResponse(requestBody,"findClassFocus");
                if(reg != null){
                    if(JsonCode.getCode(reg) == 400){
                        runOnUiThread(() -> {
                            textView.setText("已收藏");
                            textView.setBackgroundColor(getResources().getColor(R.color.colorGray_1));
                            textView.setClickable(true);
                        });
                    }else {
                        runOnUiThread(() -> {
                            textView.setText(" + 收藏 ");
                            textView.setBackgroundColor(getResources().getColor(R.color.colorOrange));
                            textView.setClickable(true);
                        });
                    }
                }
            }
        }.start();

    }


    private void setPlay(String url){
        jzvdStd.setUp(url, "", Jzvd.SCREEN_WINDOW_NORMAL);
        jzvdStd.startVideo();
    }

    //要获取的值  就是这个参数的值
    @Override
    public void SendMessageValue(String strValue) {
        // TODO Auto-generated method stub
        Jzvd.releaseAllVideos();
        setPlay(strValue);
    }

    @Override
    public void SendMessageValueHu(Classes strValue) {
        Jzvd.releaseAllVideos();
        // setPlay(strValue);
        video_add = strValue.getClass_add();
        video_bid = strValue.getClass_bid();
        video_section = strValue.getClass_section();
        video_select = 1;
        video_record = 1;

        video_classify = strValue.getClass_classify();

        initView();
    }


    private void initTab(){
        tabLayout.clearOnTabSelectedListeners();
        mViewPager.clearOnPageChangeListeners();
        fragments = new ArrayList<>();


        mViewPager.setOffscreenPageLimit(4);
        //设置TabLayout标签的显示方式

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (String tab:titles){
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
        //设置TabLayout点击事件
        tabLayout.setOnTabSelectedListener(this);


        Fragment fragment1 = new RoomFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("video_play_bid",video_bid);
        fragment1.setArguments(bundle1);

        Fragment fragment2 = new VideoFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("video_play_section",video_section);
        bundle2.putInt("video_play_bid",video_bid);
        bundle2.putInt("video_play_record",video_record);
        bundle2.putInt("video_play_select",video_select);
        fragment2.setArguments(bundle2);

        Fragment fragment3 = new RankFragment();


        Fragment fragment4 = new HuDongFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putInt("video_play_classify",video_classify);
        fragment4.setArguments(bundle4);


        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);

        TabLayoutAdapter mTabLayoutAdapter = new TabLayoutAdapter(getSupportFragmentManager(), titles, fragments);
        mViewPager.setAdapter(mTabLayoutAdapter);

        tabLayout.setupWithViewPager(mViewPager);

        reflex(tabLayout);
    }

    @Override
    public void onSelectedFragment(BackHandleFragment backHandleFragment) {
        this.backHandleFragment = backHandleFragment;
    }


    public void reflex(final TabLayout tabLayout){
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp10 = dip2px(tabLayout.getContext(), 10);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width ;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    //根据宽高比设置控件宽高, 如设置宽高比为5:4，那么widthRatio为5，heightRatio为4
    public static void setWidthHeightWithRatio(View view, int width, int widthRatio, int heightRatio) {
        if (width <= 0) width = view.getWidth();
        int height = width * heightRatio / widthRatio;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = height;
            layoutParams.width = width;
            view.setLayoutParams(layoutParams);
        }
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();

        //if判断里面就调用了来自Fragment的onBackPressed()
        //一样！！，如果onBackPressed是返回false，就会进入条件内进行默认的操作
        if(backHandleFragment == null || !backHandleFragment.onBackPressed()){
            if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                super.onBackPressed();
            }else{
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JzvdStd.goOnPlayOnPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //home back
        JzvdStd.goOnPlayOnResume();
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        JzvdStd.releaseAllVideos();
        super.onDestroy();
    }

}

