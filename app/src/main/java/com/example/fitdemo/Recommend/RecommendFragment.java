package com.example.fitdemo.Recommend;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitdemo.Adapter.TabLayoutAdapter;
import com.example.fitdemo.R;
import com.example.fitdemo.ViewHelper.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/11/26.
 */

public class RecommendFragment extends BaseFragment implements TabLayout.OnTabSelectedListener{

    private ViewPager mViewPager;
    private String[] titles=new String[]{"  跑步机  ", " 动感单车 ", " 力量训练 ", "  瑜伽  "};
    private List<Fragment> fragments=new ArrayList<>();

    @Override
    public void onStart(){
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.recommendfragment, null);
        initView(view); //界面
        return view;
    }

    @SuppressLint("NewApi")
    private void initView(View view){
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.recommendFragment_mainTool);
        toolbar.setTitle("预约课程");

        TabLayout mTabLayout = (TabLayout) view.findViewById(R.id.recommendFragment_layout);
        mViewPager = (ViewPager)view.findViewById(R.id.recommendFragment_viewpager);

        mViewPager.setOffscreenPageLimit(2);

        //设置TabLayout标签的显示方式
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (String tab:titles){
            mTabLayout.addTab(mTabLayout.newTab().setText(tab));
        }
        //设置TabLayout点击事件
        mTabLayout.setOnTabSelectedListener(this);

        //课程选择跳转
        fragments.add(new FitFragment());
        fragments.add(new RunningFragment());
        fragments.add(new YogaFragment());
    //    fragments.add(new DanceFragment());
        fragments.add(new CyclingFragment());

        TabLayoutAdapter mTabLayoutAdapter = new TabLayoutAdapter(getChildFragmentManager(), titles, fragments);
        mViewPager.setAdapter(mTabLayoutAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }



    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
