package com.example.itigao.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itigao.Adapter.InteractAdapter;
import com.example.itigao.Adapter.SelectAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JDBCTools;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.AutoProject.Tip;
import com.example.itigao.Media.JZMediaIjkplayer;
import com.example.itigao.R;
import com.example.itigao.Utils.StatusBarUtils;
import com.mysql.jdbc.Connection;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.ChatRoomManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.RequestCallback;
import cn.jpush.im.android.api.event.ChatRoomMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * Created by 最美人间四月天 on 2019/1/19.
 */

public class VideoActivity extends AppCompatActivity{

    private JzvdStd jzvdStd;
    private LinearLayout linearLayout;
    private EditText editText;
    private TextView imageView;



    private String userPhone;
    private long roomID;
    private String video_add;
    private int video_bid;
    private int video_section;
    private int video_select;
    private int video_record; //0表示主页推荐内容不计，1表示课程列表加入记录，2观看记录


    private TextView title, itr, advice;
    private RecyclerView recyclerViews;

    ArrayList<String> name;
    ArrayList<String> content;
    ArrayList<String> nut;
    ArrayList<String> add;
    ArrayList<String> cover;
    ArrayList<Integer> select,classify;


    private RecyclerView recyclerView;
    private InteractAdapter interactAdapter;
    private List<InteractAdapter.Interact> interacts = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);
        StatusBarUtils.setWindowStatusBarColor(VideoActivity.this, R.color.colorWhite);
        Jzvd.setMediaInterface(new JZMediaIjkplayer());
       // Jzvd.SAVE_PROGRESS = false;

        Intent intent = getIntent();
        video_add = intent.getStringExtra("video_add");
        video_bid = intent.getIntExtra("video_bid",0);
        video_section = intent.getIntExtra("video_section",0);
        video_select = intent.getIntExtra("video_select",1);
        video_record = intent.getIntExtra("video_record",1);

        userPhone = SharePreferences.getString(VideoActivity.this, AppConstants.USER_PHONE);
        initView();
    }

    private void initView(){
        jzvdStd = (JzvdStd) findViewById(R.id.video_play1_vv);
        linearLayout = (LinearLayout) findViewById(R.id.video_play1_li);
        editText = (EditText) findViewById(R.id.video_play1_et);
        imageView = (TextView) findViewById(R.id.video_play1_iv);

        title = (TextView) findViewById(R.id.video_fragment1_title);
        itr = (TextView) findViewById(R.id.video_fragment1_itr);
        advice = (TextView) findViewById(R.id.video_fragment1_advice);


        recyclerViews = (RecyclerView) findViewById(R.id.video_fragment1_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VideoActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViews.setLayoutManager(linearLayoutManager);

        //获取屏幕宽
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);

        assert wm != null;
        int width = wm.getDefaultDisplay().getWidth();
        setWidthHeightWithRatio(jzvdStd,width,16,9);

        setPlay(video_add);
        connectData();

        recyclerView = (RecyclerView) findViewById(R.id.interact1_rv);
        recyclerView.setLayoutManager(setNoSco());
        interactAdapter = new InteractAdapter(interacts);
        recyclerView.setAdapter(interactAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(VideoActivity.this));

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

    }

    private void setPlay(String url){
        jzvdStd.setUp(url, "", Jzvd.SCREEN_WINDOW_NORMAL);
        jzvdStd.startVideo();
    }

    private void connectData(){

        name = new ArrayList<>();
        content = new ArrayList<>();
        nut = new ArrayList<>();
        add = new ArrayList<>();
        select = new ArrayList<>();
        classify = new ArrayList<>();

        cover = new ArrayList<>();

        new Thread(){
            public void run(){
                Looper.prepare();
                try{
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        Statement stmt = conn.createStatement();
                        String sql = "SELECT * FROM class WHERE class_bid = " +
                                video_bid +
                                " ORDER BY class_select";
                        ResultSet resultSet = stmt.executeQuery(sql);
                        while(resultSet.next()){
                            name.add(resultSet.getString("class_name"));
                            content.add(resultSet.getString("class_content"));
                            add.add(resultSet.getString("class_add"));
                            nut.add(resultSet.getString("class_nut"));
                            select.add(resultSet.getInt("class_select"));
                            classify.add(resultSet.getInt("class_classify"));
                            cover.add(resultSet.getString("class_cover"));
                        }

                        if(resultSet.first()){
                            roomID = resultSet.getLong("class_group");
                        }

                        resultSet.close();

                        android.os.Message message = new android.os.Message();
                        message.what = 1222;
                        handler.sendMessage(message);

                        //观看记录上传
                        String sql_query = "SELECT record_bid FROM record WHERE record_bid = " +
                                video_bid +
                                " AND record_user ='" +
                                SharePreferences.getString(VideoActivity.this,AppConstants.USER_PHONE) +
                                "'";
                        ResultSet resultSet1 = stmt.executeQuery(sql_query);
                        if(!resultSet1.next()){
                            if(video_record == 1 && name.size()>0){
                                String sql_insert = "INSERT INTO record (record_user,record_classify,record_name,record_bid,record_add," +
                                        "record_section,record_cover,record_select) values (?,?,?,?,?,?,?,?)";
                                PreparedStatement preparedStatement = conn.prepareStatement(sql_insert,Statement.RETURN_GENERATED_KEYS);
                                preparedStatement.setString(1,SharePreferences.getString(VideoActivity.this,AppConstants.USER_PHONE));
                                preparedStatement.setInt(2,classify.get(0));
                                preparedStatement.setString(3,name.get(0));
                                preparedStatement.setInt(4,video_bid);
                                preparedStatement.setString(5,add.get(0));
                                preparedStatement.setInt(6,video_section);
                                preparedStatement.setString(7,cover.get(0));
                                preparedStatement.setInt(8,1);
                                preparedStatement.executeUpdate();
                                preparedStatement.close();
                                setBroad();
                            }
                        }
                        resultSet1.close();

                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Tip.showTip(VideoActivity.this,"请检查网络");
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
    }

    private void Update(final int select){
        new Thread(){
            public void run(){
                Looper.prepare();
                try{
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        Statement stmt = conn.createStatement();
                        String sql = "UPDATE record SET record_add = ?, record_select = ? WHERE record_bid = " +
                                video_bid +
                                "";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql);
                        preparedStatement.setString(1,add.get(select));
                        preparedStatement.setInt(2,select + 1);
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                        JDBCTools.releaseConnection(stmt,conn);
                        setBroad();
                    }else {
                        Tip.showTip(VideoActivity.this,"请检查网络");
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
    }

    private void setBroad(){
        Intent intent = new Intent(AppConstants.BROAD_RECORD);
        LocalBroadcastManager.getInstance(VideoActivity.this).sendBroadcast(intent);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(android.os.Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case 1222:{
                    initData();
                    if(roomID != 0){
                        ImRoom();
                    }
                    //进入聊天室
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });


    private void initData(){
        title.setText(name.get(video_select-1));
        System.out.println("内容" + content.get(video_select-1));
        itr.setText(content.get(video_select-1));
        advice.setText(nut.get(video_select-1));

        ArrayList<Integer> id = new ArrayList<>();
        for (int i=1; i<video_section+1; i++){
            id.add(i);
        }
        setAdapter(id);
    }

    private void setAdapter(final ArrayList<Integer> id){
        List<SelectAdapter.Select> selects = new ArrayList<>();
        for(int i=0; i<id.size(); i++){
            SelectAdapter selectAdapter = new SelectAdapter(selects);
            SelectAdapter.Select select = selectAdapter.new Select(id.get(i));
            selects.add(select);
        }
        final SelectAdapter selectAdapter = new SelectAdapter(selects);
        recyclerViews.setAdapter(selectAdapter);
        selectAdapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                title.setText(name.get(position));
                itr.setText(content.get(position));
                advice.setText(nut.get(position));

                Jzvd.releaseAllVideos();
                setPlay(add.get(position));

                if(video_record != 0){
                    Update(position);
                }

            }
        });
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
        ChatRoomManager.enterChatRoom(roomID, new RequestCallback<Conversation>() {
            @Override
            public void gotResult(int i, String s, Conversation conversation) {
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
        Conversation.createChatRoomConversation(roomID);

        /**
         * 获取聊天室会话信息
         *
         * @param roomID 群组的groupID
         * @return 返回会话信息，若不存在和指定对象的会话则返回null
         * @since 2.4.0
         */
        JMessageClient.getChatRoomConversation(roomID);

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
        ChatRoomManager.leaveChatRoom(roomID, new BasicCallback() {
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
                    JMessageClient.sendMessage(msg);
                    editText.setText("");
                    InteractAdapter.Interact interact = interactAdapter.new Interact("我", text);
                    interactAdapter.addDataAt(interactAdapter.getItemCount(),interact);
//                    if(interactAdapter.getItemCount() != 0){
//                        recyclerView.scrollToPosition(interactAdapter.getItemCount()-1);
//                    }
                }else {
                    Toast.makeText(VideoActivity.this,"内容不能为空",Toast.LENGTH_LONG).show();
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
                if(msg.getFromID().equals(SharePreferences.getString(VideoActivity.this, AppConstants.USER_PHONE))){
                    InteractAdapter.Interact interact = interactAdapter.new Interact("我", getCon);
                    interactAdapter.addDataAt(interactAdapter.getItemCount(),interact);
                }else {
                    InteractAdapter.Interact interact = interactAdapter.new Interact(msg.getFromName(), getCon);
                    interactAdapter.addDataAt(interactAdapter.getItemCount(),interact);
                }
//                if(interactAdapter.getItemCount() != 0){
//                    recyclerView.scrollToPosition(interactAdapter.getItemCount()-1);
//                }
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

    private LinearLayoutManager setNoSco(){
        return new LinearLayoutManager(VideoActivity.this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
    }


    @Override
    public void onStart() {
        super.onStart();
        //注册
        JMessageClient.registerEventReceiver(this);
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
        if(roomID != 0){
            ImRemove();
        }
        JzvdStd.releaseAllVideos();
        JMessageClient.registerEventReceiver(this);
        super.onDestroy();
    }

}
