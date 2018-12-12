package com.example.fitdemo.Subscribe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitdemo.Adapter.SelectAdapter;
import com.example.fitdemo.R;
import com.example.fitdemo.Utils.Tip;
import com.example.fitdemo.ViewHelper.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/12/11.
 */

public class VideoFragment extends BaseFragment {

    private TextView title, itr, advice;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, container, false);
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

        initData();
    }

    private void initData(){
        ArrayList<Integer> id = new ArrayList<>();
        for (int i=0; i<8; i++){
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

        SelectAdapter selectAdapter = new SelectAdapter(selects);
        recyclerView.setAdapter(selectAdapter);
        selectAdapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Tip.showTip(getActivity(), String.valueOf(id.get(position)));
            }
        });
    }

}
