package com.example.itigao.Video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.Media.JZMediaIjkplayer;
import com.example.itigao.R;
import com.example.itigao.Utils.StatusBarUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import static cn.jzvd.JZUtils.dip2px;

/**
 * Created by 最美人间四月天 on 2019/3/9.
 */

public class VideoNewActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,VideoFragment.CallBackValue{

    private JzvdStd jzvdStd;
    private TabLayout tabLayout;
    private ViewPager mViewPager;


    private String[] titles = new String[]{" 聊天 ", " 视频 ", " 互动 ", " 排行 "};
    private List<Fragment> fragments = new ArrayList<>();

    private String userPhone;
    private String video_add;
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

        userPhone = SharePreferences.getString(VideoNewActivity.this, AppConstants.USER_PHONE);

        initView();
    }

    private void initView(){
        jzvdStd = (JzvdStd) findViewById(R.id.video_new_vv);
        tabLayout = (TabLayout) findViewById(R.id.video_new_layout);
        mViewPager = (ViewPager) findViewById(R.id.video_new_viewpager);
        //获取屏幕宽
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);

        assert wm != null;
        int width = wm.getDefaultDisplay().getWidth();
        setWidthHeightWithRatio(jzvdStd,width,16,9);
        initTab();

        setPlay(video_add);

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


    private void initTab(){
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

        Fragment fragment3 = new HuDongFragment();
        Fragment fragment4 = new RankFragment();

        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);

        TabLayoutAdapter mTabLayoutAdapter = new TabLayoutAdapter(getSupportFragmentManager(),titles, fragments);
        mViewPager.setAdapter(mTabLayoutAdapter);

        tabLayout.setupWithViewPager(mViewPager);

        reflex(tabLayout);
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

