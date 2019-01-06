package com.example.fitdemo.Subscribe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitdemo.Adapter.InteractAdapter;
import com.example.fitdemo.AutoProject.JDBCTools;
import com.example.fitdemo.R;
import com.example.fitdemo.AutoProject.Tip;
import com.example.fitdemo.ViewHelper.BaseFragment;
import com.mysql.jdbc.Connection;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.jmessage.biz.httptask.task.GetEventNotificationTaskMng;
import cn.jpush.im.android.api.ChatRoomManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.RequestCallback;
import cn.jpush.im.android.api.event.ChatRoomMessageEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.ChatRoomInfo;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by 最美人间四月天 on 2018/12/11.
 */

public class InteractFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private int video_bid;
    private long groupId = 0;
    CallBackValue callBackValue;
    private InteractAdapter interactAdapter;
    private List<InteractAdapter.Interact> interacts = new ArrayList<>();

    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> content = new ArrayList<>();



    /**
     * fragment与activity产生关联是  回调这个方法
     */
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue =(InteractFragment.CallBackValue) getActivity();
    }

    //定义一个回调接口
    public interface CallBackValue{
        void SendMessageValueGroup(long strValue);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.interact_fragment, container, false);
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

    public void onStart() {
        super.onStart();
        //注册
        JMessageClient.registerEventReceiver(this);
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
                InteractAdapter.Interact interact = interactAdapter.new Interact(msg.getFromName(), getCon);
              //  interacts.add(interact);
                System.out.println(interact);
                interactAdapter.addDataAt(0,interact);
              //  interactAdapter.notifyItemInserted(0);
            }

            System.out.println("聊天消息"+ msg.getFromName()+getCon);
        }

    }


    private void initView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.interact_rv);
        interactAdapter = new InteractAdapter(interacts);
        recyclerView.setAdapter(interactAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setAdapter();

//        if(groupId != null){
//            MobIM.getChatManager().setConversationDisturb(groupId, IMConversation.TYPE_GROUP, true, new MobIMCallback<Boolean>() {
//                @Override
//                public void onSuccess(Boolean aBoolean) {
//                    System.out.println("这是群回话");
//                }
//
//                @Override
//                public void onError(int i, String s) {
//
//                }
//            });
//
//            MobIM.addMessageReceiver(new MobIMMessageReceiver() {
//                @Override
//                public void onMessageReceived(List<IMMessage> list) {
//                    list.get(0).getBody();
//                    System.out.println("是否接受消息"+list.get(0).getBody());
//                }
//
//                @Override
//                public void onMsgWithDraw(String s, String s1) {
//
//                }
//            });
//        }

    }


    private void setAdapter(){
//        for(int i=0; i<name.size(); i++){
//            InteractAdapter interactAdapter = new InteractAdapter(interacts);
//            InteractAdapter.Interact interact = interactAdapter.new Interact(name.get(i), content.get(i));
//            interacts.add(interact);
//        }


        interactAdapter.setOnItemClickListener(new InteractAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Tip.showTip(getActivity(),name.get(position));
            }
        });
    }


    private void getGroup(){
        new Thread(){
            public void run(){
                Looper.prepare();
                try{
                    final Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        final Statement stmt = conn.createStatement();
                        String sql = "SELECT class_group,class_name FROM class WHERE class_bid = " +
                                video_bid +
                                " ORDER BY class_select LIMIT 1";
                        ResultSet resultSet = stmt.executeQuery(sql);
                        if(resultSet.next()){
                            groupId = resultSet.getInt("class_group");
                            if(groupId == 0){
                                Tip.showTip(getActivity(),"暂时没有聊天室");
//                                MobIM.getGroupManager().createGroup(resultSet.getString("class_name"), resultSet.getString("class_name"), new String[]{
//                                        "15333101110"
//                                }, new MobIMCallback<IMGroup>() {
//                                    @Override
//                                    public void onSuccess(IMGroup imGroup) {
//                                        //更新数据
//                                        groupId = imGroup.getId();
//                                        updateGroup();
//                                    }
//
//                                    @Override
//                                    public void onError(int i, String s) {
//                                        Log.e("错误码",s);
//                                    }
//                                });
                            }

                        }

                        Message message = new Message();
                        message.what = 11;
                        handler.sendMessage(message);

                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Tip.showTip(getActivity(),"请检查网络");
                    }
                }catch (SQLException e) {
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
                System.out.println("聊天室内容" + conversation);
                if(groupId != 0){
                    callBackValue.SendMessageValueGroup(groupId);
                }
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


    @Override
    public void onDestroy(){
        if(groupId != 0){
            ImRemove();
        }
        JMessageClient.registerEventReceiver(getActivity());
        super.onDestroy();
    }




//    private void updateGroup(){
//        new Thread(){
//            public void run(){
//                try {
//                    final Connection conn = JDBCTools.getConnection();
//                    if(conn != null){
//                        String sql = "UPDATE class SET class_group = ? WHERE class_bid = '" +
//                                video_bid +
//                                "'";
//                        PreparedStatement preparedStatement = conn.prepareStatement(sql);
//                        preparedStatement.setString(1,groupId);
//                        preparedStatement.executeUpdate();
//                        preparedStatement.close();
//                        JDBCTools.releaseConnection(null,conn);
//
//                        if(groupId != null){
//                            callBackValue.SendMessageValueGroup(groupId);
//                        }
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
}
