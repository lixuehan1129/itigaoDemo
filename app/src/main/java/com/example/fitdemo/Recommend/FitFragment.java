package com.example.fitdemo.Recommend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
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
import com.example.fitdemo.Adapter.ClassVideoAdapter;
import com.example.fitdemo.R;
import com.example.fitdemo.ViewHelper.BaseFragment;
import com.example.fitdemo.ViewHelper.DividerItemChange;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ROBOSOFT on 2018/11/27.
 */

public class FitFragment extends BaseFragment {


    private RadioGroup radioGroup;
    private RadioButton button1, button2, button3, button4, button5, button6, button7;
   // private ScrollView scrollView;
    private NestedScrollView scrollView;
    private ClassSelectAdapter newData;
    private List<ClassSelectAdapter.Class_Select> class_selects;
    private ClassSelectAdapter.Class_Select class_select;
    ArrayList<String> introduce;
    ArrayList<String> coach;
    ArrayList<String> time;
    ArrayList<Integer> check;
    ArrayList<Integer> image;

    private LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayout5, linearLayout6, linearLayout7;
    private TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3, recyclerView4, recyclerView5, recyclerView6, recyclerView7;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.class_check, container, false);
        initView(view);
        for(int i = 0; i < 7; i++){
            setData(i);
        }
        initGroup();
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
        recyclerView2.setLayoutManager(setNoSco());
        recyclerView3.setLayoutManager(setNoSco());
        recyclerView4.setLayoutManager(setNoSco());
        recyclerView5.setLayoutManager(setNoSco());
        recyclerView6.setLayoutManager(setNoSco());
        recyclerView7.setLayoutManager(setNoSco());

    }

    private void setData(int week){
        introduce = new ArrayList<>();
        coach = new ArrayList<>();
        time = new ArrayList<>();
        check = new ArrayList<>();
        image = new ArrayList<>();

        switch (week){
            case 0:{
                introduce.add("健身课程  一周拥有好身材");
                introduce.add("如何健身  你该这么做");
                coach.add("金牌教练 杰克马 我从来不要工资");
                coach.add("教学有方 校长 微博抽奖送热狗");
                time.add("星期一 11-26 08：30-10：00");
                time.add("星期一 11-26 14：30-16：00");
                check.add(1);
                check.add(0);
                image.add(R.mipmap.ic_run1);
                image.add(R.mipmap.ic_yoga1);
                break;
            }
            case 1:{
                introduce.add("今天你瘦了吗  教你越吃又瘦");
                introduce.add("如何健身2  你该这么做");
                coach.add("特约教练 王老板 先定一个小目标");
                coach.add("兼职教练 刘美男 买饮料刷脸能免单");
                time.add("星期二 11-26 08：30-10：00");
                time.add("星期二 11-26 14：30-16：00");
                check.add(0);
                check.add(0);
                image.add(R.mipmap.ic_yoga1);
                image.add(R.mipmap.ic_run1);
                break;
            }
            case 2:{
                introduce.add("健身课程2  一周拥有好身材");
                coach.add("金牌教练 老罗 其实我更喜欢演讲");
                time.add("星期三 11-26 08：30-10：00");
                check.add(1);
                image.add(R.mipmap.ic_yoga1);
                break;
            }
            case 3:{
                introduce.add("健身课程  一周拥有好身材");
                introduce.add("如何健身  你该这么做");
                coach.add("金牌教练 杰克马 我从来不要工资");
                coach.add("兼职教练 刘美男 开黑吗，我辅助贼6");
                time.add("星期四 11-26 08：30-10：00");
                time.add("星期四 11-26 14：30-16：00");
                image.add(R.mipmap.ic_yoga1);
                image.add(R.mipmap.ic_run1);
                check.add(1);
                check.add(0);
                break;
            }
            case 4:{
                introduce.add("今天你瘦了吗  教你越吃又瘦");
                introduce.add("如何健身2  你该这么做");
                coach.add("美女教练 锦鲤杨 燃烧我的卡路里");
                coach.add("特约教练 东子 我这个人脸盲");
                time.add("星期五 11-26 08：30-10：00");
                time.add("星期五 11-26 14：30-16：00");
                check.add(0);
                check.add(0);
                image.add(R.mipmap.ic_yoga1);
                image.add(R.mipmap.ic_run1);
                break;
            }
            case 5:{
                introduce.add("健身课程2  一周拥有好身材");
                coach.add("金牌教练 小马 都亏到坐公交了");
                time.add("星期六 11-26 08：30-10：00");
                check.add(0);
                image.add(R.mipmap.ic_yoga1);
                break;
            }
            case 6:{
                introduce.add("健身课程  一周拥有好身材");
                introduce.add("如何健身  你该这么做");
                coach.add("金牌教练 杰克马 我从来不要工资");
                coach.add("教学有方 校长 微博抽奖送跑车");
                time.add("星期日 11-26 08：30-10：00");
                time.add("星期日 11-26 14：30-16：00");
                image.add(R.mipmap.ic_run1);
                image.add(R.mipmap.ic_yoga1);
                check.add(1);
                check.add(0);
                break;
            }
            default:
                break;
        }

        initData(week);

    }

    private void changeData(int week, int position, ClassSelectAdapter classSelectAdapter){

        switch (week){
            case 0:{
                if(check.get(position) == 0){
                    check.set(position,1);
                }else {
                    check.set(position,0);
                }
                break;
            }
            case 1:{
                if(check.get(position) == 0){
                    check.set(position,1);
                }else {
                    check.set(position,0);
                }
                break;
            }
            case 2:{
                if(check.get(position) == 0){
                    check.set(position,1);
                }else {
                    check.set(position,0);
                }
                break;
            }
            case 3:{
                if(check.get(position) == 0){
                    check.set(position,1);
                }else {
                    check.set(position,0);
                }
                break;
            }
            case 4:{
                if(check.get(position) == 0){
                    check.set(position,1);
                }else {
                    check.set(position,0);
                }
                break;
            }
            case 5:{
                if(check.get(position) == 0){
                    check.set(position,1);
                }else {
                    check.set(position,0);
                }
                break;
            }
            case 6:{
                if(check.get(position) == 0){
                    check.set(position,1);
                }else {
                    check.set(position,0);
                }
                break;
            }
            default:
                break;
        }

        classSelectAdapter.notifyItemChanged(position);

    }


    private void initData(int week){

        class_selects = new ArrayList<>();
        for(int i = 0; i < introduce.size(); i++){
            newData = new ClassSelectAdapter(class_selects);
            class_select = newData.new Class_Select(introduce.get(i),coach.get(i),
                    time.get(i),image.get(i),check.get(i));
            class_selects.add(class_select);
        }

        switch (week){
            case 0:{
                setAdapter(recyclerView1,week);
                break;
            }
            case 1:{
                setAdapter(recyclerView2,week);
                break;
            }
            case 2:{
                setAdapter(recyclerView3,week);
                break;
            }
            case 3:{
                setAdapter(recyclerView4,week);
                break;
            }
            case 4:{
                setAdapter(recyclerView5,week);
                break;
            }
            case 5:{
                setAdapter(recyclerView6,week);
                break;
            }
            case 6:{
                setAdapter(recyclerView7,week);
                break;
            }
            default:
                break;

        }
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

    private void setAdapter(RecyclerView recyclerView, final int week){
        final ClassSelectAdapter classSelectAdapter = new ClassSelectAdapter(class_selects);
        recyclerView.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
        recyclerView.setAdapter(classSelectAdapter);
        classSelectAdapter.setOnItemClickListener(new ClassSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(check.get(position) != null){
                    showNormalDialog(week,position,classSelectAdapter);
                }
            }
        });
    }

    private void showNormalDialog(final int week, final int position, final ClassSelectAdapter classSelectAdapter){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());
        switch (check.get(position)){
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
                        changeData(week,position,classSelectAdapter);
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
