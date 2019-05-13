package com.example.itigao.Video;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.itigao.Adapter.ClassVideoAdapter;
import com.example.itigao.AutoProject.JsonCode;
import com.example.itigao.ClassAb.Classes;
import com.example.itigao.R;
import com.example.itigao.ViewHelper.BaseFragment;
import com.example.itigao.okHttp.OkHttpBase;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;


/**
 * Created by 最美人间四月天 on 2019/3/9.
 */

public class HuDongFragment extends BaseFragment {

    private int video_classify;
    private RecyclerView recyclerView;
    private ClassVideoAdapter videoAdapter;

    private List<Classes> classes = new ArrayList<>();
    private List<ClassVideoAdapter.Class_Video> videos = new ArrayList<>();

    CallBackValueHu callBackValue;

    /**
     * fragment与activity产生关联是  回调这个方法
     */
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue = (CallBackValueHu) getActivity();
    }

    //定义一个回调接口
    public interface CallBackValueHu{
        void SendMessageValueHu(Classes strValue);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_hudong, container, false);
        Bundle bundle = getArguments();
        assert bundle != null;
        video_classify = bundle.getInt("video_play_classify",1);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void initView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.video_hudong_rv);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setNestedScrollingEnabled(false);
        videoAdapter = new ClassVideoAdapter(videos);
        recyclerView.setAdapter(videoAdapter);

        connectVideo();

    }

    private void connectVideo(){
        new Thread() {
            public void run() {
                Looper.prepare();

                RequestBody requestBody = new FormBody.Builder()
                        .add("class_classify", String.valueOf(video_classify))
                        .add("class_select", "1")
                        .build();
                String reg = OkHttpBase.getResponse(requestBody,"getClassMessage");
                if(JsonCode.getCode(reg) == 200){
                    String jsonData = JsonCode.getData(reg);
                    classes = JsonCode.jsonToList(jsonData,Classes.class);
                    Message message = new Message();
                    message.what = 302;
                    handler.sendMessage(message);
                }
                Looper.loop();
            }
        }.start();
    }

    private Handler handler = new Handler(msg -> {
        // TODO Auto-generated method stub
        switch (msg.what){
            case 302:{
                initDataVideo();
                break;
            }
            default:
                break;
        }
        return false;
    });


    private void initDataVideo(){
        if(classes.size() > 0){
            for(int i = 0; i<classes.size(); i++){
                videoAdapter.addDataAt(videoAdapter.getItemCount(),
                        videoAdapter.new Class_Video(classes.get(i).getClass_name(),
                                classes.get(i).getClass_cover()));
            }

            videoAdapter.setOnItemClickListener((view, position) -> {
                callBackValue.SendMessageValueHu(classes.get(position));
            });

        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}

