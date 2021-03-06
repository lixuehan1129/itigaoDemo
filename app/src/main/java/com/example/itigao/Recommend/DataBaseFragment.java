package com.example.itigao.Recommend;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.itigao.Adapter.ClassSelectAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JsonCode;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.AutoProject.Tip;
import com.example.itigao.ClassAb.Appoint;
import com.example.itigao.R;
import com.example.itigao.Utils.DateUtils;
import com.example.itigao.ViewHelper.BaseFragment;
import com.example.itigao.ViewHelper.DividerItemChange;
import com.example.itigao.okHttp.OkHttpBase;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class DataBaseFragment extends BaseFragment {

    private LocalBroadcastManager broadcastManager;
    private IntentFilter intentFilter;
    private BroadcastReceiver mReceiver;

    private RadioGroup radioGroup;
    private RadioButton button1, button2, button3, button4, button5, button6, button7;
    private ScrollView scrollView;

    private LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayout5, linearLayout6, linearLayout7;
    private TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3, recyclerView4, recyclerView5, recyclerView6, recyclerView7;

    private List<Appoint> data1,data2,data3,data4,data5,data6,data7;
    private List<Appoint> appoints = new ArrayList<>();

    private ClassSelectAdapter classSelectAdapter1,classSelectAdapter2,classSelectAdapter3,classSelectAdapter4,classSelectAdapter5,
                               classSelectAdapter6,classSelectAdapter7;

    private int appoint_classify_yu = 0;
    private String[] titles = {"小学预约","初中预约","高中预约","大学预约","研究生预约"};


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.BROAD_CHANGE);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                //收到广播后所作的操作
                appoint_classify_yu = SharePreferences.getInt(getActivity(),AppConstants.USER_CLASSIFY);
                //appoint_classify_yu = bundle.getInt("appoint_classify_yu",0);
                if(appoint_classify_yu == 4){
                    appoint_classify_yu = 3;
                }
                initView(getView());
                initGroup();
                connectData();
            }
        };

        broadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.class_check, container, false);
//        Bundle bundle = getArguments();
//        assert bundle != null;
        appoint_classify_yu = SharePreferences.getInt(getActivity(),AppConstants.USER_CLASSIFY);
        //appoint_classify_yu = bundle.getInt("appoint_classify_yu",0);
        if(appoint_classify_yu == 4){
            appoint_classify_yu = 3;
        }
        initView(view);
        initGroup();
        connectData();
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser&&isResumed()){
            scrollView.post(() -> scrollView.smoothScrollTo(0,linearLayout1.getTop()));
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
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.class_check_mainTool);
        toolbar.setTitle(titles[appoint_classify_yu]);
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

        classSelectAdapter1 = new ClassSelectAdapter(appoints);
        classSelectAdapter2 = new ClassSelectAdapter(appoints);
        classSelectAdapter3 = new ClassSelectAdapter(appoints);
        classSelectAdapter4 = new ClassSelectAdapter(appoints);
        classSelectAdapter5 = new ClassSelectAdapter(appoints);
        classSelectAdapter6 = new ClassSelectAdapter(appoints);
        classSelectAdapter7 = new ClassSelectAdapter(appoints);

        recyclerView1.setLayoutManager(setNoSco());
        recyclerView1.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
        recyclerView1.setAdapter(classSelectAdapter1);
        recyclerView2.setLayoutManager(setNoSco());
        recyclerView2.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
        recyclerView2.setAdapter(classSelectAdapter2);
        recyclerView3.setLayoutManager(setNoSco());
        recyclerView3.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
        recyclerView3.setAdapter(classSelectAdapter3);
        recyclerView4.setLayoutManager(setNoSco());
        recyclerView4.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
        recyclerView4.setAdapter(classSelectAdapter4);
        recyclerView5.setLayoutManager(setNoSco());
        recyclerView5.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
        recyclerView5.setAdapter(classSelectAdapter5);
        recyclerView6.setLayoutManager(setNoSco());
        recyclerView6.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
        recyclerView6.setAdapter(classSelectAdapter6);
        recyclerView7.setLayoutManager(setNoSco());
        recyclerView7.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
        recyclerView7.setAdapter(classSelectAdapter7);
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

        appoints = new ArrayList<>();

    }

    private void connectData(){
        setArray();
        new Thread(){
            public void run(){
                Looper.prepare();

                RequestBody requestBody = new FormBody.Builder()
                        .add("appoint_classify", String.valueOf(appoint_classify_yu + 1))
                        .add("yu_user",SharePreferences.getString(getActivity(), AppConstants.USER_PHONE))
                        .add("yu_time", String.valueOf(DateUtils.IntTime(0)-1))
                        .build();
                String regData = OkHttpBase.getResponse(requestBody,"getAppoint");
                if(regData != null){
                    if(JsonCode.getCode(regData) == 200){
                        String jsonData = JsonCode.getData(regData);
                        appoints = JsonCode.jsonToList(jsonData, Appoint.class);
                        for(int i = 0; i < appoints.size(); i++){
                            switch (appoints.get(i).getAppoint_week()){
                                case 1:{
                                    data1.add(new Appoint(appoints.get(i).getAppoint_name(),appoints.get(i).getAppoint_coach(),appoints.get(i).getAppoint_time()
                                    ,appoints.get(i).getAppoint_cover(),appoints.get(i).getAppoint_yu_check(),appoints.get(i).getAppoint_bid()));
                                    break;
                                }
                                case 2:{
                                    data2.add(new Appoint(appoints.get(i).getAppoint_name(),appoints.get(i).getAppoint_coach(),appoints.get(i).getAppoint_time()
                                            ,appoints.get(i).getAppoint_cover(),appoints.get(i).getAppoint_yu_check(),appoints.get(i).getAppoint_bid()));
                                    break;
                                }
                                case 3:{
                                    data3.add(new Appoint(appoints.get(i).getAppoint_name(),appoints.get(i).getAppoint_coach(),appoints.get(i).getAppoint_time()
                                            ,appoints.get(i).getAppoint_cover(),appoints.get(i).getAppoint_yu_check(),appoints.get(i).getAppoint_bid()));
                                    break;
                                }
                                case 4:{
                                    data4.add(new Appoint(appoints.get(i).getAppoint_name(),appoints.get(i).getAppoint_coach(),appoints.get(i).getAppoint_time()
                                            ,appoints.get(i).getAppoint_cover(),appoints.get(i).getAppoint_yu_check(),appoints.get(i).getAppoint_bid()));
                                    break;
                                }
                                case 5:{
                                    data5.add(new Appoint(appoints.get(i).getAppoint_name(),appoints.get(i).getAppoint_coach(),appoints.get(i).getAppoint_time()
                                            ,appoints.get(i).getAppoint_cover(),appoints.get(i).getAppoint_yu_check(),appoints.get(i).getAppoint_bid()));
                                    break;
                                }
                                case 6:{
                                    data6.add(new Appoint(appoints.get(i).getAppoint_name(),appoints.get(i).getAppoint_coach(),appoints.get(i).getAppoint_time()
                                            ,appoints.get(i).getAppoint_cover(),appoints.get(i).getAppoint_yu_check(),appoints.get(i).getAppoint_bid()));
                                    break;
                                }
                                case 7:{
                                    data7.add(new Appoint(appoints.get(i).getAppoint_name(),appoints.get(i).getAppoint_coach(),appoints.get(i).getAppoint_time()
                                            ,appoints.get(i).getAppoint_cover(),appoints.get(i).getAppoint_yu_check(),appoints.get(i).getAppoint_bid()));
                                    break;
                                }
                                default:
                                    break;
                            }
                        }
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
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
                    ArrayList<List<Appoint>> Data = new ArrayList<>();
                    Data.add(data1);
                    Data.add(data2);
                    Data.add(data3);
                    Data.add(data4);
                    Data.add(data5);
                    Data.add(data6);
                    Data.add(data7);


                    initAdapter(classSelectAdapter1, Data.get(DateUtils.IntWeek(0)),0);
                    initAdapter(classSelectAdapter2, Data.get(DateUtils.IntWeek(1)),1);
                    initAdapter(classSelectAdapter3, Data.get(DateUtils.IntWeek(2)),2);
                    initAdapter(classSelectAdapter4, Data.get(DateUtils.IntWeek(3)),3);
                    initAdapter(classSelectAdapter5, Data.get(DateUtils.IntWeek(4)),4);
                    initAdapter(classSelectAdapter6, Data.get(DateUtils.IntWeek(5)),5);
                    initAdapter(classSelectAdapter7, Data.get(DateUtils.IntWeek(6)),6);
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });

    private void initAdapter(final ClassSelectAdapter classSelectAdapter, final List<Appoint> class_selects, final int week){
        classSelectAdapter.addDataAt(class_selects);
        classSelectAdapter.setOnItemClickListener((view, position) -> {
            if(class_selects.get(position) != null){
                showNormalDialog(classSelectAdapter,position,class_selects.get(position),week);
            }
        });

    }

    private void showNormalDialog(final ClassSelectAdapter classSelectAdapter, final int position, final Appoint class_selects, final int week){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());
        switch (class_selects.getAppoint_yu_check()){
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
                        if(class_selects.getAppoint_yu_check() == 0){
                            class_selects.setAppoint_yu_check(1);
                            setCheck(class_selects,week);
                        }else {
                            class_selects.setAppoint_yu_check(0);
                            deleteCheck(class_selects,week);
                        }

                        classSelectAdapter.changeData(position,class_selects);
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

    private void setCheck(final Appoint class_selects, final int week){
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),"","请稍后...",true);
        new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run(){
                Looper.prepare();
                RequestBody requestBody = new FormBody.Builder()
                        .add("yu_user",SharePreferences.getString(getActivity(),AppConstants.USER_PHONE))
                        .add("yu_time", String.valueOf(DateUtils.IntTime(week)))
                        .add("yu_bid", String.valueOf(class_selects.getAppoint_bid()))
                        .build();
                String regData = OkHttpBase.getResponse(requestBody,"insertYu");
                if(regData != null){
                    progressDialog.dismiss();

                    Intent intent = new Intent(getActivity(), AutoReceiver.class);
                    /***/
                    intent.setAction(class_selects.getAppoint_bid() + "_" + DateUtils.IntTime(week));
                    // PendingIntent这个类用于处理即将发生的事情 
                    PendingIntent sender = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
                    AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    // AlarmManager.ELAPSED_REALTIME_WAKEUP表示闹钟在睡眠状态下会唤醒系统并执行提示功能，该状态下闹钟使用相对时间
                    // SystemClock.elapsedRealtime()表示手机开始到现在经过的时间
                    Calendar calendar = Calendar.getInstance();
                    int year = Integer.parseInt(String.valueOf(DateUtils.IntTime(week)).substring(0,4));
                    int month = Integer.parseInt(String.valueOf(DateUtils.IntTime(week)).substring(4,6));
                    int day = Integer.parseInt(String.valueOf(DateUtils.IntTime(week)).substring(6,8));
                    int hour = class_selects.getAppoint_time() == 1? 8:14;
                    int minute = 20;
                    calendar.set(year, month - 1, day, hour, minute);
                    //calendar.set(2019, 6, 4, 9, 54);
                    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

                }else {
                    Tip.showTip(getActivity(),"请检查网络");
                    progressDialog.dismiss();
                }
                Looper.loop();
            }
        }.start();

    }

    private void deleteCheck(final Appoint class_selects,final int week){
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),"","请稍后...",true);

        new Thread(){
            public void run(){
                Looper.prepare();
                RequestBody requestBody = new FormBody.Builder()
                        .add("yu_user",SharePreferences.getString(getActivity(),AppConstants.USER_PHONE))
                        .add("yu_bid", String.valueOf(class_selects.getAppoint_bid()))
                        .build();
                String regData = OkHttpBase.getResponse(requestBody,"deleteYu");
                if(regData != null){
                    progressDialog.dismiss();

                    Intent intent = new Intent(getActivity(), AutoReceiver.class);
                    /***/
                    intent.setAction(class_selects.getAppoint_bid() + "_" + DateUtils.IntTime(week));
                    // PendingIntent这个类用于处理即将发生的事情 
                    PendingIntent sender = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
                    // 与上面的intent匹配（filterEquals(intent)）的闹钟会被取消
                    AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    am.cancel(sender);

                }else {
                    Tip.showTip(getActivity(),"请检查网络");
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
        button.setOnClickListener(view -> scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0,linearLayout.getTop());
            }
        }));
    }



    @Override
    public void onStart(){
        super.onStart();
    }
    @Override
    public void onDestroy() {
        broadcastManager.unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
