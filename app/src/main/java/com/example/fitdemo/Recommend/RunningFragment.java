package com.example.fitdemo.Recommend;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.fitdemo.Adapter.ClassSelectAdapter;
import com.example.fitdemo.AutoProject.AppConstants;
import com.example.fitdemo.AutoProject.JDBCTools;
import com.example.fitdemo.AutoProject.SharePreferences;
import com.example.fitdemo.AutoProject.Tip;
import com.example.fitdemo.Classes.RunActivity;
import com.example.fitdemo.MainActivity;
import com.example.fitdemo.R;
import com.example.fitdemo.User.UserLoginActivity;
import com.example.fitdemo.Utils.Class_select;
import com.example.fitdemo.Utils.DateUtils;
import com.example.fitdemo.ViewHelper.BaseFragment;
import com.example.fitdemo.ViewHelper.DividerItemChange;
import com.mysql.jdbc.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ROBOSOFT on 2018/11/27.
 */

public class RunningFragment extends BaseFragment {

    private RadioGroup radioGroup;
    private RadioButton button1, button2, button3, button4, button5, button6, button7;
    private ScrollView scrollView;

    private LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayout5, linearLayout6, linearLayout7;
    private TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3, recyclerView4, recyclerView5, recyclerView6, recyclerView7;

    private ArrayList<Class_select> data1,data2,data3,data4,data5,data6,data7;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.class_check, container, false);
        initView(view);
        initGroup();
        connectData();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser&&isResumed()){
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0,linearLayout1.getTop());
                }
            });
            //TODO now it's visible to user
        } else {
            //TODO now it's invisible to user
        }
    }

    @Override
    protected void onFragmentFirstVisible(){
       // initView(getView());
       // connectData();
    }

    private void initView(View view){
        //这是什么弱智代码
        radioGroup = view.findViewById(R.id.class_check_group);
        button1 = view.findViewById(R.id.class_check_button1);
        button2 = view.findViewById(R.id.class_check_button2);
        button3 = view.findViewById(R.id.class_check_button3);
        button4 = view.findViewById(R.id.class_check_button4);
        button5 = view.findViewById(R.id.class_check_button5);
        button6 = view.findViewById(R.id.class_check_button6);
        button7 = view.findViewById(R.id.class_check_button7);
        scrollView = view.findViewById(R.id.class_check_sc);
        linearLayout1 = view.findViewById(R.id.class_check_l1);
        linearLayout2 = view.findViewById(R.id.class_check_l2);
        linearLayout3 = view.findViewById(R.id.class_check_l3);
        linearLayout4 = view.findViewById(R.id.class_check_l4);
        linearLayout5 = view.findViewById(R.id.class_check_l5);
        linearLayout6 = view.findViewById(R.id.class_check_l6);
        linearLayout7 = view.findViewById(R.id.class_check_l7);
        textView1 = view.findViewById(R.id.class_check_tv1);
        textView2 = view.findViewById(R.id.class_check_tv2);
        textView3 = view.findViewById(R.id.class_check_tv3);
        textView4 = view.findViewById(R.id.class_check_tv4);
        textView5 = view.findViewById(R.id.class_check_tv5);
        textView6 = view.findViewById(R.id.class_check_tv6);
        textView7 = view.findViewById(R.id.class_check_tv7);
        recyclerView1 = view.findViewById(R.id.class_check_rv1);
        recyclerView2 = view.findViewById(R.id.class_check_rv2);
        recyclerView3 = view.findViewById(R.id.class_check_rv3);
        recyclerView4 = view.findViewById(R.id.class_check_rv4);
        recyclerView5 = view.findViewById(R.id.class_check_rv5);
        recyclerView6 = view.findViewById(R.id.class_check_rv6);
        recyclerView7 = view.findViewById(R.id.class_check_rv7);

        recyclerView1.setLayoutManager(setNoSco());
        recyclerView1.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
        recyclerView2.setLayoutManager(setNoSco());
        recyclerView2.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
        recyclerView3.setLayoutManager(setNoSco());
        recyclerView3.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
        recyclerView4.setLayoutManager(setNoSco());
        recyclerView4.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
        recyclerView5.setLayoutManager(setNoSco());
        recyclerView5.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
        recyclerView6.setLayoutManager(setNoSco());
        recyclerView6.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
        recyclerView7.setLayoutManager(setNoSco());
        recyclerView7.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));

        initDate();


    }

    private void initDate(){
        button1.setText(DateUtils.StringWeek(0));
        button2.setText(DateUtils.StringWeek(1));
        button3.setText(DateUtils.StringWeek(2));
        button4.setText(DateUtils.StringWeek(3));
        button5.setText(DateUtils.StringWeek(4));
        button6.setText(DateUtils.StringWeek(5));
        button7.setText(DateUtils.StringWeek(6));

        textView1.setText(DateUtils.StringTime(0));
        textView2.setText(DateUtils.StringTime(1));
        textView3.setText(DateUtils.StringTime(2));
        textView4.setText(DateUtils.StringTime(3));
        textView5.setText(DateUtils.StringTime(4));
        textView6.setText(DateUtils.StringTime(5));
        textView7.setText(DateUtils.StringTime(6));

    }

    private void setArray(){
        data1 = new ArrayList<>();
        data2 = new ArrayList<>();
        data3 = new ArrayList<>();
        data4 = new ArrayList<>();
        data5 = new ArrayList<>();
        data6 = new ArrayList<>();
        data7 = new ArrayList<>();

    }

    private void connectData(){

        setArray();
        new Thread(){
            public void run(){
                Looper.prepare();
                try{
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        Statement stmt = conn.createStatement();
                        String sql = "SELECT * FROM appoint WHERE appoint_classify = 1 ORDER BY appoint_time";
                        ResultSet resultSet = stmt.executeQuery(sql);
                     //   ResultSet resultSet1 = null;
                        while(resultSet.next()){
                            int bid = resultSet.getInt("appoint_bid");
                            int week = resultSet.getInt("appoint_week");
                            int time = resultSet.getInt("appoint_time");
                            int check = 0;
                            int Today = DateUtils.IntTime(0)-1;//是否大于当前日期
                            String sql_check = "SELECT * From yu WHERE yu_user = '" +
                                    SharePreferences.getString(getActivity(), AppConstants.USER_PHONE) +
                                    "' AND yu_bid = " +
                                    bid +
                                    " AND yu_time > " +
                                    Today +
                                    "";

                            Statement stmt1 = conn.createStatement();
                            ResultSet resultSet1 = stmt1.executeQuery(sql_check);
                            if(resultSet1.first()){
                                check = 1;
                            }
                            resultSet1.close();
                            String rTime;
                            if(time == 1){
                                rTime = "08:30-10:00";
                            }else {
                                rTime = "14:30-16:00";
                            }
                            switch (week){
                                case 1:{
                                    data1.add(new Class_select(resultSet.getString("appoint_name"),
                                            resultSet.getString("appoint_coach"),rTime,resultSet.getString("appoint_cover"),
                                            check,resultSet.getInt("appoint_place"),bid,week));
                                    break;
                                }
                                case 2:{
                                    data2.add(new Class_select(resultSet.getString("appoint_name"),
                                            resultSet.getString("appoint_coach"),rTime,resultSet.getString("appoint_cover"),
                                            check,resultSet.getInt("appoint_place"),bid,week));
                                    break;
                                }
                                case 3:{
                                    data3.add(new Class_select(resultSet.getString("appoint_name"),
                                            resultSet.getString("appoint_coach"),rTime,resultSet.getString("appoint_cover"),
                                            check,resultSet.getInt("appoint_place"),bid,week));
                                    break;
                                }
                                case 4:{
                                    data4.add(new Class_select(resultSet.getString("appoint_name"),
                                            resultSet.getString("appoint_coach"),rTime,resultSet.getString("appoint_cover"),
                                            check,resultSet.getInt("appoint_place"),bid,week));
                                    break;
                                }
                                case 5:{
                                    data5.add(new Class_select(resultSet.getString("appoint_name"),
                                            resultSet.getString("appoint_coach"),rTime,resultSet.getString("appoint_cover"),
                                            check,resultSet.getInt("appoint_place"),bid,week));
                                    break;
                                }
                                case 6:{
                                    data6.add(new Class_select(resultSet.getString("appoint_name"),
                                            resultSet.getString("appoint_coach"),rTime,resultSet.getString("appoint_cover"),
                                            check,resultSet.getInt("appoint_place"),bid,week));
                                    break;
                                }
                                case 7:{
                                    data7.add(new Class_select(resultSet.getString("appoint_name"),
                                            resultSet.getString("appoint_coach"),rTime,resultSet.getString("appoint_cover"),
                                            check,resultSet.getInt("appoint_place"),bid,week));
                                    break;
                                }
                                default:
                                    break;
                            }
                        }
                        System.out.println(data1);
                        Message message = new Message();
                        message.what = 1;
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
                case 1:{
                    ArrayList<ArrayList<Class_select>> Data = new ArrayList<>();
                    Data.add(data1);
                    Data.add(data2);
                    Data.add(data3);
                    Data.add(data4);
                    Data.add(data5);
                    Data.add(data6);
                    Data.add(data7);


                    initAdapter(recyclerView1, Data.get(DateUtils.IntWeek(0)),0);
                    initAdapter(recyclerView2, Data.get(DateUtils.IntWeek(1)),1);
                    initAdapter(recyclerView3, Data.get(DateUtils.IntWeek(2)),2);
                    initAdapter(recyclerView4, Data.get(DateUtils.IntWeek(3)),3);
                    initAdapter(recyclerView5, Data.get(DateUtils.IntWeek(4)),4);
                    initAdapter(recyclerView6, Data.get(DateUtils.IntWeek(5)),5);
                    initAdapter(recyclerView7, Data.get(DateUtils.IntWeek(6)),6);
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });

    private void initAdapter(final RecyclerView recyclerView, final ArrayList<Class_select> class_selects, final int week){
        final ClassSelectAdapter classSelectAdapter = new ClassSelectAdapter(class_selects);
        recyclerView.setAdapter(classSelectAdapter);
        classSelectAdapter.setOnItemClickListener(new ClassSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(class_selects.get(position) != null){
                    showNormalDialog(recyclerView,position,class_selects,week);
                }
            }
        });

    }

    private void showNormalDialog(final RecyclerView recyclerView, final int position, final ArrayList<Class_select> class_selects, final int week){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());
        switch (class_selects.get(position).getCheck()){
            case 0:{
                normalDialog.setMessage("确定要选择该课程？");
                break;
            }
            case 1:{
                normalDialog.setMessage("取消该课程");
                break;
            }
            default:
                break;
        }
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        if(class_selects.get(position).getCheck() == 0){
                            class_selects.get(position).setCheck(1);
                            setCheck(class_selects.get(position).getBid(),week);
                        }else {
                            class_selects.get(position).setCheck(0);
                            deleteCheck(class_selects.get(position).getBid());
                        }

                        initAdapter(recyclerView,class_selects,week);
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }

    private void setCheck(final int bid, final int week){
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),"","请稍后...",true);
        new Thread(){
            public void run(){
                Looper.prepare();//用于toast
                try{
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        //根据手机号查找数据库
                        Statement stmt = conn.createStatement();
                        String sql = "INSERT INTO yu (yu_user,yu_time,yu_bid) VALUES (?,?,?)";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                        preparedStatement.setString(1,SharePreferences.getString(getActivity(),AppConstants.USER_PHONE));
                        preparedStatement.setInt(2,DateUtils.IntTime(week));
                        preparedStatement.setInt(3,bid);
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                        JDBCTools.releaseConnection(stmt,conn);
                        progressDialog.dismiss();
                    }else {
                        Tip.showTip(getActivity(),"请检查网络");
                        progressDialog.dismiss();
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                Looper.loop();
            }
        }.start();
    }

    private void deleteCheck(final int bid){
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),"","请稍后...",true);
        new Thread(){
            public void run(){
                Looper.prepare();//用于toast
                try{
                    Connection conn = JDBCTools.getConnection();
                    if(conn != null){
                        //根据手机号查找数据库
                        Statement stmt = conn.createStatement();
                        String sql = "DELETE FROM yu WHERE yu_bid = " +
                                bid +
                                "";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                        preparedStatement.close();
                        JDBCTools.releaseConnection(stmt,conn);
                        progressDialog.dismiss();
                    }else {
                        Tip.showTip(getActivity(),"请检查网络");
                        progressDialog.dismiss();
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                Looper.loop();
            }
        }.start();
    }

    private void initGroup(){
        radioGroup.check(0);//默认为第一个
        button1.setChecked(true);
        //按钮点击事件
        setButton(button1,linearLayout1);
        setButton(button2,linearLayout2);
        setButton(button3,linearLayout3);
        setButton(button4,linearLayout4);
        setButton(button5,linearLayout5);
        setButton(button6,linearLayout6);
        setButton(button7,linearLayout7);
    }

    private LinearLayoutManager setNoSco(){
        return new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
    }

    private void setButton(Button button, final LinearLayout linearLayout){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0,linearLayout.getTop());
                    }
                });
            }
        });
    }



    @Override
    public void onStart(){
        super.onStart();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
