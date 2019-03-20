package com.example.itigao.None;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.support.design.widget.TabLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itigao.Adapter.TabLayoutAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.AutoProject.Tip;
import com.example.itigao.Media.JZMediaIjkplayer;
import com.example.itigao.R;
import com.example.itigao.Utils.StatusBarUtils;
import com.example.itigao.Video.VideoFragment;


import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


/**
 * Created by 最美人间四月天 on 2018/11/29.
 */

public class VideoPlayActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, VideoFragment.CallBackValue,InteractFragment.CallBackValue{

    private JzvdStd jzvdStd;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private LinearLayout linearLayout;
    private EditText editText;
    private TextView imageView;


    private String[] titles = new String[]{"    视频    ", "    互动    "};
    private List<Fragment> fragments=new ArrayList<>();

    private String userPhone;
    private long roomID;
    private String video_add;
    private int video_bid;
    private int video_section;
    private int video_select;
    private int video_record; //0表示主页推荐内容不计，1表示课程列表加入记录，2观看记录

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_activity);
        StatusBarUtils.setWindowStatusBarColor(VideoPlayActivity.this, R.color.colorWhite);
        Jzvd.setMediaInterface(new JZMediaIjkplayer());
       // Jzvd.SAVE_PROGRESS = false;

        Intent intent = getIntent();
        video_add = intent.getStringExtra("video_add");
        video_bid = intent.getIntExtra("video_bid",0);
        video_section = intent.getIntExtra("video_section",0);
        video_select = intent.getIntExtra("video_select",1);
        video_record = intent.getIntExtra("video_record",1);

        userPhone = SharePreferences.getString(VideoPlayActivity.this, AppConstants.USER_PHONE);
        initView();
    }

    private void initView(){
        jzvdStd = (JzvdStd) findViewById(R.id.video_play_vv);
        tabLayout = (TabLayout) findViewById(R.id.video_play_layout);
        mViewPager = (ViewPager) findViewById(R.id.video_play_viewpager);
        linearLayout = (LinearLayout) findViewById(R.id.video_play_li);
        editText = (EditText) findViewById(R.id.video_play_et);
        imageView = (TextView) findViewById(R.id.video_play_iv);

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

    //要获取的值  就是这个参数的值
    @Override
    public void SendMessageValueGroup(long strValue) {
        // TODO Auto-generated method stub
        System.out.println("聊天室号"+strValue);
        roomID = strValue;
        //获取聊天室信息
        Tip.showTip(VideoPlayActivity.this,"现在可以发出消息");
        if(roomID != 0){
            if(TextUtils.isEmpty(editText.getText().toString())){
                imageView.setTextColor(getResources().getColor(R.color.colorGray));
            }else {
                imageView.setTextColor(getResources().getColor(R.color.colorGreen));
            }
            imageOnClick();
        }
    }

    private void imageOnClick(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(editText.getText())){
                    InputMethodManager imm =(InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                    // 发送聊天室消息
                    Conversation conv = JMessageClient.getChatRoomConversation(roomID);
                    if (null == conv) {
                        conv = Conversation.createChatRoomConversation(roomID);
                    }
                    String text = editText.getText().toString();
                    final Message msg = conv.createSendTextMessage(text);
                    msg.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int responseCode, String responseMessage) {
                            if (0 == responseCode) {
                                System.out.println("MessageSent" + responseCode + responseMessage + msg);
                            } else {
                                System.out.println("MessageSent" + responseCode + responseMessage + "失败");
                            }
                        }
                    });
                    Intent intent_broad = new Intent(AppConstants.BROAD_IM);
                    intent_broad.putExtra("video_con",editText.getText().toString());
                    LocalBroadcastManager.getInstance(VideoPlayActivity.this).sendBroadcast(intent_broad);

                    JMessageClient.sendMessage(msg);
                    editText.setText("");

                }else {
                    Toast.makeText(VideoPlayActivity.this,"内容不能为空",Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    private void initTab(){
        mViewPager.setOffscreenPageLimit(2);
        //设置TabLayout标签的显示方式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (String tab:titles){
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
        //设置TabLayout点击事件
        tabLayout.setOnTabSelectedListener(this);

        Fragment fragment1 = new VideoFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("video_play_section",video_section);
        bundle1.putInt("video_play_bid",video_bid);
        bundle1.putInt("video_play_record",video_record);
        bundle1.putInt("video_play_select",video_select);
        fragment1.setArguments(bundle1);

        Fragment fragment2 = new InteractFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("video_play_bid",video_bid);
        fragment2.setArguments(bundle2);

        fragments.add(fragment1);
        fragments.add(fragment2);
        TabLayoutAdapter mTabLayoutAdapter = new TabLayoutAdapter(getSupportFragmentManager(),titles, fragments);
        mViewPager.setAdapter(mTabLayoutAdapter);

        tabLayout.setupWithViewPager(mViewPager);
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
     //   Jzvd.releaseAllVideos();
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
//        JMessageClient.registerEventReceiver(this);
        JzvdStd.releaseAllVideos();
        super.onDestroy();
    }

}


