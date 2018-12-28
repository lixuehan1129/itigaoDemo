package com.example.fitdemo.Subscribe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.os.UserManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitdemo.Adapter.InteractAdapter;
import com.example.fitdemo.AutoProject.AppConstants;
import com.example.fitdemo.AutoProject.JDBCTools;
import com.example.fitdemo.AutoProject.SharePreferences;
import com.example.fitdemo.R;
import com.example.fitdemo.AutoProject.Tip;
import com.example.fitdemo.ViewHelper.BaseFragment;
import com.mob.MobSDK;
import com.mob.imsdk.IUserManager;
import com.mob.imsdk.MobIM;
import com.mob.imsdk.MobIMCallback;
import com.mob.imsdk.model.IMGroup;
import com.mob.imsdk.model.IMUser;
import com.mysql.jdbc.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/12/11.
 */

public class InteractFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private int video_bid;
    private String groupId = null;
    CallBackValue callBackValue;

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
        void SendMessageValueGroup(String strValue);
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

    private void initView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.interact_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initData();
    }

    private void initData(){
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> content = new ArrayList<>();
        for(int i=0; i<8; i++){
            name.add("用户名" + i);
            content.add("看完这个视频，我想说点什么呢" + i);
        }
        name.add("用户名");
        content.add("看完这个视频，我想说点什么呢，我觉得真的很好很有用，6666666666");
        setAdapter(name,content);
    }

    private void setAdapter(final ArrayList<String> name, ArrayList<String> content){
        List<InteractAdapter.Interact> interacts = new ArrayList<>();
        for(int i=0; i<name.size(); i++){
            InteractAdapter interactAdapter = new InteractAdapter(interacts);
            InteractAdapter.Interact interact = interactAdapter.new Interact(name.get(i), content.get(i));
            interacts.add(interact);
        }

        InteractAdapter interactAdapter = new InteractAdapter(interacts);
        recyclerView.setAdapter(interactAdapter);
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
                            //如果不存在，就见一个群组，如果存在就直接找到这个群号
                            if(resultSet.getString("class_group") == null){
                                MobIM.getGroupManager().createGroup(resultSet.getString("class_name"), resultSet.getString("class_name"), new String[]{
                                        "15333101111"
                                }, new MobIMCallback<IMGroup>() {
                                    @Override
                                    public void onSuccess(IMGroup imGroup) {
                                        //更新数据
                                        groupId = imGroup.getId();
                                        updateGroup();
                                    }

                                    @Override
                                    public void onError(int i, String s) {
                                        Log.e("错误码",s);
                                    }
                                });
                            }else {
                                groupId = resultSet.getString("class_group");
                                if(groupId != null){
                                    callBackValue.SendMessageValueGroup(groupId);
                                }
                            }

                        }
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

    private void updateGroup(){
        new Thread(){
            public void run(){
                try {
                    final Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        String sql = "UPDATE class SET class_group = ? WHERE class_bid = '" +
                                video_bid +
                                "'";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql);
                        preparedStatement.setString(1,groupId);
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                        JDBCTools.releaseConnection(null,conn);

                        if(groupId != null){
                            callBackValue.SendMessageValueGroup(groupId);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
