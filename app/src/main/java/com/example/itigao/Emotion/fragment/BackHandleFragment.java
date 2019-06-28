package com.example.itigao.Emotion.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.itigao.ViewHelper.BaseFragment;

public abstract class BackHandleFragment extends BaseFragment {

    private BackHandleInterface backHandleInterface;

    /**
     * 所有继承BackHandledFragment的子类都将在这个方法中实现物理Back键按下后的逻辑
     * FragmentActivity捕捉到物理返回键点击事件后会首先询问Fragment是否消费该事件
     * 如果没有Fragment消息时FragmentActivity自己才会消费该事件
     */
    public abstract boolean onBackPressed();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity() instanceof BackHandleInterface){
            this.backHandleInterface = (BackHandleInterface)getActivity();
        }else{
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        backHandleInterface.onSelectedFragment(this);
    }
}
