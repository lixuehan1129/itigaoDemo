package com.example.fitdemo.Recommend;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.fitdemo.Adapter.ClassSelectAdapter;
import com.example.fitdemo.R;
import com.example.fitdemo.ViewHelper.BaseFragment;
import com.example.fitdemo.ViewHelper.DividerItemChange;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ROBOSOFT on 2018/11/27.
 */

public class RunningFragment extends BaseFragment {

    private List<ClassSelectAdapter.Class_Select> class_selects;
    private ClassSelectAdapter classSelectAdapter;

    private RadioGroup radioGroup;
    private RadioButton button1, button2, button3, button4, button5, button6, button7;
    private ScrollView scrollView;
  //  private NestedScrollView scrollView;
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

        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView4.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView5.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView6.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView7.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

    }

    private void setData(int week){
        ArrayList<String> introduce = new ArrayList<>();
        ArrayList<String> coach = new ArrayList<>();
        ArrayList<String> time = new ArrayList<>();
        ArrayList<Integer> check = new ArrayList<>();
        ArrayList<Integer> image = new ArrayList<>();

        switch (week){
            case 0:{
                introduce.add("健身课程  一周拥有好身材");
                introduce.add("如何健身  你该这么做");
                coach.add("金牌教练 杰克马 我从来不要工资");
                coach.add("教学有方 校长 请你吃热狗");
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
                coach.add("特约教练 东子 我这个人脸盲");
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
                coach.add("教学有方 校长 请你吃热狗");
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
                coach.add("特约教练 王老板 先定一个小目标");
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
                coach.add("金牌教练 老罗 其实我更适合演讲");
                time.add("星期六 11-26 08：30-10：00");
                check.add(0);
                image.add(R.mipmap.ic_yoga1);
                break;
            }
            case 6:{
                introduce.add("健身课程  一周拥有好身材");
                introduce.add("如何健身  你该这么做");
                coach.add("金牌教练 杰克马 我从来不要工资");
                coach.add("教学有方 校长 请你吃热狗");
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

        initData(introduce,coach,time,check,image,week);

    }

    private void initData(ArrayList<String> introduce, ArrayList<String> coach, ArrayList<String> time,
                          ArrayList<Integer> check, ArrayList<Integer> image, int week){

        class_selects = new ArrayList<>();
        for(int i = 0; i < introduce.size(); i++){
            ClassSelectAdapter newData = new ClassSelectAdapter(class_selects);
            ClassSelectAdapter.Class_Select class_select = newData.new Class_Select(introduce.get(i),coach.get(i),
                    time.get(i),image.get(i),check.get(i));
            class_selects.add(class_select);
        }

        classSelectAdapter = new ClassSelectAdapter(class_selects);
        switch (week){
            case 0:{
                recyclerView1.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
                recyclerView1.setAdapter(classSelectAdapter);
                classSelectAdapter.setOnItemClickListener(new ClassSelectAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
                break;
            }
            case 1:{
                recyclerView2.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
                recyclerView2.setAdapter(classSelectAdapter);
                classSelectAdapter.setOnItemClickListener(new ClassSelectAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
                break;
            }
            case 2:{
                recyclerView3.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
                recyclerView3.setAdapter(classSelectAdapter);
                classSelectAdapter.setOnItemClickListener(new ClassSelectAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
                break;
            }
            case 3:{
                recyclerView4.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
                recyclerView4.setAdapter(classSelectAdapter);
                classSelectAdapter.setOnItemClickListener(new ClassSelectAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
                break;
            }
            case 4:{
                recyclerView5.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
                recyclerView5.setAdapter(classSelectAdapter);
                classSelectAdapter.setOnItemClickListener(new ClassSelectAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
                break;
            }
            case 5:{
                recyclerView6.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
                recyclerView6.setAdapter(classSelectAdapter);
                classSelectAdapter.setOnItemClickListener(new ClassSelectAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
                break;
            }
            case 6:{
                recyclerView7.addItemDecoration(new DividerItemChange(getActivity(),DividerItemChange.VERTICAL));
                recyclerView7.setAdapter(classSelectAdapter);
                classSelectAdapter.setOnItemClickListener(new ClassSelectAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
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
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0,linearLayout1.getTop());
                    }
                });
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0,linearLayout2.getTop());
                    }
                });
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0,linearLayout3.getTop());
                    }
                });
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0,linearLayout4.getTop());
                    }
                });
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0,linearLayout5.getTop());
                    }
                });
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0,linearLayout6.getTop());
                    }
                });
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0,linearLayout7.getTop());
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
