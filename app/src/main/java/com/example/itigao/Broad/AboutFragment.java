package com.example.itigao.Broad;

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

import com.example.itigao.Adapter.AnchorActivityAdapter;
import com.example.itigao.AutoProject.AppConstants;
import com.example.itigao.AutoProject.JsonCode;
import com.example.itigao.AutoProject.SharePreferences;
import com.example.itigao.ClassAb.Anchor;
import com.example.itigao.R;
import com.example.itigao.ViewHelper.BaseFragment;
import com.example.itigao.okHttp.OkHttpBase;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;


public class AboutFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private AnchorActivityAdapter anchorAdapter;
    private List<Anchor> anchors_get = new ArrayList<>();

    private int mClassify;

    CallBackValueAn callBackValue;

    /**
     * fragment与activity产生关联是  回调这个方法
     */
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue = (CallBackValueAn) getActivity();
    }

    //定义一个回调接口
    public interface CallBackValueAn{
        void SendMessageValueAn(Anchor strValue);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.broad_about, container, false);
        Bundle bundle = getArguments();
        assert bundle != null;
        mClassify = bundle.getInt("broad_go_classify",1);
        initView(view);
        return view;
    }

    private void initView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.broad_about_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setNestedScrollingEnabled(false);
        anchorAdapter = new AnchorActivityAdapter(anchors_get);
        recyclerView.setAdapter(anchorAdapter);

        connectAnchor();
    }

    //获取主播列表
    private void connectAnchor(){
        anchors_get = new ArrayList<>();
        new Thread() {
            public void run() {
                Looper.prepare();
                RequestBody requestBody = new FormBody.Builder()
                        .add("anchor_classify", String.valueOf(mClassify))
                        .add("anchor_state","1")
                        .add("focus_user", SharePreferences.getString(getActivity(), AppConstants.USER_PHONE))
                        .build();
                String regData = OkHttpBase.getResponse(requestBody,"anchor");
                if(JsonCode.getCode(regData) == 200) {
                    String jsonData = JsonCode.getData(regData);
                    anchors_get = JsonCode.jsonToList(jsonData, Anchor.class);
                    Message message = new Message();
                    message.what = 1085;
                    handler.sendMessage(message);
                }
                Looper.loop();
            }
        }.start();
    }

    private Handler handler = new Handler(msg -> {
        // TODO Auto-generated method stub
        switch (msg.what){
            case 1085:{
                initDataAnchor();
                break;
            }
            default:
                break;
        }
        return false;
    });

    private void initDataAnchor(){
        if(anchors_get.size() > 0){
            anchorAdapter.addDataAt(anchors_get);
            anchorAdapter.setOnItemClickListener((view, position) -> {
                callBackValue.SendMessageValueAn(anchors_get.get(position));
            });
        }else {
            anchors_get.add(new Anchor("暂无",null));
            anchorAdapter.addDataAt(anchors_get);
        }
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
