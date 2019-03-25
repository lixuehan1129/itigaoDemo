package com.example.itigao.Video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.itigao.Adapter.SelectAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JDBCTools;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.R;
import com.example.itigao.AutoProject.Tip;
import com.example.itigao.ViewHelper.BaseFragment;
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

public class VideoFragment extends BaseFragment {

    private TextView title, itr, advice;
    private RecyclerView recyclerView;
    private int video_section;
    private int video_bid;
    private int video_select;
    private int video_record;   //0表示主页推荐内容不计，1表示课程列表加入记录，2观看记录

    ArrayList<String> name;
    ArrayList<String> content;
    ArrayList<String> nut;
    ArrayList<String> add;
    ArrayList<Integer> select,classify;
    ArrayList<String> cover;
    CallBackValue callBackValue;

    /**
     * fragment与activity产生关联是  回调这个方法
     */
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue =(CallBackValue) getActivity();
    }

    //定义一个回调接口
    public interface CallBackValue{
        void SendMessageValue(String strValue);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, container, false);
        Bundle bundle = getArguments();
        assert bundle != null;
        video_section = bundle.getInt("video_play_section",0);
        video_bid = bundle.getInt("video_play_bid",0);
        video_record = bundle.getInt("video_play_record",1);
        video_select = bundle.getInt("video_play_select",1);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View view){
        title = (TextView) view.findViewById(R.id.video_fragment_title);
        itr = (TextView) view.findViewById(R.id.video_fragment_itr);
        advice = (TextView) view.findViewById(R.id.video_fragment_advice);
        recyclerView = (RecyclerView) view.findViewById(R.id.video_fragment_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        connectData();
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

                        resultSet.close();

                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);

                        //观看记录上传
                        String sql_query = "SELECT record_bid FROM record WHERE record_bid = " +
                                video_bid +
                                " AND record_user ='" +
                                SharePreferences.getString(getActivity(),AppConstants.USER_PHONE) +
                                "'";
                        ResultSet resultSet1 = stmt.executeQuery(sql_query);
                        if(!resultSet1.next()){
                            if(video_record == 1 && name.size()>0){
                                String sql_insert = "INSERT INTO record (record_user,record_classify,record_name,record_bid,record_add," +
                                        "record_section,record_cover,record_select) values (?,?,?,?,?,?,?,?)";
                                PreparedStatement preparedStatement = conn.prepareStatement(sql_insert,Statement.RETURN_GENERATED_KEYS);
                                preparedStatement.setString(1,SharePreferences.getString(getActivity(),AppConstants.USER_PHONE));
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
                        Tip.showTip(getActivity(),"请检查网络");
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
                        Tip.showTip(getActivity(),"请检查网络");
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
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case 1:{
                    initData();
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
        recyclerView.setAdapter(selectAdapter);
        selectAdapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                title.setText(name.get(position));
                itr.setText(content.get(position));
                advice.setText(nut.get(position));

                callBackValue.SendMessageValue(add.get(position));

                if(video_record != 0){
                    Update(position);
                }

            }
        });
    }


}
