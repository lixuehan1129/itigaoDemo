package com.example.itigao.Broad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.example.itigao.ClassAb.Anchor;
import com.example.itigao.R;
import com.example.itigao.ViewHelper.BaseFragment;

import de.hdodenhof.circleimageview.CircleImageView;


public class IntroduceFragment extends BaseFragment {

    private CircleImageView circleImageView;
    private TextView name,intro;
    private Anchor anchor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.broad_intro, container, false);
        Bundle bundle = getArguments();
        assert bundle != null;
        anchor = (Anchor) bundle.getSerializable("broad_intro_all");

        initView(view);
        return view;
    }

    private void initView(View view){
        circleImageView = (CircleImageView) view.findViewById(R.id.intro_fra_iv);
        name = (TextView) view.findViewById(R.id.intro_fra_name);
        intro = (TextView) view.findViewById(R.id.intro_fra_in);

        if(anchor != null){
            if(Util.isOnMainThread()) {
                Glide.with(getActivity())
                        .load(anchor.getAnchor_cover())
                        .asBitmap()
                        .dontAnimate()
                        .placeholder(R.mipmap.ic_touxiang21)
                        .error(R.mipmap.ic_touxiang21)
                        .into(circleImageView);
            }

            name.setText(anchor.getAnchor_name());
            if(anchor.getAnchor_data() == null){
                intro.setText("暂无简介");
            }else {
                intro.setText(anchor.getAnchor_data());
            }
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