package com.example.itigao.Broad;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.itigao.Adapter.InteractAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.AutoProject.Tip;
import com.example.itigao.Emotion.fragment.BackDataListener;
import com.example.itigao.Emotion.fragment.DataListener;
import com.example.itigao.Emotion.fragment.EmotionMainFragment;
import com.example.itigao.R;
import com.example.itigao.Video.RoomFragment;
import com.example.itigao.ViewHelper.BaseFragment;


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
import cn.jpush.im.api.BasicCallback;

/**
 * Created by 最美人间四月天 on 2019/3/11.
 */

public class BroadRoomFragment extends BaseFragment {


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
        video_bid = bundle.getInt("broad_new_bid",0);
        initView(view);
        if(video_bid != 0){
            groupId = video_bid;
            ImRoom();
        }
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
        recyclerView.setLayoutManager(setNoSco());
        interactAdapter = new InteractAdapter(interacts);
        recyclerView.setAdapter(interactAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        initEmotionMainFragment();
    }

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
            InteractAdapter.Interact interact = interactAdapter.new Interact("我", data);
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

    private LinearLayoutManager setNoSco(){
        return new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
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
