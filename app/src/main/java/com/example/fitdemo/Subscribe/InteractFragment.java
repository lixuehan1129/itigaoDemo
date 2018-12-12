package com.example.fitdemo.Subscribe;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitdemo.Adapter.InteractAdapter;
import com.example.fitdemo.R;
import com.example.fitdemo.Utils.Tip;
import com.example.fitdemo.ViewHelper.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/12/11.
 */

public class InteractFragment extends BaseFragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.interact_fragment, container, false);
        initView(view);
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
}
