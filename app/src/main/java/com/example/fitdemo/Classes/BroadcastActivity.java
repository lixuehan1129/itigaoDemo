package com.example.fitdemo.Classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitdemo.Adapter.InteractAdapter;

import com.example.fitdemo.AutoProject.AppConstants;
import com.example.fitdemo.AutoProject.SharePreferences;
import com.example.fitdemo.R;
import com.example.fitdemo.Utils.StatusBarUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.ChatRoomManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.RequestCallback;
import cn.jpush.im.android.api.event.ChatRoomMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;


/**
 * Created by 最美人间四月天 on 2018/12/14.
 */

public class BroadcastActivity extends AppCompatActivity {


    private LinearLayout linearLayout;
    private RecyclerView recyclerView;
    private EditText editText;
    private TextView imageView;

    private int anchorId, anchorRoom;
    private String url;
    private String URL_F = "rtmp://zb.tipass.com:1935/live/";

    private StandardGSYVideoPlayer videoPlayer;
    private OrientationUtils orientationUtils;

    private InteractAdapter interactAdapter;
    private List<InteractAdapter.Interact> interacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_broad_activity);
        StatusBarUtils.setWindowStatusBarColor(BroadcastActivity.this, R.color.colorWhite);
        PlayerFactory.setPlayManager(IjkPlayerManager.class);//ijk模式
        Intent intent = getIntent();
        anchorId = intent.getIntExtra("anchor_bid",0);
        anchorRoom = intent.getIntExtra("anchor_room",0);
        initView();
    }

    private void initView(){
        videoPlayer = (StandardGSYVideoPlayer) findViewById(R.id.video_broad_vv);
        recyclerView = (RecyclerView) findViewById(R.id.video_broad_rv) ;
        linearLayout = (LinearLayout) findViewById(R.id.video_broad_li);
        editText = (EditText) findViewById(R.id.video_broad_et);
        imageView = (TextView) findViewById(R.id.video_broad_iv);

        interactAdapter = new InteractAdapter(interacts);
        recyclerView.setAdapter(interactAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(BroadcastActivity.this));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editText.getText().toString())){
                    imageView.setTextColor(getResources().getColor(R.color.colorGray_1));
                }else {
                    imageView.setTextColor(getResources().getColor(R.color.colorGreen));
                }
            }
        });

        //获取屏幕宽
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
           setWidthHeightWithRatio(videoPlayer,width,16,9);

        setPlay();
        if(anchorRoom != 0){
            ImRoom();
        }

    }


    private void setPlay(){
        url = URL_F + anchorId;

        /**
         * 设置右下角 显示切换到全屏 的按键资源
         * 必须在setUp之前设置
         * 不设置使用默认
         */
        videoPlayer.setEnlargeImageRes(R.mipmap.ic_full);
        /**
         * 设置右下角 显示退出全屏 的按键资源
         * 必须在setUp之前设置
         * 不设置使用默认
         */
        videoPlayer.setShrinkImageRes(R.mipmap.ic_full_b);


        videoPlayer.setUp(url,false,null);
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        videoPlayer.setNeedShowWifiTip(true);
        videoPlayer.setIsTouchWiget(false);


        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);


        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayer.startWindowFullscreen(BroadcastActivity.this,true,true);
            }
        });

        //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
        videoPlayer.setAutoFullWithSize(true);
//        //音频焦点冲突时是否释放
//        videoPlayer.setReleaseWhenLossAudio(false);
//        //自动转屏
//        videoPlayer.setRotateViewAuto(true);

        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        videoPlayer.startPlayLogic();
    }

    private void ImRoom(){

        /**
         * 进入聊天室.
         * 用户只有成功调用此接口之后，才能收到聊天室消息，以及在此聊天室中发言。
         * 成功进入聊天室之后，会将聊天室中最近若干条聊天记录同步到本地并以{@link cn.jpush.im.android.api.event.ChatRoomMessageEvent}事件的形式通知到上层。
         *
         * @param roomID   聊天室的roomID
         * @param callback 接口回调
         * @since 2.4.0
         */
        ChatRoomManager.enterChatRoom(anchorRoom, new RequestCallback<Conversation>() {
            @Override
            public void gotResult(int i, String s, Conversation conversation) {
                System.out.println("进入聊天室" + anchorRoom);
                imageOnClick();
            }
        });


        /**
         * 创建聊天室会话，如果本地已存在对应会话，则不会重新创建，直接返回本地会话对象。
         *
         * @param roomID 聊天室的roomID
         * @return 会话对象
         * @since 2.4.0
         */
        Conversation.createChatRoomConversation(anchorRoom);

        /**
         * 获取聊天室会话信息
         *
         * @param roomID 群组的groupID
         * @return 返回会话信息，若不存在和指定对象的会话则返回null
         * @since 2.4.0
         */
        JMessageClient.getChatRoomConversation(anchorRoom);

//        onEventMainThread(new ChatRoomMessageEvent(0,null,null));



    }



    private void ImRemove(){

        /**
         * 离开聊天室.
         * 成功调用此接口之后，用户将能不在此聊天室收发消息。
         *
         * @param roomID   聊天室的roomID
         * @param callback 接口回调
         * @since 2.4.0
         */
        ChatRoomManager.leaveChatRoom(anchorRoom, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                System.out.println("退出");
            }
        });
    }

    private void imageOnClick(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(editText.getText())){
                    //将输入法隐藏，mPasswordEditText 代表密码输入框
                    InputMethodManager imm =(InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                    // 发送聊天室消息
                    Conversation conv = JMessageClient.getChatRoomConversation(anchorRoom);
                    if (null == conv) {
                        conv = Conversation.createChatRoomConversation(anchorRoom);
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
                    JMessageClient.sendMessage(msg);
                    editText.setText("");
                    InteractAdapter.Interact interact = interactAdapter.new Interact("我", text);
                    interactAdapter.addDataAt(interactAdapter.getItemCount(),interact);
                    if(interactAdapter.getItemCount() != 0){
                        recyclerView.scrollToPosition(interactAdapter.getItemCount()-1);
                    }
                }else {
                    Toast.makeText(BroadcastActivity.this,"内容不能为空",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // 接收聊天室消息
    // 主线程
    @Subscribe(sticky = true, threadMode = ThreadMode.ASYNC)
    public void onEventMainThread(ChatRoomMessageEvent event) {
        Log.d("tag", "ChatRoomMessageEvent received .");
        List<cn.jpush.im.android.api.model.Message> msgs = event.getMessages();
        for (cn.jpush.im.android.api.model.Message msg : msgs) {
            //这个页面仅仅展示聊天室会话的消息
            String msgCon = msg.getContent().toJson();
            String getCon = null;
            try {
                JSONObject jsonObject = new JSONObject(msgCon);
                getCon = jsonObject.getString("text");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(getCon != null){
                //System.out.println("我的数字"+interacts.size());
                if(msg.getFromID().equals(SharePreferences.getString(BroadcastActivity.this, AppConstants.USER_PHONE))){
                    InteractAdapter.Interact interact = interactAdapter.new Interact("我", getCon);
                    interactAdapter.addDataAt(interactAdapter.getItemCount(),interact);
                }else {
                    InteractAdapter.Interact interact = interactAdapter.new Interact(msg.getFromName(), getCon);
                    interactAdapter.addDataAt(interactAdapter.getItemCount(),interact);
                }
                if(interactAdapter.getItemCount() != 0){
                    recyclerView.scrollToPosition(interactAdapter.getItemCount()-1);
                }
            }
        }

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
    public void onStart() {
        super.onStart();
        //注册
        JMessageClient.registerEventReceiver(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        videoPlayer.onVideoResume();
        if(anchorRoom != 0){
            ImRemove();
        }
        JMessageClient.registerEventReceiver(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }

        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }

        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //点击返回或不允许后台播放时 释放资源
    }

}
