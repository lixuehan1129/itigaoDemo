package com.example.itigao.Video;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.itigao.Adapter.InteractAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JsonCode;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.AutoProject.Tip;
import com.example.itigao.Emotion.fragment.BackDataListener;
import com.example.itigao.Emotion.fragment.BackHandleFragment;
import com.example.itigao.Emotion.fragment.DataListener;
import com.example.itigao.Emotion.fragment.EmotionMainFragment;
import com.example.itigao.R;

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

public class RoomFragment extends BackHandleFragment {


    private RecyclerView recyclerView;
    private int video_bid;
    private long groupId = 0;
    private InteractAdapter interactAdapter;
    private List<InteractAdapter.Interact> interacts = new ArrayList<>();

    private EmotionMainFragment emotionMainFragment;

    private static DataListener mDataListener;

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
        interactAdapter = new InteractAdapter(interacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(interactAdapter);

        initEmotionMainFragment();
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
                        .url(AppConstants.URL + "classOne")
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

    /**
     * 初始化表情面板
     */
    public void initEmotionMainFragment(){
        //构建传递参数
        Bundle bundle = new Bundle();
        //绑定主内容编辑框
        bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT,true);
        //隐藏控件
        bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN,false);
        //替换fragment
        //创建修改实例
        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment.class,bundle);
        emotionMainFragment.bindToContentView(recyclerView);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        // Replace whatever is in thefragment_container view with this fragment,
        // and add the transaction to the backstack
        transaction.replace(R.id.fl_emotionview_main,emotionMainFragment);
        transaction.addToBackStack(null);
        //提交修改
        transaction.commit();
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
        ChatRoomManager.enterChatRoom(groupId, new RequestCallback<Conversation>() {
            @Override
            public void gotResult(int i, String s, Conversation conversation) {
                Tip.showTip(getActivity(),"现在可以发出消息");
                //editText.setEnabled(true);
                //调用接口中处理数据的函数,这里实际是多态的方式调用的是实现类的函数
                mDataListener.onData(groupId);
                EmotionMainFragment.setOnDataListener(new OnBackDataListener());
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

    //用于实现回调的类,实现的是处理回调的接口,并实现接口里面的函数
    class OnBackDataListener implements BackDataListener {
        //实现接口中处理数据的函数,只要右边的Fragment调用onData函数,这里就会收到传递的数据
        public void backData(String data) {
            InteractAdapter.Interact interact = interactAdapter.new Interact(SharePreferences.getString(getActivity(), AppConstants.USER_NAME), data);
            interactAdapter.addDataAt(interactAdapter.getItemCount(),interact);
            recyclerView.smoothScrollToPosition(interactAdapter.getItemCount()-1);
        }
    }

    //创建注册回调的函数
    public static void setOnDataListener(DataListener dataListener){
        //将参数赋值给接口类型的成员变量
        mDataListener = dataListener;
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
//                if(msg.getFromID().equals(SharePreferences.getString(getActivity(), AppConstants.USER_PHONE))){
//                    InteractAdapter.Interact interact = interactAdapter.new Interact("我", getCon);
//                    interactAdapter.addDataAt(interactAdapter.getItemCount(),interact);
//
//                    recyclerView.smoothScrollToPosition(interactAdapter.getItemCount()-1);
//                }else {
                    InteractAdapter.Interact interact = interactAdapter.new Interact(msg.getFromName(), getCon);
                    interactAdapter.addDataAt(interactAdapter.getItemCount(),interact);

                    recyclerView.smoothScrollToPosition(interactAdapter.getItemCount()-1);
            //    }
            }
        }

    }

    @Override
    public boolean onBackPressed() {
        /**
         * 判断是否拦截返回键操作
         *
         */

        if (!emotionMainFragment.isInterceptBackPress()) {
            return false;
        }else {
            return true;
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
