package com.example.itigao.Video;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itigao.Adapter.InteractAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JsonCode;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.AutoProject.Tip;
import com.example.itigao.R;
import com.example.itigao.ViewHelper.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.ChatRoomManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.RequestCallback;
import cn.jpush.im.android.api.event.ChatRoomMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.api.BasicCallback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 最美人间四月天 on 2019/3/9.
 */

public class RoomFragment extends BaseFragment {


    private RecyclerView recyclerView;
    private EditText editText;
    private TextView imageView;
    private LinearLayout linearLayout;
    private int video_bid;
    private long groupId = 0;
    private InteractAdapter interactAdapter;
    private List<InteractAdapter.Interact> interacts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.room_fragment, container, false);
        Bundle bundle = getArguments();
        assert bundle != null;
        video_bid = bundle.getInt("video_play_bid",0);
        initView(view);
        getGroup();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //注册
        JMessageClient.registerEventReceiver(this);
    }


    private void initView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.room_rv);
        linearLayout = (LinearLayout) view.findViewById(R.id.video_room_li);
        editText = (EditText) view.findViewById(R.id.video_room_et);
        imageView = (TextView) view.findViewById(R.id.video_room_iv);


        interactAdapter = new InteractAdapter(interacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(interactAdapter);

        editText.setEnabled(false);

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

    private void getGroup(){
        new Thread(){
            public void run(){
                Looper.prepare();
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("class_bid", String.valueOf(video_bid))
                        .build();
                //构建一个请求对象
                Request request = new Request.Builder()
                        .url("http://39.105.213.41:8080/StudyAppService/StudyServlet/classOne")
                        .post(requestBody)
                        .build();
                //发送请求获取响应
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    //判断请求是否成功
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        String regData = response.body().string();
                        System.out.println("返回room"+regData);
                        if(JsonCode.getCode(regData) == 200){
                            String jsonData = JsonCode.getData(regData);
                            try {
                                JSONArray jsonArray = new JSONArray(jsonData);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                groupId = jsonObject.getInt("class_group");
                                if(groupId == 0){
                                    Tip.showTip(getActivity(),"暂时没有聊天室");
                                }
                                Message message = new Message();
                                message.what = 11;
                                handler.sendMessage(message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
    }

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case 11:{
                    ImRoom();
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });




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
        ChatRoomManager.enterChatRoom(groupId, new RequestCallback<Conversation>() {
            @Override
            public void gotResult(int i, String s, Conversation conversation) {
                Tip.showTip(getActivity(),"现在可以发出消息");
                editText.setEnabled(true);
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
        Conversation.createChatRoomConversation(groupId);

        /**
         * 获取聊天室会话信息
         *
         * @param roomID 群组的groupID
         * @return 返回会话信息，若不存在和指定对象的会话则返回null
         * @since 2.4.0
         */
        JMessageClient.getChatRoomConversation(groupId);

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
        ChatRoomManager.leaveChatRoom(groupId, new BasicCallback() {
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
                    InputMethodManager imm =(InputMethodManager)getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                    // 发送聊天室消息
                    Conversation conv = JMessageClient.getChatRoomConversation(groupId);
                    if (null == conv) {
                        conv = Conversation.createChatRoomConversation(groupId);
                    }
                    String text = editText.getText().toString();
                    if(!text.isEmpty()){
                        final cn.jpush.im.android.api.model.Message msg = conv.createSendTextMessage(text);
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

                        recyclerView.smoothScrollToPosition(interactAdapter.getItemCount()-1);
                    }

//                    if(interactAdapter.getItemCount() != 0){
//                        recyclerView.scrollToPosition(interactAdapter.getItemCount()-1);
//                    }
                }else {
                    Toast.makeText(getActivity(),"内容不能为空",Toast.LENGTH_LONG).show();
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
                if(msg.getFromID().equals(SharePreferences.getString(getActivity(), AppConstants.USER_PHONE))){
                    InteractAdapter.Interact interact = interactAdapter.new Interact("我", getCon);
                    interactAdapter.addDataAt(interactAdapter.getItemCount(),interact);

                    recyclerView.smoothScrollToPosition(interactAdapter.getItemCount()-1);
                }else {
                    InteractAdapter.Interact interact = interactAdapter.new Interact(msg.getFromName(), getCon);
                    interactAdapter.addDataAt(interactAdapter.getItemCount(),interact);

                    recyclerView.smoothScrollToPosition(interactAdapter.getItemCount()-1);
                }
//                if(interactAdapter.getItemCount() != 0){
//                    recyclerView.scrollToPosition(interactAdapter.getItemCount()-1);
//                }
            }
        }

    }


    @Override
    public void onDestroy(){
        if(groupId != 0){
            ImRemove();
        }
        JMessageClient.registerEventReceiver(this);
        super.onDestroy();
    }

}
