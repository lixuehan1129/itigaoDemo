package com.example.itigao.Sport;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cy.cyflowlayoutlibrary.FlowLayoutAdapter;
import com.cy.cyflowlayoutlibrary.FlowLayoutScrollView;
import com.example.itigao.Adapter.WeekTarAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.ClassAb.Target;
import com.example.itigao.R;
import com.example.itigao.ViewHelper.BaseFragment;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 最美人间四月天 on 2018/11/26.
 */

public class SportFragment extends BaseFragment {

    private SQLiteDatabase db;

    private TextView tv1, tv2, tv3, tv4;
    private RecyclerView recyclerViewT;
    private WeekTarAdapter weekTarAdapterT;
    private List<WeekTarAdapter.WeekTar> weekTars = new ArrayList<>();
    private FlowLayoutAdapter flowLayoutAdapterT,flowLayoutAdapterA;
    private FlowLayoutScrollView flowLayoutScrollViewT,flowLayoutScrollViewA;
    private List<String> list = new ArrayList<>();
    private List<String> listT = new ArrayList<>();
    private List<String> listA = new ArrayList<>();
    private LinearLayout linearLayoutT;
    private PopupWindow popupWindow;
    private View contentView;



    @Override
    public void onStart(){
        LitePal.initialize(getActivity());
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.sportfragment, null);
        initView(view); //界面
        return view;
    }

    @SuppressLint({"NewApi", "ClickableViewAccessibility"})
    private void initView(View view){
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.sportFragment_mainTool);
        toolbar.setTitle("学习目标");

        //运动界面textView
        tv1 = (TextView) view.findViewById(R.id.sportFragment_data1);
        tv2 = (TextView) view.findViewById(R.id.sportFragment_data2);
        tv3 = (TextView) view.findViewById(R.id.sportFragment_data3);
        tv4 = (TextView) view.findViewById(R.id.sportFragment_data4);
        linearLayoutT = (LinearLayout) view.findViewById(R.id.sportFragment_ly);

        recyclerViewT = (RecyclerView) view.findViewById(R.id.sportFragment_rvT);
        recyclerViewT.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                linearLayoutT.performClick();  //模拟父控件的点击
            }
            return false;
        });


        recyclerViewT.setLayoutManager(new LinearLayoutManager(getActivity()));
        weekTarAdapterT = new WeekTarAdapter(weekTars);
        recyclerViewT.setAdapter(weekTarAdapterT);

        db = LitePal.getDatabase();

        initList();

        showPop();
        linearLayoutT.setOnClickListener(v -> {
            addFlow();
            putFlow();
            popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);

            if(popupWindow != null){
                addBackground();
            }
        });

        setData();

    }

    private void setData(){
        switch (SharePreferences.getInt(getActivity(), AppConstants.USER_CLASSIFY)){
            case 0: tv1.setText("小学");
                    break;
            case 1: tv1.setText("初中");
                break;
            case 2: tv1.setText("高中");
                break;
            case 3: tv1.setText("大学");
                break;
            case 4: tv1.setText("研究生");
                break;
        }

        tv2.setText("127");
        tv3.setText("" + SharePreferences.getInt(getActivity(), AppConstants.USER_LOGIN_COUNT));
        tv4.setText("85");
    }

    private void showPop(){
        //加载弹出框的布局
        contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.week_pop_item, null);
        ImageView fd = (ImageView) contentView.findViewById(R.id.week_pop_down);
        flowLayoutScrollViewT = (FlowLayoutScrollView) contentView.findViewById(R.id.week_pop_tar);
        flowLayoutScrollViewA = (FlowLayoutScrollView) contentView.findViewById(R.id.week_pop_add);

        //设置弹出框的宽度和高度
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        //点击外部消失
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        //进入退出的动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);

        fd.setOnClickListener(v -> {
            initList();
            popupWindow.dismiss();
        });




    }

    private void addBackground() {
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.2f;//调节透明度
        getActivity().getWindow().setAttributes(lp);
        //dismiss时恢复原样
        popupWindow.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp1 = getActivity().getWindow().getAttributes();
            lp1.alpha = 1f;
            getActivity().getWindow().setAttributes(lp1);
        });
    }

    private void putFlow(){
        flowLayoutAdapterT = new FlowLayoutAdapter<String>(listT) {

            @Override
            public void bindDataToView(ViewHolder holder, int position, String bean) {
                holder.setText(R.id.i_tv,bean);

            }

            @Override
            public void onItemClick(int position, String bean) {
                flowLayoutAdapterT.remove(position);
                flowLayoutAdapterA.add(bean);
                LitePal.deleteAll(Target.class,"cont = ?", bean);

            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                return R.layout.week_pop_item_l;
            }
        };

        flowLayoutScrollViewT.setAdapter(flowLayoutAdapterT);
    }

    private void addFlow(){


        flowLayoutAdapterA = new FlowLayoutAdapter<String>(list) {

            @Override
            public void bindDataToView(ViewHolder holder, int position, String bean) {
                holder.setText(R.id.i_tv,bean);

            }

            @Override
            public void onItemClick(int position, String bean) {
                flowLayoutAdapterA.remove(position);
                flowLayoutAdapterT.add(bean);
                new Target(bean).save();

            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                return R.layout.week_pop_item_l;
            }
        };

        flowLayoutScrollViewA.setAdapter(flowLayoutAdapterA);
    }

    private void initList(){
        weekTars = new ArrayList<>();
        list = new ArrayList<>();
        list.add("本周背单词  200个");
        list.add("看视频  50分钟");
        list.add("阅读文章  5篇");
        list.add("习题册  10页");
        list.add("上课  12课时");
        list.add("累积背单词  2000个");
        list.add("读书  2小时");
        list.add("玩游戏  4小时");
        list.add("数学卷  2套");
        list.add("口语练习  10次");
        list.add("课外交流  2次");
        list.add("综合练习  1次");
        list.add("班会  2小时");
        list.add("实践  1天");

        list.add("本周背单词  300个");
        list.add("看视频  120分钟");
        list.add("阅读文章  9篇");
        list.add("习题册  15页");
        list.add("上课  19课时");
        list.add("累积背单词  2400个");
        list.add("读书  4小时");
        list.add("玩游戏  4小时");
        list.add("数学卷  5套");
        list.add("口语练习  15次");
        list.add("课外交流  4次");
        list.add("综合练习  7次");
        list.add("班会  1小时");
        list.add("实践  2天");

        list.add("本周背单词  800个");
        list.add("看视频  500分钟");
        list.add("阅读文章  50篇");
        list.add("习题册  100页");
        list.add("上课  102课时");
        list.add("累积背单词  6000个");
        list.add("读书  20小时");
        list.add("玩游戏  40小时");
        list.add("数学卷  24套");
        list.add("口语练习  21次");
        list.add("课外交流  21次");
        list.add("综合练习  10次");
        list.add("班会  7小时");
        list.add("实践  3天");


        listT = new ArrayList<>();
        List<Target> targets = LitePal.findAll(Target.class);
        for(int i = 0; i<targets.size(); i++){
            listT.add(targets.get(i).getCont());
            weekTars.add(weekTarAdapterT.new WeekTar(listT.get(i),0,0));
        }

      //  listA = new ArrayList<>();
        list.removeAll(listT);

        weekTarAdapterT = new WeekTarAdapter(weekTars);
        recyclerViewT.setAdapter(weekTarAdapterT);


    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                System.out.println("弹出1");
                popupWindow.dismiss();
                initList();
                return true;

            }else {
                System.out.println("弹出2");
                return false;
            }
        });
    }








//    @Override
//    public boolean onKey(View v, int keyCode, KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            if (popupWindow != null && popupWindow.isShowing()) {
//                popupWindow.dismiss();
//                return true;
//            }
//        }
//        return false;
//    }
}
