package com.example.itigao.Video;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.itigao.Adapter.VideoRankAdapter;
import com.example.itigao.R;
import com.example.itigao.ViewHelper.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2019/3/9.
 */

public class RankFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private VideoRankAdapter videoRankAdapter;
    private List<VideoRankAdapter.Rank> ranks = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_rank, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.video_rank_rv) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        videoRankAdapter = new VideoRankAdapter(ranks);
        recyclerView.setAdapter(videoRankAdapter);

        initData();
    }

    private void initData(){
        ranks.add(videoRankAdapter.new Rank("http://39.105.213.41:8080/myVideos/hudongPi/1554798921336597.jpeg","智慧体育云用户","1"));
        ranks.add(videoRankAdapter.new Rank("http://39.105.213.41:8080/myVideos/hudongPi/1556160896980357.png","智慧云用户","2"));
        ranks.add(videoRankAdapter.new Rank(null,"我","3"));
        ranks.add(videoRankAdapter.new Rank("http://39.105.213.41:8080/myVideos/hudongPi/share_131670838986960450.png","智慧体育用户","4"));
        ranks.add(videoRankAdapter.new Rank("http://39.105.213.41:8080/myVideos/hudongPi/1548822477648125.jpeg","智慧用户","5"));

        videoRankAdapter.addDataAt(ranks);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

}

